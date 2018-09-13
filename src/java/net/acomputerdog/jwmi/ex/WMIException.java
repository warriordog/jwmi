package net.acomputerdog.jwmi.ex;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import net.acomputerdog.jwmi.nat.NativeObject;

/**
 * Indicates that an error occurred in WMI code
 */
public class WMIException extends NativeHresultException {
    /**
     * Pointer to object where error occurred.  May be unknown.
     */
    private final Pointer pointer;

    public WMIException(WinNT.HRESULT hresult, Pointer pointer) {
        super(hresult);
        this.pointer = pointer;
    }

    public WMIException(String message, WinNT.HRESULT hresult, Pointer pointer) {
        super(message, hresult);
        this.pointer = pointer;
    }

    public WMIException(WinNT.HRESULT hresult, NativeObject obj) {
        this(hresult, obj.getPointer());
    }

    public WMIException(String message, WinNT.HRESULT hresult, NativeObject obj) {
        this(message, hresult, obj.getPointer());
    }

    public WMIException(WinNT.HRESULT hresult) {
        this(hresult, Pointer.NULL);
    }

    public WMIException(String message, WinNT.HRESULT hresult) {
        this(message, hresult, Pointer.NULL);
    }

    public Pointer getPointer() {
        return pointer;
    }
}
