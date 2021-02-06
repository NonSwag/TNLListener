package net.nonswag.tnl.bridge.proxy;

import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListenerPlayIn;
import net.nonswag.tnl.bridge.PacketListenerPlayOut;
import net.nonswag.tnl.bridge.PacketUtil;
import net.nonswag.tnl.listener.NMSMain;

import javax.annotation.Nonnull;
import java.net.Socket;

public abstract class PacketHandler {

    public static void readPacket(@Nonnull Socket socket, @Nonnull Packet<? extends PacketListenerPlayIn> packet) {
        System.out.println(packet.encode(packet));
    }

    public static void sendPacket(@Nonnull ConnectedServer server, @Nonnull Packet<? extends PacketListenerPlayOut> packet) {
        try {
            server.getPrintWriter().write(PacketUtil.encode(packet) + "\n");
            server.getPrintWriter().flush();
        } catch (Throwable t) {
            NMSMain.stacktrace(t, "Error while writing packet");
        }
    }
}
