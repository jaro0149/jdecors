package sk.jdecors.engine.nulls.collector;

import com.google.common.collect.Sets;
import com.sun.istack.internal.NotNull;
import sk.jdecors.annotations.locking.LockedForRead;
import sk.jdecors.annotations.locking.LockedForWrite;
import sk.jdecors.annotations.logging.LogException;
import sk.utils.MapEntry;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
class DefaultObjectDataBuilder {
    private static final int PERMITTED_CRAFTING_AT_BOOT_STAMP = 1;
    private static final int PERMITTED_CRAFTING_AT_RUNTIME_STAMP = 2;
    private static final int CRASH_ON_MISSING_NULL_OBJECT_STAMP = 3;
    private static final int DEFAULT_OBJECTS_STAMP = 4;
    private static final int BASE_PACKAGES_STAMP = 5;

    private final DefaultObjectData buildingDefaultObjectData = new DefaultObjectData();

    DefaultObjectDataBuilder() {}

    DefaultObjectDataBuilder(@NotNull DefaultObjectData defaultObjectData) {
        setBasePackages(defaultObjectData.basePackages);
        setCrashOnMissingNullObject(defaultObjectData.crashOnMissingNullObject);
        setDefaultObjects(defaultObjectData.defaultObjects);
        setPermittedCraftingAtBoot(defaultObjectData.permittedCraftingAtBoot);
        setPermittedCraftingAtRuntime(buildingDefaultObjectData.permittedCraftingAtRuntime);
    }

    @LockedForWrite(stamp = PERMITTED_CRAFTING_AT_BOOT_STAMP)
    DefaultObjectDataBuilder setPermittedCraftingAtBoot(@NotNull Map.Entry<Package, Boolean> permittedCraftingAtBoot) {
        buildingDefaultObjectData.permittedCraftingAtBoot = new MapEntry<>(
                permittedCraftingAtBoot.getKey(),
                permittedCraftingAtBoot.getValue()
        );
        return this;
    }

    @LockedForWrite(stamp = PERMITTED_CRAFTING_AT_BOOT_STAMP)
    DefaultObjectDataBuilder setPermittedCraftingAtBootToDef() {
        buildingDefaultObjectData.permittedCraftingAtBoot = new MapEntry<>(
                null,
                DefaultObjectData.DEFAULT_PERMITTED_CRAFTING_AT_BOOT
        );
        return this;
    }

    @LockedForWrite(stamp = PERMITTED_CRAFTING_AT_RUNTIME_STAMP)
    DefaultObjectDataBuilder setPermittedCraftingAtRuntime(
            @NotNull Map.Entry<Package, Boolean> permittedCraftingAtRuntime) {
        buildingDefaultObjectData.permittedCraftingAtRuntime = new MapEntry<>(
                permittedCraftingAtRuntime.getKey(),
                permittedCraftingAtRuntime.getValue()
        );
        return this;
    }

    @LockedForWrite(stamp = PERMITTED_CRAFTING_AT_RUNTIME_STAMP)
    DefaultObjectDataBuilder setPermittedCraftingAtRuntimeToDef() {
        buildingDefaultObjectData.permittedCraftingAtRuntime = new MapEntry<>(
                null,
                DefaultObjectData.DEFAULT_PERMITTED_CRAFTING_AT_RUNTIME
        );
        return this;
    }

    @LockedForWrite(stamp = CRASH_ON_MISSING_NULL_OBJECT_STAMP)
    DefaultObjectDataBuilder setCrashOnMissingNullObject(
            @NotNull Map.Entry<Package, Boolean> crashOnMissingNullObject) {
        buildingDefaultObjectData.crashOnMissingNullObject = new MapEntry<>(
                crashOnMissingNullObject.getKey(),
                crashOnMissingNullObject.getValue()
        );
        return this;
    }

    @LockedForWrite(stamp = CRASH_ON_MISSING_NULL_OBJECT_STAMP)
    DefaultObjectDataBuilder setCrashOnMissingNullObjectToDef() {
        buildingDefaultObjectData.crashOnMissingNullObject = new MapEntry<>(
                null,
                DefaultObjectData.DEFAULT_CRASH_ON_MISSING_NULL_OBJECT
        );
        return this;
    }

    @LockedForWrite(stamp = DEFAULT_OBJECTS_STAMP)
    DefaultObjectDataBuilder setDefaultObjects(@NotNull Collection<Object> objects) {
        buildingDefaultObjectData.defaultObjects = Sets.newHashSet(objects);
        return this;
    }

    @LockedForWrite(stamp = DEFAULT_OBJECTS_STAMP)
    DefaultObjectDataBuilder addDefaultObject(@NotNull Object object) {
        buildingDefaultObjectData.defaultObjects.add(object);
        return this;
    }

    @LockedForWrite(stamp = DEFAULT_OBJECTS_STAMP)
    DefaultObjectDataBuilder addDefaultObjects(@NotNull Collection<Object> objects) {
        buildingDefaultObjectData.defaultObjects.addAll(objects);
        return this;
    }

    @LockedForWrite(stamp = DEFAULT_OBJECTS_STAMP)
    DefaultObjectDataBuilder removeDefaultObject(@NotNull Object object) {
        buildingDefaultObjectData.defaultObjects.remove(object);
        return this;
    }

    @LockedForWrite(stamp = DEFAULT_OBJECTS_STAMP)
    DefaultObjectDataBuilder removeDefaultObjects(@NotNull Collection<Object> objects) {
        buildingDefaultObjectData.defaultObjects.removeAll(objects);
        return this;
    }

    @LockedForWrite(stamp = DEFAULT_OBJECTS_STAMP)
    DefaultObjectDataBuilder removeAllDefaultObjects() {
        buildingDefaultObjectData.defaultObjects.clear();
        return this;
    }

    @LockedForWrite(stamp = BASE_PACKAGES_STAMP)
    DefaultObjectDataBuilder setBasePackages(@NotNull Collection<Package> basePackages) {
        buildingDefaultObjectData.basePackages = Sets.newHashSet(basePackages);
        return this;
    }

    @LockedForWrite(stamp = BASE_PACKAGES_STAMP)
    DefaultObjectDataBuilder addBasePackage(@NotNull Package basePackage) {
        buildingDefaultObjectData.basePackages.add(basePackage);
        return this;
    }

    @LockedForWrite(stamp = BASE_PACKAGES_STAMP)
    DefaultObjectDataBuilder addBasePackages(@NotNull Collection<Package> basePackages) {
        buildingDefaultObjectData.basePackages.addAll(basePackages);
        return this;
    }

    @LockedForWrite(stamp = BASE_PACKAGES_STAMP)
    DefaultObjectDataBuilder removeBasePackage(@NotNull Package basePackage) {
        buildingDefaultObjectData.basePackages.remove(basePackage);
        return this;
    }

    @LockedForWrite(stamp = BASE_PACKAGES_STAMP)
    DefaultObjectDataBuilder removeBasePackages(@NotNull Collection<Package> basePackages) {
        buildingDefaultObjectData.basePackages.removeAll(basePackages);
        return this;
    }

    @LockedForWrite(stamp = BASE_PACKAGES_STAMP)
    DefaultObjectDataBuilder removeAllBasePackages() {
        buildingDefaultObjectData.basePackages.clear();
        return this;
    }

    @LockedForRead(stamp = DEFAULT_OBJECTS_STAMP)
    Set<Object> getDefaultObjects() {
        return Sets.newHashSet(buildingDefaultObjectData.getDefaultObjects());
    }

    @LockedForRead(stamp = BASE_PACKAGES_STAMP)
    Set<Package> getBasePackages() {
        return Sets.newHashSet(buildingDefaultObjectData.getBasePackages());
    }

    @LockedForRead(stamp = PERMITTED_CRAFTING_AT_BOOT_STAMP)
    Map.Entry<Package, Boolean> getPermittedCraftingAtBoot() {
        return new MapEntry<>(
                buildingDefaultObjectData.permittedCraftingAtBoot.getKey(),
                buildingDefaultObjectData.permittedCraftingAtBoot.getValue());
    }

    @LockedForRead(stamp = PERMITTED_CRAFTING_AT_RUNTIME_STAMP)
    Map.Entry<Package, Boolean> getPermittedCraftingAtRuntime() {
        return new MapEntry<>(
                buildingDefaultObjectData.permittedCraftingAtRuntime.getKey(),
                buildingDefaultObjectData.permittedCraftingAtRuntime.getValue());
    }

    @LockedForRead(stamp = CRASH_ON_MISSING_NULL_OBJECT_STAMP)
    Map.Entry<Package, Boolean> getCrashOnMissingNullObject() {
        return new MapEntry<>(
                buildingDefaultObjectData.crashOnMissingNullObject.getKey(),
                buildingDefaultObjectData.crashOnMissingNullObject.getValue());
    }

    @LockedForRead(stamp = {
            BASE_PACKAGES_STAMP,
            DEFAULT_OBJECTS_STAMP,
            PERMITTED_CRAFTING_AT_BOOT_STAMP,
            PERMITTED_CRAFTING_AT_RUNTIME_STAMP,
            CRASH_ON_MISSING_NULL_OBJECT_STAMP})
    @LogException(loggingLevel = LogException.LevelDispatcher.SEVERE)
    DefaultObjectData build() {
        Objects.requireNonNull(
                buildingDefaultObjectData.defaultObjects,
                "Set of null objects must be specified by non-null reference.");
        Objects.requireNonNull(
                buildingDefaultObjectData.basePackages,
                "Sets of configuring base packages must be specified by non-null reference.");
        Objects.requireNonNull(
                buildingDefaultObjectData.crashOnMissingNullObject,
                "Crashing on null object setting must be specified.");
        Objects.requireNonNull(
                buildingDefaultObjectData.permittedCraftingAtBoot,
                "Permitted class crafting at boot must be specified.");
        Objects.requireNonNull(
                buildingDefaultObjectData.permittedCraftingAtRuntime,
                "Permitted class crafting at runtime must be specified.");

        final DefaultObjectData defaultObjectData = new DefaultObjectData();
        defaultObjectData.defaultObjects = Sets.newHashSet(buildingDefaultObjectData.defaultObjects);
        defaultObjectData.basePackages = Sets.newHashSet(buildingDefaultObjectData.basePackages);
        defaultObjectData.crashOnMissingNullObject = new MapEntry<>(
                buildingDefaultObjectData.getCrashOnMissingNullObject().getKey(),
                buildingDefaultObjectData.getCrashOnMissingNullObject().getValue()
        );
        defaultObjectData.permittedCraftingAtBoot = new MapEntry<>(
                buildingDefaultObjectData.getPermittedCraftingAtBoot().getKey(),
                buildingDefaultObjectData.getPermittedCraftingAtBoot().getValue()
        );
        defaultObjectData.permittedCraftingAtRuntime = new MapEntry<>(
                buildingDefaultObjectData.getPermittedCraftingAtRuntime().getKey(),
                buildingDefaultObjectData.getPermittedCraftingAtRuntime().getValue()
        );
        return defaultObjectData;
    }
}