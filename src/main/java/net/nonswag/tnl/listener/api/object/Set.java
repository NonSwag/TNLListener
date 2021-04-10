package net.nonswag.tnl.listener.api.object;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class Set<K, V> {

    @Nonnull
    private K key;
    @Nullable
    private V value;

    public Set(@Nonnull K key, @Nullable V value) {
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

    @Override
    public String toString() {
        return "Set{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Set<?, ?> set = (Set<?, ?>) o;
        return key.equals(set.key) && Objects.equals(value, set.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
