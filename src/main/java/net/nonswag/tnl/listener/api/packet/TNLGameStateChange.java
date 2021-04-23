package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.ServerVersion;

import javax.annotation.Nonnull;

public abstract class TNLGameStateChange {

    @Nonnull
    public static Object create(@Nonnull int id, float value) {
        Object packet;
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4) || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5)) {
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutGameStateChange(new net.minecraft.server.v1_16_R3.PacketPlayOutGameStateChange.a(id), value);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutGameStateChange(id, value);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_10)) {
            packet = new net.minecraft.server.v1_7_R4.PacketPlayOutGameStateChange(id, value);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            packet = new net.minecraft.server.v1_7_R1.PacketPlayOutGameStateChange(id, value);
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        return packet;
    }
}
