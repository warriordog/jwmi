package net.acomputerdog.jwmi.wbem;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import net.acomputerdog.jwmi.WMIException;
import net.acomputerdog.jwmi.nat.WMIWrapper;
import net.acomputerdog.jwmi.nat.ReleasableNativeObject;

import java.util.Iterator;

public class EnumWbemClassObject extends ReleasableNativeObject implements Iterator<WbemClassObject>, Iterable<WbemClassObject> {
    private WbemClassObject next;

    public EnumWbemClassObject(Pointer pointer) {
        super(pointer);

        // Make sure that the first object is loaded
        getNext();
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    public WbemClassObject next() {
        WbemClassObject rtn = this.next;
        getNext();
        return rtn;
    }

    private void getNext() {
        PointerByReference clsObj = new PointerByReference();
        WinDef.ULONGByReference count = new WinDef.ULONGByReference();

        WinNT.HRESULT result = WMIWrapper.INSTANCE.IEnumWbemClassObject_Next(this.getPointer(), clsObj.getPointer(), count);

        if (result.longValue() != WMIWrapper.WBEM_S_NO_ERROR && result.longValue() != WMIWrapper.WBEM_S_FALSE) {
            throw new WMIException("Error getting next object: " + result.toString(), result);
        }

        if (count.getValue().longValue() > 0) {
            next = new WbemClassObject(clsObj.getValue());
        } else {
            next = null;
        }
    }

    @Override
    public void release() {
        if (next != null) {
            next.release();
        }

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
