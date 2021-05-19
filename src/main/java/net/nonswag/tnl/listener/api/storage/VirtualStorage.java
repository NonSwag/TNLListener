package net.nonswag.tnl.listener.api.storage;

import net.nonswag.tnl.listener.api.object.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class VirtualStorage {

    @Nonnull
    private final HashMap<String, Object> storage = new HashMap<>();

    @Nonnull
    private HashMap<String, Object> getStorage() {
        return storage;
    }

    @Nonnull
    public Objects<?> get(@Nonnull String key) {
        return new Objects<>(getStorage().get(key));
    }

    @Nonnull
    public <T> Objects<T> get(@Nonnull String key, @Nonnull Class<? extends T> clazz) {
        if (clazz.isInstance(getStorage().get(key))) {
            return new Objects<>((T) getStorage().get(key));
        }
        return (Objects<T>) Objects.EMPTY;
    }

    public <T> void put(@Nonnull String key, @Nullable T value) {
        getStorage().put(key, value);
    }

    public void removeAfter(@Nonnull String key, long millis) {
        new Thread(() -> {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException ignored) {
            } finally {
                remove(key);
            }
        }).start();
    }

    @Nonnull
    public <T> Objects<T> getOrDefault(@Nonnull String key, @Nonnull T object, Class<? extends T> clazz) {
        if (clazz.isInstance(getStorage().get(key))) {
            return new Objects<>((T) getStorage().get(key));
        }
        return new Objects<>(object);
    }

    @Nonnull
    public <T> Objects<T> getOrDefault(@Nonnull String key, @Nonnull T object) {
        if (object.getClass().isInstance(getStorage().get(key))) {
            return new Objects<>((T) getStorage().get(key));
        }
        return new Objects<>(object);
    }

    @Nonnull
    public Objects<Number> getNumber(@Nonnull String key) {
        return get(key, Number.class);
    }

    @Nonnull
    public <T> Objects<List<T>> getList(@Nonnull String key, @Nonnull Class<? extends T> clazz) {
        return (Objects<List<T>>) get(key);
    }

    @Nonnull
    public Objects<String> getString(@Nonnull String key) {
        return get(key, String.class);
    }

    public boolean remove(@Nonnull String key, @Nonnull Object value) {
        return getStorage().remove(key, value);
    }

    public void remove(@Nonnull String key) {
        getStorage().remove(key);
    }

    public boolean containsKey(@Nonnull String key) {
        return getStorage().containsKey(key);
    }

    public boolean containsValue(@Nonnull Object value) {
        return getStorage().containsValue(value);
    }

    public <T> boolean compareInstance(@Nonnull String key, @Nonnull Class<? extends T> clazz) {
        return containsKey(key) && get(key, clazz).hasValue();
    }
}
