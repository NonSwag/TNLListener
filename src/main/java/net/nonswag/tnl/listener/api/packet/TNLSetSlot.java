package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.gui.GUIItem;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public abstract class TNLSetSlot {

    @Nonnull
    public static Object create(int slot, @Nonnull ItemStack item) {
        Object packet;
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4) || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5)) {
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutSetSlot(1, slot, org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asNMSCopy(item));
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutSetSlot(1, slot, org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack.asNMSCopy(item));
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_10)) {
            packet = new net.minecraft.server.v1_7_R4.PacketPlayOutSetSlot(1, slot, org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack.asNMSCopy(item));
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            packet = new net.minecraft.server.v1_7_R1.PacketPlayOutSetSlot(1, slot, org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemStack.asNMSCopy(item));
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        return packet;
    }

    @Nonnull
    public static Object create(int slot, @Nonnull TNLItem item) {
        return create(slot, item.getItemStack());
    }

    @Nonnull
    public static Object create(int slot, @Nonnull GUIItem item) {
        if (item.getBuilder().hasValue()) return create(slot, item.getBuilder().nonnull());
        return create(slot, new ItemStack(Material.AIR));
    }
}
