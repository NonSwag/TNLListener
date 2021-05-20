package net.nonswag.tnl.listener.api.entity;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.item.TNLItem;
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
        if (location.getWorld() != null) {
            return create(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }
        throw new NullPointerException();
    }

    void setX(double x);

    void setY(double y);

    void setZ(double z);

    void updateSize();

    boolean doAITick();

    void setHeadRotation(float f);

    void tick();

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

    void setHeadPose(float x, float y, float z);

    void setBodyPose(float x, float y, float z);

    void setLeftArmPose(float x, float y, float z);

    void setRightArmPose(float x, float y, float z);

    void setLeftLegPose(float x, float y, float z);

    void setRightLegPose(float x, float y, float z);

    boolean isInteractable();

    void setCustomNameVisible(boolean flag);

    void setCustomName(@Nonnull String customName);

    <D> D getDataWatcher();

    void setVisible(boolean visible);

    void setInvulnerable(boolean invulnerable);

    void setGravity(boolean gravity);

    void setItemInMainHand(@Nonnull TNLItem item);

    void setItemInOffHand(@Nonnull TNLItem item);

    void setHelmet(@Nonnull TNLItem item);

    void setChestplate(@Nonnull TNLItem item);

    void setLeggings(@Nonnull TNLItem item);

    void setBoots(@Nonnull TNLItem item);
}
