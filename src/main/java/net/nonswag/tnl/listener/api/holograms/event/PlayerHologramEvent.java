package net.nonswag.tnl.listener.api.holograms.event;

import net.nonswag.tnl.listener.api.holograms.Hologram;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.player.event.PlayerEvent;

import javax.annotation.Nonnull;

public abstract class PlayerHologramEvent extends PlayerEvent {

    @Nonnull
    private final Hologram hologram;

    protected PlayerHologramEvent(@Nonnull Hologram hologram, @Nonnull TNLPlayer player) {
        super(player);
        this.hologram = hologram;
    }

    @Nonnull
    public Hologram getHologram() {
        return hologram;
    }
}
