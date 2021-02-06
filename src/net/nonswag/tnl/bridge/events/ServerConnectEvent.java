package net.nonswag.tnl.bridge.events;

import net.nonswag.tnl.api.event.Event;

import javax.annotation.Nonnull;
import java.net.Socket;
import java.util.Objects;

public class ServerConnectEvent extends Event {

    @Nonnull private final Socket socket;

    public ServerConnectEvent(@Nonnull Socket socket) {
        this.socket = socket;
    }

    @Nonnull
    public Socket getSocket() {
        return socket;
    }

    @Override
    public String toString() {
        return "ServerConnectEvent{" +
                "socket=" + socket +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerConnectEvent that = (ServerConnectEvent) o;
        return socket.equals(that.socket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket);
    }
}
