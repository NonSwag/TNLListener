package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.entity.TNLEntityPlayer;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.Version;

import javax.annotation.Nonnull;

public abstract class TNLPlayerInfo {

    @Nonnull
    public static Object create(@Nonnull TNLEntityPlayer player, @Nonnull Action action) {
        Object packet;
        if (TNLListener.getInstance().getVersion().equals(Version.v1_16_4)) {
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo(net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction.valueOf(action.name()), ((org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer) player.getBukkitEntity()).getHandle());
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_15_2)) {
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo(net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction.valueOf(action.name()), ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer) player.getBukkitEntity()).getHandle());
        } else {
            Logger.error.println("Version <'" + TNLListener.getInstance().getVersion().getRecentVersion() + "'> is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        return packet;
    }

    public enum Action {
        ADD_PLAYER,
        UPDATE_GAME_MODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }
}
