package net.nonswag.tnl.listener.api.entity.v1_16.R3;

import net.minecraft.server.v1_16_R3.*;
import net.nonswag.tnl.listener.api.entity.TNLArmorStand;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.object.Objects;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;

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
    public void setHeadPose(float x, float y, float z) {
        super.setHeadPose(new Vector3f(x, y, z));
    }

    @Override
    public void setBodyPose(float x, float y, float z) {
        super.setBodyPose(new Vector3f(x, y, z));
    }

    @Override
    public void setLeftArmPose(float x, float y, float z) {
        super.setLeftArmPose(new Vector3f(x, y, z));
    }

    @Override
    public void setRightArmPose(float x, float y, float z) {
        super.setRightArmPose(new Vector3f(x, y, z));
    }

    @Override
    public void setLeftLegPose(float x, float y, float z) {
        super.setLeftLegPose(new Vector3f(x, y, z));
    }

    @Override
    public void setRightLegPose(float x, float y, float z) {
        super.setRightLegPose(new Vector3f(x, y, z));
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
    public void setItemInMainHand(@Nonnull TNLItem item) {
        super.setSlot(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(item.build()), true);
    }

    @Override
    public void setItemInOffHand(@Nonnull TNLItem item) {
        super.setSlot(EnumItemSlot.OFFHAND, CraftItemStack.asNMSCopy(item.build()), true);
    }

    @Override
    public void setHelmet(@Nonnull TNLItem item) {
        super.setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(item.build()), true);
    }

    @Override
    public void setChestplate(@Nonnull TNLItem item) {
        super.setSlot(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(item.build()), true);
    }

    @Override
    public void setLeggings(@Nonnull TNLItem item) {
        super.setSlot(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(item.build()), true);
    }

    @Override
    public void setBoots(@Nonnull TNLItem item) {
        super.setSlot(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(item.build()), true);
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
