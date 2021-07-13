package net.nonswag.tnl.listener.api.entity;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.Version;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface TNLArmorStand extends TNLEntity {

    @Nonnull
    static TNLArmorStand create(@Nonnull World world, double x, double y, double z, float yaw, float pitch) {
        if (TNLListener.getInstance().getVersion().equals(Version.v1_16_4)) {
            return new net.nonswag.tnl.listener.api.entity.v1_16.R3.NMSArmorStand(world, x, y, z, yaw, pitch);
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_15_2)) {
            return new net.nonswag.tnl.listener.api.entity.v1_15.R1.NMSArmorStand(world, x, y, z, yaw, pitch);
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_7_6)) {
            throw new UnsupportedOperationException("method is not supported in this version");
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_7_2)) {
            throw new UnsupportedOperationException("method is not supported in this version");
        } else {
            Logger.error.println("Version <'" + TNLListener.getInstance().getVersion().getRecentVersion() + "'> is not registered please report this error to an contributor");
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

    void setHeadPose(@Nullable Pose pose);

    void setBodyPose(@Nullable Pose pose);

    void setLeftArmPose(@Nullable Pose pose);

    void setRightArmPose(@Nullable Pose pose);

    void setLeftLegPose(@Nullable Pose pose);

    void setRightLegPose(@Nullable Pose pose);

    boolean isInteractable();

    void setCustomNameVisible(boolean flag);

    void setCustomName(@Nonnull String customName);

    <D> D getDataWatcher();

    void setVisible(boolean visible);

    void setInvulnerable(boolean invulnerable);

    void setGravity(boolean gravity);

    void setItemInMainHand(@Nullable TNLItem item);

    void setItemInOffHand(@Nullable TNLItem item);

    void setHelmet(@Nullable TNLItem item);

    void setChestplate(@Nullable TNLItem item);

    void setLeggings(@Nullable TNLItem item);

    void setBoots(@Nullable TNLItem item);

    class Pose {

        private final float x;
        private final float y;
        private final float z;

        public Pose(double x, double y, double z) {
            this.x = Math.round(x);
            this.y = Math.round(y);
            this.z = Math.round(z);
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }
    }
}
