package net.nonswag.tnl.listener.eventhandler;

import net.minecraft.server.v1_15_R1.PacketPlayInBlockDig;
import net.nonswag.tnl.listener.Loader;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;

public class PlayerDamageBlockEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final TNLPlayer player;
    private final Block block;
    private final BlockDamageType blockDamageType;
    private boolean cancelled = false;

    public PlayerDamageBlockEvent(TNLPlayer player, Block block) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.block = block;
        this.blockDamageType = BlockDamageType.UNKNOWN;
    }

    public PlayerDamageBlockEvent(TNLPlayer player, Block block, BlockDamageType blockDamageType) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.block = block;
        this.blockDamageType = blockDamageType;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public BlockDamageType getBlockDamageType() {
        return blockDamageType;
    }

    public TNLPlayer getPlayer() {
        return player;
    }

    public Block getBlock() {
        return block;
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
        if (isCancelled()) {
            if (getBlockDamageType().equals(BlockDamageType.STOP_DESTROY_BLOCK)
                    || getBlockDamageType().equals(BlockDamageType.START_DESTROY_BLOCK)) {
                Bukkit.getScheduler().runTask(Loader.getInstance(), () -> {
                    getBlock().getState().update();
                    getPlayer().sendBlockChange(getBlock().getLocation(), getBlock().getBlockData());
                    getPlayer().sendBlockChange(getBlock().getRelative(BlockFace.EAST).getLocation(),
                            getBlock().getRelative(BlockFace.EAST).getLocation().getBlock().getBlockData());
                    getPlayer().sendBlockChange(getBlock().getRelative(BlockFace.NORTH).getLocation(),
                            getBlock().getRelative(BlockFace.NORTH).getLocation().getBlock().getBlockData());
                    getPlayer().sendBlockChange(getBlock().getRelative(BlockFace.SOUTH).getLocation(),
                            getBlock().getRelative(BlockFace.SOUTH).getLocation().getBlock().getBlockData());
                    getPlayer().sendBlockChange(getBlock().getRelative(BlockFace.WEST).getLocation(),
                            getBlock().getRelative(BlockFace.WEST).getLocation().getBlock().getBlockData());
                    getPlayer().sendBlockChange(getBlock().getRelative(BlockFace.UP).getLocation(),
                            getBlock().getRelative(BlockFace.UP).getLocation().getBlock().getBlockData());
                    getPlayer().sendBlockChange(getBlock().getRelative(BlockFace.DOWN).getLocation(),
                            getBlock().getRelative(BlockFace.DOWN).getLocation().getBlock().getBlockData());
                });
            } else if(getBlockDamageType().equals(BlockDamageType.DROP_ITEM)
                    || getBlockDamageType().equals(BlockDamageType.DROP_ALL_ITEMS)) {
                getPlayer().updateInventory();
            }
        }
    }

    @Override
    public String toString() {
        return "PlayerDamageBlockEvent{" +
                "player=" + player +
                ", block=" + block +
                ", blockDamageType=" + blockDamageType +
                ", cancelled=" + cancelled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDamageBlockEvent that = (PlayerDamageBlockEvent) o;
        return cancelled == that.cancelled && player.equals(that.player) && block.equals(that.block) && blockDamageType == that.blockDamageType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, block, blockDamageType, cancelled);
    }

    public enum BlockDamageType {
        START_DESTROY_BLOCK,
        ABORT_DESTROY_BLOCK,
        STOP_DESTROY_BLOCK,
        DROP_ALL_ITEMS,
        DROP_ITEM,
        RELEASE_USE_ITEM,
        SWAP_HELD_ITEMS,
        UNKNOWN,
        ;

        BlockDamageType() {
        }

        public static BlockDamageType fromString(String string) {
            try {
                for (BlockDamageType blockDamageType : BlockDamageType.values()) {
                    if (blockDamageType.name().equalsIgnoreCase(string)) {
                        return blockDamageType;
                    }
                }
            } catch (Throwable ignored) {
            }
            return UNKNOWN;
        }

        public static BlockDamageType fromObject(Object object) {
            try {
                return fromString(object.toString() + "");
            } catch (Throwable t) {
                return UNKNOWN;
            }
        }

        public static BlockDamageType fromPacket(PacketPlayInBlockDig packet) {
            if(packet.d().equals(PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK)) {
                return START_DESTROY_BLOCK;
            } else if(packet.d().equals(PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK)) {
                return STOP_DESTROY_BLOCK;
            } else if(packet.d().equals(PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK)) {
                return ABORT_DESTROY_BLOCK;
            } else if(packet.d().equals(PacketPlayInBlockDig.EnumPlayerDigType.SWAP_HELD_ITEMS)) {
                return SWAP_HELD_ITEMS;
            } else if(packet.d().equals(PacketPlayInBlockDig.EnumPlayerDigType.DROP_ALL_ITEMS)) {
                return DROP_ALL_ITEMS;
            } else if(packet.d().equals(PacketPlayInBlockDig.EnumPlayerDigType.DROP_ITEM)) {
                return DROP_ITEM;
            } else if(packet.d().equals(PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM)) {
                return RELEASE_USE_ITEM;
            } else {
                return UNKNOWN;
            }
        }
    }
}
