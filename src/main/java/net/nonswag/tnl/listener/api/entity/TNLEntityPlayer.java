package net.nonswag.tnl.listener.api.entity;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.GameProfile;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;

public interface TNLEntityPlayer extends TNLEntity {

    @Nonnull
    static TNLEntityPlayer create(@Nonnull World world, double x, double y, double z, float yaw, float pitch, @Nonnull GameProfile profile) {
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5) || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            return new net.nonswag.tnl.listener.api.entity.v1_16.R3.NMSEntityPlayer(world, x, y, z, yaw, pitch, profile);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            return new net.nonswag.tnl.listener.api.entity.v1_15.R1.NMSEntityPlayer(world, x, y, z, yaw, pitch, profile);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_10)) {
            throw new UnsupportedOperationException("method is not supported in this version");
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            throw new UnsupportedOperationException("method is not supported in this version");
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }

    @Nonnull
    static TNLEntityPlayer create(@Nonnull World world, double x, double y, double z, @Nonnull GameProfile profile) {
        return create(world, x, y, z, 0f, 0f, profile);
    }

    @Nonnull
    static TNLEntityPlayer create(@Nonnull Location location, @Nonnull GameProfile profile) {
        if (location.getWorld() == null) throw new NullPointerException();
        return create(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), profile);
    }

    void setPing(int ping);

    int getPing();

    @Nonnull
    GameProfile getGameProfile();
}
