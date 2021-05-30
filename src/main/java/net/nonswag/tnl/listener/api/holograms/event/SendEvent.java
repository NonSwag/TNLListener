package net.nonswag.tnl.listener.api.holograms.event;

import net.nonswag.tnl.listener.api.entity.TNLArmorStand;
import net.nonswag.tnl.listener.api.holograms.Hologram;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;
import java.util.List;

public class SendEvent extends PlayerHologramEvent {

    @Nonnull
    private final List<TNLArmorStand> armorStands;

    public SendEvent(@Nonnull Hologram hologram, @Nonnull TNLPlayer player, @Nonnull List<TNLArmorStand> armorStands) {
        super(hologram, player);
        this.armorStands = armorStands;
    }

    @Nonnull
    public List<TNLArmorStand> getArmorStands() {
        return armorStands;
    }
}
