package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class PlayerBottleFillEvent extends Event implements Cancellable {

    @Nonnull private static final HandlerList handlers = new HandlerList();
    @Nonnull private final TNLPlayer player;
    @Nullable private ItemStack itemStack;
    @Nullable private Block block;
    private boolean cancelled;

    public PlayerBottleFillEvent(TNLPlayer player, @Nullable ItemStack itemStack, @Nullable Block block) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.cancelled = false;
        this.itemStack = itemStack;
        this.block = block;
    }

    @Nullable
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    public void setItemStack(@Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setBlock(@Nullable Block block) {
        this.block = block;
    }

    @Nullable
    public Block getBlock() {
        return block;
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    @Override
    public String toString() {
        return "PlayerBottleFillEvent{" +
                "player=" + player +
                ", itemStack=" + itemStack +
                ", block=" + block +
                ", cancelled=" + cancelled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerBottleFillEvent that = (PlayerBottleFillEvent) o;
        return cancelled == that.cancelled && player.equals(that.player) && Objects.equals(itemStack, that.itemStack) && Objects.equals(block, that.block);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, itemStack, block, cancelled);
    }
}
