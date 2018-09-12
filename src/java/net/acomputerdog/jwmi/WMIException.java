package net.acomputerdog.jwmi;

import com.sun.jna.platform.win32.WinNT;

/**
 * Any exception that occurs in WMI code
 */
public class WMIException extends RuntimeException {
    private final WinNT.HRESULT hresult;

    public WMIException() {
        super();
        this.hresult = null;
    }

    public WMIException(String message) {
        super(message);
        this.hresult = null;
    }

    public WMIException(WinNT.HRESULT hresult) {
        super();
        this.hresult = hresult;
    }

    public WMIException(String message, WinNT.HRESULT hresult) {
        super(message);
        this.hresult = hresult;
    }

    public WinNT.HRESULT getHresult() {
        return hresult;
    }
}
