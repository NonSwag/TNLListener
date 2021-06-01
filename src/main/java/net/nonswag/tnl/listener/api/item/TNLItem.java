package net.nonswag.tnl.listener.api.item;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.gui.GUIItem;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URI;
import java.util.*;

public interface TNLItem {

    @Nonnull
    default GUIItem toGUIItem() {
        return new GUIItem(this);
    }

    @Nonnull
    TNLItem setItemStack(@Nonnull ItemStack itemStack);

    @Nonnull
    TNLItem setItemMeta(@Nonnull ItemMeta itemMeta);

    @Nonnull
    ItemStack getItemStack();

    @Nullable
    ItemMeta getItemMeta();

    @Nonnull
    default ItemStack build() {
        getItemStack().setItemMeta(getItemMeta());
        return getItemStack();
    }

    @Nonnull
    default String getName() {
        return getItemMeta() != null ? getItemMeta().getDisplayName() : "";
    }

    @Nonnull
    default TNLItem setName(@Nullable String name) {
        if (getItemMeta() != null) {
            getItemMeta().setDisplayName(name);
        }
        if (getItemMeta() instanceof BookMeta) {
            ((BookMeta) getItemMeta()).setTitle(name);
        }
        return this;
    }

    @Nonnull
    default TNLItem setAuthor(@Nonnull String author) {
        if (getItemMeta() instanceof BookMeta) {
            ((BookMeta) getItemMeta()).setAuthor(author);
        }
        return this;
    }

    @Nonnull
    default TNLItem setGeneration(@Nonnull BookMeta.Generation generation) {
        if (getItemMeta() instanceof BookMeta) {
            ((BookMeta) getItemMeta()).setGeneration(generation);
        }
        return this;
    }

    @Nonnull
    default TNLItem addPage(@Nonnull String... text) {
        if (getItemMeta() instanceof BookMeta) {
            ((BookMeta) getItemMeta()).addPage(text);
        }
        return this;
    }

    @Nonnull
    default TNLItem setAuthor(@Nonnull Enchantment enchantment, int level) {
        if (getItemMeta() != null) {
            getItemMeta().addEnchant(enchantment, level, true);
        }
        return this;
    }

    @Nonnull
    default TNLItem enchant(@Nonnull Enchantment enchantment, int level) {
        if (getItemMeta() != null) {
            getItemMeta().addEnchant(enchantment, level, true);
        }
        return this;
    }

    @Nonnull
    default TNLItem unEnchant(@Nonnull Enchantment enchantment) {
        if (getItemMeta() != null && getItemMeta().hasEnchant(enchantment)) {
            getItemMeta().removeEnchant(enchantment);
        }
        return this;
    }

    @Nonnull
    default TNLItem unEnchant() {
        if (getItemMeta() != null) {
            for (Enchantment enchantment : getItemMeta().getEnchants().keySet()) {
                unEnchant(enchantment);
            }
        }
        return this;
    }

    @Nonnull
    default TNLItem setDamage(float damage) {
        if (getItemMeta() instanceof Damageable) {
            ((Damageable) getItemMeta()).setDamage(((short) damage));
        }
        return this;
    }

    @Nonnull
    default TNLItem hideFlag(@Nonnull ItemFlag itemFlag) {
        if (getItemMeta() != null) {
            getItemMeta().addItemFlags(itemFlag);
        }
        return this;
    }

    @Nonnull
    default TNLItem setSkullOwner(@Nonnull OfflinePlayer owner) {
        if (getItemMeta() instanceof SkullMeta) {
            ((SkullMeta) getItemMeta()).setOwningPlayer(owner);
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    default TNLItem setSkullOwner(@Nonnull String name) {
        if (getItemMeta() instanceof SkullMeta) {
            ((SkullMeta) getItemMeta()).setOwningPlayer(Bukkit.getOfflinePlayer(name));
        }
        return this;
    }

    @Nonnull
    default TNLItem setSkullImgURL(@Nonnull String url) {
        try {
            setSkullValue(Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"" + new URI(url) + "\"}}}").getBytes()));
        } catch (Exception e) {
            Logger.error.println(e);
        }
        return this;
    }

    @Nonnull
    default TNLItem setSkullValue(@Nonnull String base64) {
        try {
            modifyNBT("{SkullOwner:{Id:\"" + new UUID(base64.hashCode(), base64.hashCode()) + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}");
        } catch (Exception e) {
            Logger.error.println(e);
        }
        return this;
    }

    @Nonnull
    default TNLItem setColor(@Nonnull Color color) {
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
    default TNLItem addBannerPattern(@Nonnull Pattern pattern) {
        if (getItemMeta() instanceof BannerMeta) {
            ((BannerMeta) getItemMeta()).addPattern(pattern);
        }
        return this;
    }

    @Nonnull
    default List<Pattern> getBannerPatterns() {
        if (getItemMeta() instanceof BannerMeta) {
            ((BannerMeta) getItemMeta()).getPatterns();
        }
        return new ArrayList<>();
    }

    @Nonnull
    default TNLItem setCustomModelData(int customModelData) {
        modifyNBT("{CustomModelData:" + customModelData + "}");
        return this;
    }

    int getCustomModelData();

    @Nonnull
    <T> T getNBT();

    @SuppressWarnings("deprecation")
    @Nonnull
    default TNLItem modifyNBT(@Nonnull String nbt) {
        setItemStack(Bukkit.getUnsafe().modifyItemStack(getItemStack(), nbt));
        if (getItemStack().getItemMeta() != null) {
            setItemMeta(getItemStack().getItemMeta());
        }
        return this;
    }

    @Nonnull
    default TNLItem setPower(int power) {
        if (getItemMeta() instanceof FireworkMeta) {
            ((FireworkMeta) getItemMeta()).setPower(power);
        }
        return this;
    }

    @Nonnull
    default TNLItem setEffect(@Nonnull FireworkEffect effect) {
        if (getItemMeta() instanceof FireworkEffectMeta) {
            ((FireworkEffectMeta) getItemMeta()).setEffect(effect);
        }
        return this;
    }

    @Nonnull
    default TNLItem addEffects(@Nonnull PotionEffect... effects) {
        if (getItemMeta() instanceof PotionMeta) {
            for (PotionEffect effect : effects) {
                ((PotionMeta) getItemMeta()).addCustomEffect(effect, false);
            }
        }
        return this;
    }

    @Nonnull
    default TNLItem hideFlags() {
        if (getItemMeta() != null) {
            getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS);
        }
        return this;
    }

    @Nonnull
    default TNLItem addAttribute(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier) {
        if (getItemMeta() != null) {
            getItemMeta().addAttributeModifier(attribute, modifier);
        }
        return this;
    }

    @Nonnull
    default TNLItem removeAttributes(@Nonnull Attribute... attributes) {
        if (getItemMeta() != null) {
            for (Attribute attribute : attributes) {
                getItemMeta().removeAttributeModifier(attribute);
            }
        }
        return this;
    }

    @Nonnull
    default TNLItem addFlags(@Nonnull ItemFlag... itemFlags) {
        if (getItemMeta() != null) {
            getItemMeta().addItemFlags(itemFlags);
        }
        return this;
    }

    @Nonnull
    default TNLItem addStoredEnchantment(@Nonnull Enchantment enchantment, int level) {
        if (getItemMeta() instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta) getItemMeta()).addStoredEnchant(enchantment, level, true);
        }
        return this;
    }

    @Nonnull
    default TNLItem removeStoredEnchantment(@Nonnull Enchantment enchantment) {
        if (getItemMeta() instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta) getItemMeta()).removeStoredEnchant(enchantment);
        }
        return this;
    }

    @Nonnull
    default TNLItem setLore(@Nonnull String... lore) {
        return setLore(Arrays.asList(lore));
    }

    @Nonnull
    default TNLItem setLore(@Nonnull List<String> lore) {
        if (getItemMeta() != null) {
            getItemMeta().setLore(lore);
        }
        return this;
    }

    @Nonnull
    default TNLItem removeLore() {
        if (getItemMeta() != null) {
            getItemMeta().setLore(null);
        }
        return this;
    }

    @Nonnull
    default TNLItem setAmount(int amount) {
        getItemStack().setAmount(amount);
        return this;
    }

    @Nonnull
    default TNLItem setUnbreakable(boolean unbreakable) {
        if (getItemMeta() != null) {
            getItemMeta().setUnbreakable(unbreakable);
        }
        return this;
    }

    @Nonnull
    default Material getType() {
        return getItemStack().getType();
    }

    default boolean isAir() {
        return TNLItemType.isAir(this);
    }

    default int getMaxStackSize() {
        return getItemStack().getMaxStackSize();
    }

    default int getAmount() {
        return getItemStack().getAmount();
    }

    @Nonnull
    static TNLItem create(@Nonnull ItemStack itemStack) {
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4) || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5)) {
            return new net.nonswag.tnl.listener.api.item.v1_16.R3.NMSItem(itemStack);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            return new net.nonswag.tnl.listener.api.item.v1_15.R1.NMSItem(itemStack);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_10)) {
            return new net.nonswag.tnl.listener.api.item.v1_7.R4.NMSItem(itemStack);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            return new net.nonswag.tnl.listener.api.item.v1_7.R1.NMSItem(itemStack);
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }

    @Nonnull
    static TNLItem create(@Nonnull Material material) {
        return create(new ItemStack(material));
    }

    @Nonnull
    static TNLItem create(@Nonnull OfflinePlayer player) {
        return create(Material.PLAYER_HEAD).setSkullOwner(player);
    }
}
