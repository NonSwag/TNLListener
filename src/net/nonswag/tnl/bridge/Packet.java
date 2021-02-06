package net.nonswag.tnl.bridge;

import net.nonswag.tnl.listener.api.reflection.Reflection;

import javax.annotation.Nonnull;

public interface Packet<T extends PacketListener> {

    @Nonnull
    default String getPath() {
        return getClass().getName();
    }

    @Nonnull
    default String getName() {
        return getClass().getSimpleName();
    }

    @Nonnull
    default String encode(@Nonnull Object clazz) {
        return Reflection.toJsonObject(clazz).toJSONString();
    }
}
