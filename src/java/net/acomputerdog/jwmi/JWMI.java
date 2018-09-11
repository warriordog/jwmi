package net.acomputerdog.jwmi;

import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import net.acomputerdog.jwmi.nat.WMIWrapper;
import net.acomputerdog.jwmi.wbem.WbemLocator;

public class JWMI {
    private static JWMI INSTANCE;

    // private constructor, only one instance
    private JWMI() {
        // open com
        WinNT.HRESULT hresult = WMIWrapper.INSTANCE.openCOM();

        // check for errors
        if (hresult.longValue() != WMIWrapper.S_OK) {
            throw new WMIException("Unable to open COM: 0x" + Long.toHexString(hresult.longValue()), hresult);
        }
    }

    public WbemLocator createWbemLocator() {
        PointerByReference locatorRef = new PointerByReference();

        // create instance
        WinNT.HRESULT hresult = WMIWrapper.INSTANCE.createLocator(locatorRef);
        if (hresult.longValue() == WMIWrapper.S_OK) {
            return new WbemLocator(locatorRef.getValue());
        } else {
            throw new WMIException("Error creating locator: 0x" + Long.toHexString(hresult.longValue()), hresult);
        }
    }

    public static JWMI getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            return (INSTANCE = new JWMI());
        }
    }
}
