package net.nonswag.tnl.listener.api.object;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Objects {

    protected Objects() {
    }

    @Nonnull
    public static <V> V getOrDefault(@Nullable V value, @Nonnull V defaultValue) {
        return value == null ? defaultValue : value;
    }

    @Nonnull
    public static <V> V nonnull(@Nullable V value) {
        if (value != null) {
            return value;
        }
        throw new NullPointerException();
    }
}
