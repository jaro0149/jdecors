package frinx.jdecors.engine.nulls.loader;

import com.google.common.collect.Sets;
import com.sun.istack.internal.NotNull;
import java.util.Set;

@SuppressWarnings({"unused"})
class NullsLoaderState {
    Set<String> allowedPackageUrls = Sets.newHashSet();
    Boolean permittedCraftingAtBoot = false;
    Boolean permittedCraftingAtRuntime = false;
    Boolean crashOnMissingNullObject = true;

    public Set<String> getAllowedPackageUrls() {
        return Sets.newHashSet(allowedPackageUrls);
    }


    public Boolean getPermittedCraftingAtBoot() {
        return permittedCraftingAtBoot;
    }

    public Boolean getPermittedCraftingAtRuntime() {
        return permittedCraftingAtRuntime;
    }

    public Boolean getCrashOnMissingNullObject() {
        return crashOnMissingNullObject;
    }

    NullsLoaderMemento createSnapshot() {
        final NullsLoaderMemento nullsLoaderMemento = new NullsLoaderMemento();
        nullsLoaderMemento.allowedPackageUrls = Sets.newHashSet(allowedPackageUrls);
        nullsLoaderMemento.permittedCraftingAtBoot = permittedCraftingAtBoot;
        nullsLoaderMemento.permittedCraftingAtRuntime = permittedCraftingAtRuntime;
        nullsLoaderMemento.crashOnMissingNullObject = crashOnMissingNullObject;
        return nullsLoaderMemento;
    }

    void loadSnapshot(@NotNull NullsLoaderMemento snapshot) {
        allowedPackageUrls = Sets.newHashSet(snapshot.allowedPackageUrls);
        permittedCraftingAtBoot = snapshot.permittedCraftingAtBoot;
        permittedCraftingAtRuntime = snapshot.permittedCraftingAtRuntime;
        crashOnMissingNullObject = snapshot.crashOnMissingNullObject;
    }
}