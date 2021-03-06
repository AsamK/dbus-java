Use in gradle projects
----------------------
Install dbus-java.so on your system. It’s provided by most distributions.

build.gradle:
```
repositories {
    maven {
        url "https://raw.github.com/AsamK/maven/master/releases/"
    }
}

dependencies {
    compile 'org.freedesktop.dbus:dbus-java:2.7.0'
}
```

D-Bus Java README
-----------------

Compilation and installation is described in the INSTALL file.

This will install two jar files, three binaries and some documentation in the
form of an HTML/PDF guide to writing D-Bus Java programs, JavaDoc API for the
library and man pages for the supplied programs. Read the documentation, it's
there for a reason.

The installed programs are documented in their respective man pages.

CreateInterface is a tool for creating interface stubs for D-Bus programs.  It
will connect to services running on the bus and introspect on them to find
their API. This will then be written as Java interface definitions suitable for
importing into your program. A file containing the introspection data can be
used instead.

ListDBus lists the names currently connected to a bus.

DBusViewer is a graphical tool which combines the two tools. It will list the
names on a bus and allow you to introspect on them and save the result as Java
interface files. This currently only introspects on the root object, however.

To run a Java program using D-Bus you need to have the libdbus-java,
libunix-java and libdebug jar files in your classpath and the libunix-java
shared library in your library path. With the default install paths you may
have to do something like:

java -cp /usr/local/share/java/dbus.jar:/usr/local/share/java/unix.jar:/usr/local/share/java/debug-disable.jar -Djava.library.path=/usr/local/lib/jni


