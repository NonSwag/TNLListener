package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.version.ServerVersion;

import javax.annotation.Nonnull;

public abstract class TNLPlayerRespawn {

    @Nonnull
    public static Object create(@Nonnull TNLPlayer player) {
        Object packet;
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutRespawn();
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutRespawn(((net.nonswag.tnl.listener.api.player.v1_15.R1.NMSPlayer) player).getEntityPlayer().dimension,
                    0, net.minecraft.server.v1_15_R1.WorldType.NORMAL, ((net.nonswag.tnl.listener.api.player.v1_15.R1.NMSPlayer) player).getEntityPlayer().server.getGamemode());
        } else {
            Logger.error.println("Version <'" + TNLListener.getInstance().getVersion().getRecentVersion() + "'> is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        return packet;
    }
}
