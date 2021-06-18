package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;

public class PlayerInteractAtEntityEvent extends TNLEvent {

    @Nonnull
    private final TNLPlayer player;
    @Nonnull
    private final Entity entity;

    public PlayerInteractAtEntityEvent(@Nonnull TNLPlayer player, @Nonnull Entity entity) {
        this.player = player;
        this.entity = entity;
    }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    @Nonnull
    public Entity getEntity() {
        return entity;
    }
}
