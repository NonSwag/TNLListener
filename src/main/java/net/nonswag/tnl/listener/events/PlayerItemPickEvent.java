package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PlayerItemPickEvent extends Event implements Cancellable {

    @Nonnull
    private static final HandlerList handlers = new HandlerList();
    @Nonnull
    private final TNLPlayer player;
    private final int slot;
    private boolean cancelled = false;

    public PlayerItemPickEvent(@Nonnull TNLPlayer player, @Nonnull int slot) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.slot = slot;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    public int getSlot() {
        return slot;
    }

    @Nonnull
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

    @Override
    public String toString() {
        return "PlayerItemPickEvent{" +
                "player=" + player +
                ", slot=" + slot +
                ", cancelled=" + cancelled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerItemPickEvent that = (PlayerItemPickEvent) o;
        return slot == that.slot && cancelled == that.cancelled && player.equals(that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, slot, cancelled);
    }
}
