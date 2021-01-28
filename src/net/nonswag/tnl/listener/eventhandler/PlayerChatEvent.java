package net.nonswag.tnl.listener.eventhandler;

import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;

public class PlayerChatEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final TNLPlayer player;
    private String message;
    private String format = null;
    private boolean cancelled = false;

    public PlayerChatEvent(TNLPlayer player, String message) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.message = message;
    }

    public boolean isCommand() { return getMessage().startsWith("/"); }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TNLPlayer getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }

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
