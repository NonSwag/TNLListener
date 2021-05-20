package net.nonswag.tnl.listener.api.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import java.util.Objects;

public class TNLEvent extends Event implements Cancellable {

    @Nonnull
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    public TNLEvent() {
        super(!Bukkit.isPrimaryThread());
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public boolean call() {
        Bukkit.getPluginManager().callEvent(this);
        return !isCancelled();
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public String toString() {
        return "TNLEvent{" +
                "cancelled=" + cancelled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TNLEvent tnlEvent = (TNLEvent) o;
        return cancelled == tnlEvent.cancelled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cancelled);
    }
}
