package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PlayerInteractEvent extends Event implements Cancellable {

    @Nonnull
    private static final HandlerList handlers = new HandlerList();
    @Nonnull
    private final TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> player;
    @Nonnull
    private final Block block;
    @Nonnull
    private final ItemStack heldItem;
    private boolean cancelled = false;

    public PlayerInteractEvent(@Nonnull TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> player, @Nonnull Block block, @Nonnull ItemStack heldItem) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.block = block;
        this.heldItem = heldItem;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Nonnull
    public ItemStack getHeldItem() {
        return heldItem;
    }

    @Nonnull
    public Block getBlock() {
        return block;
    }

    @Nonnull
    public TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String toString() {
        return "PlayerInteractEvent{" +
                "player=" + player +
                ", block=" + block +
                ", heldItem=" + heldItem +
                ", cancelled=" + cancelled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerInteractEvent that = (PlayerInteractEvent) o;
        return cancelled == that.cancelled && player.equals(that.player) && block.equals(that.block) && Objects.equals(heldItem, that.heldItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, block, heldItem, cancelled);
    }
}
