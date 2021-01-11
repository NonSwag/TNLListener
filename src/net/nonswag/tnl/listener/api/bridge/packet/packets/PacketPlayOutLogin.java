package net.nonswag.tnl.listener.api.bridge.packet.packets;

import net.nonswag.tnl.listener.api.bridge.packet.Packet;
import net.nonswag.tnl.listener.api.bridge.packet.PacketListenerPlayOut;

import java.util.Objects;

/*******************************************************
 * Copyright (C) 2019-2023 NonSwag kirschnerdavid2466@gmail.com
 *
 * This file is part of TNLListener and was created at the 10/31/20
 *
 * TNLListener can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/

public class PacketPlayOutLogin implements Packet<PacketListenerPlayOut> {

    private final String serverName;
    private final String forwardingSecret;

    public PacketPlayOutLogin(String serverName, String forwardingSecret) {
        this.serverName = serverName;
        this.forwardingSecret = forwardingSecret;
    }

    public String getServerName() {
        return serverName;
    }

    public String getForwardingSecret() {
        return forwardingSecret;
    }

    @Override
    public String toString() {
        return "PacketPlayOutLogin{" +
                "serverName='" + serverName + '\'' +
                ", forwardingSecret='" + forwardingSecret + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacketPlayOutLogin that = (PacketPlayOutLogin) o;
        return Objects.equals(serverName, that.serverName) &&
                Objects.equals(forwardingSecret, that.forwardingSecret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverName, forwardingSecret);
    }
}
