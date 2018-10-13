package net.acomputerdog.jwmi.nat;

import com.sun.jna.IntegerType;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinNT;
import net.acomputerdog.jwmi.ex.WMIException;

import static com.sun.jna.platform.win32.Variant.*;

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

    public Object getJavaType() {
        Object value = getValue();

        switch (getVarType().intValue()) {
            case VT_EMPTY:
            case VT_NULL:
                return null;
            case VT_BOOL:
            case VT_BYREF | VT_BOOL:
                return ((OaIdl.VARIANT_BOOL) value).booleanValue();
            case VT_BSTR:
            case VT_BYREF | VT_BSTR:
                return value.toString();
            case VT_UI1:
            case VT_BYREF | VT_UI1:
            case VT_I1:
            case VT_BYREF | VT_I1:
                return ((IntegerType) value).byteValue();
            case VT_I2:
            case VT_BYREF | VT_I2:
            case VT_UI2:
            case VT_BYREF | VT_UI2:
                return ((IntegerType) value).shortValue();
            case VT_I4:
            case VT_BYREF | VT_I4:
            case VT_BYREF | VT_UI4:
            case VT_UI4:
            case VT_BYREF | VT_INT:
            case VT_BYREF | VT_UINT:
            case VT_INT:
            case VT_UINT:
                return ((IntegerType) value).intValue();
            case VT_I8:
            case VT_BYREF | VT_I8:
            case VT_UI8:
            case VT_BYREF | VT_UI8:
                return ((IntegerType) value).longValue();
            case VT_R4:
            case VT_BYREF | VT_R4:
                return ((IntegerType) value).floatValue();
            case VT_R8:
            case VT_BYREF | VT_R8:
                return ((IntegerType) value).doubleValue();
            default:
                throw new UnsupportedOperationException(String.format("Unsupported variant type: 0x%08X\n", getVarType().intValue()));
        }
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
            throw new WMIException(String.format("Error clearing variant: 0x%08X\n", hresult.intValue()), hresult, this.getPointer());
        }
    }

    @Override
    public boolean isReleased() {
        return released;
    }

    @Override
    protected void finalize() {
        if (!released) {
            System.err.printf("Variant was not cleared: %s\n", this.getPointer().toString());

            this.release();
        }
    }
}
