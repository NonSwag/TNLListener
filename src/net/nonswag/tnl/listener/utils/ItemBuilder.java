package net.nonswag.tnl.listener.utils;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.nonswag.tnl.listener.api.logger.Logger;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {

    @Nonnull private ItemStack itemStack;
    @Nonnull private ItemMeta itemMeta;

    public ItemBuilder(@Nonnull ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(@Nonnull Material material) {
        this(new ItemStack(material));
    }

    public void setItemStack(@Nonnull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setItemMeta(@Nonnull ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
    }

    @Nonnull
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Nonnull
    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    @Nonnull
    public ItemStack build() {
        getItemStack().setItemMeta(getItemMeta());
        return getItemStack();
    }

    @Nonnull
    public ItemBuilder setName(@Nonnull String name) {
        getItemMeta().setDisplayName(name);
        return this;
    }

    @Nonnull
    public ItemBuilder enchant(@Nonnull Enchantment enchantment, int level) {
        getItemMeta().addEnchant(enchantment, level, true);
        return this;
    }


    @Nonnull
    public ItemBuilder setDamage(float damage) {
        if (getItemMeta() instanceof Damageable) {
            ((Damageable) getItemMeta()).setDamage(((short) damage));
        }
        return this;
    }

    @Nonnull
    public ItemBuilder hideFlag(@Nonnull ItemFlag itemFlag) {
        getItemMeta().addItemFlags(itemFlag);
        return this;
    }

    @Nonnull
    public ItemBuilder setSkullOwner(@Nonnull OfflinePlayer owner) {
        if (getItemMeta() instanceof SkullMeta) {
            ((SkullMeta) getItemMeta()).setOwningPlayer(owner);
        }
        return this;
    }

    @Nonnull
    public ItemBuilder setSkullOwner(@Nonnull String name) {
        if (getItemMeta() instanceof SkullMeta) {
            ((SkullMeta) getItemMeta()).setOwningPlayer(Bukkit.getOfflinePlayer(name));
        }
        return this;
    }

    @Nonnull
    public ItemBuilder setSkullImgURL(@Nonnull String url) {
        try {
            setSkullValue(Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"" + new URI(url).toString() + "\"}}}").getBytes()));
        } catch (Exception e) {
            Logger.error.println(e);
        }
        return this;
    }

    @Nonnull
    public ItemBuilder setSkullValue(@Nonnull String base64) {
        try {
            modifyNBT("{SkullOwner:{Id:\"" + new UUID(base64.hashCode(), base64.hashCode()) + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}");
        } catch (Exception e) {
            Logger.error.println(e);
        }
        return this;
    }

    @Nonnull
    public ItemBuilder setColor(@Nonnull Color color) {
        if (getItemMeta() instanceof PotionMeta) {
            ((PotionMeta) getItemMeta()).setColor(color);
        } else if (getItemMeta() instanceof FireworkEffectMeta) {
            setEffect(FireworkEffect.builder().withColor(color).build());
        } else if (getItemMeta() instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) getItemMeta()).setColor(color);
        }
        return this;
    }

    @Nonnull
    public ItemBuilder setCustomModelData(int customModelData) {
        modifyNBT("{CustomModelData:" + customModelData + "}");
        return this;
    }

    public int getCustomModelData() {
        return getNBT().getInt("{CustomModelData}");
    }

    @Nonnull
    public NBTTagCompound getNBT() {
        return CraftItemStack.asNMSCopy(getItemStack()).getOrCreateTag();
    }

    @Nonnull
    public ItemBuilder modifyNBT(@Nonnull String nbt) {
        setItemStack(Bukkit.getUnsafe().modifyItemStack(getItemStack(), nbt));
        setItemMeta(getItemStack().getItemMeta());
        return this;
    }

    @Nonnull
    public ItemBuilder setPower(int power) {
        if (getItemMeta() instanceof FireworkMeta) {
            ((FireworkMeta) getItemMeta()).setPower(power);
        }
        return this;
    }

    @Nonnull
    public ItemBuilder setEffect(@Nonnull FireworkEffect effect) {
        if (getItemMeta() instanceof FireworkEffectMeta) {
            ((FireworkEffectMeta) getItemMeta()).setEffect(effect);
        }
        return this;
    }

    @Nonnull
    public ItemBuilder addEffects(@Nonnull PotionEffect... effects) {
        if (getItemMeta() instanceof PotionMeta) {
            for (PotionEffect effect : effects) {
                ((PotionMeta) getItemMeta()).addCustomEffect(effect, false);
            }
        }
        return this;
    }

    @Nonnull
    public ItemBuilder hideFlags() {
        getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS);
        return this;
    }

    @Nonnull
    public ItemBuilder addAttribute(Attribute attribute, AttributeModifier modifier) {
        getItemMeta().addAttributeModifier(attribute, modifier);
        return this;
    }

    @Nonnull
    public ItemBuilder removeAttributes(Attribute... attributes) {
        for (Attribute attribute : attributes) {
            getItemMeta().removeAttributeModifier(attribute);
        }
        return this;
    }

    @Nonnull
    public ItemBuilder addFlags(ItemFlag... itemFlags) {
        getItemMeta().addItemFlags(itemFlags);
        return this;
    }

    @Nonnull
    public ItemBuilder addStoredEnchantment(Enchantment enchantment, int level) {
        if (getItemMeta() instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta) getItemMeta()).addStoredEnchant(enchantment, level, true);
        }
        return this;
    }

    @Nonnull
    public ItemBuilder setLore(String... lore) {
        getItemMeta().setLore(Arrays.asList(lore));
        return this;
    }

    @Nonnull
    public ItemBuilder setLore(List<String> lore) {
        getItemMeta().setLore(lore);
        return this;
    }

    @Nonnull
    public ItemBuilder setAmount(int amount) {
        getItemStack().setAmount(amount);
        return this;
    }

    @Nonnull
    public ItemBuilder setUnbreakable() {
        getItemMeta().setUnbreakable(true);
        return this;
    }

    @Nonnull
    public ItemBuilder setBreakable() {
        getItemMeta().setUnbreakable(false);
        return this;
    }
}
