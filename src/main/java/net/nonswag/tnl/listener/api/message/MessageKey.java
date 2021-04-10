package net.nonswag.tnl.listener.api.message;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageKey {
    @Nonnull
    private static final List<MessageKey> keys = new ArrayList<>();

    @Nonnull
    public static MessageKey LOG_INFO = new MessageKey("log-info", true);
    @Nonnull
    public static MessageKey LOG_WARN = new MessageKey("log-warn", true);
    @Nonnull
    public static MessageKey LOG_ERROR = new MessageKey("log-error", true);
    @Nonnull
    public static MessageKey LOG_DEBUG = new MessageKey("log-debug", true);
    @Nonnull
    public static MessageKey CHAT_FORMAT = new MessageKey("chat-format", true);
    @Nonnull
    public static MessageKey PREFIX = new MessageKey("prefix", true);
    @Nonnull
    public static MessageKey SERVER_BRAND = new MessageKey("server-brand", true);
    @Nonnull
    public static MessageKey ITEM_NAME_STANDARD = new MessageKey("item-name-standard", true);
    @Nonnull
    public static MessageKey ITEM_NAME_RARE = new MessageKey("item-name-rare", true);
    @Nonnull
    public static MessageKey ITEM_NAME_EPIC = new MessageKey("item-name-epic", true);
    @Nonnull
    public static MessageKey KICKED_SPAMMING = new MessageKey("kicked-spamming", true);

    @Nonnull
    public static MessageKey NO_PERMISSION = new MessageKey("no-permission");
    @Nonnull
    public static MessageKey UNKNOWN_COMMAND = new MessageKey("unknown-command");
    @Nonnull
    public static MessageKey DISABLED_COMMAND = new MessageKey("disabled-command");
    @Nonnull
    public static MessageKey PLAYER_COMMAND = new MessageKey("player-command");
    @Nonnull
    public static MessageKey CONSOLE_COMMAND = new MessageKey("console-command");
    @Nonnull
    public static MessageKey KICKED = new MessageKey("kicked");
    @Nonnull
    public static MessageKey FIRST_JOIN_MESSAGE = new MessageKey("first-join-message");
    @Nonnull
    public static MessageKey JOIN_MESSAGE = new MessageKey("join-message");
    @Nonnull
    public static MessageKey QUIT_MESSAGE = new MessageKey("quit-message");
    @Nonnull
    public static MessageKey WORLD_SAVED = new MessageKey("world-saved");
    @Nonnull
    public static MessageKey CHANGED_GAMEMODE = new MessageKey("changed-gamemode");
    @Nonnull
    public static MessageKey PLAYER_NOT_ONLINE = new MessageKey("player-not-online");
    @Nonnull
    public static MessageKey NOT_A_PLAYER = new MessageKey("not-a-player");

    @Nonnull
    private final String key;
    private final boolean systemMessage;

    public MessageKey(@Nonnull String key) {
        this(key, false);
    }

    public MessageKey(@Nonnull String key, boolean systemMessage) {
        this.key = key;
        this.systemMessage = systemMessage;
        getKeys().add(this);
    }

    @Nonnull
    public String getKey() {
        return key;
    }

    public boolean isSystemMessage() {
        return systemMessage;
    }

    @Override
    public String toString() {
        return "MessageKey{" +
                "key='" + key + '\'' +
                ", systemMessage=" + systemMessage +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageKey that = (MessageKey) o;
        return systemMessage == that.systemMessage && key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, systemMessage);
    }

    @Nonnull
    public static List<MessageKey> getKeys() {
        return keys;
    }
}
