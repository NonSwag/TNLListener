package net.nonswag.tnl.listener.v1_16_R1.eventhandler;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class PlayerBottleFillEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private boolean cancelled;
    private ItemStack itemStack;
    private Block block;

    public PlayerBottleFillEvent(boolean async, Player player, ItemStack itemStack, Block block) {

        super(async);
        this.player = player;
        this.cancelled = false;
        this.itemStack = itemStack;
        this.block = block;

    }

    public ItemStack getItemStack() { return itemStack; }

    public Player getPlayer() { return player; }

    public void setItemStack(ItemStack itemStack) { this.itemStack = itemStack; }

    public void setPlayer(Player player) { this.player = player; }

    public void setBlock(Block block) { this.block = block; }

    public Block getBlock() { return block; }

    @Override
    public String toString() {
        return "PlayerBottleFillEvent{" +
                "player=" + player +
                ", cancelled=" + cancelled +
                ", itemStack=" + itemStack +
                ", block=" + block +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException { return super.clone(); }

    @Override
    protected void finalize() throws Throwable { super.finalize(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerBottleFillEvent that = (PlayerBottleFillEvent) o;
        return cancelled == that.cancelled &&
                Objects.equals(player, that.player) &&
                Objects.equals(itemStack, that.itemStack) &&
                Objects.equals(block, that.block);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, cancelled, itemStack, block);
    }

    @Override
    public String getEventName() { return super.getEventName(); }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

}
