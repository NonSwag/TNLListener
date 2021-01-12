package net.nonswag.tnl.bridge.packet.packets;

import net.nonswag.tnl.bridge.packet.Packet;
import net.nonswag.tnl.bridge.packet.PacketListenerPlayOut;

import java.util.Objects;

public class PacketPlayInLogin implements Packet<PacketListenerPlayOut> {

    private final String serverName;
    private final String forwardingSecret;

    public PacketPlayInLogin(String serverName, String forwardingSecret) {
        this.serverName = serverName;
        this.forwardingSecret = forwardingSecret;
    }

    public PacketPlayInLogin() {
        this(null, null);
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
        PacketPlayInLogin that = (PacketPlayInLogin) o;
        return Objects.equals(serverName, that.serverName) &&
                Objects.equals(forwardingSecret, that.forwardingSecret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverName, forwardingSecret);
    }
}
