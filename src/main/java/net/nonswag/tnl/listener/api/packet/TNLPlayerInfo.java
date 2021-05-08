package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.version.ServerVersion;

import javax.annotation.Nonnull;

public abstract class TNLPlayerInfo {

    @Nonnull
    public static Object create(@Nonnull TNLPlayer player, @Nonnull Action action) {
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4) || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5)) {
            return new net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo(net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction.valueOf(action.name()), ((net.nonswag.tnl.listener.api.player.v1_16.R3.NMSPlayer) player).getEntityPlayer());
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            return new net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo(net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction.valueOf(action.name()), ((net.nonswag.tnl.listener.api.player.v1_15.R1.NMSPlayer) player).getEntityPlayer());
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }

    public enum Action {
        ADD_PLAYER,
        UPDATE_GAME_MODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }
}
