package net.nonswag.tnl.listener.api.bridge;

import net.nonswag.tnl.bridge.Bridge;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.api.bridge.packet.PacketUtil;
import net.nonswag.tnl.listener.api.bridge.packet.packets.PacketPlayOutLogin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.UnexpectedException;

/*******************************************************
 * Copyright (C) 2019-2023 NonSwag kirschnerdavid2466@gmail.com
 *
 * This file is part of TNLListener and was created at the 10/31/20
 *
 * TNLListener can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/

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
                                    proxyServer.readPacket(PacketUtil.decode(packet));
                                } else {
                                    throw new UnexpectedException("The packet can't be cull");
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
