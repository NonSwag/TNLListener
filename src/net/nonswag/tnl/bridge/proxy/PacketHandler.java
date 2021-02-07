package net.nonswag.tnl.bridge.proxy;

import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListener;
import net.nonswag.tnl.bridge.PacketUtil;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.api.object.List;

import javax.annotation.Nonnull;
import java.net.Socket;

public abstract class PacketHandler {

    public static void readPacket(@Nonnull Socket socket, @Nonnull Packet<? extends PacketListener> packet) {
        System.out.println(packet.encode(packet));
    }

    public static void sendPacket(@Nonnull ConnectedServer server, @Nonnull Packet<? extends PacketListener> packet) {
        try {
            server.getPrintWriter().write(PacketUtil.encode(packet) + "\n");
            server.getPrintWriter().flush();
        } catch (Throwable t) {
            NMSMain.stacktrace(t, "Error while writing packet");
        }
    }

    public static void sendPackets(@Nonnull ConnectedServer server, @Nonnull List<Packet<PacketListener>> packets) {
        for (Packet<? extends PacketListener> packet : packets.getObjects()) {
            sendPacket(server, packet);
        }
    }
}
