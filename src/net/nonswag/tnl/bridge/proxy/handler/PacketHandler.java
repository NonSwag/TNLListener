package net.nonswag.tnl.bridge.proxy.handler;

import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.listener.NMSMain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.Socket;

public abstract class PacketHandler {

    public static void readPacket(@Nullable Socket socket, @Nullable Packet<?> packet) {
        if (packet == null) {
            throw new NullPointerException("The Packet can't be null");
        } else if (socket == null) {
            throw new NullPointerException("The Socket can't be null");
        } else {
            System.out.println(packet.encode(packet));
        }
    }

    public static void sendPacket(@Nonnull ConnectedServer server, @Nonnull Packet<?> packet) {
        try {
            server.getPrintWriter().write(packet + "\n");
            server.getPrintWriter().flush();
        } catch (Throwable t) {
            NMSMain.stacktrace(t, "Error while writing packet");
        }
    }
}
