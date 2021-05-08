package net.nonswag.tnl.listener.api.entity.v1_15.R1;

import net.minecraft.server.v1_15_R1.*;
import net.nonswag.tnl.listener.api.entity.TNLArmorStand;
import net.nonswag.tnl.listener.api.object.Objects;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;

import javax.annotation.Nonnull;

public class NMSArmorStand extends EntityArmorStand implements TNLArmorStand {

    public NMSArmorStand(@Nonnull Location location) {
        this(new Objects<>(location.getWorld()).nonnull("World can't be null"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public NMSArmorStand(@Nonnull World world, double x, double y, double z) {
        this(world, x, y, z, 0f, 0f);
    }

    public NMSArmorStand(@Nonnull World world, double x, double y, double z, float yaw, float pitch) {
        super(((CraftWorld) world).getHandle(), x, y, z);
        super.setYawPitch(yaw, pitch);
    }

    @Override
    public void setX(double x) {
        super.f(x, super.locY(), super.locZ());
    }

    @Override
    public void setY(double y) {
        super.f(super.locX(), y, super.locZ());
    }

    @Override
    public void setZ(double z) {
        super.f(super.locX(), super.locY(), z);
    }

    @Override
    public <I, S> void setSlot(S slot, I item) {
        super.setSlot(((EnumItemSlot) slot), ((ItemStack) item));
    }

    @Override
    public <V> void setHeadPose(V vector3f) {
        super.setHeadPose(((Vector3f) vector3f));
    }

    @Override
    public <V> void setBodyPose(V vector3f) {
        super.setBodyPose(((Vector3f) vector3f));
    }

    @Override
    public <V> void setLeftArmPose(V vector3f) {
        super.setLeftArmPose(((Vector3f) vector3f));
    }

    @Override
    public <V> void setRightArmPose(V vector3f) {
        super.setRightArmPose(((Vector3f) vector3f));
    }

    @Override
    public <V> void setLeftLegPose(V vector3f) {
        super.setLeftLegPose(((Vector3f) vector3f));
    }

    @Override
    public <V> void setRightLegPose(V vector3f) {
        super.setRightLegPose(((Vector3f) vector3f));
    }

    @Override
    public void setCustomName(@Nonnull String customName) {
        super.setCustomName(new ChatMessage(customName));
    }

    @Override
    public DataWatcher getDataWatcher() {
        return super.getDataWatcher();
    }

    @Nonnull
    @Override
    public CraftEntity getBukkitEntity() {
        return super.getBukkitEntity();
    }
}
