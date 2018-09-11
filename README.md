jWMI - java bindings for the Windows Management Interface
---

jWMI is a set of JNA-based bindings for the Windows Management Interface (WMI).  It includes a small C++ DLL that handles the COM communications and wraps the COM methods in C-compatible functions.  The Java bindings use JNA to load the library and create shell objects that map to native COM objects via pointers.

Currently jWMI implements only a small subset of the WMI library, and many method arguments are hard-coded (to simplify usage on the java side).
