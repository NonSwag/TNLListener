package net.nonswag.tnl.listener.api.holograms.event;

import net.nonswag.tnl.listener.api.entity.TNLArmorStand;
import net.nonswag.tnl.listener.api.holograms.Hologram;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;
import java.util.List;

public class SendEvent extends HologramEvent {

    @Nonnull
    private final TNLPlayer player;
    @Nonnull
    private final List<TNLArmorStand> armorStands;

    public SendEvent(@Nonnull TNLPlayer player, @Nonnull Hologram hologram, @Nonnull List<TNLArmorStand> armorStands) {
        super(hologram);
        this.player = player;
        this.armorStands = armorStands;
    }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    @Nonnull
    public List<TNLArmorStand> getArmorStands() {
        return armorStands;
    }
}
