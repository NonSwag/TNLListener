package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.entity.TNLEntity;
import net.nonswag.tnl.listener.api.item.SlotType;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class TNLEntityEquipment {

    @Nonnull
    public static Object create(int id, @Nonnull SlotType slotType, @Nonnull ItemStack item) {
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4) || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5)) {
            List<com.mojang.datafixers.util.Pair<net.minecraft.server.v1_16_R3.EnumItemSlot, net.minecraft.server.v1_16_R3.ItemStack>> items = new ArrayList<>();
            items.add(new com.mojang.datafixers.util.Pair<>((net.minecraft.server.v1_16_R3.EnumItemSlot) slotType.toNMS(), org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asNMSCopy(item)));
            return new net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment(id, items);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            return new net.minecraft.server.v1_15_R1.PacketPlayOutEntityEquipment(id, (net.minecraft.server.v1_15_R1.EnumItemSlot) slotType.toNMS(), org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack.asNMSCopy(item));
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_10)) {
            return new net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment(id, (int) slotType.toNMS(), org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack.asNMSCopy(item));
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            return new net.minecraft.server.v1_7_R1.PacketPlayOutEntityEquipment(id, (int) slotType.toNMS(), org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemStack.asNMSCopy(item));
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }

    @Nonnull
    public static Object create(@Nonnull Entity entity, @Nonnull SlotType slotType, @Nonnull ItemStack item) {
        return create(entity.getEntityId(), slotType, item);
    }

    @Nonnull
    public static Object create(@Nonnull TNLEntity entity, @Nonnull SlotType slotType, @Nonnull ItemStack item) {
        return create(entity.getId(), slotType, item);
    }
}
