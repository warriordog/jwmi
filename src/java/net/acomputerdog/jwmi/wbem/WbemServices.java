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

/**
 * Java shadow of native IWbemServices.  Used to interact with a WMI namespace.
 */
public class WbemServices extends ReleasableNativeObject {

    /**
     * Create a new WbemServices from a pointer to a real native instance.
     *
     * @param pointer Pointer to the native instance
     */
    public WbemServices(Pointer pointer) {
        super(pointer);
    }

    /**
     * Executes a query.  This is an implementation of the traditional IWbemServices::ExecQuery() method
     * that returns an EnumWbemClassObject just like in the real thing.  That class can be clunky to use from Java,
     * so easier to use wrapper functions are provided below.
     *
     * @param query The query to execute
     * @return Return an EnumWbemClassObject instance with the results
     */
    public EnumWbemClassObject execQuery(String query) {
        PointerByReference clsObjEnum = new PointerByReference(); // will hold results
        BSTR bstr = new BSTR(query); // convert query to correct string type

        // execute query
        HRESULT result = WMIWrapper.INSTANCE.IWbemServices_ExecQuery(this.getPointer(), bstr, clsObjEnum);

        if (result.longValue() == WMIWrapper.S_OK) {
            return new EnumWbemClassObject(clsObjEnum.getValue());
        } else {
            throw new WMIException("WMI error occurred: 0x" + Long.toHexString(result.longValue()), result);
        }
    }

    /**
     * Wrapper for execQuery() that reads all results into a List.
     *
     * You MUST iterate through and release ALL objects in the list to avoid memory leaks (bad leaks
     * that won't go away when this process ends!)
     *
     * @param query The query to execute.
     * @return Return a List containing all returned WbemClassObjects
     */
    public List<WbemClassObject> execQueryBuffered(String query) {
        // Perform normal query and auto free
        try (EnumWbemClassObject clsObjEnum = execQuery(query)) {
            List<WbemClassObject> clsObjList = new ArrayList<>();

            try {
                // add all objects to the list
                while (clsObjEnum.hasNext()) {
                    clsObjList.add(clsObjEnum.next());
                }

                return clsObjList;
            } catch (Throwable t) {
                // If an error occurs, then free all objects before returning
                clsObjList.forEach(WbemClassObject::release);

                throw t;
            }
        }
    }

    /**
     * Wrapper for execQuery() that returns only a single result (whichever is returned first from WMI).
     *
     * @param query The query to execute
     * @return Return the first result of the query
     */
    public WbemClassObject execQuerySingle(String query) {
        // execute query and auto close
        try (EnumWbemClassObject clsObjEnum = execQuery(query)) {
            // if we have a result then return it, otherwise return null
            if (clsObjEnum.hasNext()) {
                return clsObjEnum.next();
            } else {
                return null;
            }
        }
    }
}
