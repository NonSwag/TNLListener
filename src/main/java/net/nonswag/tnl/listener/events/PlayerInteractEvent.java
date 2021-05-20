package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PlayerInteractEvent extends TNLEvent {

    @Nonnull
    private final TNLPlayer player;
    @Nonnull
    private final Block block;
    @Nonnull
    private final ItemStack heldItem;

    public PlayerInteractEvent(@Nonnull TNLPlayer player, @Nonnull Block block, @Nonnull ItemStack heldItem) {
        this.player = player;
        this.block = block;
        this.heldItem = heldItem;
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
    public TNLPlayer getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "PlayerInteractEvent{" +
                "player=" + player +
                ", block=" + block +
                ", heldItem=" + heldItem +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PlayerInteractEvent that = (PlayerInteractEvent) o;
        return player.equals(that.player) && block.equals(that.block) && heldItem.equals(that.heldItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), player, block, heldItem);
    }
}
