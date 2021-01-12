package net.nonswag.tnl.bridge.packet;

import net.nonswag.tnl.listener.api.reflection.Reflection;

public interface Packet<T> {

    default String getPath() {
        return getClass().getName();
    }

    default String getName() {
        return getClass().getSimpleName();
    }

    default String encode(Object clazz) {
        return getName() + Reflection.toJsonObject(clazz).toJSONString();
    }
}
