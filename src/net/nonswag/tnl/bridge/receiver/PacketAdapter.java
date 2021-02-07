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
                    while (NMSMain.getPlugin().isEnabled() && proxyServer.isConnected()) {
                        try {
                            packet = reader.readLine();
                            try {
                                if (packet != null) {
                                    Packet<PacketListener> decode = PacketUtil.decode(packet, proxyServer.getSocket());
                                    if (decode != null) {
                                        PacketEvent event = new PacketEvent(proxyServer.getSocket(), decode, ChannelDirection.BRIDGE_IN);
                                        EventManager.callEvent(event);
                                        if (!event.isCancelled()) {
                                            PacketHandler.readPacket(decode);
                                        }
                                    }
                                }
                            } catch (Throwable t) {
                                NMSMain.stacktrace(t);
                            }
                        } catch (Throwable ignored) {
                            return;
                        }
                    }
                } catch (Throwable t) {
                    proxyServer.disconnect();
                    NMSMain.stacktrace(t);
                }
            }).start();
        } else {
            NMSMain.stacktrace("Failed to start bridge");
        }
    }
}
