package net.nonswag.tnl.bridge.proxy;

import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListener;
import net.nonswag.tnl.listener.api.object.List;

import javax.annotation.Nonnull;
import java.net.Socket;

public abstract class PacketHandler {

    public static void readPacket(@Nonnull Socket socket, @Nonnull Packet<? extends PacketListener> packet) {
        String encode = packet.encode(packet);
    }

    public static void sendPacket(@Nonnull ConnectedServer server, @Nonnull Packet<PacketListener> packet) {
        try {
            server.getPrintWriter().write(packet.encode(packet) + "\n");
            server.getPrintWriter().flush();
        } catch (Throwable t) {
            Bridge.stacktrace(t, "Error while writing packet '" + packet.getName() + "'");
        }
    }

    public static void sendPackets(@Nonnull ConnectedServer server, @Nonnull List<Packet<PacketListener>> packets) {
        for (Packet<PacketListener> packet : packets.getObjects()) {
            sendPacket(server, packet);
        }
    }
}
