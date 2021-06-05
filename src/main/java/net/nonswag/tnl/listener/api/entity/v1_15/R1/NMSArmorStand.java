package net.nonswag.tnl.listener.api.entity.v1_15.R1;

import net.minecraft.server.v1_15_R1.*;
import net.nonswag.tnl.listener.api.entity.TNLArmorStand;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.object.Objects;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;

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
    public void setHeadPose(@Nullable Pose pose) {
        if (pose != null) super.setHeadPose(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setBodyPose(@Nullable Pose pose) {
        if (pose != null) super.setBodyPose(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setLeftArmPose(@Nullable Pose pose) {
        if (pose != null) super.setLeftArmPose(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setRightArmPose(@Nullable Pose pose) {
        if (pose != null) super.setRightArmPose(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setLeftLegPose(@Nullable Pose pose) {
        if (pose != null) super.setLeftLegPose(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setRightLegPose(@Nullable Pose pose) {
        if (pose != null) super.setRightLegPose(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setCustomName(@Nonnull String customName) {
        super.setCustomName(new ChatMessage(customName));
    }

    @Override
    public void setVisible(boolean visible) {
        super.setInvisible(!visible);
    }

    @Override
    public void setGravity(boolean gravity) {
        super.setNoGravity(!gravity);
    }

    @Override
    public void setBasePlate(boolean flag) {
        super.setBasePlate(!flag);
    }

    @Override
    public void setItemInMainHand(@Nullable TNLItem item) {
        super.setSlot(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(item != null ? item.build() : null));
    }

    @Override
    public void setItemInOffHand(@Nullable TNLItem item) {
        super.setSlot(EnumItemSlot.OFFHAND, CraftItemStack.asNMSCopy(item != null ? item.build() : null));
    }

    @Override
    public void setHelmet(@Nullable TNLItem item) {
        super.setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(item != null ? item.build() : null));
    }

    @Override
    public void setChestplate(@Nullable TNLItem item) {
        super.setSlot(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(item != null ? item.build() : null));
    }

    @Override
    public void setLeggings(@Nullable TNLItem item) {
        super.setSlot(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(item != null ? item.build() : null));
    }

    @Override
    public void setBoots(@Nullable TNLItem item) {
        super.setSlot(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(item != null ? item.build() : null));
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
