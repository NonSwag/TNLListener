package net.nonswag.tnl.listener.api.holograms.event;

import net.nonswag.tnl.listener.api.holograms.Hologram;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;

public class InteractEvent extends HologramEvent {

    @Nonnull
    private final TNLPlayer player;
    @Nonnull
    private final Type type;

    public InteractEvent(@Nonnull TNLPlayer player, @Nonnull Hologram hologram, @Nonnull Type type) {
        super(hologram);
        this.player = player;
        this.type = type;
    }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
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
