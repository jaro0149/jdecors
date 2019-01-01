package frinx.jdecors.engine.nulls.loader;

import com.sun.istack.internal.NotNull;
import frinx.jdecors.interfaces.nulls.ClosableWithRuntimeException;
import java.util.function.Function;

public interface NullsLoader extends ClosableWithRuntimeException {

    void startNullsLoader();

    void stopNullsLoader();

    Boolean isNullsLoaderInvoked();

    void setNullsLoaderState(@NotNull NullsLoaderMemento snapshot);

    void getAndSetNullsLoaderState(@NotNull Function<NullsLoaderMemento, NullsLoaderMemento> mementoTransformer);

    NullsLoaderMemento getNullsLoaderState();
}