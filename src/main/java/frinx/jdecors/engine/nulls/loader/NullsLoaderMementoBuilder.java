package frinx.jdecors.engine.nulls.loader;

import com.google.common.collect.Sets;
import frinx.jdecors.annotations.logging.LogException;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class NullsLoaderMementoBuilder extends NullsLoaderMemento {

    public NullsLoaderMementoBuilder() {
    }

    public NullsLoaderMementoBuilder(NullsLoaderMemento memento) {
        // memento is immutable, so it is safe to assign its values to builder
        super.allowedPackageUrls = memento.allowedPackageUrls;
        super.permittedCraftingAtBoot = memento.permittedCraftingAtBoot;
    }

    public NullsLoaderMementoBuilder allowPackageUrls(Set<String> allowedPackageUrls) {
        super.allowedPackageUrls = Sets.newHashSet(allowedPackageUrls);
        return this;
    }

    public NullsLoaderMementoBuilder allowPackageUrls(String... allowedPackageUrls) {
        super.allowedPackageUrls = Sets.newHashSet(allowedPackageUrls);
        return this;
    }

    public NullsLoaderMementoBuilder appendToAllowedPackageUrls(Set<String> allowedPackageUrls) {
        super.allowedPackageUrls.addAll(allowedPackageUrls);
        return this;
    }

    public NullsLoaderMementoBuilder appendToAllowedPackageUrls(String... allowedPackageUrls) {
        super.allowedPackageUrls.addAll(Sets.newHashSet(allowedPackageUrls));
        return this;
    }

    public NullsLoaderMementoBuilder removeFromAllowedPackageUrls(Set<String> allowedPackageUrls) {
        super.allowedPackageUrls.removeAll(allowedPackageUrls);
        return this;
    }

    public NullsLoaderMementoBuilder removeFromAllowedPackageUrls(String... allowedPackageUrls) {
        super.allowedPackageUrls.removeAll(Sets.newHashSet(allowedPackageUrls));
        return this;
    }

    public NullsLoaderMementoBuilder permitClassCraftingAtBoot() {
        super.permittedCraftingAtBoot = true;
        return this;
    }

    public NullsLoaderMementoBuilder denyClassCraftingAtBoot() {
        super.permittedCraftingAtBoot = false;
        return this;
    }

    public NullsLoaderMementoBuilder crashOnMissingNullObject() {
        super.crashOnMissingNullObject = true;
        return this;
    }

    public NullsLoaderMementoBuilder dontCrashOnMissingNullObject() {
        super.crashOnMissingNullObject = false;
        return this;
    }

    public NullsLoaderMementoBuilder permitClassCraftingAtRuntime() {
        super.permittedCraftingAtBoot = true;
        return this;
    }

    public NullsLoaderMementoBuilder denyClassCraftingAtRuntime() {
        super.permittedCraftingAtBoot = false;
        return this;
    }

    @LogException(loggingLevel = LogException.LevelDispatcher.SEVERE)
    public NullsLoaderMemento build() {
        Objects.requireNonNull(
                permittedCraftingAtBoot,
                "Class crafting at boot must be specified.");
        Objects.requireNonNull(
                permittedCraftingAtRuntime,
                "Class crafting at runtime must be specified."
        );
        Objects.requireNonNull(
                crashOnMissingNullObject,
                "Crashing on missing null object must be specified."
        );
        Objects.requireNonNull(
                allowedPackageUrls,
                "Allowed package URLs must be specified.");

        final NullsLoaderMemento nullsLoaderMemento = new NullsLoaderMemento();
        nullsLoaderMemento.setPermittedClassCrafting(permittedCraftingAtBoot);
        nullsLoaderMemento.setAllowedPackageUrls(allowedPackageUrls);
        return nullsLoaderMemento;
    }
}