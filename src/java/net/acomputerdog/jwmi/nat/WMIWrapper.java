package net.acomputerdog.jwmi.nat;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;

/**
 * Java shadow of native WMI library.  These are internal functions that should not be used
 * unless you know what you are doing.
 *
 * All HRESULT methods return S_OK on success and something else on failure, unless otherwise noted.
 */
public interface WMIWrapper extends StdCallLibrary {
    WMIWrapper INSTANCE = createInstance();

    /**
     * General HRESULT value for success
     */
    long S_OK = 0;

    /**
     * General HRESULT value for an invalid argument (usually null)
     *
     * Note: null BSTR arguments will not trigger this error, see WBEM_E_INVALID_PARAMETER
     */
    long E_INVALIDARG = 0x80070057;

    /**
     * wbem HRESULT value for success
     */
    long WBEM_S_NO_ERROR = 0;

    /**
     * wbem HRESULT value for false, but used to indicate that we have reached the end of an enumeration.
     */
    long WBEM_S_FALSE = 1;

    /**
     * wbem HRESULT value for an invalid argument
     */
    long WBEM_E_INVALID_PARAMETER = 0x80041008;

    /**
     * Initializes the COM library, sets security, and opens a connection
     *
     * @return Return S_OK if COM opened
     */
    HRESULT openCOM();

    /**
     * Creates an instance of the IWbemLocator class
     *
     * @param locator Ref to pointer where instance will be stored
     * @return Return S_OK if instance created
     */
    HRESULT createLocator(PointerByReference locator);

    /**
     * Sets blanket security on an IWbemServices object
     *
     * @param services Pointer to the object
     * @return Return S_OK if security set
     */
    HRESULT setSecurity(Pointer services);

    /**
     * Closes COM connection and cleans up
     *
     * @return Return S_OK if COM closed
     */
    HRESULT closeCOM();

    /**
     * Calls IWbemLocator::ConnectServer()
     *
     * @param locator   The instance to call through
     * @param ns        The WMI namespace to connect to
     * @param services  Ref to pointer where IWbemServices instance will be stored
     * @return Return S_OK if WMI server connected and instance created
     */
    HRESULT IWbemLocator_ConnectServer(
        Pointer             locator,
        BSTR                ns,
        PointerByReference  services
    );

    /**
     * Calls IWbemServices::ExecQuery()
     *
     * @param services      Instance to call through
     * @param query         Query to execute
     * @param clsObjEnum    Ref to pointer where IEnumWbemClassObject will be stored
     * @return Return S_OK if query successful and enumeration created
     */
    HRESULT IWbemServices_ExecQuery(
        Pointer             services,
        BSTR                query,
        PointerByReference  clsObjEnum
    );

    /**
     * Calls IEnumWbemClassObject::Next()
     *
     * @param clsObjEnum    The instance to call through
     * @param clsObj        Ref to pointer where IWbemClassObject will be written
     * @param count         Pointer to ULONG that will store number of objects written
     * @return  Return WBEM_S_NO_ERROR if there are more objects
     *          Return WBEM_S_FALSE if no more objects
     */
    HRESULT IEnumWbemClassObject_Next(
        Pointer                 clsObjEnum,
        PointerByReference      clsObj,
        WinDef.ULONGByReference count
    );

    /**
     * Calls IUnknown::Release().
     *
     * Released objects should be discarded.
     *
     * @param obj Instance to call through
     * @return Return S_OK if released.
     */
    HRESULT IUnknown_Release(
        Pointer   obj
    );

    /**
     * Calls IWbemClassObject::Get()
     *
     * @param clsObj    Instance to call through
     * @param name      Name of the property to get
     * @param value     Pointer to VARIANT where result will be written
     * @return Return S_OK if property read and value written
     */
    HRESULT IWbemClassObject_Get(
        Pointer     clsObj,
        BSTR        name,
        Pointer     value
    );

    /**
     * Clears a variant (frees its memory in the remote process)
     *
     * Cleared variants must be discarded.
     *
     * @param variant Pointer to VARIANT to clear
     * @return Return S_OK if variant cleared.
     */
    HRESULT ClearVariant(
        Pointer    variant
    );

    /**
     * Loads and maps the native library.  Throws and prints any exceptions on failure.
     * @return Return the new instance
     */
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
