package net.nonswag.tnl.listener.api.item.v1_16.R3;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.logger.Logger;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.banner.Pattern;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.*;

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

    @Override
    public void setItemStack(@Nonnull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public void setItemMeta(@Nonnull ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
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

    @Nonnull
    @Override
    public ItemStack build() {
        getItemStack().setItemMeta(getItemMeta());
        return getItemStack();
    }

    @Nonnull
    @Override
    public TNLItem setName(@Nonnull String name) {
        getItemMeta().setDisplayName(name);
        return this;
    }

    @Nonnull
    @Override
    public TNLItem enchant(@Nonnull Enchantment enchantment, int level) {
        getItemMeta().addEnchant(enchantment, level, true);
        return this;
    }

    @Nonnull
    @Override
    public TNLItem setDamage(float damage) {
        if (getItemMeta() instanceof Damageable) {
            ((Damageable) getItemMeta()).setDamage(((short) damage));
        }
        return this;
    }

    @Nonnull
    @Override
    public TNLItem hideFlag(@Nonnull ItemFlag itemFlag) {
        getItemMeta().addItemFlags(itemFlag);
        return this;
    }

    @Nonnull
    @Override
    public TNLItem setSkullOwner(@Nonnull OfflinePlayer owner) {
        if (getItemMeta() instanceof SkullMeta) {
            ((SkullMeta) getItemMeta()).setOwningPlayer(owner);
        }
        return this;
    }

    @Nonnull
    @Override
    public TNLItem setSkullOwner(@Nonnull String name) {
        if (getItemMeta() instanceof SkullMeta) {
            ((SkullMeta) getItemMeta()).setOwningPlayer(Bukkit.getOfflinePlayer(name));
        }
        return this;
    }

    @Nonnull
    @Override
    public TNLItem setSkullImgURL(@Nonnull String url) {
        try {
            setSkullValue(Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"" + new URI(url).toString() + "\"}}}").getBytes()));
        } catch (Exception e) {
            Logger.error.println(e);
        }
        return this;
    }

    @Nonnull
    @Override
    public TNLItem setSkullValue(@Nonnull String base64) {
        try {
            modifyNBT("{SkullOwner:{Id:\"" + new UUID(base64.hashCode(), base64.hashCode()) + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}");
        } catch (Exception e) {
            Logger.error.println(e);
        }
        return this;
    }

    @Nonnull
    @Override
    public TNLItem setColor(@Nonnull Color color) {
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
    @Override
    public TNLItem addBannerPattern(@Nonnull Pattern pattern) {
        if (getItemMeta() instanceof BannerMeta) {
            ((BannerMeta) getItemMeta()).addPattern(pattern);
        }
        return this;
    }

    @Nonnull
    @Override
    public List<Pattern> getBannerPatterns() {
        if (getItemMeta() instanceof BannerMeta) {
            ((BannerMeta) getItemMeta()).getPatterns();
        }
        return new ArrayList<>();
    }

    @Nonnull
    @Override
    public TNLItem setCustomModelData(int customModelData) {
        modifyNBT("{CustomModelData:" + customModelData + "}");
        return this;
    }

    @Override
    public int getCustomModelData() {
        return getNBT().getInt("{CustomModelData}");
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBT() {
        return CraftItemStack.asNMSCopy(getItemStack()).getOrCreateTag();
    }

    @Nonnull
    @Override
    public TNLItem modifyNBT(@Nonnull String nbt) {
        setItemStack(Bukkit.getUnsafe().modifyItemStack(getItemStack(), nbt));
        setItemMeta(getItemStack().getItemMeta());
        return this;
    }

    @Nonnull
    @Override
    public TNLItem setPower(int power) {
        if (getItemMeta() instanceof FireworkMeta) {
            ((FireworkMeta) getItemMeta()).setPower(power);
        }
        return this;
    }

    @Nonnull
    @Override
    public TNLItem setEffect(@Nonnull FireworkEffect effect) {
        if (getItemMeta() instanceof FireworkEffectMeta) {
            ((FireworkEffectMeta) getItemMeta()).setEffect(effect);
        }
        return this;
    }

    @Nonnull
    @Override
    public TNLItem addEffects(@Nonnull PotionEffect... effects) {
        if (getItemMeta() instanceof PotionMeta) {
            for (PotionEffect effect : effects) {
                ((PotionMeta) getItemMeta()).addCustomEffect(effect, false);
            }
        }
        return this;
    }

    @Nonnull
    @Override
    public TNLItem hideFlags() {
        getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS);
        return this;
    }

    @Nonnull
    @Override
    public TNLItem addAttribute(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier) {
        getItemMeta().addAttributeModifier(attribute, modifier);
        return this;
    }

    @Nonnull
    @Override
    public TNLItem removeAttributes(@Nonnull Attribute... attributes) {
        for (Attribute attribute : attributes) {
            getItemMeta().removeAttributeModifier(attribute);
        }
        return this;
    }

    @Nonnull
    @Override
    public TNLItem addFlags(@Nonnull ItemFlag... itemFlags) {
        getItemMeta().addItemFlags(itemFlags);
        return this;
    }

    @Nonnull
    @Override
    public TNLItem addStoredEnchantment(@Nonnull Enchantment enchantment, int level) {
        if (getItemMeta() instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta) getItemMeta()).addStoredEnchant(enchantment, level, true);
        }
        return this;
    }

    @Nonnull
    @Override
    public TNLItem setLore(@Nonnull String... lore) {
        getItemMeta().setLore(Arrays.asList(lore));
        return this;
    }

    @Nonnull
    @Override
    public TNLItem setLore(@Nonnull List<String> lore) {
        getItemMeta().setLore(lore);
        return this;
    }

    @Nonnull
    @Override
    public TNLItem setAmount(int amount) {
        getItemStack().setAmount(amount);
        return this;
    }

    @Nonnull
    @Override
    public TNLItem setUnbreakable() {
        getItemMeta().setUnbreakable(true);
        return this;
    }

    @Nonnull
    @Override
    public TNLItem setBreakable() {
        getItemMeta().setUnbreakable(false);
        return this;
    }
}