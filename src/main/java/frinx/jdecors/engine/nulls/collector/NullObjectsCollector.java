package frinx.jdecors.engine.nulls.collector;

import com.sun.istack.internal.NotNull;
import java.util.Set;

public interface NullObjectsCollector {

    void addPermittedFilterUrls(@NotNull Set<String> urlsOfPackages,
                                @NotNull Boolean permittedCraftingAtBoot,
                                @NotNull Boolean permittedCraftingAtRuntime,
                                @NotNull Boolean crashOnMissingNullObject);

    void modifyClassCraftingAtBoot(@NotNull Set<String> urlsOfPackages,
                                   @NotNull Boolean permittedCraftingAtBoot);

    void modifyClassCraftingAtRuntime(@NotNull Set<String> urlsOfPackages,
                                      @NotNull Boolean permittedCraftingAtRuntime);

    void modifyCrashOnMissingNullObject(@NotNull Set<String> urlsOfPackages,
                                        @NotNull Boolean crashOnMissingNullObject);

    void removePermittedFilterUrls(@NotNull Set<String> urlsOfPackage);
}