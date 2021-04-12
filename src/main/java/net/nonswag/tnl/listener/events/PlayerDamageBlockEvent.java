package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PlayerDamageBlockEvent extends Event implements Cancellable {

    @Nonnull
    private static final HandlerList handlers = new HandlerList();
    @Nonnull
    private final TNLPlayer player;
    @Nonnull
    private final Block block;
    @Nonnull
    private final BlockDamageType blockDamageType;
    private boolean cancelled = false;
    private boolean update = false;

    public PlayerDamageBlockEvent(@Nonnull TNLPlayer player, @Nonnull Block block, @Nonnull BlockDamageType blockDamageType) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.block = block;
        this.blockDamageType = blockDamageType;
    }

    public static HandlerList getHandlerList() {
        return handlers;
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isUpdate() {
        return update;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
        UNKNOWN,
        ;

        BlockDamageType() {
        }

        @Nonnull
        public static BlockDamageType fromString(@Nonnull String string) {
            try {
                return valueOf(string.toUpperCase());
            } catch (Exception ignored) {
            }
            return UNKNOWN;
        }

        @Nonnull
        public static BlockDamageType fromObject(@Nonnull Object object) {
            try {
                return fromString(object.toString() + "");
            } catch (Exception t) {
                return UNKNOWN;
            }
        }
    }
}
