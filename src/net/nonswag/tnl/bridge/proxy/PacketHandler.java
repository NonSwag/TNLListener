package net.nonswag.tnl.bridge.proxy;

import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListener;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class PacketHandler {

    public static void sendPacket(@Nonnull ConnectedServer server, @Nonnull Packet<PacketListener> packet) {
        try {
            server.getPrintWriter().write(packet.encode(packet) + "\n");
            server.getPrintWriter().flush();
        } catch (Throwable t) {
            Bridge.stacktrace(t, "Error while writing packet '" + packet.getName() + "'");
        }
    }

    public static void sendPackets(@Nonnull ConnectedServer server, @Nonnull List<Packet<PacketListener>> packets) {
        for (Packet<PacketListener> packet : packets) {
            sendPacket(server, packet);
        }
    }
}
