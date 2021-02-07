package net.nonswag.tnl.bridge.receiver;

import net.nonswag.tnl.api.event.EventManager;
import net.nonswag.tnl.bridge.ChannelDirection;
import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListener;
import net.nonswag.tnl.bridge.PacketUtil;
import net.nonswag.tnl.bridge.events.PacketEvent;
import net.nonswag.tnl.bridge.packets.LoginPacket;
import net.nonswag.tnl.listener.NMSMain;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

class PacketAdapter {

    PacketAdapter(@Nonnull ProxyServer proxyServer) {
        if (proxyServer.getSocket() != null) {
            NMSMain.print("Started bridge on '" + proxyServer.getAddress().getAsString() + "'");
            new Thread(() -> {
                PacketHandler.sendPacket(new LoginPacket(NMSMain.getServerName(), NMSMain.getForwardingSecret()));
                try {
                    InputStream inputStream = proxyServer.getSocket().getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String packet;
                    while (NMSMain.getPlugin().isEnabled() && proxyServer.isConnected() && (packet = reader.readLine()) != null) {
                        try {
                            Packet<PacketListener> decode = PacketUtil.decode(packet, proxyServer.getSocket());
                            if (decode != null) {
                                EventManager.callEvent(new PacketEvent(proxyServer.getSocket(), decode, ChannelDirection.BRIDGE_IN));
                            }
                        } catch (Exception e) {
                            NMSMain.stacktrace(e);
                        }
                    }
                } catch (Exception e) {
                    proxyServer.disconnect();
                    NMSMain.stacktrace(e);
                }
            }).start();
        } else {
            NMSMain.stacktrace("Failed to start bridge");
        }
    }
}
