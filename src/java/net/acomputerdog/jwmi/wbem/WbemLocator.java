package net.acomputerdog.jwmi.wbem;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;
import net.acomputerdog.jwmi.ex.WMIException;
import net.acomputerdog.jwmi.nat.ReleasableNativeObject;
import net.acomputerdog.jwmi.nat.WMIWrapper;

/**
 * Java implementation of IWbemLocator.  Locates and connects to WMI namespaces.
 */
public class WbemLocator extends ReleasableNativeObject {

    /**
     * Create a WbemLocator from a pointer to a native object
     * @param pointer Pointer to the native instance
     */
    public WbemLocator(Pointer pointer) {
        super(pointer);
    }

    /**
     * Connects to a WMI namespace
     *
     * @param namespace The namespace to connect to (ex. "root\CIMv2")
     * @return Return a WbemServices instance that is connected to the namespace
     */
    public WbemServices connectServer(String namespace) {
        PointerByReference servicesRef = new PointerByReference(); // will hold result

        // connect to namespace
        HRESULT hresult = WMIWrapper.INSTANCE.IWbemLocator_ConnectServer(this.getPointer(),  new BSTR(namespace), servicesRef);

        if (hresult.intValue() == WMIWrapper.S_OK) {
            // create services instance
            WbemServices services = new WbemServices(servicesRef.getValue());

            try {
                // set service security
                hresult = WMIWrapper.INSTANCE.setSecurity(services.getPointer());

                if (hresult.intValue() == WMIWrapper.S_OK) {
                    return services;
                } else {
                    throw new WMIException(String.format("Unable to set security on WbemServices: 0x%08X\n", hresult.intValue()), hresult, this);
                }
            } catch (Throwable t) {

                // if an error occurs setting security, then free instance
                services.release();
                throw t;
            }
        } else {
            throw new WMIException(String.format("Unable to create IWbemServices: 0x%08X\n", hresult.intValue()), hresult, this);
        }
    }
}
