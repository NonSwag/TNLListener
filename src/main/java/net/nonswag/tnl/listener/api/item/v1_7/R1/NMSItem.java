package net.nonswag.tnl.listener.api.item.v1_7.R1;

import net.minecraft.server.v1_7_R1.NBTTagCompound;
import net.nonswag.tnl.listener.api.item.TNLItem;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

public class NMSItem implements TNLItem {

    @Nonnull
    private ItemStack itemStack;
    @Nonnull
    private ItemMeta itemMeta;

    public NMSItem(@Nonnull ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public NMSItem(@Nonnull Material material) {
        this(new ItemStack(material));
    }

    @Nonnull
    @Override
    public TNLItem setItemStack(@Nonnull ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    @Nonnull
    @Override
    public TNLItem setItemMeta(@Nonnull ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
        return this;
    }

    @Override
    @Nonnull
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    @Nonnull
    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    @Override
    public int getCustomModelData() {
        return getNBT().getInt("{CustomModelData}");
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBT() {
        return CraftItemStack.asNMSCopy(getItemStack()).getTag();
    }
}
