jWMI - java bindings for the Windows Management Interface
---

### Introduction

jWMI is a library that provides Java bindings for the Windows Management Interface (WMI).  It allows access to WMI objects without the need to write any native code.

jWMI is made of two parts:

1. A small C++ DLL that handles COM communications and wraps the C++ COM methods in C-compatible functions.
2. Java bindings that use [Java Native Access](https://github.com/java-native-access/jna) to dynamically load the native library and create Java objects that map to native WMI objects.

Currently jWMI implements only a small subset of the WMI library, and many method arguments are hard-coded (to simplify usage on the java side).
Over time the library will be expanded to cover all WMI functions and possibly expose flag arguments to allow fine-grained control.

### Compiling

For now jWMI must be built on windows due to most cross-compilers not supporting WMI.

##### Prerequisites:
* JDK 8 or newer.  OpenJDK is untested but should work.
* Microsoft Visual Studio OR Microsoft Visual C++ Build Tools 2015 or newer.  Older versions may work but are untested.
* Maven 3

##### Steps:
1. Clone or download the repository to an accessible place
2. Navigate to the repository root (in File Explorer or CMD)
3. Run make.bat
4. jWMI JAR will be created in .\target\jwmi-X.X.X.jar and installed into local maven repository.

##### Building on other platforms:

Currently, the native component of jWMI must be built on Windows due to a lack of WMI support in all tested cross-compilers.
But the Java components can be built on any platform as long as libwmi.dll is built on Windows and placed into the correct location.

Platform|Location
---|---
x86|\<root\>\res\win32-x86\libwmi.dll
x86_64|\<root\>\res\win32-x86-64\libwmi.dll

Once the DLL is placed in the correct location, jWMI can be built on any platform using maven as follows:

    # To build and install into local repository
    mvn clean install
    
    # To build and create a JAR
    mvn clean package
    
    # To build, create a JAR, and install into local repository
    mvn clean package install