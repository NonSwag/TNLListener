package net.nonswag.tnl.listener.v1_16_R1.eventHandler;

import com.sun.istack.internal.NotNull;
import net.minecraft.server.v1_16_R1.PacketPlayInBlockDig;
import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;

public class PlayerDamageBlockEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Block block;
    private final BlockDamageType blockDamageType;
    private boolean cancelled = false;

    public PlayerDamageBlockEvent(@NotNull Player player, @NotNull Block block) {
        super(true);
        this.player = player;
        this.block = block;
        this.blockDamageType = BlockDamageType.UNKNOWN;
    }

    public PlayerDamageBlockEvent(@NotNull Player player, @NotNull Block block, @NotNull BlockDamageType blockDamageType) {
        super(true);
        this.player = player;
        this.block = block;
        this.blockDamageType = blockDamageType;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    public BlockDamageType getBlockDamageType() {
        return blockDamageType;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDamageBlockEvent that = (PlayerDamageBlockEvent) o;
        return cancelled == that.cancelled &&
                Objects.equals(player, that.player) &&
                Objects.equals(block, that.block) &&
                blockDamageType == that.blockDamageType;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, block, blockDamageType, cancelled);
    }

    public Player getPlayer() {
        return this.player;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public String getEventName() {
        return super.getEventName();
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
                NMSMain.runTask(() -> {
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

    public enum BlockDamageType {
        START_DESTROY_BLOCK,
        ABORT_DESTROY_BLOCK,
        STOP_DESTROY_BLOCK,
        DROP_ALL_ITEMS,
        DROP_ITEM,
        RELEASE_USE_ITEM,
        SWAP_ITEM_WITH_OFFHAND,
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
            } else if(packet.d().equals(PacketPlayInBlockDig.EnumPlayerDigType.SWAP_ITEM_WITH_OFFHAND)) {
                return SWAP_ITEM_WITH_OFFHAND;
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

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
