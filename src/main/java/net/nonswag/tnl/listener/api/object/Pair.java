package net.nonswag.tnl.listener.api.object;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Pair<K, V> {

    @Nonnull
    private K key;
    @Nullable
    private V value;

    public Pair(@Nonnull K key, @Nullable V value) {
        this.value = value;
        this.key = key;
    }

    @Nonnull
    public K getKey() {
        return key;
    }

    @Nullable
    public V getValue() {
        return value;
    }

    public void setKey(@Nonnull K key) {
        this.key = key;
    }

    public void setValue(@Nullable V value) {
        this.value = value;
    }
}
