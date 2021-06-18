package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.entity.TNLEntity;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.ServerVersion;

import javax.annotation.Nonnull;

public abstract class TNLEntityDestroy {

    @Nonnull
    public static Object create(int... ids) {
        Object packet;
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy(ids);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy(ids);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_6)) {
            packet = new net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy(ids);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            packet = new net.minecraft.server.v1_7_R1.PacketPlayOutEntityDestroy(ids);
        } else {
            Logger.error.println("Version <'" + TNLListener.getInstance().getVersion().getRecentVersion() + "'> is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        return packet;
    }

    @Nonnull
    public static Object create(@Nonnull TNLEntity... entities) {
        int[] ids = new int[entities.length];
        for (int i = 0; i < entities.length; i++) {
            ids[i] = entities[i].getId();
        }
        return create(ids);
    }
}
