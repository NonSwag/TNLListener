package net.nonswag.tnl.listener.api.chat;

import net.minecraft.server.v1_15_R1.ChatMessage;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Message {

    @Nonnull
    private final List<String> components = new ArrayList<>();

    public Message() {
        getComponents().add("[\"\"");
    }

    @Nonnull
    public Message addText(@Nonnull String text) {
        getComponents().add(",{\"text\":\"" + text + "\"}");
        return this;
    }

    @Nonnull
    public Message addKeyBind(@Nonnull Component.KeyBind keyBind, @Nonnull String text) {
        getComponents().add(",{\"keybind\":\"" + keyBind.getTag() + "\"}");
        return this;
    }

    @Nonnull
    public Message addClickEvent(@Nonnull Component.ClickEvent clickEvent, @Nonnull String text, @Nonnull String value) {
        getComponents().add(",{\"text\":\"" + text + "\",\"clickEvent\":{\"action\":\"" + clickEvent.getEvent() + "\",\"value\":\"" + value + "\"}}");
        return this;
    }

    @Nonnull
    public Message addHoverEvent(@Nonnull Component.HoverEvent hoverEvent, @Nonnull String text, @Nonnull String value) {
        getComponents().add(",{\"text\":\"" + text + "\",\"hoverEvent\":{\"action\":\"" + hoverEvent.getEvent() + "\",\"value\":\"" + value + "\"}}");
        return this;
    }

    @Nonnull
    public List<String> getComponents() {
        return components;
    }

    @Nonnull
    public Message createMessage() {
        getComponents().add("]");
        return this;
    }

    @Nonnull
    public String getMessage() {
        return String.join("", getComponents());
    }

    @Nonnull
    public ChatMessage getChatMessage() {
        return new ChatMessage(getMessage());
    }
}
