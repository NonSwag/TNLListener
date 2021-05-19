package net.nonswag.tnl.listener.api.item;

import net.nonswag.tnl.listener.api.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public enum TNLItemType {
    ;

    public boolean isAir() {
        return false;
    }

    public static boolean isAir(@Nullable Material material) {
        return material == null || material.equals(Material.AIR) || material.equals(Material.CAVE_AIR) || material.equals(Material.VOID_AIR);
    }

    public static boolean isAir(@Nullable GUIItem item) {
        return item == null || !item.getBuilder().hasValue() || isAir(item.getBuilder().nonnull().getType());
    }

    public static boolean isAir(@Nullable TNLItem item) {
        return item == null || isAir(item.getType());
    }

    public static boolean isAir(@Nullable ItemStack itemStack) {
        return itemStack == null || isAir(itemStack.getType());
    }
}
