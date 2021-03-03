package net.nonswag.tnl.listener.api.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.api.serializer.PacketSerializer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class Server {

    @Nonnull private static final HashMap<String, Server> servers = new HashMap<>();

    @Nonnull private final String name;
    @Nonnull private final InetSocketAddress inetSocketAddress;

    private boolean online = false;
    private int playerCount = 0;
    private int maxPlayerCount = 0;
    private long lastUpdateTime = 0;

    public Server(@Nonnull String name, int port) {
        this(name, new InetSocketAddress(port));
    }

    public Server(@Nonnull String name, @Nonnull InetSocketAddress inetSocketAddress) {
        this.name = name;
        this.inetSocketAddress = inetSocketAddress;
        servers.put(this.getName(), this);
        this.update(System.currentTimeMillis());
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
        this.update(System.currentTimeMillis());
        return playerCount;
    }

    public int getMaxPlayerCount() {
        this.update(System.currentTimeMillis());
        return maxPlayerCount;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    private void setOnline(boolean online) {
        this.online = online;
    }

    private void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    private void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public boolean isOnline() {
        this.update(System.currentTimeMillis());
        return online;
    }

    public void update(long time) {
        if (time <= getLastUpdateTime()) {
            return;
        }
        setLastUpdateTime(time);
        new Thread(() -> {
            Socket socket;
            try {
                socket = new Socket();
                socket.connect(getInetSocketAddress(), 1500);
                setOnline(true);
                try {
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    DataOutputStream handshake = new DataOutputStream(buffer);
                    handshake.writeByte(0x00);
                    PacketSerializer.writeVarInt(handshake, 754);
                    PacketSerializer.writeString(handshake, getInetSocketAddress().getHostName());
                    handshake.writeShort(getInetSocketAddress().getPort());
                    PacketSerializer.writeVarInt(handshake, 1);
                    byte[] handshakeMessage = buffer.toByteArray();
                    PacketSerializer.writeVarInt(output, handshakeMessage.length);
                    output.write(handshakeMessage);
                    output.writeByte(0x01);
                    output.writeByte(0x00);
                    byte[] in = new byte[PacketSerializer.readVarInt(input)];
                    input.readFully(in);
                    JsonElement object = new JsonParser().parse(new String(in).substring(3));
                    JsonObject players = object.getAsJsonObject().get("players").getAsJsonObject();
                    setMaxPlayerCount(players.get("max").getAsInt());
                    setPlayerCount(players.get("online").getAsInt());
                } catch (Throwable t) {
                    NMSMain.stacktrace(t);
                }
            } catch (Throwable ignored) {
                setOnline(false);
                setPlayerCount(0);
                setMaxPlayerCount(0);
            }
        }).start();
    }

    @Nullable
    public static Server wrap(@Nonnull String name) {
        return servers.get(name);
    }

    @Nonnull
    public static Server getOrDefault(@Nonnull String name, @Nonnull Server server) {
        Server wrap = wrap(name);
        return wrap != null ? wrap : server;
    }

    @Override
    public String toString() {
        return "Server{" +
                "name='" + name + '\'' +
                ", inetSocketAddress=" + inetSocketAddress +
                ", online=" + online +
                ", playerCount=" + playerCount +
                ", maxPlayerCount=" + maxPlayerCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return online == server.online && playerCount == server.playerCount && maxPlayerCount == server.maxPlayerCount && name.equals(server.name) && inetSocketAddress.equals(server.inetSocketAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, inetSocketAddress, online, playerCount, maxPlayerCount);
    }

    public static Collection<Server> getServers() {
        return servers.values();
    }
}
