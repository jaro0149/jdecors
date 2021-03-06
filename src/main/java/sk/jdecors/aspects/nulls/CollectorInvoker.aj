package sk.jdecors.aspects.nulls;

import com.google.common.collect.Sets;
import sk.jdecors.engine.nulls.collector.NullObjectsCollector;
import sk.jdecors.engine.nulls.collector.NullObjectsCollectorImpl;
import sk.jdecors.engine.nulls.loader.NullsLoader;
import sk.jdecors.engine.nulls.loader.NullsLoaderMemento;
import sk.jdecors.engine.nulls.loader.NullsLoaderMementoBuilder;
import java.util.Set;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect("perthis(inspectedContext())")
public class CollectorInvoker {
    private Boolean startedNullsLoader = false;

    @Pointcut("within(sk.jdecors.engine.nulls.loader.NullsLoader+)")
    public void inspectedContext() {
    }

    @Pointcut(value = "inspectedContext() && this(loader)",
            argNames = "loader")
    public void loaderObjectInContext(NullsLoader loader) {
    }

    @Pointcut(value = "execution(* startNullsLoader(..)) "
            + "&& loaderObjectInContext(loader)",
            argNames = "loader")
    public void startingOfNullsLoader(NullsLoader loader) {
    }

    @Pointcut(value = "execution(* stopNullsLoader(..)) "
            + "&& loaderObjectInContext(loader)",
            argNames = "loader")
    public void stoppingOfNullsLoader(NullsLoader loader) {
    }

    @Pointcut("execution(* sk.jdecors.engine.nulls.loader.NullsLoaderState.loadSnapshot(NullsLoaderMemento)) "
            + "&& args(stateSnapshot)")
    public void loadStateSnapshotPointcut(NullsLoaderMemento stateSnapshot) {
    }

    @SuppressWarnings("SpringAopErrorsInspection")
    @Pointcut(value = "cflow(inspectedContext()) "
            + "&& loadStateSnapshotPointcut(stateSnapshot) "
            + "&& this(nullsLoader)",
            argNames = "stateSnapshot,nullsLoader")
    public void loadingOfStateSnapshotFromNullsLoader(NullsLoaderMemento stateSnapshot, NullsLoader nullsLoader) {
    }

    @After(value = "startingOfNullsLoader(loader)",
            argNames = "loader")
    public synchronized void startNullsLoader(NullsLoader loader) {
        if (!startedNullsLoader) {
            startedNullsLoader = true;
            final NullsLoaderMemento oldNullsLoaderState = new NullsLoaderMementoBuilder().build();
            final NullsLoaderMemento newNullsLoaderState = loader.getNullsLoaderState();
            final Package basePackage = loader.getBasePackage();
            processStateDiff(basePackage, oldNullsLoaderState, newNullsLoaderState);
        }
    }

    @After(value = "stoppingOfNullsLoader(loader)",
            argNames = "loader")
    public synchronized void stopNullsLoader(NullsLoader loader) {
        if (startedNullsLoader) {
            startedNullsLoader = false;
            final NullsLoaderMemento oldNullsLoaderState = loader.getNullsLoaderState();
            final NullsLoaderMemento newNullsLoaderState = new NullsLoaderMementoBuilder().build();
            final Package basePackage = loader.getBasePackage();
            processStateDiff(basePackage, oldNullsLoaderState, newNullsLoaderState);
        }
    }

    @Around(value = "loadingOfStateSnapshotFromNullsLoader(stateSnapshot, nullsLoader)",
            argNames = "joinPoint,stateSnapshot,nullsLoader")
    public synchronized Object writeNewStateData(ProceedingJoinPoint joinPoint,
                                                 NullsLoaderMemento stateSnapshot,
                                                 NullsLoader nullsLoader) throws Throwable {
        if (startedNullsLoader) {
            final NullsLoaderMemento oldNullsLoaderState = nullsLoader.getNullsLoaderState();
            final Package basePackage = nullsLoader.getBasePackage();
            final Object returnedData = joinPoint.proceed(new Object[] {stateSnapshot});
            processStateDiff(basePackage, oldNullsLoaderState, stateSnapshot);
            return returnedData;
        }
        return joinPoint.proceed(new Object[] {stateSnapshot});
    }

    private static void processStateDiff(Package basePackage,
                                         NullsLoaderMemento oldNullsLoaderState,
                                         NullsLoaderMemento newNullsLoaderState) {
        final NullObjectsCollector collector = NullObjectsCollectorImpl.getInstance();
        final Set<String> oldAllowedPackageUrls = oldNullsLoaderState.getAllowedPackageUrls();
        final Set<String> newAllowedPackageUrls = newNullsLoaderState.getAllowedPackageUrls();
        final Set<String> toBeAddedUrls = Sets.difference(newAllowedPackageUrls, oldAllowedPackageUrls);
        final Set<String> toBeRemovedUrls = Sets.difference(oldAllowedPackageUrls, newAllowedPackageUrls);
        final Set<String> toBeUpdatedUrls = Sets.difference(oldAllowedPackageUrls, toBeRemovedUrls);
        final Boolean oldCraftAtBoot = oldNullsLoaderState.getPermittedCraftingAtBoot();
        final Boolean oldCraftAtRuntime = oldNullsLoaderState.getPermittedCraftingAtRuntime();
        final Boolean oldCrashing = oldNullsLoaderState.getCrashOnMissingNullObject();

        collector.removePermittedFilterUrls(basePackage, toBeRemovedUrls);
        collector.addPermittedFilterUrls(
                basePackage,
                toBeAddedUrls,
                newNullsLoaderState.getPermittedCraftingAtBoot(),
                newNullsLoaderState.getPermittedCraftingAtRuntime(),
                newNullsLoaderState.getCrashOnMissingNullObject());
        if (oldCraftAtBoot != newNullsLoaderState.getPermittedCraftingAtBoot()) {
            collector.modifyClassCraftingAtBoot(basePackage, toBeUpdatedUrls, !oldCraftAtBoot);
        }
        if (oldCrashing != newNullsLoaderState.getCrashOnMissingNullObject()) {
            collector.modifyCrashOnMissingNullObject(basePackage, toBeUpdatedUrls, !oldCrashing);
        }
        if (oldCraftAtRuntime != newNullsLoaderState.getPermittedCraftingAtRuntime()) {
            collector.modifyClassCraftingAtRuntime(basePackage, toBeUpdatedUrls, !oldCraftAtRuntime);
        }
    }
}