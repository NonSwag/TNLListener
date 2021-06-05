package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.gui.GUIItem;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class TNLWindowItems {

    @Nonnull
    public static Object create(@Nonnull List<ItemStack> items) {
        Object packet;
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            net.minecraft.server.v1_16_R3.NonNullList<net.minecraft.server.v1_16_R3.ItemStack> itemStacks = net.minecraft.server.v1_16_R3.NonNullList.a();
            for (ItemStack item : items) {
                itemStacks.add(org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asNMSCopy(item));
            }
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutWindowItems(1, itemStacks);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            net.minecraft.server.v1_15_R1.NonNullList<net.minecraft.server.v1_15_R1.ItemStack> itemStacks = net.minecraft.server.v1_15_R1.NonNullList.a();
            for (ItemStack item : items) {
                itemStacks.add(org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack.asNMSCopy(item));
            }
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutWindowItems(1, itemStacks);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_6)) {
            List<net.minecraft.server.v1_7_R4.ItemStack> itemStacks = new ArrayList<>();
            for (ItemStack item : items) {
                itemStacks.add(org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack.asNMSCopy(item));
            }
            packet = new net.minecraft.server.v1_7_R4.PacketPlayOutWindowItems(1, itemStacks);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            List<net.minecraft.server.v1_7_R1.ItemStack> itemStacks = new ArrayList<>();
            for (ItemStack item : items) {
                itemStacks.add(org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemStack.asNMSCopy(item));
            }
            packet = new net.minecraft.server.v1_7_R1.PacketPlayOutWindowItems(1, itemStacks);
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getRecentVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        return packet;
    }

    @Nonnull
    public static Object createFromGUIItems(@Nonnull List<GUIItem> items) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (GUIItem item : items) {
            if (item.getBuilder().hasValue()) {
                itemStacks.add(item.getBuilder().nonnull().getItemStack());
            } else {
                itemStacks.add(new ItemStack(Material.AIR));
            }
        }
        return create(itemStacks);
    }

    @Nonnull
    public static Object createFromTNLItems(@Nonnull List<TNLItem> items) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (TNLItem item : items) {
            itemStacks.add(item.getItemStack());
        }
        return create(itemStacks);
    }
}
