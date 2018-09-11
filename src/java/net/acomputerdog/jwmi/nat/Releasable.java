package net.acomputerdog.jwmi.nat;

public interface Releasable extends AutoCloseable {

    void release();

    @Override
    default void close() {
        this.release();
    }
}
