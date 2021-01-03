package net.nonswag.tnl.listener.api.labyModAPI;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.api.labyModAPI.event.LabyModPlayerJoinEvent;
import net.nonswag.tnl.listener.api.labyModAPI.event.MessageReceiveEvent;
import net.nonswag.tnl.listener.api.labyModAPI.event.MessageSendEvent;
import net.nonswag.tnl.listener.api.labyModAPI.listener.JoinListener;
import net.nonswag.tnl.listener.api.labyModAPI.utils.PacketUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class LabyModPlugin {

    private static final JsonParser jsonParser = new JsonParser();
    private final LabyModAPI api = new LabyModAPI();
    private PacketUtils packetUtils;

    public void onEnable() {
        this.packetUtils = new PacketUtils();
        Bukkit.getPluginManager().registerEvents(new JoinListener(), NMSMain.getPlugin());
        Bukkit.getMessenger().registerIncomingPluginChannel(NMSMain.getPlugin(), "LABYMOD", (channel, player, bytes) -> {
            ByteBuf buf = Unpooled.wrappedBuffer(bytes);
            try {
                final String version = api.readString(buf, Short.MAX_VALUE);
                Bukkit.getScheduler().runTask(NMSMain.getPlugin(), () -> {
                    if (player.isOnline()) {
                        Bukkit.getPluginManager().callEvent(new LabyModPlayerJoinEvent(player, version, false, 0, new ArrayList<>()));
                    }
                });
            } catch (RuntimeException ignored) {
            }
        });
        Bukkit.getMessenger().registerIncomingPluginChannel(NMSMain.getPlugin(), "LMC", (channel, player, bytes) -> {
            ByteBuf buf = Unpooled.wrappedBuffer(bytes);
            try {
                final String messageKey = api.readString(buf, Short.MAX_VALUE);
                final String messageContents = api.readString(buf, Short.MAX_VALUE);
                final JsonElement jsonMessage = jsonParser.parse(messageContents);
                Bukkit.getScheduler().runTask(NMSMain.getPlugin(), () -> {
                    if (!player.isOnline()) {
                        return;
                    }
                    if (messageKey.equals("INFO") && jsonMessage.isJsonObject()) {
                        JsonObject jsonObject = jsonMessage.getAsJsonObject();
                        String version = jsonObject.has("version")
                                && jsonObject.get("version").isJsonPrimitive()
                                && jsonObject.get("version").getAsJsonPrimitive().isString() ? jsonObject.get("version").getAsString() : "Unknown";
                        boolean chunkCachingEnabled = false;
                        int chunkCachingVersion = 0;
                        if (jsonObject.has("ccp") && jsonObject.get("ccp").isJsonObject()) {
                            JsonObject chunkCachingObject = jsonObject.get("ccp").getAsJsonObject();
                            if (chunkCachingObject.has("enabled")) {
                                chunkCachingEnabled = chunkCachingObject.get("enabled").getAsBoolean();
                            }
                            if (chunkCachingObject.has("version")) {
                                chunkCachingVersion = chunkCachingObject.get("version").getAsInt();
                            }
                        }
                        Bukkit.getPluginManager().callEvent(new LabyModPlayerJoinEvent(player, version,
                                chunkCachingEnabled, chunkCachingVersion, Addon.getAddons(jsonObject)));
                        return;
                    }
                    Bukkit.getPluginManager().callEvent(new MessageReceiveEvent(player, messageKey, jsonMessage));
                });
            } catch (RuntimeException ignored) {
            }
        });
    }

    public void onDisable() {
        Bukkit.getMessenger().unregisterIncomingPluginChannel(NMSMain.getPlugin(), "LABYMOD");
        Bukkit.getMessenger().unregisterIncomingPluginChannel(NMSMain.getPlugin(), "LMC");
    }


    public void sendServerMessage(Player player, String messageKey, JsonElement messageContents) {
        messageContents = cloneJson(messageContents);
        MessageSendEvent sendEvent = new MessageSendEvent(player, messageKey, messageContents, false);
        Bukkit.getPluginManager().callEvent(sendEvent);
        if (!sendEvent.isCancelled()) {
            packetUtils.sendPacket(player, packetUtils.getPluginMessagePacket("LMC", api.getBytesToSend(messageKey, messageContents.toString())));
        }
    }

    public JsonElement cloneJson(JsonElement cloneElement) {
        try {
            return jsonParser.parse(cloneElement.toString());
        } catch (JsonParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static JsonParser getJsonParser() {
        return jsonParser;
    }

    public LabyModAPI getApi() {
        return api;
    }

    public PacketUtils getPacketUtils() {
        return packetUtils;
    }

    public void setPacketUtils(PacketUtils packetUtils) {
        this.packetUtils = packetUtils;
    }
}
