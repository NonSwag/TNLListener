package net.nonswag.tnl.listener.v1_16_R1.utils;

import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.net.URI;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

public class ItemBuilder {

    private ItemStack item;
    private ItemMeta itemMeta;

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
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }


    public ItemBuilder setDamage(float damage) {
        if (itemMeta instanceof Damageable) {
            ((Damageable) itemMeta).setDamage(((short) damage));
        }
        return this;
    }

    public ItemBuilder hideFlag(ItemFlag itemFlag) {
        itemMeta.addItemFlags(itemFlag);
        return this;
    }

    public ItemBuilder setSkullOwner(OfflinePlayer owner) {
        if (itemMeta instanceof SkullMeta) {
            ((SkullMeta) itemMeta).setOwningPlayer(owner);
        }
        return this;
    }

    public ItemBuilder setSkullOwner(String name) {
        if (itemMeta instanceof SkullMeta) {
            ((SkullMeta) itemMeta).setOwningPlayer(Bukkit.getOfflinePlayer(name));
        }
        return this;
    }

    public ItemBuilder setSkullImgURL(String url) {
        try {
            setSkullValue(Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"" + new URI(url).toString() + "\"}}}").getBytes()));
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        return this;
    }

    public ItemBuilder setSkullValue(String base64) {
        try {
            modifyNBT("{SkullOwner:{Id:\"" + new UUID(base64.hashCode(), base64.hashCode()) + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}");
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        return this;
    }

    public ItemBuilder modifyNBT(String nbt) {
        try {
            item = Bukkit.getUnsafe().modifyItemStack(item, nbt);
            itemMeta = item.getItemMeta();
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        return this;
    }

    public ItemBuilder setColor(Color color) {
        if (itemMeta instanceof PotionMeta) {
            ((PotionMeta) itemMeta).setColor(color);
        } else if (itemMeta instanceof FireworkEffectMeta) {
            setEffect(FireworkEffect.builder().withColor(color).build());
        }
        return this;
    }

    public ItemBuilder setPower(int power) {
        if (itemMeta instanceof FireworkMeta) {
            ((FireworkMeta) itemMeta).setPower(power);
        }
        return this;
    }

    public ItemBuilder setEffect(FireworkEffect effect) {
        if (itemMeta instanceof FireworkEffectMeta) {
            ((FireworkEffectMeta) itemMeta).setEffect(effect);
        }
        return this;
    }

    public ItemBuilder hideFlags() {
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        if (amount > item.getType().getMaxStackSize()) {
            net.minecraft.server.v1_15_R1.ItemStack itemStack = CraftItemStack.asNMSCopy(getItem());
            itemStack.setCount(amount);
            this.item = CraftItemStack.asBukkitCopy(itemStack);
        } else {
            item.setAmount(amount);
        }
        return this;
    }

    public ItemBuilder setUnbreakable() {
        itemMeta.setUnbreakable(true);
        return this;
    }
}
