package net.nonswag.tnl.listener.v1_15_R1.api.labyModAPI;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.PacketDataSerializer;
import net.minecraft.server.v1_15_R1.PacketPlayOutCustomPayload;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.api.labyModAPI.enumerations.Emote;
import net.nonswag.tnl.listener.api.labyModAPI.enumerations.EntryType;
import net.nonswag.tnl.listener.v1_15_R1.api.playerAPI.TNLPlayer;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Deprecated
public class LabyMod {

    private static final List<Player> labyModPlayers = new ArrayList<>();

    public static List<Player> getLabyModPlayers() {
        return labyModPlayers;
    }

    public static boolean isLabyModPlayer(Player player) {
        return getLabyModPlayers().contains(player);
    }

    private static void writePacket(TNLPlayer player, String key, JsonObject messageContent) {
        try {
            byte[] bytes = LabyMod.getBytes(key, messageContent.toString());
            assert bytes != null;
            PacketDataSerializer pds = new PacketDataSerializer(Unpooled.wrappedBuffer(bytes));
            PacketPlayOutCustomPayload payloadPacket = new PacketPlayOutCustomPayload(new MinecraftKey("lmc"), pds);
            player.sendPacket(payloadPacket);
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    private static byte[] getBytes(String messageKey, String messageContents) {
        try {
            ByteBuf byteBuf = Unpooled.buffer();
            stringWriter(byteBuf, messageKey);
            stringWriter(byteBuf, messageContents);
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            return bytes;
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        return null;
    }

    private static void bufferWriter(ByteBuf buffer, int input) {
        try {
            while ((input & -128) != 0) {
                buffer.writeByte(input & 127 | 128);
                input >>>= 7;
            }
            buffer.writeByte(input);
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    private static void stringWriter(ByteBuf buffer, String string) {
        try {
            byte[] b = string.getBytes(StandardCharsets.UTF_8);
            if (b.length > Short.MAX_VALUE) {
                throw new EncoderException("String too big (was " + string.length() + " bytes encoded, max " + Short.MAX_VALUE + ")");
            } else {
                bufferWriter(buffer, b.length);
                buffer.writeBytes(b);
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    private static int bufferReader(ByteBuf buffer) {
        try {
            int i = 0, j = 0;
            byte b0;
            do {
                b0 = buffer.readByte();
                i |= (b0 & 127) << j++ * 7;
                if (j > 5) {
                    throw new RuntimeException("VarInt too big");
                }
            } while ((b0 & 128) == 128);
            return i;
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        return 0;
    }

    private static String stringReader(ByteBuf buffer, int maxLength) {
        try {
            int i = bufferReader(buffer);
            if (i > maxLength * 4) {
                throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
            } else if (i < 0) {
                throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
            } else {
                byte[] bytes = new byte[i];
                buffer.readBytes(bytes);
                String s = new String(bytes, StandardCharsets.UTF_8);
                if (s.length() > maxLength) {
                    throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
                } else {
                    return s;
                }
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        return "";
    }

    public static void sendServerMessage(TNLPlayer player, String string) {
        try {
            JsonObject object = new JsonObject();
            object.addProperty("show_gamemode", true);
            object.addProperty("gamemode_name", string);
            writePacket(player, "server_gamemode", object);
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public static void disableAddon(TNLPlayer player, String addon) {
        try {
            JsonObject object = new JsonObject();
            object.addProperty("allowed", false);
            writePacket(player, addon, object);
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public static void enableAddon(TNLPlayer player, String addon) {
        try {
            JsonObject object = new JsonObject();
            object.addProperty("allowed", true);
            writePacket(player, addon, object);
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public static void playEmote(TNLPlayer receiver, UUID npc, Emote emote) {
        try {
            JsonObject forcedEmote = new JsonObject();
            forcedEmote.addProperty("uuid", npc.toString());
            forcedEmote.addProperty("emote_id", emote.getId());
            writePacket(receiver, "emote_api", forcedEmote);
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public static void sendSticker(TNLPlayer receiver, UUID npc, short stickerId) {
        try {
            JsonObject forcedSticker = new JsonObject();
            forcedSticker.addProperty("uuid", npc.toString());
            forcedSticker.addProperty("sticker_id", stickerId);
            writePacket(receiver, "sticker_api", forcedSticker);
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public static void setSubtitle(TNLPlayer viewer, UUID viewed, String subtitle, Double size) {
        try {
            JsonObject subtitleObject = new JsonObject();
            subtitleObject.addProperty("uuid", viewed.toString());
            subtitleObject.addProperty("size", size == null ? 0.8d : size);
            if (subtitle != null) {
                subtitleObject.addProperty("value", subtitle);
            }
            writePacket(viewer, "account_subtitle", subtitleObject);
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public static void setSubtitle(TNLPlayer viewer, TNLPlayer viewed, String subtitle, Double size) {
        setSubtitle(viewer, viewed.getUniqueId(), subtitle, size);
    }

    public static void connectScreen(TNLPlayer player, String information, String address) {
        try {
            JsonObject object = new JsonObject();
            object.addProperty("title", information);
            object.addProperty("address", address);
            object.addProperty("preview", true);
            writePacket(player, "server_switch", object);
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public static void recommendAddons(TNLPlayer player, HashMap<UUID, Boolean> addons) {
        try {
            JsonObject object = new JsonObject();
            JsonArray addonArray = new JsonArray();
            for (UUID addonId : addons.keySet()) {
                JsonObject addon = new JsonObject();
                addon.addProperty("uuid", addonId.toString());
                addon.addProperty("required", true);
                addonArray.add(addon);
            }
            object.add("addons", addonArray);
            writePacket(player, "addon_recommendation", object);
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public static class MiddleClickAction {

        private final TNLPlayer player;
        private final JsonArray jsonArray = new JsonArray();

        public MiddleClickAction(TNLPlayer player) {
            this.player = player;
        }

        public TNLPlayer getPlayer() {
            return player;
        }

        private JsonArray getJsonArray() {
            return jsonArray;
        }

        public MiddleClickAction addEntry(EntryType entryType, String value, String displayName) {
            try {
                JsonObject entry = new JsonObject();
                entry.addProperty("displayName", displayName);
                entry.addProperty("type", entryType.name());
                entry.addProperty("value", value);
                getJsonArray().add(entry);
            } catch (Throwable t) {
                NMSMain.stacktrace(t);
            }
            return this;
        }

        public void setMiddleClickAction() {
            try {
                writePacket(getPlayer(), "user_menu_actions", getJsonArray().getAsJsonObject());
            } catch (Throwable t) {
                NMSMain.stacktrace(t);
            }
        }
    }
}
