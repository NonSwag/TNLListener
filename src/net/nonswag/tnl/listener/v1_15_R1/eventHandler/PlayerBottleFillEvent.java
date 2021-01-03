package net.nonswag.tnl.listener.v1_15_R1.eventHandler;

import net.nonswag.tnl.listener.v1_15_R1.api.playerAPI.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PlayerBottleFillEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    @Nonnull private final TNLPlayer player;
    @Nonnull private ItemStack itemStack;
    @Nonnull private Block block;
    private boolean cancelled;

    public PlayerBottleFillEvent(@Nonnull TNLPlayer player, @Nonnull ItemStack itemStack, @Nonnull Block block) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.cancelled = false;
        this.itemStack = itemStack;
        this.block = block;
    }

    @Nonnull
    public ItemStack getItemStack() { return itemStack; }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    public void setItemStack(@Nonnull ItemStack itemStack) { this.itemStack = itemStack; }

    public void setBlock(@Nonnull Block block) { this.block = block; }

    @Nonnull
    public Block getBlock() { return block; }

    @Nonnull
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
        return cancelled == that.cancelled && player.equals(that.player) && itemStack.equals(that.itemStack) && block.equals(that.block);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, itemStack, block, cancelled);
    }
}
