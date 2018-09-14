package net.acomputerdog.jwmi;

import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import net.acomputerdog.jwmi.ex.NativeHresultException;
import net.acomputerdog.jwmi.ex.WMIException;
import net.acomputerdog.jwmi.nat.WMIWrapper;
import net.acomputerdog.jwmi.wbem.WbemLocator;

/**
 * jWMI main class.
 *
 * Should be used via JWMI.getInstance()
 */
public class JWMI {
    private static JWMI INSTANCE;

    // private constructor, only one instance
    private JWMI() {
        // open com
        WinNT.HRESULT hresult = WMIWrapper.INSTANCE.openCOM();

        // check for errors
        if (hresult.intValue() != WMIWrapper.S_OK) {
            throw new NativeHresultException(String.format("Unable to open COM: 0x%08X\n", hresult.intValue()), hresult);
        }
    }

    /**
     * Creates a new instance of WbemLocator
     *
     * @throws WMIException if native error occurs
     *
     * @return Returns a new WbemLocator
     */
    public WbemLocator createWbemLocator() {
        PointerByReference locatorRef = new PointerByReference();

        // create instance
        WinNT.HRESULT hresult = WMIWrapper.INSTANCE.createLocator(locatorRef);

        if (hresult.intValue() == WMIWrapper.S_OK) {
            return new WbemLocator(locatorRef.getValue());
        } else {
            throw new NativeHresultException(String.format("Unable to create IWbemLocator: 0x%08X\n", hresult.intValue()), hresult);
        }
    }

    @Override
    protected void finalize() {
        // close when instance is GC'ed
        WinNT.HRESULT hresult = WMIWrapper.INSTANCE.closeCOM();

        if (hresult.intValue() != WMIWrapper.S_OK) {
            System.err.printf("Error closing COM: 0x%08X\n", hresult.intValue());
        }
    }

    /**
     * Gets or creates the shared instance of JWMI.
     *
     * A method is used instead of a static field because this way any exceptions will bubble back up
     * instead of being swallowed or thrown at odd times.
     *
     * @return Return the JWMI instance
     */
    public static JWMI getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            return (INSTANCE = new JWMI());
        }
    }
}
