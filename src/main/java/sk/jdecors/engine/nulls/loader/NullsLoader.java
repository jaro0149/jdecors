package sk.jdecors.engine.nulls.loader;

import com.sun.istack.internal.NotNull;
import sk.jdecors.interfaces.nulls.ClosableWithRuntimeException;
import java.util.function.Function;

@SuppressWarnings("unused")
public interface NullsLoader extends ClosableWithRuntimeException {

    void startNullsLoader();

    void stopNullsLoader();

    Boolean isNullsLoaderInvoked();

    void setNullsLoaderState(@NotNull NullsLoaderMemento snapshot);

    void getAndSetNullsLoaderState(@NotNull Function<NullsLoaderMemento, NullsLoaderMemento> mementoTransformer);

    NullsLoaderMemento getNullsLoaderState();

    Package getBasePackage();
}