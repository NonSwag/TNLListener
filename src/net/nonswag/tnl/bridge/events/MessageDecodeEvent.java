package net.nonswag.tnl.bridge.events;

import net.nonswag.tnl.api.event.Event;
import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListenerPlayIn;
import org.json.simple.JSONObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class MessageDecodeEvent extends Event {

    @Nonnull private final JSONObject message;
    @Nullable private Packet<PacketListenerPlayIn> packet = null;

    public MessageDecodeEvent(@Nonnull JSONObject message) {
        this.message = message;
    }

    @Nonnull
    public JSONObject getMessage() {
        return message;
    }

    @Nullable
    public Packet<PacketListenerPlayIn> getPacket() {
        return packet;
    }

    public void setPacket(@Nonnull Packet<PacketListenerPlayIn> packet) {
        this.packet = packet;
    }

    @Override
    public String toString() {
        return "MessageDecodeEvent{" +
                "message=" + message +
                ", packet=" + packet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDecodeEvent that = (MessageDecodeEvent) o;
        return message.equals(that.message) && Objects.equals(packet, that.packet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, packet);
    }
}
