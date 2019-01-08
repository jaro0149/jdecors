package sk.jdecors.engine.nulls.collector;

import com.google.common.collect.Maps;
import com.sun.istack.internal.NotNull;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import sk.jdecors.annotations.locking.LockedForRead;
import sk.jdecors.annotations.locking.LockedForWrite;
import sk.jdecors.annotations.logging.LogException;
import sk.jdecors.annotations.nulls.NullObject;
import sk.jdecors.interfaces.nulls.NullObjectIdentity;

public class NullObjectsCollectorImpl implements NullObjectsCollector {
    private static NullObjectsCollector nullsLoaderCollector;
    private final Map<Class, DefaultObjectData> defaultObjectDataMap = Maps.newHashMap();

    public static synchronized NullObjectsCollector getInstance() {
        if (Objects.isNull(nullsLoaderCollector)) {
            nullsLoaderCollector = new NullObjectsCollectorImpl();
        }
        return nullsLoaderCollector;
    }

    @Override
    @LockedForWrite
    public void addPermittedFilterUrls(@NotNull Package basePackage,
                                       @NotNull Set<String> urlsOfPackages,
                                       @NotNull Boolean permittedCraftingAtBoot,
                                       @NotNull Boolean permittedCraftingAtRuntime,
                                       @NotNull Boolean crashOnMissingNullObject) {

    }

    @Override
    @LockedForWrite
    public void modifyClassCraftingAtBoot(@NotNull Package basePackage,
                                          @NotNull Set<String> urlsOfPackages,
                                          @NotNull Boolean permittedCraftingAtBoot) {

    }

    @Override
    @LockedForWrite
    public void modifyClassCraftingAtRuntime(@NotNull Package basePackage,
                                             @NotNull Set<String> urlsOfPackages,
                                             @NotNull Boolean permittedCraftingAtRuntime) {

    }

    @Override
    @LockedForWrite
    public void modifyCrashOnMissingNullObject(@NotNull Package basePackage,
                                               @NotNull Set<String> urlsOfPackages,
                                               @NotNull Boolean crashOnMissingNullObject) {

    }

    @Override
    @LockedForWrite
    public void removePermittedFilterUrls(@NotNull Package basePackage, @NotNull Set<String> urlsOfPackage) {

    }

    @Override
    @LockedForRead
    @LogException(loggingLevel = LogException.LevelDispatcher.SEVERE)
    public Object readNullObject(@NotNull Class nullObjectType,
                                 @NotNull Class subjectDeclaredType,
                                 @NotNull String identification) {
        if (nullObjectType.equals(NullObjectIdentity.class)) {
            // todo: find usable null object using subjectDeclaredType parameter
            return null;
        } else {
            return getNullObject(nullObjectType, identification);
        }
    }

    private Object getNullObject(Class objectType, String identification) {
        final DefaultObjectData defaultObjectData = defaultObjectDataMap.get(objectType);
        if (Objects.isNull(defaultObjectData)) {
            throw new IllegalArgumentException(String.format(
                    "Cannot find null object with identification '%s': Class '%s' that constructs null object"
                            + " hasn't been registered in nulls objects collector.",
                    identification,
                    objectType));
        } else {
            final Optional<Object> nullObjectOpt = defaultObjectData.getDefaultObjects().stream()
                    .filter(nullObject -> filterNullObject(identification, nullObject))
                    .findAny();
            if (nullObjectOpt.isPresent()) {
                return nullObjectOpt.get();
            } else if (defaultObjectData.permittedCraftingAtRuntime.getValue()) {
                // todo: try to craft an instance at runtime
                return null;
            } else if (defaultObjectData.crashOnMissingNullObject.getValue()) {
                throw new IllegalArgumentException(String.format(
                        "Cannot find null object with identification '%s': Null object class '%s' hasn't been "
                                + "associated with object that is identified by such string. Correct an issue in "
                                + "source code or try to disable crashing on missing null object feature.",
                        identification,
                        objectType));
            } else {
                return null;
            }
        }
    }

    private static boolean filterNullObject(String identification, Object nullObject) {
        if (nullObject instanceof NullObjectIdentity) {
            final String identifier = ((NullObjectIdentity) nullObject).getIdentifier();
            return identifier.equals(identification);
        } else {
            return identification.equals(NullObject.DEFAULT_IDENTIFICATION);
        }
    }

    @SuppressWarnings("unused")
    private static Set<URL> getUrls() {
        final ClassLoader[] classloaders = {
                NullObjectsCollectorImpl.class.getClassLoader(),
                Thread.currentThread().getContextClassLoader()
        };
        return Arrays.stream(classloaders)
                .filter(classLoader -> classLoader instanceof URLClassLoader)
                .flatMap(classLoader -> Arrays.stream(((URLClassLoader) classLoader).getURLs()))
                .collect(Collectors.toSet());
    }
}