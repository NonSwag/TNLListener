package net.nonswag.tnl.listener.api.serverAPI;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class Server {

    private final String name;
    private final InetSocketAddress inetSocketAddress;
    private static final HashMap<String, Server> servers = new HashMap<>();

    public Server(String name, InetSocketAddress inetSocketAddress) {
        this.name = name;
        this.inetSocketAddress = inetSocketAddress;
        servers.put(this.getName(), this);
    }

    public boolean isOnline() {
        try {
            Socket socket = new Socket(getInetSocketAddress().getAddress(), getInetSocketAddress().getPort());
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    public String getName() {
        return name;
    }

    public static Collection<Server> getServers() {
        return servers.values();
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
}
