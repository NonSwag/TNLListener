package net.nonswag.tnl.bridge.events;

import net.nonswag.tnl.api.event.Event;
import net.nonswag.tnl.bridge.proxy.ConnectedServer;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ServerDisconnectEvent extends Event {

    @Nonnull private final ConnectedServer connectedServer;

    public ServerDisconnectEvent(@Nonnull ConnectedServer connectedServer) {
        this.connectedServer = connectedServer;
    }

    @Nonnull
    public ConnectedServer getConnectedServer() {
        return connectedServer;
    }

    @Override
    public String toString() {
        return "ServerDisconnectEvent{" +
                "connectedServer=" + connectedServer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerDisconnectEvent that = (ServerDisconnectEvent) o;
        return connectedServer.equals(that.connectedServer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectedServer);
    }
}
