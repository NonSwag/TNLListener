package net.nonswag.tnl.listener.api.entity;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;

public interface TNLArmorStand extends TNLEntity {

    @Nonnull
    static TNLArmorStand create(@Nonnull World world, double x, double y, double z, float yaw, float pitch) {
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5) || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            return new net.nonswag.tnl.listener.api.entity.v1_16.R3.NMSArmorStand(world, x, y, z, yaw, pitch);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            return new net.nonswag.tnl.listener.api.entity.v1_15.R1.NMSArmorStand(world, x, y, z, yaw, pitch);
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
    static TNLArmorStand create(@Nonnull World world, double x, double y, double z) {
        return create(world, x, y, z, 0f, 0f);
    }

    @Nonnull
    static TNLArmorStand create(@Nonnull Location location) {
        return create(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    void updateSize();

    boolean doAITick();

    <I, S> void setSlot(S slot, I item);

    void setHeadRotation(float f);

    void tick();

    void updatePose();

    void setInvisible(boolean flag);

    boolean isBaby();

    void killEntity();

    void setSmall(boolean flag);

    boolean isSmall();

    void setArms(boolean flag);

    boolean hasArms();

    void setBasePlate(boolean flag);

    boolean hasBasePlate();

    void setMarker(boolean flag);

    boolean isMarker();

    <V> void setHeadPose(V vector3f);

    <V> void setBodyPose(V vector3f);

    <V> void setLeftArmPose(V vector3f);

    <V> void setRightArmPose(V vector3f);

    <V> void setLeftLegPose(V vector3f);

    <V> void setRightLegPose(V vector3f);

    boolean isInteractable();

    void setCustomNameVisible(boolean flag);

    void setCustomName(@Nonnull String customName);

    <D> D getDataWatcher();
}
