package net.nonswag.tnl.bridge.proxy;

import net.nonswag.tnl.api.event.EventManager;
import net.nonswag.tnl.bridge.ChannelDirection;
import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListener;
import net.nonswag.tnl.bridge.PacketUtil;
import net.nonswag.tnl.bridge.events.PacketEvent;
import net.nonswag.tnl.bridge.events.ServerConnectEvent;
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
                    InputStream inputStream = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    Object packet;
                    while (!socket.isClosed() && socket.isConnected() && (packet = reader.readLine()) != null) {
                        try {
                            Packet<PacketListener> decode = PacketUtil.decode(packet.toString(), socket);
                            if (decode != null) {
                                PacketEvent packetEvent = new PacketEvent(socket, decode, ChannelDirection.BRIDGE_IN);
                                EventManager.callEvent(packetEvent);
                                if (!packetEvent.isCancelled()) {
                                    PacketHandler.readPacket(socket, decode);
                                }
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                    Bridge.print("§8[§fconnected-remote§8] §cServer §8'§4" + event.getSocket().getInetAddress().toString() + "§8'§c has disconnected");
                } catch (Throwable t) {
                    t.printStackTrace();
                    ConnectionHandler.stop();
                    return;
                }
            }
        }).start();
    }
}
