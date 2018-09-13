package net.acomputerdog.jwmi.nat;

/**
 * Represents a resources that can be released.  Implements AutoClosable for use in code structures such as
 *
 * try (Releasable r = getSomeReleasable()) {
 *      r.doSomething();
 * }
 */
public interface Releasable extends AutoCloseable {

    /**
     * Releases any resources held by this object
     *
     * @throws IllegalStateException if this object has already been released
     */
    void release();

    /**
     * Checks if this object has been released.
     * @return return true if this object has been released, false otherwise
     */
    boolean isReleased();

    @Override
    default void close() {
        this.release();
    }
}
