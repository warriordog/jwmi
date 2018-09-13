package net.acomputerdog.jwmi.ex;

/**
 * Indicates that an error occurred in native code
 */
public class NativeException extends RuntimeException {
    public NativeException() {
        super();
    }

    public NativeException(String message) {
        super(message);
    }

    public NativeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NativeException(Throwable cause) {
        super(cause);
    }
}
