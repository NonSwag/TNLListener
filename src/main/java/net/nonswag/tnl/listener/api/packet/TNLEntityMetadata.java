package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.entity.TNLEntity;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.Version;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;

public abstract class TNLEntityMetadata {

    @Nonnull
    public static Object create(int id, @Nonnull Object dataWatcher) {
        if (TNLListener.getInstance().getVersion().equals(Version.v1_16_4)) {
            return new net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata(id, (net.minecraft.server.v1_16_R3.DataWatcher) dataWatcher, true);
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_15_2)) {
            return new net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata(id, (net.minecraft.server.v1_15_R1.DataWatcher) dataWatcher, true);
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_7_6)) {
            return new net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata(id, (net.minecraft.server.v1_7_R4.DataWatcher) dataWatcher, true);
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_7_2)) {
            return new net.minecraft.server.v1_7_R1.PacketPlayOutEntityMetadata(id, (net.minecraft.server.v1_7_R1.DataWatcher) dataWatcher, true);
        } else {
            Logger.error.println("Version <'" + TNLListener.getInstance().getVersion().getRecentVersion() + "'> is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }

    @Nonnull
    public static Object create(@Nonnull TNLEntity entity) {
        return create(entity.getBukkitEntity());
    }

    @Nonnull
    public static Object create(@Nonnull Entity entity) {
        if (TNLListener.getInstance().getVersion().equals(Version.v1_16_4)) {
            return new net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata(entity.getEntityId(), ((org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity) entity).getHandle().getDataWatcher(), true);
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_15_2)) {
            return new net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata(entity.getEntityId(), ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity) entity).getHandle().getDataWatcher(), true);
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_7_6)) {
            return new net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata(entity.getEntityId(), ((org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity) entity).getHandle().getDataWatcher(), true);
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_7_2)) {
            return new net.minecraft.server.v1_7_R1.PacketPlayOutEntityMetadata(entity.getEntityId(), ((org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity) entity).getHandle().getDataWatcher(), true);
        } else {
            Logger.error.println("Version <'" + TNLListener.getInstance().getVersion().getRecentVersion() + "'> is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }
}
