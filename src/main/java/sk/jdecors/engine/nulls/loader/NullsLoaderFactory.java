package sk.jdecors.engine.nulls.loader;

import com.google.common.collect.Maps;
import com.sun.istack.internal.NotNull;
import sk.jdecors.annotations.logging.LogException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings({"WeakerAccess", "unused"})
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
            final NullsLoader nullsLoader = new NullsLoaderImpl(
                    createInitialLoaderState(secretToken.getPackage()),
                    secretToken.getPackage());
            NULLS_LOADERS.put(secretToken, nullsLoader);
            LOGGER.log(
                    Level.INFO,
                    "A new nulls loader with the base package '{0}' has been created and registered.",
                    secretToken.getPackage()
            );
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
        } else {
            LOGGER.log(
                    Level.INFO,
                    "Nulls loader that was associated with package name '{0}', has been successfully released.",
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
            LOGGER.log(
                    Level.INFO,
                    "NullsLoader '{0}' has been successfully released.");
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