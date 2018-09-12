package net.acomputerdog.jwmi.wbem;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;
import net.acomputerdog.jwmi.WMIException;
import net.acomputerdog.jwmi.nat.ReleasableNativeObject;
import net.acomputerdog.jwmi.nat.ReleasableVariant;
import net.acomputerdog.jwmi.nat.WMIWrapper;

/**
 * Java implementation of IWbemClassObject.  Represents an instance of a WMI class.
 */
public class WbemClassObject extends ReleasableNativeObject{
    /**
     * Creates an instance of WbemClassObject from a pointer to a native object
     * @param pointer Pointer to native instance
     */
    public WbemClassObject(Pointer pointer) {
        super(pointer);
    }

    /**
     * Gets the value of a property as a VARIANT
     *
     * @param name The name of the property to get
     * @return Return the value of the property as a ReleasableVariant
     */
    public ReleasableVariant get(String name) {
        ReleasableVariant variant = new ReleasableVariant(); // will hold result

        // get value
        WinNT.HRESULT result = WMIWrapper.INSTANCE.IWbemClassObject_Get(this.getPointer(),  new WTypes.BSTR(name), variant.getPointer());

        if (result.longValue() == WMIWrapper.S_OK) {
            return variant;
        } else {
            throw new WMIException("WMI error occurred: 0x" + Long.toHexString(result.longValue()), result);
        }
    }
}
