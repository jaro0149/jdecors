package frinx.jdecors.engine.nulls.loader;

import com.google.common.collect.Maps;
import com.sun.istack.internal.NotNull;
import frinx.jdecors.annotations.logging.LogException;
import frinx.jdecors.engine.nulls.models.SecretToken;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class NullsLoaderFactory {
    private static final Logger LOGGER = Logger.getLogger(NullsLoaderFactory.class.getName());
    private static final Map<SecretToken, NullsLoader> NULLS_LOADERS = Maps.newHashMap();

    private NullsLoaderFactory() {
    }

    public static synchronized NullsLoader getNullsLoader(@NotNull Integer secret, @NotNull Package basePackage) {
        final SecretToken secretToken = new SecretToken(secret, basePackage);
        return getNullsLoader(secretToken);
    }

    @LogException(loggingLevel = LogException.LevelDispatcher.SEVERE)
    public static synchronized NullsLoader getNullsLoader(@NotNull SecretToken secretToken) {
        final boolean thereIsTokenOfSamePackage = NULLS_LOADERS.keySet().stream()
                .anyMatch(token -> token.getPackage().equals(secretToken.getPackage()));
        if (thereIsTokenOfSamePackage) {
            if (NULLS_LOADERS.containsKey(secretToken)) {
                return NULLS_LOADERS.get(secretToken);
            } else {
                throw new IllegalArgumentException(String.format(
                        "Package '%s' is already associated with different key (invalid input secret).",
                        secretToken.getPackage()));
            }
        } else {
            final NullsLoader nullsLoader = new NullsLoaderImpl(createInitialLoaderState(secretToken.getPackage()));
            NULLS_LOADERS.put(secretToken, nullsLoader);
            return nullsLoader;
        }
    }

    public static synchronized void releaseNullsLoader(@NotNull Integer secret, @NotNull Package basePackage) {
        final SecretToken secretToken = new SecretToken(secret, basePackage);
        releaseNullsLoader(secretToken);
    }

    public static synchronized void releaseNullsLoader(@NotNull SecretToken secretToken) {
        final NullsLoader remove = NULLS_LOADERS.remove(secretToken);
        if (Objects.isNull(remove)) {
            LOGGER.log(
                    Level.WARNING,
                    "Token with package name '{0}' hasn't been registered - nothing is released.",
                    secretToken.getPackage());
        }
    }

    public static synchronized void releaseNullsLoader(@NotNull NullsLoader nullsLoader) {
        final Optional<SecretToken> secretToken = NULLS_LOADERS.entrySet().stream()
                .filter(secretTokenNullsLoaderEntry -> secretTokenNullsLoaderEntry.getValue().equals(nullsLoader))
                .map(Map.Entry::getKey)
                .findFirst();
        if (secretToken.isPresent()) {
            NULLS_LOADERS.remove(secretToken.get());
        } else {
            LOGGER.log(
                    Level.WARNING,
                    "NullsLoader '{0}' hasn't been registered - nothing is released.",
                    nullsLoader);
        }
    }

    private static NullsLoaderMemento createInitialLoaderState(Package basicPackage) {
        return new NullsLoaderMementoBuilder()
                .allowPackageUrls(basicPackage.getName() + ".*")
                .crashOnMissingNullObject()
                .denyClassCraftingAtBoot()
                .denyClassCraftingAtRuntime()
                .build();
    }
}