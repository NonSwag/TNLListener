package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.entity.TNLEntityPlayer;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.Version;

import javax.annotation.Nonnull;

public abstract class TNLNamedEntitySpawn {

    @Nonnull
    public static Object create(@Nonnull TNLEntityPlayer player) {
        Object packet;
        if (TNLListener.getInstance().getVersion().equals(Version.v1_16_4)) {
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn((net.minecraft.server.v1_16_R3.EntityPlayer) player);
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_15_2)) {
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn((net.minecraft.server.v1_15_R1.EntityPlayer) player);
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_7_6)) {
            packet = new net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn((net.minecraft.server.v1_7_R4.EntityPlayer) player);
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_7_2)) {
            packet = new net.minecraft.server.v1_7_R1.PacketPlayOutNamedEntitySpawn((net.minecraft.server.v1_7_R1.EntityPlayer) player);
        } else {
            Logger.error.println("Version '" + TNLListener.getInstance().getVersion().getRecentVersion() + "'> is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        return packet;
    }
}
