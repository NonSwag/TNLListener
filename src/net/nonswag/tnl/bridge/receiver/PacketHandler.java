package net.nonswag.tnl.bridge.receiver;

import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListener;
import net.nonswag.tnl.listener.NMSMain;

import javax.annotation.Nonnull;

public abstract class PacketHandler {

    public static void sendPacket(@Nonnull Packet<PacketListener> packet) {
        try {
            if (ProxyServer.getInstance() != null && ProxyServer.getInstance().getWriter() != null) {
                ProxyServer.getInstance().getWriter().write(packet.encode(packet) + "\n");
                ProxyServer.getInstance().getWriter().flush();
            } else {
                NMSMain.stacktrace("Error while writing packet '" + packet.getName() + "'");
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t, "Error while writing packet '" + packet.getName() + "'");
        }
    }
}
