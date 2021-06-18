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
    public static Object create(@Nonnull Inventory inventory, int slot, @Nonnull ItemStack item) {
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            return new net.minecraft.server.v1_16_R3.PacketPlayOutSetSlot(inventory.getId(), slot, org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asNMSCopy(item));
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            return new net.minecraft.server.v1_15_R1.PacketPlayOutSetSlot(inventory.getId(), slot, org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack.asNMSCopy(item));
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_6)) {
            return new net.minecraft.server.v1_7_R4.PacketPlayOutSetSlot(inventory.getId(), slot, org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack.asNMSCopy(item));
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            return new net.minecraft.server.v1_7_R1.PacketPlayOutSetSlot(inventory.getId(), slot, org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemStack.asNMSCopy(item));
        } else {
            Logger.error.println("Version <'" + TNLListener.getInstance().getVersion().getRecentVersion() + "'> is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }

    @Nonnull
    public static Object create(@Nonnull Inventory inventory, int slot, @Nonnull Material material) {
        return create(inventory, slot, new ItemStack(material));
    }

    @Nonnull
    public static Object create(@Nonnull Inventory inventory, int slot, @Nonnull TNLItem item) {
        return create(inventory, slot, item.getItemStack());
    }

    @Nonnull
    public static Object create(@Nonnull Inventory inventory, int slot, @Nonnull GUIItem item) {
        if (item.getBuilder().hasValue()) return create(inventory, slot, item.getBuilder().nonnull());
        return create(inventory, slot, new ItemStack(Material.AIR));
    }

    @Nonnull
    public static Object create(int slot, @Nonnull Material material) {
        return create(Inventory.PLAYER, slot, new ItemStack(material));
    }

    @Nonnull
    public static Object create(int slot, @Nonnull ItemStack item) {
        return create(Inventory.PLAYER, slot, item);
    }

    @Nonnull
    public static Object create(int slot, @Nonnull TNLItem item) {
        return create(Inventory.PLAYER, slot, item);
    }

    @Nonnull
    public static Object create(int slot, @Nonnull GUIItem item) {
        return create(Inventory.PLAYER, slot, item);
    }

    public enum Inventory {
        COURSER(-1),
        PLAYER(0),
        TOP(1);

        private final int id;

        Inventory(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
