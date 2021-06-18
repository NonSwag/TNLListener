package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.entity.TNLEntity;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.version.ServerVersion;

import javax.annotation.Nonnull;

public abstract class TNLEntityHeadRotation {

    @Nonnull
    public static Object create(int id, float yaw) {
        Object packet;
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutEntityHeadRotation();
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutEntityHeadRotation();
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_6)) {
            packet = new net.minecraft.server.v1_7_R4.PacketPlayOutEntityHeadRotation();
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            packet = new net.minecraft.server.v1_7_R1.PacketPlayOutEntityHeadRotation();
        } else {
            Logger.error.println("Version '" + TNLListener.getInstance().getVersion().getRecentVersion() + "'> is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        Reflection.setField(packet, "a", id);
        Reflection.setField(packet, "b", (byte) (yaw * 256 / 360));
        return packet;
    }

    @Nonnull
    public static Object create(@Nonnull TNLEntity entity, float yaw) {
        return create(entity.getId(), yaw);
    }

    @Nonnull
    public static Object create(@Nonnull TNLEntity entity) {
        return create(entity.getId(), entity.getBukkitEntity().getLocation().getYaw());
    }
}
