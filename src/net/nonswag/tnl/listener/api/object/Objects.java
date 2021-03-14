package net.nonswag.tnl.listener.api.object;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Objects<V> {

    @Nonnull
    public static final Objects<?> EMPTY = new Objects<>();

    @Nullable
    private final V value;

    public Objects(V value) {
        this.value = value;
    }

    public Objects() {
        this(null);
    }

    @Nullable
    public V getValue() {
        return value;
    }

    @Nonnull
    public V getOrDefault(@Nonnull V defaultValue) {
        return getValue() == null ? defaultValue : getValue();
    }

    @Nonnull
    public V nonnull() {
        if (hasValue()) {
            assert getValue() != null;
            return getValue();
        }
        throw new NullPointerException();
    }

    public boolean hasValue() {
        return getValue() != null;
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
