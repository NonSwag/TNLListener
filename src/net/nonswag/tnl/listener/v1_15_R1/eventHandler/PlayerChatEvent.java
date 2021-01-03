package net.nonswag.tnl.listener.v1_15_R1.eventHandler;

import net.nonswag.tnl.listener.v1_15_R1.api.playerAPI.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class PlayerChatEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    @Nonnull private final TNLPlayer player;
    @Nonnull private String message;
    @Nullable private String format = null;
    private boolean cancelled = false;

    public PlayerChatEvent(@Nonnull TNLPlayer player, @Nonnull String message) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.message = message;
    }

    public boolean isCommand() { return getMessage().startsWith("/"); }

    @Nullable
    public String getFormat() {
        return format;
    }

    public void setFormat(@Nullable String format) {
        this.format = format;
    }

    public void setMessage(@Nonnull String message) {
        this.message = message;
    }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    @Nonnull
    public String getMessage() {
        return message;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    @Override
    public String toString() {
        return "PlayerChatEvent{" +
                "player=" + player +
                ", message='" + message + '\'' +
                ", format='" + format + '\'' +
                ", cancelled=" + cancelled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerChatEvent that = (PlayerChatEvent) o;
        return cancelled == that.cancelled && player.equals(that.player) && message.equals(that.message) && Objects.equals(format, that.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, message, format, cancelled);
    }
}
