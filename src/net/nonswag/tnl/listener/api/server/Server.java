package net.nonswag.tnl.listener.api.server;

import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.api.serializer.PacketSerializer;
import org.bukkit.Bukkit;
import org.json.JSONObject;

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
    private int maxPlayerCount = Bukkit.getMaxPlayers();

    public Server(@Nonnull String name, @Nonnull InetSocketAddress inetSocketAddress) {
        this.name = name;
        this.inetSocketAddress = inetSocketAddress;
        servers.put(this.getName(), this);
        this.update();
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

    public int getMaxPlayerCount() {
        return maxPlayerCount;
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

    public boolean isOnline() {
        return online;
    }

    public void update() {
        Socket socket;
        try {
            socket = new Socket();
            socket.connect(getInetSocketAddress(), 3000);
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
                JSONObject object = new JSONObject(new String(in).substring(3));
                JSONObject players = object.getJSONObject("players");
                setMaxPlayerCount(players.getInt("max"));
                setPlayerCount(players.getInt("online"));
            } catch (Throwable t) {
                NMSMain.stacktrace(t);
            }
        } catch (Throwable ignored) {
            setOnline(false);
        }
    }

    @Nullable
    public static Server wrap(String name) {
        return servers.get(name);
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

    @Nonnull
    public static Collection<Server> getServers() {
        return servers.values();
    }
}
