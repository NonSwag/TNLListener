package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;

public class InventoryLoadedEvent extends TNLEvent {

    @Nonnull
    private final TNLPlayer player;
    @Nonnull
    private final String inventoryId;

    public InventoryLoadedEvent(@Nonnull TNLPlayer player, @Nonnull String inventoryId) {
        this.player = player;
        this.inventoryId = inventoryId;
    }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    @Nonnull
    public String getInventoryId() {
        return inventoryId;
    }
}
