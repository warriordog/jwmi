package net.acomputerdog.jwmi.wbem;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;
import net.acomputerdog.jwmi.WMIException;
import net.acomputerdog.jwmi.nat.WMIWrapper;
import net.acomputerdog.jwmi.nat.ReleasableNativeObject;

public class WbemServices extends ReleasableNativeObject {

    public WbemServices(Pointer pointer) {
        super(pointer);
    }

    public WbemServices() {
        this(Pointer.NULL);
    }

    public EnumWbemClassObject execQuery(String query) {
        PointerByReference clsObjEnum = new PointerByReference();
        BSTR bstr = new BSTR(query);

        HRESULT result = WMIWrapper.INSTANCE.IWbemServices_ExecQuery(this.getPointer(), bstr, clsObjEnum);

        if (result.longValue() == WMIWrapper.S_OK) {
            return new EnumWbemClassObject(clsObjEnum.getValue());
        } else {
            throw new WMIException("WMI error occurred: " + result.toString(), result);
        }
    }
}
