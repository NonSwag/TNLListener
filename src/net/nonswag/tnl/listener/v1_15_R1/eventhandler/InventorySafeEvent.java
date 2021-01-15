package net.nonswag.tnl.listener.v1_15_R1.eventhandler;

import net.nonswag.tnl.listener.v1_15_R1.api.player.TNLPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;

public class InventorySafeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final TNLPlayer player;
    private final String inventoryId;

    public InventorySafeEvent(boolean async, TNLPlayer player, String inventoryId) {
        super(async);
        this.player = player;
        this.inventoryId = inventoryId;
    }

    public InventorySafeEvent(TNLPlayer player, String inventoryId) {
        this(false, player, inventoryId);
    }

    public TNLPlayer getPlayer() {
        return player;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    @Override
    public String toString() {
        return "InventoryLoadEvent{" +
                "player=" + player +
                ", inventoryId='" + inventoryId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventorySafeEvent that = (InventorySafeEvent) o;
        return Objects.equals(player, that.player) &&
                Objects.equals(inventoryId, that.inventoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, inventoryId);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}