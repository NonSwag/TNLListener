package net.nonswag.tnl.bridge.receiver;

import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListener;
import net.nonswag.tnl.bridge.PacketUtil;
import net.nonswag.tnl.bridge.packets.PacketPlayOutLogin;
import net.nonswag.tnl.bridge.proxy.Bridge;
import net.nonswag.tnl.listener.NMSMain;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.UnexpectedException;

class PacketAdapter {

    PacketAdapter(ProxyServer proxyServer) {
        if (proxyServer != null && proxyServer.getSocket() != null) {
            NMSMain.print("Started bridge on '" + proxyServer.getAddress().getAsString() + "'");
            proxyServer.sendPacket(new PacketPlayOutLogin(NMSMain.getServerName(), Bridge.getForwardingSecret()));
            new Thread(() -> {
                try {
                    InputStream inputStream = proxyServer.getSocket().getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    while (proxyServer.isConnected() && NMSMain.getPlugin().isEnabled()) {
                        try {
                            String packet;
                            while (NMSMain.getPlugin().isEnabled() && proxyServer.isConnected()) {
                                packet = reader.readLine();
                                if (packet != null) {
                                    Packet<? extends PacketListener> decode = PacketUtil.decode(packet);
                                    if (decode != null) {
                                        proxyServer.readPacket(decode);
                                    }
                                } else {
                                    throw new UnexpectedException("The packet can't be null");
                                }
                            }
                        } catch (Throwable ignored) {
                            return;
                        }
                    }
                } catch (Throwable ignored) {
                    proxyServer.disconnect();
                }
            }).start();
        } else {
            NMSMain.stacktrace("Failed to start bridge");
        }
    }
}
