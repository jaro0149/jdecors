package frinx.jdecors.interfaces.nulls;

public interface ClosableWithRuntimeException extends AutoCloseable {
    @Override
    void close() throws RuntimeException;
}