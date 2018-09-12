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
     */
    void release();

    @Override
    default void close() {
        this.release();
    }
}
