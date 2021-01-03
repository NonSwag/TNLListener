package net.nonswag.tnl.listener.v1_14_R1.eventHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;

public class PlayerChatEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private String message;
    private String format = null;
    private boolean cancelled = false;

    public PlayerChatEvent(Player player, String message) {
        super(true);
        this.player = player;
        this.message = message;
    }

    public boolean isCommand() { return getMessage().startsWith("/"); }

    public String getFormat() { return format; }

    public void setFormat(String format) { this.format = format; }

    public void setMessage(String message) { this.message = message; }

    public void setPlayer(Player player) { this.player = player; }

    public String getMessage() { return message; }

    public Player getPlayer() { return this.player; }

    @Override
    public String getEventName() { return super.getEventName(); }

    @Override
    protected void finalize() throws Throwable { super.finalize(); }

    @Override
    protected Object clone() throws CloneNotSupportedException { return super.clone(); }

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
        return cancelled == that.cancelled &&
                Objects.equals(player, that.player) &&
                Objects.equals(message, that.message) &&
                Objects.equals(format, that.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, message, format, cancelled);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

}
