package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

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
}
