package net.nonswag.tnl.listener.api.server;

import javax.annotation.Nonnull;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class Server {

    @Nonnull private static final HashMap<String, Server> servers = new HashMap<>();

    @Nonnull private final String name;
    @Nonnull private final InetSocketAddress inetSocketAddress;

    private final boolean online;
    private final int playerCount;

    public Server(@Nonnull String name, @Nonnull InetSocketAddress inetSocketAddress) {
        this.name = name;
        this.inetSocketAddress = inetSocketAddress;
        this.playerCount = -1;
        boolean b;
        try {
            Socket socket = new Socket(getInetSocketAddress().getAddress(), getInetSocketAddress().getPort());
            b = true;
        } catch (Throwable ignored) {
            b = false;
        }
        this.online = b;
        servers.put(this.getName(), this);
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public boolean isOnline() {
        return online;
    }

    public static Server wrap(String name) {
        return servers.get(name);
    }

    @Override
    public String toString() {
        return "Server{" +
                "name='" + name + '\'' +
                ", inetSocketAddress=" + inetSocketAddress +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return Objects.equals(name, server.name) &&
                Objects.equals(inetSocketAddress, server.inetSocketAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, inetSocketAddress);
    }

    @Nonnull
    public static Collection<Server> getServers() {
        return servers.values();
    }
}
