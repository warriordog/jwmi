package net.acomputerdog.jwmi.nat;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Variant;

/**
 * Extension of JNA Variant that can be released.  This class must be used over the JNA implementation because
 * WMI VARIANTS exist on a remote process and references are counted.
 *
 * All ReleasableVariants MUST be release()'ed so that they can be cleared from remote process memory.  Trust me,
 * you don't want a persistent memory leak in a system process.
 */
public class ReleasableVariant extends Variant.VARIANT implements Releasable {
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
        // clear the variant
        WMIWrapper.INSTANCE.ClearVariant(getPointer());
    }
}
