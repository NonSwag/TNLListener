package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.entity.TNLEntity;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.version.ServerVersion;

import javax.annotation.Nonnull;

public abstract class TNLCamera {

    @Nonnull
    public static Object create(int id) {
        Object packet;
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutCamera();
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutCamera();
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getRecentVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        Reflection.setField(packet, "a", id);
        return packet;
    }

    @Nonnull
    public static Object create(@Nonnull TNLEntity entity) {
        return create(entity.getId());
    }
}
