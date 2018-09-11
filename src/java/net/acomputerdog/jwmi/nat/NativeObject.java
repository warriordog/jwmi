package net.acomputerdog.jwmi.nat;

import com.sun.jna.Pointer;

public class NativeObject {
    private Pointer pointer;

    public NativeObject(Pointer pointer) {
        this.pointer = pointer;
    }

    public Pointer getPointer() {
        return pointer;
    }

    public void setPointer(Pointer pointer) {
        this.pointer = pointer;
    }
}
