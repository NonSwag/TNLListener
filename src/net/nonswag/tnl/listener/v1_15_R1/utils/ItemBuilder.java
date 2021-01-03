package net.nonswag.tnl.listener.v1_15_R1.utils;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.net.URI;
import java.util.*;

public class ItemBuilder {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setItemMeta(ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public ItemMeta getItemMeta() {
        return this.itemMeta;
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
        return "ItemBuilder{" +
                "itemStack=" + itemStack +
                ", itemMeta=" + itemMeta +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemBuilder builder = (ItemBuilder) o;
        return Objects.equals(itemStack, builder.itemStack) &&
                Objects.equals(itemMeta, builder.itemMeta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemStack, itemMeta);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemStack build() {
        getItemStack().setItemMeta(getItemMeta());
        return getItemStack();
    }

    public ItemBuilder setName(String name) {
        getItemMeta().setDisplayName(name);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        getItemMeta().addEnchant(enchantment, level, true);
        return this;
    }


    public ItemBuilder setDamage(float damage) {
        if (getItemMeta() instanceof Damageable) {
            ((Damageable) getItemMeta()).setDamage(((short) damage));
        }
        return this;
    }

    public ItemBuilder hideFlag(ItemFlag itemFlag) {
        getItemMeta().addItemFlags(itemFlag);
        return this;
    }

    public ItemBuilder setSkullOwner(OfflinePlayer owner) {
        if (getItemMeta() instanceof SkullMeta) {
            ((SkullMeta) getItemMeta()).setOwningPlayer(owner);
        }
        return this;
    }

    public ItemBuilder setSkullOwner(String name) {
        if (getItemMeta() instanceof SkullMeta) {
            ((SkullMeta) getItemMeta()).setOwningPlayer(Bukkit.getOfflinePlayer(name));
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

    public ItemBuilder setColor(Color color) {
        if (getItemMeta() instanceof PotionMeta) {
            ((PotionMeta) getItemMeta()).setColor(color);
        } else if (getItemMeta() instanceof FireworkEffectMeta) {
            setEffect(FireworkEffect.builder().withColor(color).build());
        } else if (getItemMeta() instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) getItemMeta()).setColor(color);
        }
        return this;
    }

    public ItemBuilder setCustomModelData(int customModelData) {
        modifyNBT("{CustomModelData:" + customModelData + "}");
        return this;
    }

    public int getCustomModelData() {
        return getNBT().getInt("{CustomModelData}");
    }

    public NBTTagCompound getNBT() {
        return CraftItemStack.asNMSCopy(getItemStack()).getOrCreateTag();
    }

    public ItemBuilder modifyNBT(String nbt) {
        setItemStack(Bukkit.getUnsafe().modifyItemStack(getItemStack(), nbt));
        setItemMeta(getItemStack().getItemMeta());
        return this;
    }

    public ItemBuilder setPower(int power) {
        if (getItemMeta() instanceof FireworkMeta) {
            ((FireworkMeta) getItemMeta()).setPower(power);
        }
        return this;
    }

    public ItemBuilder setEffect(FireworkEffect effect) {
        if (getItemMeta() instanceof FireworkEffectMeta) {
            ((FireworkEffectMeta) getItemMeta()).setEffect(effect);
        }
        return this;
    }

    public ItemBuilder hideFlags() {
        getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS);
        return this;
    }

    public ItemBuilder addFlags(ItemFlag... itemFlags) {
        getItemMeta().addItemFlags(itemFlags);
        return this;
    }

    public ItemBuilder addStoredEnchantment(Enchantment enchantment, int level) {
        if (getItemMeta() instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta) getItemMeta()).addStoredEnchant(enchantment, level, true);
        }
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        getItemMeta().setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        getItemMeta().setLore(lore);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        getItemStack().setAmount(amount);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        getItemMeta().setUnbreakable(true);
        return this;
    }

    public ItemBuilder setBreakable() {
        getItemMeta().setUnbreakable(false);
        return this;
    }
}
