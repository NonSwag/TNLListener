package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class PlayerBottleFillEvent extends TNLEvent {

    @Nonnull
    private final TNLPlayer player;
    @Nullable
    private ItemStack itemStack;
    @Nullable
    private Block block;

    public PlayerBottleFillEvent(@Nonnull TNLPlayer player, @Nullable ItemStack itemStack, @Nullable Block block) {
        this.player = player;
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
    public String toString() {
        return "PlayerBottleFillEvent{" +
                "player=" + player +
                ", itemStack=" + itemStack +
                ", block=" + block +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PlayerBottleFillEvent that = (PlayerBottleFillEvent) o;
        return player.equals(that.player) && Objects.equals(itemStack, that.itemStack) && Objects.equals(block, that.block);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), player, itemStack, block);
    }
}
