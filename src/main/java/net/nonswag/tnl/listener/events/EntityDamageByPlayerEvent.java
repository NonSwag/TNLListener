package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;
import java.util.Objects;

public class EntityDamageByPlayerEvent extends TNLEvent {

    @Nonnull
    private final TNLPlayer player;
    @Nonnull
    private final Entity entity;

    public EntityDamageByPlayerEvent(@Nonnull TNLPlayer player, @Nonnull Entity entity) {
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

    @Override
    public String toString() {
        return "EntityDamageByPlayerEvent{" +
                "player=" + player +
                ", entity=" + entity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityDamageByPlayerEvent that = (EntityDamageByPlayerEvent) o;
        return player.equals(that.player) && entity.equals(that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, entity);
    }
}
