package net.nonswag.tnl.listener.api.object;

import javax.annotation.Nonnull;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "Set{" +
                "value=" + value +
                ", key=" + key +
                '}';
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Set<?, ?> set = (Set<?, ?>) o;
        return value.equals(set.value) && key.equals(set.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, key);
    }
}
