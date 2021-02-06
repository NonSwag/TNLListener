package net.nonswag.tnl.bridge.proxy;

import net.nonswag.tnl.api.event.EventManager;
import net.nonswag.tnl.bridge.*;
import net.nonswag.tnl.bridge.events.PacketEvent;
import net.nonswag.tnl.bridge.events.ServerConnectEvent;
import net.nonswag.tnl.cloud.api.system.Console;
import net.nonswag.tnl.listener.enumerations.InternetProtocolAddress;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class PacketAdapter {

    PacketAdapter() {
        new Thread(() -> {
            while(ConnectionHandler.getServerSocket() != null && !ConnectionHandler.getServerSocket().isClosed()) {
                try {
                    Socket socket = ConnectionHandler.getServerSocket().accept();
                    ServerConnectEvent event = new ServerConnectEvent(socket);
                    EventManager.callEvent(event);
                    if (event.isCancelled()) {
                        socket.close();
                        continue;
                    }
                    InternetProtocolAddress address = new InternetProtocolAddress(socket.getInetAddress().getHostAddress(), socket.getPort());
                    Console.print("§8[§fremote-connection§8] §aIncoming bridge connection from §8'§6" + address.getAsString() + "§8'");
                    InputStream inputStream = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    Object packet;
                    while (!socket.isClosed() && socket.isConnected() && (packet = reader.readLine()) != null) {
                        try {
                            Packet<? extends PacketListenerPlayIn> decode = PacketUtil.decode(packet.toString(), socket);
                            if (decode != null) {
                                PacketEvent packetEvent = new PacketEvent(socket, decode, ChannelDirection.IN);
                                EventManager.callEvent(packetEvent);
                                if (!packetEvent.isCancelled()) {
                                    PacketHandler.readPacket(socket, decode);
                                }
                            }
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
