package frinx.jdecors.engine.nulls.loader;

import com.sun.istack.internal.NotNull;
import frinx.jdecors.annotations.locking.LockedForRead;
import frinx.jdecors.annotations.locking.LockedForWrite;
import java.util.function.Function;

@SuppressWarnings("unused")
final class NullsLoaderImpl implements NullsLoader {
    private NullsLoaderState nullsLoaderState;
    private Boolean invokedNullsLoader = false;

    NullsLoaderImpl(@NotNull NullsLoaderMemento initState) {
        nullsLoaderState = initState;
    }

    @Override
    @LockedForWrite
    public void startNullsLoader() {
        invokedNullsLoader = true;
    }

    @Override
    @LockedForWrite
    public void stopNullsLoader() {
        invokedNullsLoader = false;
    }

    @Override
    @LockedForRead
    public Boolean isNullsLoaderInvoked() {
        return invokedNullsLoader;
    }

    @Override
    @LockedForWrite
    public void setNullsLoaderState(@NotNull NullsLoaderMemento snapshot) {
        this.nullsLoaderState.loadSnapshot(snapshot);
    }

    @Override
    @LockedForWrite
    public void getAndSetNullsLoaderState( @NotNull Function<NullsLoaderMemento, NullsLoaderMemento> transformer) {
        final NullsLoaderMemento updatedState = transformer.apply(nullsLoaderState.createSnapshot());
        this.nullsLoaderState.loadSnapshot(updatedState);
    }

    @Override
    @LockedForRead
    public NullsLoaderMemento getNullsLoaderState() {
        return nullsLoaderState.createSnapshot();
    }

    @Override
    public void close() {
        stopNullsLoader();
        NullsLoaderFactory.releaseNullsLoader(this);
    }
}