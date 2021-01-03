package net.nonswag.tnl.listener.v1_8_R3.utils;

import net.minecraft.server.v1_8_R3.Packet;
import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PacketUtil {

    public static void sendPacket(Player player, Packet<?> packet) {
        NMSMain.runTask( () -> ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet));
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
        List<String> fields = new ArrayList<>();
        for(Field field : packet.getClass().getDeclaredFields()) {
            fields.add(field.getName());
        }
        return fields;
    }

    public static String getPacketName(Packet packet) {
        return packet.getClass().getSimpleName();
    }
}
