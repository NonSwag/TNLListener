package net.nonswag.tnl.listener.v1_14_R1.api.chat;

import net.minecraft.server.v1_14_R1.ChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Message {

    private final List<String> components = new ArrayList<>();

    public Message() {
        getComponents().add("[\"\"");
    }

    public Message addText(String text) {
        getComponents().add(",{\"text\":\"" + text + "\"}");
        return this;
    }

    public Message addKeyBind(Component.KeyBind keyBind, String text) {
        getComponents().add(",{\"keybind\":\"" + keyBind.getTag() + "\"}");
        return this;
    }

    public Message addClickEvent(Component.ClickEvent clickEvent, String text, String value) {
        getComponents().add(",{\"text\":\"" + text + "\",\"clickEvent\":{\"action\":\"" + clickEvent.getEvent() + "\",\"value\":\"" + value + "\"}}");
        return this;
    }

    public Message addHoverEvent(Component.HoverEvent hoverEvent, String text, String value) {
        getComponents().add(",{\"text\":\"" + text + "\",\"hoverEvent\":{\"action\":\"" + hoverEvent.getEvent() + "\",\"value\":\"" + value + "\"}}");
        return this;
    }

    public List<String> getComponents() {
        return components;
    }

    public Message createMessage() {
        getComponents().add("]");
        return this;
    }

    public String getMessage() {
        return String.join("", getComponents());
    }

    public ChatMessage getChatMessage() {
        return new ChatMessage(getMessage());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(components, message.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(components);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Message{" +
                "components=" + components +
                '}';
    }
}
