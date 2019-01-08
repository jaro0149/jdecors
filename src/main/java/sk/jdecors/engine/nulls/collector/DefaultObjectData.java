package sk.jdecors.engine.nulls.collector;

import com.google.common.collect.Sets;
import sk.utils.DefaultMapEntry;
import sk.utils.MapEntry;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
class DefaultObjectData {
    static final Boolean DEFAULT_PERMITTED_CRAFTING_AT_BOOT = false;
    static final Boolean DEFAULT_PERMITTED_CRAFTING_AT_RUNTIME = false;
    static final Boolean DEFAULT_CRASH_ON_MISSING_NULL_OBJECT = false;

    Set<Object> defaultObjects = Sets.newHashSet();
    Set<Package> basePackages = Sets.newHashSet();
    Map.Entry<Package, Boolean> permittedCraftingAtBoot = new DefaultMapEntry<>(DEFAULT_PERMITTED_CRAFTING_AT_BOOT);
    Map.Entry<Package, Boolean> permittedCraftingAtRuntime
            = new DefaultMapEntry<>(DEFAULT_PERMITTED_CRAFTING_AT_RUNTIME);
    Map.Entry<Package, Boolean> crashOnMissingNullObject = new DefaultMapEntry<>(DEFAULT_CRASH_ON_MISSING_NULL_OBJECT);

    DefaultObjectData() {
    }

    Set<Object> getDefaultObjects() {
        return Sets.newHashSet(defaultObjects);
    }

    Set<Package> getBasePackages() {
        return Sets.newHashSet(basePackages);
    }

    Map.Entry<Package, Boolean> getPermittedCraftingAtBoot() {
        return new MapEntry<>(permittedCraftingAtBoot.getKey(), permittedCraftingAtBoot.getValue());
    }

    Map.Entry<Package, Boolean> getPermittedCraftingAtRuntime() {
        return new MapEntry<>(permittedCraftingAtRuntime.getKey(), permittedCraftingAtRuntime.getValue());
    }

    Map.Entry<Package, Boolean> getCrashOnMissingNullObject() {
        return new MapEntry<>(crashOnMissingNullObject.getKey(), crashOnMissingNullObject.getValue());
    }
}