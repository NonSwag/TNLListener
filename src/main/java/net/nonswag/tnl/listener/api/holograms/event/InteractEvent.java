package net.nonswag.tnl.listener.api.holograms.event;

import net.nonswag.tnl.listener.api.holograms.Hologram;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;

public class InteractEvent extends PlayerHologramEvent {

    @Nonnull
    private final Type type;

    public InteractEvent(@Nonnull Hologram hologram, @Nonnull TNLPlayer player, @Nonnull Type type) {
        super(hologram, player);
        this.type = type;
    }

    @Nonnull
    public Type getType() {
        return type;
    }

    public enum Type {
        LEFT_CLICK,
        RIGHT_CLICK
    }
}
