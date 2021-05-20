package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "PlayerInteractAtEntityEvent{" +
                "player=" + player +
                ", entity=" + entity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PlayerInteractAtEntityEvent that = (PlayerInteractAtEntityEvent) o;
        return player.equals(that.player) && entity.equals(that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), player, entity);
    }
}
