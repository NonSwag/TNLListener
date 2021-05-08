package net.nonswag.tnl.listener.api.object;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

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
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return key.equals(pair.key) && Objects.equals(value, pair.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
