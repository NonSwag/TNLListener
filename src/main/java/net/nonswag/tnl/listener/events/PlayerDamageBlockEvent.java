package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;

public class PlayerDamageBlockEvent extends TNLEvent {

    @Nonnull
    private final TNLPlayer player;
    @Nonnull
    private final Block block;
    @Nonnull
    private final BlockDamageType blockDamageType;
    private boolean update = false;

    public PlayerDamageBlockEvent(@Nonnull TNLPlayer player, @Nonnull Block block, @Nonnull BlockDamageType blockDamageType) {
        this.player = player;
        this.block = block;
        this.blockDamageType = blockDamageType;
    }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    @Nonnull
    public Block getBlock() {
        return block;
    }

    @Nonnull
    public BlockDamageType getBlockDamageType() {
        return blockDamageType;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public enum BlockDamageType {
        START_DESTROY_BLOCK,
        ABORT_DESTROY_BLOCK,
        STOP_DESTROY_BLOCK,
        UNKNOWN,
        ;

        BlockDamageType() {
        }

        @Nonnull
        public static BlockDamageType fromString(@Nonnull String string) {
            try {
                return valueOf(string.toUpperCase());
            } catch (Exception ignored) {
                return UNKNOWN;
            }
        }

        @Nonnull
        public static BlockDamageType fromObject(@Nonnull Object object) {
            return fromString(object.toString());
        }
    }
}
