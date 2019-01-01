package frinx.jdecors.engine.nulls.collector;

import com.sun.istack.internal.NotNull;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class NullObjectsCollectorImpl implements NullObjectsCollector {

    private static NullObjectsCollector nullsLoaderCollector;

    public static synchronized NullObjectsCollector getInstance() {
        if (Objects.isNull(nullsLoaderCollector)) {
            nullsLoaderCollector = new NullObjectsCollectorImpl();
        }
        return nullsLoaderCollector;
    }

    @Override
    public void addPermittedFilterUrls(@NotNull Set<String> urlsOfPackages,
                                       @NotNull Boolean permittedCraftingAtBoot,
                                       @NotNull Boolean permittedCraftingAtRuntime,
                                       @NotNull Boolean crashOnMissingNullObject) {

    }

    @Override
    public void modifyClassCraftingAtBoot(@NotNull Set<String> urlsOfPackages,
                                          @NotNull Boolean permittedCraftingAtBoot) {

    }

    @Override
    public void modifyClassCraftingAtRuntime(@NotNull Set<String> urlsOfPackages,
                                             @NotNull Boolean permittedCraftingAtRuntime) {

    }

    @Override
    public void modifyCrashOnMissingNullObject(@NotNull Set<String> urlsOfPackages,
                                               @NotNull Boolean crashOnMissingNullObject) {

    }

    @Override
    public void removePermittedFilterUrls(@NotNull Set<String> urlsOfPackage) {

    }

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