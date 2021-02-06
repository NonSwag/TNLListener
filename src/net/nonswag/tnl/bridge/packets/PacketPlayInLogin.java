package net.nonswag.tnl.bridge.packets;

import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListenerPlayIn;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PacketPlayInLogin implements Packet<PacketListenerPlayIn> {

    @Nonnull private final String serverName;
    @Nonnull private final String forwardingSecret;

    public PacketPlayInLogin(@Nonnull String serverName, @Nonnull String forwardingSecret) {
        this.serverName = serverName;
        this.forwardingSecret = forwardingSecret;
    }

    @Nonnull
    public String getServerName() {
        return serverName;
    }

    @Nonnull
    public String getForwardingSecret() {
        return forwardingSecret;
    }

    @Override
    public String toString() {
        return "PacketPlayInLogin{" +
                "serverName='" + serverName + '\'' +
                ", forwardingSecret='" + forwardingSecret + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacketPlayInLogin that = (PacketPlayInLogin) o;
        return serverName.equals(that.serverName) && forwardingSecret.equals(that.forwardingSecret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverName, forwardingSecret);
    }
}
