package net.acomputerdog.jwmi.wbem;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import net.acomputerdog.jwmi.WMIException;
import net.acomputerdog.jwmi.nat.ReleasableNativeObject;
import net.acomputerdog.jwmi.nat.WMIWrapper;

import java.util.Iterator;

/**
 * Java implementation of IEnumWbemClassObject.  Contains an enumerable set of results from
 * WbemServices.execQuery()
 */
public class EnumWbemClassObject extends ReleasableNativeObject implements Iterator<WbemClassObject>, Iterable<WbemClassObject> {
    /**
     * The next object in the enumeration
     */
    private WbemClassObject next;

    /**
     * Creates an EnumWbemClassObject from a pointer to a native object
     *
     * @param pointer Pointer to the native instance
     */
    public EnumWbemClassObject(Pointer pointer) {
        super(pointer);

        // Make sure that the first object is loaded
        getNext();
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    /**
     * Gets the next object in the enumeration.
     * Actually returns the previous result and stores the next one for the next call.
     *
     * All objects are still returned in the correct order as long as next() is called until
     * hasNext() is false.
     *
     * @return Return the next object in the enumeration
     */
    public WbemClassObject next() {
        WbemClassObject rtn = this.next;
        getNext();
        return rtn;
    }

    /**
     * Retrieves and stores the next object in the enumeration
     */
    private void getNext() {
        PointerByReference clsObj = new PointerByReference();  // will store the result
        WinDef.ULONGByReference count = new WinDef.ULONGByReference(); // will store how many results were returned

        // request the object
        WinNT.HRESULT result = WMIWrapper.INSTANCE.IEnumWbemClassObject_Next(this.getPointer(), clsObj, count);

        // If it did not return a value or hit the end, then it failed
        if (result.intValue() != WMIWrapper.WBEM_S_NO_ERROR && result.intValue() != WMIWrapper.WBEM_S_FALSE) {
            throw new WMIException("Error getting next object: 0x" + Integer.toHexString(result.intValue()), result);
        }

        // If we got something, then it is the next value.  Otherwise set next to null.
        if (count.getValue().intValue() > 0) {
            next = new WbemClassObject(clsObj.getValue());
        } else {
            next = null;
        }
    }

    @Override
    public void release() {
        // If there is a next object that was never read, then free it because we already activated it.
        if (next != null) {
            next.release();
        }

        // make sure to actually release this object
        super.release();
    }

    @Override
    public Iterator<WbemClassObject> iterator() {
        return new WbemClassObjectIterator();
    }

    private class WbemClassObjectIterator implements Iterator<WbemClassObject> {
        @Override
        public boolean hasNext() {
            return EnumWbemClassObject.this.hasNext();
        }

        @Override
        public WbemClassObject next() {
            return EnumWbemClassObject.this.next();
        }
    }
}
