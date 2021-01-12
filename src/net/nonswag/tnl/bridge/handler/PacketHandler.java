package net.nonswag.tnl.bridge.handler;

import net.nonswag.tnl.bridge.packet.Packet;
import net.nonswag.tnl.listener.NMSMain;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

class PacketHandler {

    static void readPacket(Socket socket, Packet<?> packet) {
        if (packet == null) {
            throw new NullPointerException("The Packet can't be null");
        } else if (socket == null) {
            throw new NullPointerException("The Socket can't be null");
        } else {
            System.out.println(packet.encode(packet));
        }
    }

    static void sendPacket(ConnectedServer server, Packet<?> packet) {
        try {
            OutputStream outputStream = server.getSocket().getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream);
            writer.write(packet + "\n");
            writer.flush();
            writer.close();
        } catch (Throwable t) {
            NMSMain.stacktrace(t, "Error while writing packet");
        }
    }
}
