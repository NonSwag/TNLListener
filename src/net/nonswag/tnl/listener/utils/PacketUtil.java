package net.nonswag.tnl.listener.utils;

import net.minecraft.server.v1_15_R1.Packet;
import net.nonswag.tnl.listener.api.logger.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PacketUtil {

    public static void setPacketField(@Nonnull Packet<?> packet, @Nonnull String packetField, @Nullable Object value) {
        try {
            Field field = packet.getClass().getDeclaredField(packetField);
            field.setAccessible(true);
            field.set(packet, value);
        } catch (Exception e) {
            Logger.error.println(e);
        }
    }

    @Nullable
    public static Object getPacketField(@Nonnull String packetField, @Nonnull Packet<?> packet) {
        try {
            Field field = packet.getClass().getDeclaredField(packetField);
            field.setAccessible(true);
            return field.get(packet);
        } catch (Exception e) {
            Logger.error.println(e);
        }
        return null;
    }

    @Nonnull
    public static List<String> getPacketFields(@Nonnull Packet<?> packet) {
        try {
            List<String> fields = new ArrayList<>();
            for (Field field : packet.getClass().getDeclaredFields()) {
                fields.add(field.getName());
            }
            return fields;
        } catch (Exception e) {
            Logger.error.println(e);
        }
        return new ArrayList<>();
    }

    @Nonnull
    public static String getPacketName(@Nonnull Packet<?> packet) {
        try {
            return packet.getClass().getSimpleName();
        } catch (Exception e) {
            Logger.error.println(e);
        }
        return "PacketListenerPlay";
    }
}
