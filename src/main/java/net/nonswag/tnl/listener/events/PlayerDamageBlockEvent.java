package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "PlayerDamageBlockEvent{" +
                "player=" + player +
                ", block=" + block +
                ", blockDamageType=" + blockDamageType +
                ", update=" + update +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PlayerDamageBlockEvent that = (PlayerDamageBlockEvent) o;
        return update == that.update && player.equals(that.player) && block.equals(that.block) && blockDamageType == that.blockDamageType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), player, block, blockDamageType, update);
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
