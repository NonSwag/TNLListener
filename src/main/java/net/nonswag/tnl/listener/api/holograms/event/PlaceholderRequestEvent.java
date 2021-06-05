package net.nonswag.tnl.listener.api.holograms.event;

import net.nonswag.tnl.listener.api.holograms.Hologram;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;

public class PlaceholderRequestEvent extends PlayerHologramEvent {

    @Nonnull
    private String line;

    public PlaceholderRequestEvent(@Nonnull Hologram hologram, @Nonnull TNLPlayer player, @Nonnull String line) {
        super(hologram, player);
        this.line = line;
    }

    public void setLine(@Nonnull String line) {
        this.line = line;
    }

    @Nonnull
    public String getLine() {
        return line;
    }
}
