package net.nonswag.tnl.listener.eventhandler;

import net.minecraft.server.v1_15_R1.Entity;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;

public class EntityDamageByPlayerEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final TNLPlayer player;
    private final Entity entity;
    private boolean cancelled = false;

    public EntityDamageByPlayerEvent(TNLPlayer player, Entity entity) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.entity = entity;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public TNLPlayer getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return "EntityDamageByPlayerEvent{" +
                "player=" + player +
                ", entity=" + entity +
                ", cancelled=" + cancelled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityDamageByPlayerEvent that = (EntityDamageByPlayerEvent) o;
        return cancelled == that.cancelled && player.equals(that.player) && Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, entity, cancelled);
    }
}
