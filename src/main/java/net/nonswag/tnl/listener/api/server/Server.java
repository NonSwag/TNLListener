package net.nonswag.tnl.listener.api.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.serializer.PacketSerializer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

public class Server {

    @Nonnull
    private final String name;
    @Nonnull
    private final InetSocketAddress inetSocketAddress;

    @Nonnull
    private Status status = Status.OFFLINE;
    private int playerCount = 0;
    private int maxPlayerCount = 0;
    private long lastUpdateTime = 0;

    public Server(@Nonnull String name, int port) {
        this(name, new InetSocketAddress(port));
    }

    public Server(@Nonnull String name, @Nonnull InetSocketAddress inetSocketAddress) {
        this.name = name;
        this.inetSocketAddress = inetSocketAddress;
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
        this.update();
        return playerCount;
    }

    public int getMaxPlayerCount() {
        this.update();
        return maxPlayerCount;
    }

    private long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setStatus(@Nonnull Status status) {
        this.status = status;
    }

    private void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    private void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    private void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Nonnull
    public Status getStatus() {
        this.update();
        return status;
    }

    public void update() {
        long time = System.currentTimeMillis();
        if (time - getLastUpdateTime() < 1000) {
            return;
        }
        setLastUpdateTime(time);
        new Thread(() -> {
            Socket socket;
            try {
                socket = new Socket();
                socket.setSoTimeout(1500);
                socket.connect(getInetSocketAddress(), 1500);
                if (socket.isConnected()) {
                    try {
                        JsonElement jsonElement = sendHandshake(socket);
                        if (jsonElement.isJsonObject()) {
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            if (jsonObject.has("players")) {
                                if (jsonObject.get("players").isJsonObject()) {
                                    JsonObject players = jsonObject.get("players").getAsJsonObject();
                                    if (players.has("max") && players.has("online")) {
                                        setMaxPlayerCount(players.get("max").getAsInt());
                                        setPlayerCount(players.get("online").getAsInt());
                                        setStatus(Status.ONLINE);
                                        return;
                                    }
                                }
                            }
                        }
                    } catch (Exception ignored) {
                    }
                    socket.close();
                }
                setStatus(Status.STARTING);
            } catch (Exception ignored) {
                setStatus(Status.OFFLINE);
            }
            setPlayerCount(0);
            setMaxPlayerCount(0);
        }, "update thread").start();
    }

    @Nonnull
    private JsonElement sendHandshake(@Nonnull Socket socket) throws IOException {
        if (socket.isConnected()) {
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
            for (int i = 0; i < 4; i++) {
                if (new String(in).substring(i).startsWith("{")) {
                    return new JsonParser().parse(new String(in).substring(i));
                }
            }
        }
        return new JsonObject();
    }

    @Nullable
    public static Server wrap(@Nonnull String name) {
        return TNLListener.getInstance().getServerHashMap().get(name);
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
                ", status=" + status +
                ", playerCount=" + playerCount +
                ", maxPlayerCount=" + maxPlayerCount +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return playerCount == server.playerCount && maxPlayerCount == server.maxPlayerCount && lastUpdateTime == server.lastUpdateTime && name.equals(server.name) && inetSocketAddress.equals(server.inetSocketAddress) && status == server.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, inetSocketAddress, status, playerCount, maxPlayerCount, lastUpdateTime);
    }

    public enum Status {
        ONLINE,
        OFFLINE,
        STARTING;

        public boolean isStarting() {
            return equals(STARTING);
        }

        public boolean isOnline() {
            return equals(ONLINE);
        }

        public boolean isOffline() {
            return equals(OFFLINE);
        }
    }
}
