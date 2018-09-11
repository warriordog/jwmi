package net.acomputerdog.jwmi.wbem;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;
import net.acomputerdog.jwmi.WMIException;
import net.acomputerdog.jwmi.nat.WMIWrapper;
import net.acomputerdog.jwmi.nat.ReleasableNativeObject;

public class WbemLocator extends ReleasableNativeObject {

    public WbemLocator(Pointer pointer) {
        super(pointer);
    }

    public WbemServices connectServer(String namespace) {
        PointerByReference servicesRef = new PointerByReference();
        HRESULT result = WMIWrapper.INSTANCE.IWbemLocator_ConnectServer(this.getPointer(),  new BSTR(namespace), servicesRef);

        if (result.longValue() == WMIWrapper.S_OK) {
            // create services instance
            WbemServices services = new WbemServices(servicesRef.getValue());

            try {
                // set service security
                result = WMIWrapper.INSTANCE.setSecurity(services.getPointer());

                if (result.longValue() == WMIWrapper.S_OK) {
                    return services;
                } else {
                    throw new WMIException("Unable to set security on WbemServices: " + Long.toHexString(result.longValue()), result);
                }
            } catch (Throwable t) {
                services.release();
                throw t;
            }
        } else {
            throw new WMIException("WMI error occurred: " + result.toString(), result);
        }
    }
}
