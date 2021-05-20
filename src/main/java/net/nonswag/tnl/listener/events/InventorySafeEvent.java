package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;
import java.util.Objects;

public class InventorySafeEvent extends TNLEvent {

    @Nonnull
    private final TNLPlayer player;
    @Nonnull
    private final String inventoryId;

    public InventorySafeEvent(@Nonnull TNLPlayer player, @Nonnull String inventoryId) {
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

    @Override
    public String toString() {
        return "InventorySafeEvent{" +
                "player=" + player +
                ", inventoryId='" + inventoryId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InventorySafeEvent that = (InventorySafeEvent) o;
        return player.equals(that.player) && inventoryId.equals(that.inventoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), player, inventoryId);
    }
}
