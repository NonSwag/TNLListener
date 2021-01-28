package net.nonswag.tnl.listener.utils;

import net.minecraft.server.v1_15_R1.Packet;
import net.nonswag.tnl.listener.NMSMain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PacketUtil {

    public static void setPacketField(Packet<?> packet, String packetField, Object value) {
        try {
            Field field = packet.getClass().getDeclaredField(packetField);
            field.setAccessible(true);
            field.set(packet, value);
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public static Object getPacketField(String packetField, Packet<?> packet) {
        try {
            Field field = packet.getClass().getDeclaredField(packetField);
            field.setAccessible(true);
            return field.get(packet);
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        return null;
    }

    public static List<String> getPacketFields(Packet<?> packet) {
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

    public static String getPacketName(Packet<?> packet) {
        try {
            return packet.getClass().getSimpleName();
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        return "PacketListenerPlay";
    }
}
