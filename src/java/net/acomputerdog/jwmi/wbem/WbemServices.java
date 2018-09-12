package net.acomputerdog.jwmi.wbem;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;
import net.acomputerdog.jwmi.WMIException;
import net.acomputerdog.jwmi.nat.ReleasableNativeObject;
import net.acomputerdog.jwmi.nat.WMIWrapper;

import java.util.ArrayList;
import java.util.List;

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

    public List<WbemClassObject> execQueryBuffered(String query) {
        try (EnumWbemClassObject clsObjEnum = execQuery(query)) {
            List<WbemClassObject> clsObjList = new ArrayList<>();

            try {
                while (clsObjEnum.hasNext()) {
                    clsObjList.add(clsObjEnum.next());
                }

                return clsObjList;
            } catch (Throwable t) {
                clsObjList.forEach(WbemClassObject::release);

                throw t;
            }
        }
    }

    public WbemClassObject execQuerySingle(String query) {
        try (EnumWbemClassObject clsObjEnum = execQuery(query)) {
            if (clsObjEnum.hasNext()) {
                return clsObjEnum.next();
            } else {
                return null;
            }
        }
    }
}
