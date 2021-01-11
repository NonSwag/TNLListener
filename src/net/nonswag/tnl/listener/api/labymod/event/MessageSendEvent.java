package net.nonswag.tnl.listener.api.labymod.event;

import com.google.gson.JsonElement;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class MessageSendEvent extends Event implements Cancellable {

    private final static HandlerList handlerList = new HandlerList();

    private Player player;
    private String messageKey;
    private JsonElement jsonElement;
    private boolean cancelled;

    public MessageSendEvent(Player player, String messageKey, JsonElement jsonElement, boolean cancelled) {
        setPlayer(player);
        setMessageKey(messageKey);
        setJsonElement(jsonElement);
        setCancelled(cancelled);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setJsonElement(JsonElement jsonElement) {
        this.jsonElement = jsonElement;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public JsonElement getJsonElement() {
        return jsonElement;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMessageKey() {
        return messageKey;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
