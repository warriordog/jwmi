package net.acomputerdog.jwmi.nat;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinNT;
import net.acomputerdog.jwmi.ex.WMIException;

/**
 * Extension of JNA Variant that can be released.  This class must be used over the JNA implementation because
 * WMI VARIANTS exist on a remote process and references are counted.
 *
 * All ReleasableVariants MUST be release()'ed so that they can be cleared from remote process memory.  Trust me,
 * you don't want a persistent memory leak in a system process.
 */
public class ReleasableVariant extends Variant.VARIANT implements Releasable {
    /**
     * If release() has been called on this object
     */
    private boolean released = false;

    /**
     * Creates a ReleasableVariant via a pointer to a native VARIANT
     * @param pointer Pointer to native struct
     */
    public ReleasableVariant(Pointer pointer) {
        super(pointer);
    }

    /**
     * Creates an empty ReleasableVariant
     */
    public ReleasableVariant() {
        super();
    }

    @Override
    public void release() {
        if (released) {
            throw new IllegalStateException("Object has already been released");
        }
        released = true;

        // clear the variant
        WinNT.HRESULT hresult = WMIWrapper.INSTANCE.ClearVariant(getPointer());

        if (hresult.intValue() != WMIWrapper.S_OK) {
            throw new WMIException("Error clearing variant: 0x" + Integer.toHexString(hresult.intValue()), hresult, this.getPointer());
        }
    }

    @Override
    public boolean isReleased() {
        return released;
    }

    @Override
    protected void finalize() {
        if (!released) {
            System.err.println("Variant was not cleared: " + this.getPointer().toString());

            this.release();
        }
    }
}
