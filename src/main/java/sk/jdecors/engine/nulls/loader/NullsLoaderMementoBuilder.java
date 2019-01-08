package sk.jdecors.engine.nulls.loader;

import com.google.common.collect.Sets;
import com.sun.istack.internal.NotNull;
import sk.jdecors.annotations.locking.LockedForRead;
import sk.jdecors.annotations.locking.LockedForWrite;
import sk.jdecors.annotations.logging.LogException;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class NullsLoaderMementoBuilder {
    private static final int ALLOWED_PACKAGE_URLS_STAMP = 1;
    private static final int PERMITTED_CRAFT_AT_BOOT_STAMP = 2;
    private static final int PERMITTED_CRAFT_AT_RUNTIME_STAMP = 3;
    private static final int CRASH_ON_MISSING_NULL_OBJECT_STAMP = 4;

    private final NullsLoaderMemento nullsLoaderMemento = new NullsLoaderMemento();

    public NullsLoaderMementoBuilder() {
    }

    public NullsLoaderMementoBuilder(@NotNull NullsLoaderMemento memento) {
        nullsLoaderMemento.allowedPackageUrls = memento.allowedPackageUrls;
        nullsLoaderMemento.permittedCraftingAtBoot = memento.permittedCraftingAtBoot;
    }

    @LockedForWrite(stamp = ALLOWED_PACKAGE_URLS_STAMP)
    public NullsLoaderMementoBuilder allowPackageUrls(@NotNull Set<String> allowedPackageUrls) {
        nullsLoaderMemento.allowedPackageUrls = Sets.newHashSet(allowedPackageUrls);
        return this;
    }

    @LockedForWrite(stamp = ALLOWED_PACKAGE_URLS_STAMP)
    public NullsLoaderMementoBuilder allowPackageUrls(@NotNull String... allowedPackageUrls) {
        nullsLoaderMemento.allowedPackageUrls = Sets.newHashSet(allowedPackageUrls);
        return this;
    }

    @LockedForWrite(stamp = ALLOWED_PACKAGE_URLS_STAMP)
    public NullsLoaderMementoBuilder appendToAllowedPackageUrls(@NotNull Set<String> allowedPackageUrls) {
        nullsLoaderMemento.allowedPackageUrls.addAll(allowedPackageUrls);
        return this;
    }

    @LockedForWrite(stamp = ALLOWED_PACKAGE_URLS_STAMP)
    public NullsLoaderMementoBuilder appendToAllowedPackageUrls(@NotNull String... allowedPackageUrls) {
        nullsLoaderMemento.allowedPackageUrls.addAll(Sets.newHashSet(allowedPackageUrls));
        return this;
    }

    @LockedForWrite(stamp = ALLOWED_PACKAGE_URLS_STAMP)
    public NullsLoaderMementoBuilder removeFromAllowedPackageUrls(@NotNull Set<String> allowedPackageUrls) {
        nullsLoaderMemento.allowedPackageUrls.removeAll(allowedPackageUrls);
        return this;
    }

    @LockedForWrite(stamp = ALLOWED_PACKAGE_URLS_STAMP)
    public NullsLoaderMementoBuilder removeFromAllowedPackageUrls(@NotNull String... allowedPackageUrls) {
        nullsLoaderMemento.allowedPackageUrls.removeAll(Sets.newHashSet(allowedPackageUrls));
        return this;
    }

    @LockedForWrite(stamp = PERMITTED_CRAFT_AT_BOOT_STAMP)
    public NullsLoaderMementoBuilder permitClassCraftingAtBoot() {
        nullsLoaderMemento.permittedCraftingAtBoot = true;
        return this;
    }

    @LockedForWrite(stamp = PERMITTED_CRAFT_AT_BOOT_STAMP)
    public NullsLoaderMementoBuilder denyClassCraftingAtBoot() {
        nullsLoaderMemento.permittedCraftingAtBoot = false;
        return this;
    }

    @LockedForWrite(stamp = CRASH_ON_MISSING_NULL_OBJECT_STAMP)
    public NullsLoaderMementoBuilder crashOnMissingNullObject() {
        nullsLoaderMemento.crashOnMissingNullObject = true;
        return this;
    }

    @LockedForWrite(stamp = CRASH_ON_MISSING_NULL_OBJECT_STAMP)
    public NullsLoaderMementoBuilder dontCrashOnMissingNullObject() {
        nullsLoaderMemento.crashOnMissingNullObject = false;
        return this;
    }

    @LockedForWrite(stamp = PERMITTED_CRAFT_AT_RUNTIME_STAMP)
    public NullsLoaderMementoBuilder permitClassCraftingAtRuntime() {
        nullsLoaderMemento.permittedCraftingAtBoot = true;
        return this;
    }

    @LockedForWrite(stamp = PERMITTED_CRAFT_AT_RUNTIME_STAMP)
    public NullsLoaderMementoBuilder denyClassCraftingAtRuntime() {
        nullsLoaderMemento.permittedCraftingAtBoot = false;
        return this;
    }

    @LockedForRead(stamp = ALLOWED_PACKAGE_URLS_STAMP)
    public Set<String> getAllowedPackageUrls() {
        return Sets.newHashSet(nullsLoaderMemento.allowedPackageUrls);
    }

    @LockedForRead(stamp = PERMITTED_CRAFT_AT_BOOT_STAMP)
    public Boolean getPermittedCraftingAtBoot() {
        return nullsLoaderMemento.permittedCraftingAtBoot;
    }

    @LockedForRead(stamp = PERMITTED_CRAFT_AT_RUNTIME_STAMP)
    public Boolean getPermittedCraftingAtRuntime() {
        return nullsLoaderMemento.permittedCraftingAtRuntime;
    }

    @LockedForRead(stamp = CRASH_ON_MISSING_NULL_OBJECT_STAMP)
    public Boolean getCrashOnMissingNullObject() {
        return nullsLoaderMemento.crashOnMissingNullObject;
    }

    @LogException(loggingLevel = LogException.LevelDispatcher.SEVERE)
    @LockedForRead(stamp = {
            ALLOWED_PACKAGE_URLS_STAMP,
            PERMITTED_CRAFT_AT_BOOT_STAMP,
            PERMITTED_CRAFT_AT_RUNTIME_STAMP,
            CRASH_ON_MISSING_NULL_OBJECT_STAMP})
    public NullsLoaderMemento build() {
        Objects.requireNonNull(
                nullsLoaderMemento.permittedCraftingAtBoot,
                "Class crafting at boot must be specified.");
        Objects.requireNonNull(
                nullsLoaderMemento.permittedCraftingAtRuntime,
                "Class crafting at runtime must be specified."
        );
        Objects.requireNonNull(
                nullsLoaderMemento.crashOnMissingNullObject,
                "Crashing on missing null object must be specified."
        );
        Objects.requireNonNull(
                nullsLoaderMemento.allowedPackageUrls,
                "Allowed package URLs must be specified.");

        final NullsLoaderMemento nullsLoaderMemento = new NullsLoaderMemento();
        nullsLoaderMemento.permittedCraftingAtBoot = this.nullsLoaderMemento.permittedCraftingAtBoot;
        nullsLoaderMemento.permittedCraftingAtRuntime = this.nullsLoaderMemento.permittedCraftingAtRuntime;
        nullsLoaderMemento.crashOnMissingNullObject = this.nullsLoaderMemento.crashOnMissingNullObject;
        nullsLoaderMemento.allowedPackageUrls = Sets.newHashSet(this.nullsLoaderMemento.allowedPackageUrls);
        return nullsLoaderMemento;
    }
}