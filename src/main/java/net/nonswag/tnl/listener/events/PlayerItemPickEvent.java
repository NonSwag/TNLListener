package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;

public class PlayerItemPickEvent extends TNLEvent {

    @Nonnull
    private final TNLPlayer player;
    private final int slot;

    public PlayerItemPickEvent(@Nonnull TNLPlayer player, int slot) {
        this.player = player;
        this.slot = slot;
    }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    public int getSlot() {
        return slot;
    }
}
