package net.nonswag.tnl.listener.api.object;

import javax.annotation.Nonnull;

public class Set<V, K> {

    @Nonnull private final V value;
    @Nonnull private final K key;

    public Set(@Nonnull V value, @Nonnull K key) {
        this.value = value;
        this.key = key;
    }

    @Nonnull
    public V getValue() {
        return value;
    }

    @Nonnull
    public K getKey() {
        return key;
    }
}
