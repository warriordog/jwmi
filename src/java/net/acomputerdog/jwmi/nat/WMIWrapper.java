package net.acomputerdog.jwmi.nat;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface WMIWrapper extends StdCallLibrary {
    WMIWrapper INSTANCE = createInstance();

    long S_OK = 0;
    long WBEM_S_NO_ERROR = 0;
    long WBEM_S_FALSE = 1;

    HRESULT openCOM();
    HRESULT createLocator(PointerByReference locator);
    HRESULT setSecurity(Pointer locator);
    HRESULT closeCOM();

    HRESULT IWbemLocator_ConnectServer(
        Pointer             locator,
        BSTR                ns,
        PointerByReference  services
    );

    HRESULT IWbemServices_ExecQuery(
        Pointer             services,
        BSTR                query,
        PointerByReference  clsObjEnum
    );

    HRESULT IEnumWbemClassObject_Next(
        Pointer                 clsObjEnum,
        Pointer                 clsObj,
        WinDef.ULONGByReference count
    );

    HRESULT IUnknown_Release(
        Pointer   obj
    );

    HRESULT IWbemClassObject_Get(
        Pointer     clsObj,
        BSTR        name,
        Pointer     value
    );

    HRESULT ClearVariant(
        Pointer    variant
    );

    static WMIWrapper createInstance() {
        try {
            return Native.loadLibrary("libwmi", WMIWrapper.class);
        } catch (Throwable t) {
            System.err.println("Exception creating WMI wrapper");
            t.printStackTrace();
            throw t;
        }
    }
}
