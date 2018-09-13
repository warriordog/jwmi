package net.acomputerdog.jwmi.nat;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import net.acomputerdog.jwmi.WMIException;

public abstract class ReleasableNativeObject extends NativeObject implements Releasable {
    public ReleasableNativeObject(Pointer pointer) {
        super(pointer);
    }

    @Override
    public void release() {
        WinNT.HRESULT hresult = WMIWrapper.INSTANCE.IUnknown_Release(getPointer());

        if (hresult.intValue() != WMIWrapper.S_OK) {
            throw new WMIException("WMI error occurred: " + hresult.toString(), hresult);
        }
    }
}
