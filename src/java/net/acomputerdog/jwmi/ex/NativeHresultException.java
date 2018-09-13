package net.acomputerdog.jwmi.ex;

import com.sun.jna.platform.win32.WinNT;

/**
 * Indicates that a native function returned an HRESULT value indicating an error
 */
public class NativeHresultException extends NativeException {
    private final WinNT.HRESULT hresult;

    public NativeHresultException(WinNT.HRESULT hresult) {
        super();
        this.hresult = hresult;
    }

    public NativeHresultException(String message, WinNT.HRESULT hresult) {
        super(message);
        this.hresult = hresult;
    }

    public WinNT.HRESULT getHresult() {
        return hresult;
    }
}
