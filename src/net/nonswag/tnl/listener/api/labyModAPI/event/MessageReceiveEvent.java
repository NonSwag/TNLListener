package net.nonswag.tnl.listener.api.labyModAPI.event;

import com.google.gson.JsonElement;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class MessageReceiveEvent extends Event {

    private final static HandlerList handlerList = new HandlerList();

    private final Player player;
    private final String messageKey;
    private final JsonElement jsonElement;

    public MessageReceiveEvent(Player player, String messageKey, JsonElement jsonElement) {
        this.player = player;
        this.messageKey = messageKey;
        this.jsonElement = jsonElement;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public JsonElement getJsonElement() {
        return jsonElement;
    }
}
