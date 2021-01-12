package net.nonswag.tnl.bridge.handler;

import net.nonswag.tnl.bridge.packet.PacketUtil;
import net.nonswag.tnl.cloud.api.system.Console;
import net.nonswag.tnl.listener.enumerations.InternetProtocolAddress;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class PacketAdapter {

    PacketAdapter() {
        new Thread(() -> {
            while(!ConnectionHandler.getServerSocket().isClosed()) {
                try {
                    Socket socket = ConnectionHandler.getServerSocket().accept();
                    InternetProtocolAddress address = new InternetProtocolAddress(socket.getInetAddress().getHostAddress(), socket.getPort());
                    Console.print("§8[§fConnected§8-§fServer] §aIncoming bridge connection from §8'§6" + address.getAsString() + "§8'");
                    InputStream inputStream = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    Object packet;
                    while (!socket.isClosed() && socket.isConnected() && (packet = reader.readLine()) != null) {
                        try {
                            PacketHandler.readPacket(socket, PacketUtil.decode(packet.toString()));
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                    ConnectionHandler.stop();
                    return;
                }
            }
        }).start();
    }
}
