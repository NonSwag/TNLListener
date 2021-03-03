package net.nonswag.tnl.listener.api.object;

import org.json.simple.JSONObject;

import javax.annotation.Nonnull;
import java.util.Objects;

public class Set<K, V> {

    @Nonnull private final K key;
    @Nonnull private final V value;

    public Set(@Nonnull K key, @Nonnull V value) {
        this.value = value;
        this.key = key;
    }

    @Nonnull
    public K getKey() {
        return key;
    }

    @Nonnull
    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put(key.toString(), value.toString());
        return object.toString();
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Set<?, ?> set = (Set<?, ?>) o;
        return key.equals(set.key) && value.equals(set.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
