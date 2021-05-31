package net.nonswag.tnl.listener.api.holograms.event;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.holograms.Hologram;

import javax.annotation.Nonnull;

public abstract class HologramEvent extends TNLEvent {

    @Nonnull
    private final Hologram hologram;

    public HologramEvent(@Nonnull Hologram hologram) {
        this.hologram = hologram;
    }

    @Nonnull
    public Hologram getHologram() {
        return hologram;
    }
}
