package net.nonswag.tnl.bridge.events;

import net.nonswag.tnl.api.event.Event;
import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListenerPlayIn;
import org.json.simple.JSONObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.Socket;
import java.util.Collections;
import java.util.Objects;

public class MessageDecodeEvent extends Event {

    @Nonnull private final String key;
    @Nonnull private final JSONObject value;
    @Nonnull private final Socket socket;
    @Nullable private Packet<? extends PacketListenerPlayIn> packet = null;

    public MessageDecodeEvent(@Nonnull JSONObject message, @Nonnull Socket socket) {
        this.key = String.join("", Collections.singletonList(message.keySet()).get(0));
        this.value = ((JSONObject) message.get(key));
        this.socket = socket;
    }

    @Nonnull
    public String getKey() {
        return key;
    }

    @Nonnull
    public JSONObject getValue() {
        return value;
    }

    @Nonnull
    public Socket getSocket() {
        return socket;
    }

    @Nullable
    public Packet<? extends PacketListenerPlayIn> getPacket() {
        return packet;
    }

    public void setPacket(@Nonnull Packet<? extends PacketListenerPlayIn> packet) {
        this.packet = packet;
    }

    @Override
    public String toString() {
        return "MessageDecodeEvent{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", socket=" + socket +
                ", packet=" + packet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDecodeEvent that = (MessageDecodeEvent) o;
        return key.equals(that.key) && value.equals(that.value) && socket.equals(that.socket) && Objects.equals(packet, that.packet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, socket, packet);
    }
}
