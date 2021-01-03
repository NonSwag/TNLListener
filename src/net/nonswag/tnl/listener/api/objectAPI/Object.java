package net.nonswag.tnl.listener.api.objectAPI;

public class Object<T> {

    private final T t;

    public Object(T t) {
        this.t = t;
    }

    public T getT() {
        return t;
    }

    public T getOrDefault(T defaultValue) {
        return getT() == null ? defaultValue : getT();
    }
}
