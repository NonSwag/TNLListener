package net.nonswag.tnl.bridge.handler;

import net.nonswag.tnl.listener.api.server.Server;

import java.net.Socket;

public class ConnectedServer {

    private final Socket socket;
    private final Server server;

    public ConnectedServer(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public Socket getSocket() {
        return socket;
    }

    public Server getServer() {
        return server;
    }
}
