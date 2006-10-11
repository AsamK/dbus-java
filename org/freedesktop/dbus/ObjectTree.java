package org.freedesktop.dbus;

/**
 * Keeps track of the exported objects for introspection data */
class ObjectTree 
{
   class TreeNode
   {
      String name;
      DBusInterface object;
      String data;
      TreeNode right;
      TreeNode down;
      public TreeNode(String name)
      {
         this.name = name;
      }
      public TreeNode(String name, DBusInterface object, String data)
      {
         this.name = name;
         this.object = object;
         this.data = data;
      }
   }
   private TreeNode root;
   public ObjectTree()
   {
      root = new TreeNode("");
   }

   private TreeNode recursiveFind(TreeNode current, String path)
   {
      if ("/".equals(path)) return current;
      String[] elements = path.split("/", 2);
      // this is us or a parent node
      if (path.startsWith(current.name)) {
         // this is us
         if (path.equals(current.name)) {
            return current;
         }
         // recurse down
         else {
            if (current.down == null) 
               return null;
            else return recursiveFind(current.down, elements[1]);
         }
      } 
      else if (current.right == null) {
         return null;
      }
      else if (0 > current.right.name.compareTo(elements[0])) {
         return null;
      }
      // recurse right
      else {
         return recursiveFind(current.right, path);
      }
   }
   private TreeNode recursiveAdd(TreeNode current, String path, DBusInterface object, String data)
   {
      String[] elements = path.split("/", 2);
      // this is us or a parent node
      if (path.startsWith(current.name)) {
         // this is us
         if (1 == elements.length || "".equals(elements[1])) {
            current.object = object;
            current.data = data;
         }
         // recurse down
         else {
            if (current.down == null) {
               String[] el = elements[1].split("/", 2);
               current.down = new TreeNode(el[0]);
            }
            current.down = recursiveAdd(current.down, elements[1], object, data);
         }
      } 
      // need to create a new sub-tree on the end
      else if (current.right == null) {
         current.right = new TreeNode(elements[0]);
         current.right = recursiveAdd(current.right, path, object, data);
      }
      // need to insert here
      else if (0 > current.right.name.compareTo(elements[0])) {
         TreeNode t = new TreeNode(elements[0]);
         t.right = current.right;
         current.right = t;
         current.right = recursiveAdd(current.right, path, object, data);
      }
      // recurse right
      else {
         current.right = recursiveAdd(current.right, path, object, data);
      }
      return current;
   }
   public void add(String path, DBusInterface object, String data)
   {
      root = recursiveAdd(root, path, object, data);
   }
   public void remove(String path)
   {
      TreeNode t = recursiveFind(root, path);
      t.object = null;
      t.data = null;
   }

   public String Introspect(String path)
   {
      TreeNode t = recursiveFind(root, path);
      if (null == t) return null;
      StringBuilder sb = new StringBuilder();
      sb.append("<node name=\"");
      sb.append(path);
      sb.append("\">\n");
      if (null != t.data) sb.append(t.data);
      t = t.down;
      while (null != t) {
         sb.append("<node name=\"");
         sb.append(t.name);
         sb.append("\"/>\n");
         t = t.right;
      }
      sb.append("</node>");
      return sb.toString();
   }

   private String recursivePrint(TreeNode current)
   {
      String s = "";
      if (null != current) {
         s += current.name;
         if (null != current.object)
            s += "*";
         if (null != current.down) 
            s += "/{"+recursivePrint(current.down)+"}";
         if (null != current.right)
            s += ", "+recursivePrint(current.right);
      }
      return s;
   }
      
   public String toString()
   {
      return recursivePrint(root);
   }
}