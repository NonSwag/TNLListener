package net.nonswag.tnl.listener.api.entity;

import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;

public interface TNLEntity {

    int getId();

    @Nonnull
    Entity getBukkitEntity();
}
