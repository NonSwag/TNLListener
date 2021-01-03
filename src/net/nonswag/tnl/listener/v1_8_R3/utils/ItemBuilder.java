package net.nonswag.tnl.listener.v1_8_R3.utils;

import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.net.URI;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        item = new ItemStack(material);
        itemMeta = item.getItemMeta();
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ItemBuilderImpl{" +
                "item=" + item +
                ", itemMeta=" + itemMeta +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemBuilder that = (ItemBuilder) o;
        return Objects.equals(item, that.item) &&
                Objects.equals(itemMeta, that.itemMeta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, itemMeta);
    }

    public ItemBuilder(ItemStack itemStack) {
        item = itemStack;
        itemMeta = item.getItemMeta();
    }

    public ItemStack build() {
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemBuilder setName(String name) {
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setDamage(float damage) {
        item.setDurability(((short) damage));
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder hideFlag(ItemFlag itemFlag) {
        itemMeta.addItemFlags(itemFlag);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setSkullOwner(OfflinePlayer owner) {
        setSkullOwnerFromName(owner.getName());
        return this;
    }

    public ItemBuilder setSkullOwnerFromName(String name) {
        if (itemMeta instanceof SkullMeta) {
            ((SkullMeta) itemMeta).setOwner(name);
        }
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setSkullOwnerFromURL(String url) {
        if (itemMeta instanceof SkullMeta) {
            try {
                setSkullOwnerFromBase64(Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"" + new URI(url).toString() + "\"}}}").getBytes()));
            } catch (Throwable t) {
                NMSMain.stacktrace(t);
            }
        }
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setSkullOwnerFromBase64(String base64) {
        if (itemMeta instanceof SkullMeta) {
            modifyNBT("{SkullOwner:{Id:\"" + new UUID(base64.hashCode(), base64.hashCode()) + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}");
        }
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder modifyNBT(String nbt) {
        Bukkit.getUnsafe().modifyItemStack(item, nbt);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setColor(Color color) {
        NMSMain.stacktrace("Unsupported method");
        return this;
    }

    public ItemBuilder setPower(int power) {
        if (itemMeta instanceof FireworkMeta) {
            ((FireworkMeta) itemMeta).setPower(power);
        }
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setEffect(FireworkEffect effect) {
        if (itemMeta instanceof FireworkEffectMeta) {
            ((FireworkEffectMeta) itemMeta).setEffect(effect);
        }
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder hideFlags() {
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        NMSMain.stacktrace("Unsupported method");
        return this;
    }
}
