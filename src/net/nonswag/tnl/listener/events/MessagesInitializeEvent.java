package net.nonswag.tnl.listener.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class MessagesInitializeEvent extends Event {

    @Nonnull
    private static final HandlerList handlers = new HandlerList();

    public MessagesInitializeEvent() {
    }

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return handlers;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
