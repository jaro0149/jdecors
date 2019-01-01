package frinx.jdecors.engine.nulls.models;

import com.google.common.collect.Sets;
import frinx.jdecors.utils.DefaultMapEntry;
import frinx.jdecors.utils.MapEntry;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class DefaultObjectData {
    private final Set<Object> defaultObjects = Sets.newHashSet();
    private final Set<Package> basePackages = Sets.newHashSet();
    private Map.Entry<Package, Boolean> permittedCraftingAtBoot = new DefaultMapEntry<>(false);
    private Map.Entry<Package, Boolean> permittedCraftingAtRuntime = new DefaultMapEntry<>(false);
    private Map.Entry<Package, Boolean> crashOnMissingNullObject = new DefaultMapEntry<>(false);

    public DefaultObjectData() {
    }

    public Set<Object> getDefaultObjects() {
        return Sets.newHashSet(defaultObjects);
    }

    public Set<Package> getBasePackages() {
        return Sets.newHashSet(basePackages);
    }

    public Map.Entry<Package, Boolean> getPermittedCraftingAtBoot() {
        return new MapEntry<>(permittedCraftingAtBoot.getKey(), permittedCraftingAtBoot.getValue());
    }

    public Map.Entry<Package, Boolean> getPermittedCraftingAtRuntime() {
        return new MapEntry<>(permittedCraftingAtRuntime.getKey(), permittedCraftingAtRuntime.getValue());
    }

    public Map.Entry<Package, Boolean> getCrashOnMissingNullObject() {
        return new MapEntry<>(crashOnMissingNullObject.getKey(), crashOnMissingNullObject.getValue());
    }

    public void setPermittedCraftingAtBoot(Map.Entry<Package, Boolean> permittedCraftingAtBoot) {
        this.permittedCraftingAtBoot = new MapEntry<>(
                permittedCraftingAtBoot.getKey(),
                permittedCraftingAtBoot.getValue());
    }

    public void setPermittedCraftingAtRuntime(Map.Entry<Package, Boolean> permittedCraftingAtRuntime) {
        this.permittedCraftingAtRuntime = new MapEntry<>(
                permittedCraftingAtRuntime.getKey(),
                permittedCraftingAtRuntime.getValue()
        );
    }

    public void setCrashOnMissingNullObject(Map.Entry<Package, Boolean> crashOnMissingNullObject) {
        this.crashOnMissingNullObject = new MapEntry<>(
                crashOnMissingNullObject.getKey(),
                crashOnMissingNullObject.getValue()
        );
    }

    public void addDefaultObject(Object object) {
        defaultObjects.add(object);
    }

    public void addDefaultObjects(Collection<Object> objects) {
        defaultObjects.addAll(objects);
    }

    public void removeDefaultObject(Object object) {
        defaultObjects.remove(object);
    }

    public void removeAllDefaultObjects(Collection<Object> objects) {
        defaultObjects.removeAll(objects);
    }

    public void addBasePackage(Package basePackage) {
        basePackages.add(basePackage);
    }

    public void addBasePackages(Collection<Package> basePackages) {
        this.basePackages.addAll(basePackages);
    }

    public void removeBasePackage(Package basePackage) {
        basePackages.remove(basePackage);
    }

    public void removeBasePackages(Collection<Package> basePackages) {
        this.basePackages.removeAll(basePackages);
    }
}