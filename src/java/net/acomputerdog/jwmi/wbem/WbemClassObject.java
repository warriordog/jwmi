package net.acomputerdog.jwmi.wbem;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;
import net.acomputerdog.jwmi.WMIException;
import net.acomputerdog.jwmi.nat.ReleasableVariant;
import net.acomputerdog.jwmi.nat.WMIWrapper;
import net.acomputerdog.jwmi.nat.ReleasableNativeObject;

public class WbemClassObject extends ReleasableNativeObject{
    public WbemClassObject(Pointer pointer) {
        super(pointer);
    }

    public WbemClassObject() {
        this(Pointer.NULL);
    }

    public ReleasableVariant get(String name) {
        ReleasableVariant variant = new ReleasableVariant();
        WinNT.HRESULT result = WMIWrapper.INSTANCE.IWbemClassObject_Get(this.getPointer(),  new WTypes.BSTR(name), variant.getPointer());

        if (result.longValue() == WMIWrapper.S_OK) {
            return variant;
        } else {
            throw new WMIException("WMI error occurred: " + result.toString(), result);
        }
    }
}
