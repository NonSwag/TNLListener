package net.nonswag.tnl.listener.v1_8_R3.eventhandler;

import com.sun.istack.internal.NotNull;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;
import java.util.Objects;

public class PlayerChatEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private String message;
    private boolean cancelled;
    private String format;

    public PlayerChatEvent(Player player, String message) {

        super(true);
        this.player = player;
        this.message = message;
        this.cancelled = false;

    }

    @Override
    public String getEventName() { return super.getEventName(); }

    @Override
    protected void finalize() throws Throwable { super.finalize(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerChatEvent that = (PlayerChatEvent) o;
        return cancelled == that.cancelled &&
                Objects.equals(player, that.player) &&
                Objects.equals(message, that.message) &&
                Objects.equals(format, that.format);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException { return super.clone(); }

    @Override
    public String toString() {
        return "PlayerChatEvent{" +
                "player=" + player +
                ", message='" + message + '\'' +
                ", cancelled=" + cancelled +
                ", format='" + format + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, message, cancelled, format);
    }

    public void setMessage(@NotNull String message) { this.message = message; }

    public void setFormat(@Nullable String format) { this.format = format; }

    public void setPlayer(@NotNull Player player) { this.player = player; }

    public String getFormat() { return format; }

    public String getMessage() { return message; }

    public Player getPlayer() { return this.player; }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

}
