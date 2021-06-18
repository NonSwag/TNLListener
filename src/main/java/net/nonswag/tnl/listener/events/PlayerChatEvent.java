package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
}
