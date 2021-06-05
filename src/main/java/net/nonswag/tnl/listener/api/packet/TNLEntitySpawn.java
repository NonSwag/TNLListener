package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.entity.TNLEntity;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;

public abstract class TNLEntitySpawn {

    @Nonnull
    public static Object create(@Nonnull TNLEntity entity) {
        return create(entity.getBukkitEntity());
    }

    @Nonnull
    public static Object create(@Nonnull Entity entity) {
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            return new net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntity(((org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity) entity).getHandle());
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            return new net.minecraft.server.v1_15_R1.PacketPlayOutSpawnEntity(((org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity) entity).getHandle());
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_6)) {
            return new net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity(((org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity) entity).getHandle(), 0);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            return new net.minecraft.server.v1_7_R1.PacketPlayOutSpawnEntity(((org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity) entity).getHandle(), 0);
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getRecentVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }
}
