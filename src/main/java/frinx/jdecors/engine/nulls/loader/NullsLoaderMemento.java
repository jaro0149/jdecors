package frinx.jdecors.engine.nulls.loader;

import com.google.common.collect.Sets;
import com.sun.istack.internal.NotNull;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class NullsLoaderMemento extends NullsLoaderState {

    NullsLoaderMemento() {
    }

    @SuppressWarnings("unused")
    public NullsLoaderMemento(@NotNull Set<String> allowedPackageUrls,
                              @NotNull Boolean permittedClassCrafting,
                              @NotNull Boolean crashOnMissingNullObject) {
        setPermittedClassCrafting(permittedClassCrafting);
        setAllowedPackageUrls(allowedPackageUrls);
        setCrashOnMissingNullObject(crashOnMissingNullObject);
    }

    public void setAllowedPackageUrls(@NotNull Set<String> allowedPackageUrls) {
        super.allowedPackageUrls = Sets.newHashSet(allowedPackageUrls);
    }

    public void setPermittedClassCrafting(@NotNull Boolean permittedClassCrafting) {
        super.permittedCraftingAtBoot = permittedClassCrafting;
    }

    public void setCrashOnMissingNullObject(@NotNull Boolean crashOnMissingNullObject) {
        super.crashOnMissingNullObject = crashOnMissingNullObject;
    }

    public void setCrashAtRuntime(@NotNull Boolean craftAtRuntime) {
        super.permittedCraftingAtRuntime = craftAtRuntime;
    }
}