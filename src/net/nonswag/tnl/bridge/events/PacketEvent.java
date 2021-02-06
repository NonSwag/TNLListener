package net.nonswag.tnl.bridge.events;

import net.nonswag.tnl.api.event.Event;
import net.nonswag.tnl.bridge.ChannelDirection;
import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListener;

import javax.annotation.Nonnull;
import java.net.Socket;
import java.util.Objects;

public class PacketEvent extends Event {

    @Nonnull private final Socket socket;
    @Nonnull private final Packet<? extends PacketListener> packet;
    @Nonnull private final ChannelDirection channelDirection;

    public PacketEvent(@Nonnull Socket socket, @Nonnull Packet<? extends PacketListener> packet, @Nonnull ChannelDirection channelDirection) {
        this.socket = socket;
        this.packet = packet;
        this.channelDirection = channelDirection;
    }

    @Nonnull
    public Socket getSocket() {
        return socket;
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
                "socket=" + socket +
                ", packet=" + packet +
                ", channelDirection=" + channelDirection +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacketEvent that = (PacketEvent) o;
        return socket.equals(that.socket) && packet.equals(that.packet) && channelDirection == that.channelDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, packet, channelDirection);
    }
}
