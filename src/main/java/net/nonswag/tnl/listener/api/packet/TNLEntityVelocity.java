package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.entity.TNLEntity;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

public abstract class TNLEntityVelocity {

    @Nonnull
    public static Object create(int id, @Nonnull Vector vector) {
        Object packet;
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutEntityVelocity(id, new net.minecraft.server.v1_16_R3.Vec3D(vector.getX(), vector.getY(), vector.getZ()));
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutEntityVelocity(id, new net.minecraft.server.v1_15_R1.Vec3D(vector.getX(), vector.getY(), vector.getZ()));
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_6)) {
            packet = new net.minecraft.server.v1_7_R4.PacketPlayOutEntityVelocity(id, vector.getX(), vector.getY(), vector.getZ());
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            packet = new net.minecraft.server.v1_7_R1.PacketPlayOutEntityVelocity(id, vector.getX(), vector.getY(), vector.getZ());
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getRecentVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        return packet;
    }

    @Nonnull
    public static Object create(@Nonnull TNLEntity entity, @Nonnull Vector vector) {
        return create(entity.getId(), vector);
    }
}
