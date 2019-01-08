package sk.jdecors.engine.nulls.loader;

import com.sun.istack.internal.NotNull;
import java.util.Objects;

public class SecretToken {

    private Integer secret;
    private Package aPackage;

    @SuppressWarnings("WeakerAccess")
    public SecretToken(@NotNull Integer secret, @NotNull Package aPackage) {
        this.secret = secret;
        this.aPackage = aPackage;
    }

    @SuppressWarnings("unused")
    public Integer getSecret() {
        return secret;
    }

    public Package getPackage() {
        return aPackage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecretToken that = (SecretToken) o;
        return secret.equals(that.secret) &&
                aPackage.equals(that.aPackage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(secret, aPackage);
    }
}
