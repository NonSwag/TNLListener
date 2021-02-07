package net.nonswag.tnl.listener.api.object;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Object<T> {

    @Nullable private final T t;

    public Object(@Nullable T t) {
        this.t = t;
    }

    @Nullable
    public T getT() {
        return t;
    }

    @Nonnull
    public T getOrDefault(@Nonnull T defaultValue) {
        return getT() == null ? defaultValue : getT();
    }

    @Nullable
    public String toString() {
        return getT() == null ? null : getT().toString();
    }
}
