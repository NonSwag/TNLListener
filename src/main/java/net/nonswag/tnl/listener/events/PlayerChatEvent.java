package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class PlayerChatEvent extends TNLEvent {

    @Nonnull
    private final TNLPlayer player;
    @Nonnull
    private String message;
    @Nullable
    private String format = null;

    public PlayerChatEvent(@Nonnull TNLPlayer player, @Nonnull String message) {
        this.player = player;
        this.message = message;
    }

    public boolean isCommand() {
        return getMessage().startsWith("/");
    }

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

    @Override
    public String toString() {
        return "PlayerChatEvent{" +
                "player=" + player +
                ", message='" + message + '\'' +
                ", format='" + format + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PlayerChatEvent that = (PlayerChatEvent) o;
        return player.equals(that.player) && message.equals(that.message) && Objects.equals(format, that.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), player, message, format);
    }
}
