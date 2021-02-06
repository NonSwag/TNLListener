package net.nonswag.tnl.bridge.events;

import net.nonswag.tnl.api.event.Event;

import javax.annotation.Nonnull;
import java.net.ServerSocket;
import java.util.Objects;

public class BridgeStartEvent extends Event {

    @Nonnull private final ServerSocket serverSocket;

    public BridgeStartEvent(@Nonnull ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Nonnull
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public String toString() {
        return "BridgeStartEvent{" +
                "serverSocket=" + serverSocket +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BridgeStartEvent that = (BridgeStartEvent) o;
        return serverSocket.equals(that.serverSocket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverSocket);
    }
}
