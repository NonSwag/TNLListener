package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.entity.TNLEntity;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import org.bukkit.Location;

import javax.annotation.Nonnull;

public abstract class TNLEntityTeleport {

    @Nonnull
    public static Object create(int id, double x, double y, double z, float yaw, float pitch) {
        final Object packet;
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5)) {
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport();
            Reflection.setField(packet, "b", x);
            Reflection.setField(packet, "c", y);
            Reflection.setField(packet, "d", z);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutEntityTeleport();
            Reflection.setField(packet, "b", x);
            Reflection.setField(packet, "c", y);
            Reflection.setField(packet, "d", z);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_10)) {
            packet = new net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport();
            Reflection.setField(packet, "b", ((int) x));
            Reflection.setField(packet, "c", ((int) y));
            Reflection.setField(packet, "d", ((int) z));
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            packet = new net.minecraft.server.v1_7_R1.PacketPlayOutEntityTeleport();
            Reflection.setField(packet, "b", ((int) x));
            Reflection.setField(packet, "c", ((int) y));
            Reflection.setField(packet, "d", ((int) z));
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        Reflection.setField(packet, "a", id);
        Reflection.setField(packet, "e", ((byte) yaw));
        Reflection.setField(packet, "f", ((byte) pitch));
        return packet;
    }

    @Nonnull
    public static Object create(int id, double x, double y, double z) {
        return create(id, x, y, z, 0f, 0f);
    }

    @Nonnull
    public static Object create(@Nonnull TNLEntity entity, @Nonnull Location location) {
        return create(entity.getId(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
}
