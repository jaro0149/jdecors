package sk.jdecors.engine.nulls.loader;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class NullsLoaderStateLogging {
    private static final Logger LOGGER = Logger.getLogger(NullsLoaderState.class.getName());

    @SuppressWarnings("SpringAopErrorsInspection")
    @Pointcut(value = "cflow(execution(* sk.jdecors.engine.nulls.loader.NullsLoader+(..))) "
            + "&& this(nullsLoader)",
            argNames = "nullsLoader")
    public void nullsLoaderTracing(NullsLoader nullsLoader) {
    }

    @Pointcut(value = "execution(NullsLoaderMemento sk.jdecors.engine.nulls.loader.NullsLoaderState"
            + ".createSnapshot(..)) "
            + "&& nullsLoaderTracing(nullsLoader)",
            argNames = "nullsLoader")
    public void creatingOfSnapshot(NullsLoader nullsLoader) {
    }

    @Pointcut(value = "execution(* sk.jdecors.engine.nulls.loader.NullsLoaderState.loadSnapshot(NullsLoaderMemento)) "
            + "&& this(state) "
            + "&& @args(memento)"
            + "&& nullsLoaderTracing(nullsLoader)",
            argNames = "nullsLoader,state,memento")
    public void loadingOfSnapshot(NullsLoader nullsLoader, NullsLoaderState state, NullsLoaderMemento memento) {
    }

    @AfterReturning(value = "creatingOfSnapshot(nullsLoader)",
            returning = "memento",
            argNames = "nullsLoader,memento")
    public void logCreatedSnapshot(NullsLoader nullsLoader, NullsLoaderMemento memento) {
        LOGGER.log(
                Level.INFO,
                "Memento of nulls loader '{0}' state with data '{1}' has been created.",
                new Object[] {nullsLoader, memento});
    }

    @Around(value = "loadingOfSnapshot(nullsLoader, state, memento)",
            argNames = "proceedingJoinPoint,nullsLoader,state,memento")
    public Object logLoadedSnapshot(ProceedingJoinPoint proceedingJoinPoint,
                                    NullsLoader nullsLoader,
                                    NullsLoaderState state,
                                    NullsLoaderMemento memento) throws Throwable {
        // note: nullsLoaderTracing(..) pointcut disallows weaving loop
        final NullsLoaderMemento originalStateMemento = state.createSnapshot();
        final Object returnValue = proceedingJoinPoint.proceed();
        LOGGER.log(
                Level.INFO,
                "Memento '{0}' has been loaded in place of old state defined by memento '{1}' "
                        + "in nulls loader '{2}'.",
                new Object[]{memento, originalStateMemento, nullsLoader}
        );
        return returnValue;
    }
}