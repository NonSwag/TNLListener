package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.entity.TNLEntity;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;

public abstract class TNLEntityStatus {

    @Nonnull
    public static Object create(@Nonnull int id, byte value) {
        Object packet;
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5)) {
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus();
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutEntityStatus();
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_10)) {
            packet = new net.minecraft.server.v1_7_R4.PacketPlayOutEntityStatus();
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            packet = new net.minecraft.server.v1_7_R1.PacketPlayOutEntityStatus();
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        Reflection.setField(packet, "a", id);
        Reflection.setField(packet, "b", value);
        return packet;
    }

    @Nonnull
    public static Object create(@Nonnull TNLEntity entity, byte value) {
        return create(entity.getId(), value);
    }

    @Nonnull
    public static Object create(@Nonnull Entity entity, byte value) {
        return create(entity.getEntityId(), value);
    }
}
