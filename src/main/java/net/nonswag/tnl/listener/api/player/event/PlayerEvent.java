package net.nonswag.tnl.listener.api.player.event;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;

public abstract class PlayerEvent extends TNLEvent {

    @Nonnull
    private final TNLPlayer player;

    public PlayerEvent(@Nonnull TNLPlayer player) {
        this.player = player;
    }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }
}
