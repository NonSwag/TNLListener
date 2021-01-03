package net.nonswag.tnl.listener.v1_14_R1.utils;

import net.minecraft.server.v1_14_R1.ChatMessage;
import net.minecraft.server.v1_14_R1.Packet;
import net.minecraft.server.v1_14_R1.PacketPlayOutKickDisconnect;
import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PacketUtil {

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    public static void sendPacket(Player player, Packet<?> packet) {
        NMSMain.runTask( () -> ((CraftPlayer)player).getHandle().playerConnection.networkManager.sendPacket(packet));
    }

    public static void setPacketField(Packet packet, String packetField, Object value) {
        try {
            if(getPacketField(packet, packetField) != null) {
                Field field = packet.getClass().getDeclaredField(packetField);
                field.setAccessible(true);
                field.set(packet, value);
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public static void disconnect(Player player, String message) {
        NMSMain.runTask(() -> {
            PacketPlayOutKickDisconnect disconnect = new PacketPlayOutKickDisconnect(
                    new ChatMessage(message));
            sendPacket(player, disconnect);
            ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline().disconnect();
        });
    }

    public static Object getPacketField(Packet packet, String packetField) {
        try{
            for(String arg : getPacketFields(packet)) {
                if (arg.equalsIgnoreCase(packetField)) {
                    Field field = packet.getClass().getDeclaredField(arg);
                    field.setAccessible(true);
                    return field.get(packet);
                }
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        if(getPacketFields(packet).size() > 0) {
            NMSMain.stacktrace("Valid PacketType Fields for " + getPacketName(packet) + ": " + getPacketFields(packet));
        } else {
            NMSMain.stacktrace("There are no PacketType Fields for " + getPacketName(packet));
        }
        return null;
    }

    public static List<String> getPacketFields(Packet packet) {
        try {
            List<String> fields = new ArrayList<>();
            for (Field field : packet.getClass().getDeclaredFields()) {
                fields.add(field.getName());
            }
            return fields;
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        return new ArrayList<>();
    }

    public static String getPacketName(Packet packet) {
        try {
            return packet.getClass().getSimpleName();
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        return "PacketListenerPlay";
    }
}
