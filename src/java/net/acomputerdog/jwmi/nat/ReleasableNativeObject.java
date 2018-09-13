package net.acomputerdog.jwmi.nat;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import net.acomputerdog.jwmi.ex.WMIException;

/**
 * A native object that can be released
 */
public abstract class ReleasableNativeObject extends NativeObject implements Releasable {

    /**
     * If release() has been called on this object
     */
    private boolean released = false;

    public ReleasableNativeObject(Pointer pointer) {
        super(pointer);
    }

    /**
     * Releases any resources held by this object
     *
     * @throws IllegalStateException if this object has already been released
     * @throws WMIException if a WMI error occurs
     */
    @Override
    public void release() {
        if (released) {
            throw new IllegalStateException("Object has already been released");
        }
        released = true;

        WinNT.HRESULT hresult = WMIWrapper.INSTANCE.IUnknown_Release(this.getPointer());

        if (hresult.intValue() != WMIWrapper.S_OK) {
            throw new WMIException("Error releasing object: 0x" + Integer.toHexString(hresult.intValue()), hresult, this);
        }
    }

    @Override
    public boolean isReleased() {
        return released;
    }

    @Override
    protected void finalize() {
        if (!released) {
            System.err.println("NativeObject was not released: " + this.getPointer().toString());

            this.release();
        }
    }
}
