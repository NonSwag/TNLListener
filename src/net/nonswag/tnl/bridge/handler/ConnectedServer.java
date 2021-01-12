package net.nonswag.tnl.bridge.handler;

import net.nonswag.tnl.listener.api.server.Server;

import javax.annotation.Nonnull;
import java.net.Socket;

public class ConnectedServer {

    @Nonnull private final Socket socket;
    @Nonnull private final Server server;

    public ConnectedServer(@Nonnull Socket socket, @Nonnull Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Nonnull
    public Socket getSocket() {
        return socket;
    }

    @Nonnull
    public Server getServer() {
        return server;
    }
}
