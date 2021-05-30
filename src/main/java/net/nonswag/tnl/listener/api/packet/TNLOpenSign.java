package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import org.bukkit.Location;

import javax.annotation.Nonnull;

public abstract class TNLOpenSign {

    @Nonnull
    public static Object create(@Nonnull Location location) {
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4) || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5)) {
            return new net.minecraft.server.v1_16_R3.PacketPlayOutOpenSignEditor(new net.minecraft.server.v1_16_R3.BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            return new net.minecraft.server.v1_15_R1.PacketPlayOutOpenSignEditor(new net.minecraft.server.v1_15_R1.BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_10)) {
            return new net.minecraft.server.v1_7_R4.PacketPlayOutOpenSignEditor(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            return new net.minecraft.server.v1_7_R1.PacketPlayOutOpenSignEditor(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }
}
