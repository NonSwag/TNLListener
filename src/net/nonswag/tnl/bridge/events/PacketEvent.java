package net.nonswag.tnl.bridge.events;

import net.nonswag.tnl.api.event.Event;
import net.nonswag.tnl.bridge.ChannelDirection;
import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListener;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PacketEvent extends Event {

    @Nonnull private final Packet<? extends PacketListener> packet;
    @Nonnull private final ChannelDirection channelDirection;

    public PacketEvent(@Nonnull Packet<? extends PacketListener> packet, @Nonnull ChannelDirection channelDirection) {
        this.packet = packet;
        this.channelDirection = channelDirection;
    }

    @Nonnull
    public Packet<? extends PacketListener> getPacket() {
        return packet;
    }

    @Nonnull
    public ChannelDirection getChannelDirection() {
        return channelDirection;
    }

    @Override
    public String toString() {
        return "PacketEvent{" +
                "packet=" + packet +
                ", channelDirection=" + channelDirection +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacketEvent that = (PacketEvent) o;
        return packet.equals(that.packet) && channelDirection == that.channelDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(packet, channelDirection);
    }
}
