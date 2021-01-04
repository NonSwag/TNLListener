package net.nonswag.tnl.listener.v1_15_R1.eventHandler;

import net.nonswag.tnl.listener.v1_15_R1.api.playerAPI.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PlayerInteractAtEntityEvent extends Event implements Cancellable {

    @Nonnull private static final HandlerList handlers = new HandlerList();
    @Nonnull private final TNLPlayer player;
    private final int entityId;
    private boolean cancelled = false;

    public PlayerInteractAtEntityEvent(@Nonnull TNLPlayer player, int entityId) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.entityId = entityId;
    }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    public int getEntityId() {
        return entityId;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public String toString() {
        return "PlayerInteractAtEntityEvent{" +
                "player=" + player +
                ", entityId=" + entityId +
                ", cancelled=" + cancelled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerInteractAtEntityEvent that = (PlayerInteractAtEntityEvent) o;
        return entityId == that.entityId && cancelled == that.cancelled && player.equals(that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, entityId, cancelled);
    }
}
