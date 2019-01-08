package sk.jdecors.engine.nulls.collector;

import com.sun.istack.internal.NotNull;
import java.util.Set;

public interface NullObjectsCollector {

    void addPermittedFilterUrls(@NotNull Package basePackage,
                                @NotNull Set<String> urlsOfPackages,
                                @NotNull Boolean permittedCraftingAtBoot,
                                @NotNull Boolean permittedCraftingAtRuntime,
                                @NotNull Boolean crashOnMissingNullObject);

    void modifyClassCraftingAtBoot(@NotNull Package basePackage,
                                   @NotNull Set<String> urlsOfPackages,
                                   @NotNull Boolean permittedCraftingAtBoot);

    void modifyClassCraftingAtRuntime(@NotNull Package basePackage,
                                      @NotNull Set<String> urlsOfPackages,
                                      @NotNull Boolean permittedCraftingAtRuntime);

    void modifyCrashOnMissingNullObject(@NotNull Package basePackage,
                                        @NotNull Set<String> urlsOfPackages,
                                        @NotNull Boolean crashOnMissingNullObject);

    void removePermittedFilterUrls(@NotNull Package basePackage, @NotNull Set<String> urlsOfPackage);

    Object readNullObject(@NotNull Class nullObjectType,
                          @NotNull Class subjectDeclaredType,
                          @NotNull String identification);
}