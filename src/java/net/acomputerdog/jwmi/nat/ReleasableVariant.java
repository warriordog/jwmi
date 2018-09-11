package net.acomputerdog.jwmi.nat;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Variant;

public class ReleasableVariant extends Variant.VARIANT implements Releasable {
    public ReleasableVariant(Pointer pointer) {
        super(pointer);
    }

    public ReleasableVariant() {
        super();
    }

    @Override
    public void release() {
        WMIWrapper.INSTANCE.ClearVariant(getPointer());
    }
}
