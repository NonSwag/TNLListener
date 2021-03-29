package net.nonswag.tnl.listener.api.item;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nonnull;
import java.util.List;

public interface TNLItem {

    void setItemStack(@Nonnull ItemStack itemStack);

    void setItemMeta(@Nonnull ItemMeta itemMeta);

    @Nonnull
    ItemStack getItemStack();

    @Nonnull
    ItemMeta getItemMeta();

    @Nonnull
    ItemStack build();

    @Nonnull
    TNLItem setName(@Nonnull String name);

    @Nonnull
    TNLItem enchant(@Nonnull Enchantment enchantment, int level);

    @Nonnull
    TNLItem setDamage(float damage);

    @Nonnull
    TNLItem hideFlag(@Nonnull ItemFlag itemFlag);

    @Nonnull
    TNLItem setSkullOwner(@Nonnull OfflinePlayer owner);

    @Nonnull
    TNLItem setSkullOwner(@Nonnull String name);

    @Nonnull
    TNLItem setSkullImgURL(@Nonnull String url);

    @Nonnull
    TNLItem setSkullValue(@Nonnull String base64);

    @Nonnull
    TNLItem setColor(@Nonnull Color color);

    @Nonnull
    TNLItem addBannerPattern(@Nonnull Pattern pattern);

    @Nonnull
    List<Pattern> getBannerPatterns();

    @Nonnull
    TNLItem setCustomModelData(int customModelData);

    int getCustomModelData();

    @Nonnull
    <T> T getNBT();

    @Nonnull
    TNLItem modifyNBT(@Nonnull String nbt);

    @Nonnull
    TNLItem setPower(int power);

    @Nonnull
    TNLItem setEffect(@Nonnull FireworkEffect effect);

    @Nonnull
    TNLItem addEffects(@Nonnull PotionEffect... effects);

    @Nonnull
    TNLItem hideFlags();

    @Nonnull
    TNLItem addAttribute(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier);

    @Nonnull
    TNLItem removeAttributes(@Nonnull Attribute... attributes);

    @Nonnull
    TNLItem addFlags(@Nonnull ItemFlag... itemFlags);

    @Nonnull
    TNLItem addStoredEnchantment(@Nonnull Enchantment enchantment, int level);

    @Nonnull
    TNLItem setLore(@Nonnull String... lore);

    @Nonnull
    TNLItem setLore(@Nonnull List<String> lore);

    @Nonnull
    TNLItem setAmount(int amount);

    @Nonnull
    TNLItem setUnbreakable();

    @Nonnull
    TNLItem setBreakable();

}
