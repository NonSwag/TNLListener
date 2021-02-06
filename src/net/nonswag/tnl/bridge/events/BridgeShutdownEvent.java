package net.nonswag.tnl.bridge.events;

import net.nonswag.tnl.api.event.Event;

import javax.annotation.Nonnull;
import java.net.ServerSocket;

public class BridgeShutdownEvent extends Event {

    @Nonnull private final ServerSocket serverSocket;

    public BridgeShutdownEvent(@Nonnull ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Nonnull
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public String toString() {
        return "BridgeShutdownEvent{" +
                "serverSocket=" + serverSocket +
                '}';
    }
}
