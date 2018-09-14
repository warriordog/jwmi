package net.acomputerdog.jwmi.wbem;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;
import net.acomputerdog.jwmi.ex.WMIException;
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
        WinNT.HRESULT hresult = WMIWrapper.INSTANCE.IWbemClassObject_Get(this.getPointer(),  new WTypes.BSTR(name), variant.getPointer());

        if (hresult.intValue() == WMIWrapper.S_OK) {
            return variant;
        } else {
            throw new WMIException(String.format("Error getting property value: 0x%08X\n", hresult.intValue()), hresult, this);
        }
    }

    /**
     * Gets a the value of a property as a String.  Automatically closes the containing VARIANT.
     *
     * @param name Name of the property to access
     * @return Return the string value of the property
     */
    public String getString(String name) {
        try (ReleasableVariant str = get(name)) {
            return str.stringValue();
        }
    }

    /**
     * Gets a the value of a property as an int.  Automatically closes the containing VARIANT.
     *
     * @param name Name of the property to access
     * @return Return the int value of the property
     */
    public int getInt(String name) {
        try (ReleasableVariant str = get(name)) {
            return str.intValue();
        }
    }

    /**
     * Gets a the value of a property as a boolean.  Automatically closes the containing VARIANT.
     *
     * @param name Name of the property to access
     * @return Return the boolean value of the property
     */
    public boolean getBoolean(String name) {
        try (ReleasableVariant str = get(name)) {
            return str.booleanValue();
        }
    }
}
