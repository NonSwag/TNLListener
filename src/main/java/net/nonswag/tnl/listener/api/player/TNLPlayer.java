package net.nonswag.tnl.listener.api.player;

import net.nonswag.tnl.listener.Bootstrap;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.bossbar.TNLBossBar;
import net.nonswag.tnl.listener.api.chat.Conversation;
import net.nonswag.tnl.listener.api.entity.TNLEntityPlayer;
import net.nonswag.tnl.listener.api.file.FileCreator;
import net.nonswag.tnl.listener.api.gui.GUI;
import net.nonswag.tnl.listener.api.gui.GUIItem;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.logger.Color;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.message.*;
import net.nonswag.tnl.listener.api.object.Generic;
import net.nonswag.tnl.listener.api.packet.*;
import net.nonswag.tnl.listener.api.permission.PermissionManager;
import net.nonswag.tnl.listener.api.permission.Permissions;
import net.nonswag.tnl.listener.api.scoreboard.Sidebar;
import net.nonswag.tnl.listener.api.scoreboard.Team;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.listener.api.sign.SignMenu;
import net.nonswag.tnl.listener.api.storage.VirtualStorage;
import net.nonswag.tnl.listener.api.title.Title;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import net.nonswag.tnl.listener.events.InventoryLoadEvent;
import net.nonswag.tnl.listener.events.InventorySaveEvent;
import net.nonswag.tnl.listener.events.PlayerChatEvent;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * TNLPlayer is a more net.minecraft.server based player
 **/

public interface TNLPlayer extends TNLEntityPlayer, Player {

    @Nonnull
    @Override
    default Entity getBukkitEntity() {
        return getBukkitPlayer();
    }

    @Nonnull
    VirtualStorage getVirtualStorage();

    @Override
    @Nonnull
    default String getDisplayName() {
        return getBukkitPlayer().getDisplayName();
    }

    @Override
    default void setDisplayName(@Nullable String displayName) {
        getBukkitPlayer().setDisplayName(displayName);
    }

    @Override
    @Nonnull
    default String getPlayerListName() {
        return getBukkitPlayer().getPlayerListName();
    }

    @Override
    default void setPlayerListName(@Nullable String playerListName) {
        getBukkitPlayer().setPlayerListName(playerListName);
    }

    @Override
    default void setCompassTarget(@Nonnull Location location) {
        getBukkitPlayer().setCompassTarget(location);
    }

    @Override
    @Nonnull
    default Location getCompassTarget() {
        return getBukkitPlayer().getCompassTarget();
    }

    @Override
    @Nullable
    default String getPlayerListHeader() {
        return getBukkitPlayer().getPlayerListHeader();
    }

    @Override
    @Nullable
    default String getPlayerListFooter() {
        return getBukkitPlayer().getPlayerListFooter();
    }

    @Override
    default void setPlayerListHeader(@Nullable String header) {
        getBukkitPlayer().setPlayerListHeader(header);
    }

    @Override
    default void setPlayerListFooter(@Nullable String footer) {
        getBukkitPlayer().setPlayerListFooter(footer);
    }

    @Override
    default void setPlayerListHeaderFooter(@Nullable String header, @Nullable String footer) {
        getBukkitPlayer().setPlayerListHeaderFooter(header, footer);
    }

    @Override
    default void playSound(@Nonnull Location location, @Nonnull Sound sound, @Nonnull SoundCategory soundCategory, float volume, float pitch) {
        getBukkitPlayer().playSound(location, sound, soundCategory, volume, pitch);
    }

    @Override
    default void playSound(@Nonnull Location location, @Nonnull String sound, @Nonnull SoundCategory soundCategory, float volume, float pitch) {
        getBukkitPlayer().playSound(location, sound, soundCategory, volume, pitch);
    }

    @Override
    default void stopSound(@Nonnull Sound sound) {
        getBukkitPlayer().stopSound(sound);
    }

    @Override
    default void stopSound(@Nonnull String sound) {
        getBukkitPlayer().stopSound(sound);
    }

    @Override
    default void stopSound(@Nonnull Sound sound, @Nullable SoundCategory soundCategory) {
        getBukkitPlayer().stopSound(sound, soundCategory);
    }

    @Override
    default void stopSound(@Nonnull String sound, @Nullable SoundCategory soundCategory) {
        getBukkitPlayer().stopSound(sound, soundCategory);
    }

    @Override
    default void sendBlockChange(@Nonnull Location location, @Nonnull BlockData blockData) {
        getBukkitPlayer().sendBlockChange(location, blockData);
    }

    @Override
    default void sendSignChange(@Nonnull Location location, @Nullable String[] lines) throws IllegalArgumentException {
        getBukkitPlayer().sendSignChange(location, lines);
    }

    @Override
    default void sendSignChange(@Nonnull Location location, @Nullable String[] lines, @Nonnull DyeColor color) throws IllegalArgumentException {
        getBukkitPlayer().sendSignChange(location, lines, color);
    }

    @Nullable
    @Override
    default InetSocketAddress getAddress() {
        return getBukkitPlayer().getAddress();
    }

    @Override
    default void sendRawMessage(@Nonnull String message) {
        getBukkitPlayer().sendRawMessage(message);
    }

    @Override
    default void chat(@Nonnull String message) {
        chat(new PlayerChatEvent(this, message));
    }

    default void chat(@Nonnull PlayerChatEvent event) {
        String message = Color.Minecraft.unColorize(event.getMessage(), '§');
        if (message.length() <= 0 || Color.Minecraft.unColorize(message.replace(" ", ""), '&').isEmpty()) {
            event.setCancelled(true);
            return;
        }
        if (Conversation.test(event, event.getPlayer(), event.getMessage())) return;
        if (event.call()) {
            if (!event.isCommand()) {
                event.setCancelled(true);
                String[] strings = message.split(" ");
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (message.toLowerCase().contains(all.getName().toLowerCase())) {
                        for (String string : strings) {
                            if (string.equalsIgnoreCase("@" + all.getName())) {
                                message = message.replace(string + " ", "§8(§3" + all.getName() + "§8) §f");
                                message = message.replace(string, "§8(§3" + all.getName() + "§8) §f");
                                all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                            }
                        }
                    }
                }
                for (TNLPlayer all : TNLListener.getInstance().getOnlinePlayers()) {
                    if (event.getFormat() == null) {
                        all.sendMessage(MessageKey.CHAT_FORMAT, new Placeholder("world", event.getPlayer().getWorldAlias()), new Placeholder("player", event.getPlayer().getName()), new Placeholder("display_name", event.getPlayer().getDisplayName()), new Placeholder("color", event.getPlayer().getTeam().getColor().toString()), new Placeholder("message", message), new Placeholder("colored_message", net.nonswag.tnl.listener.api.logger.Color.Minecraft.colorize(message, '&')), new Placeholder("text", net.nonswag.tnl.listener.api.logger.Color.Minecraft.colorize(message, '&')));
                    } else {
                        all.sendMessage(event.getFormat(), new Placeholder("world", event.getPlayer().getWorldAlias()), new Placeholder("player", event.getPlayer().getName()), new Placeholder("display_name", event.getPlayer().getDisplayName()), new Placeholder("color", event.getPlayer().getTeam().getColor().toString()), new Placeholder("message", message), new Placeholder("colored_message", Color.Minecraft.colorize(message, '&')));
                    }
                }
            }
        } else {
            event.setCancelled(true);
        }
    }

    @Override
    default boolean performCommand(@Nonnull String command) {
        return getBukkitPlayer().performCommand(command);
    }

    @Override
    default boolean isSneaking() {
        return getBukkitPlayer().isSneaking();
    }

    @Override
    default void setSneaking(boolean sneaking) {
        getBukkitPlayer().setSneaking(sneaking);
    }

    @Override
    default boolean isSprinting() {
        return getBukkitPlayer().isSprinting();
    }

    @Override
    default void setSprinting(boolean sprinting) {
        getBukkitPlayer().setSprinting(sprinting);
    }

    @Override
    default void saveData() {
        getBukkitPlayer().saveData();
    }

    @Override
    default void loadData() {
        getBukkitPlayer().loadData();
    }

    @Override
    default void setSleepingIgnored(boolean sleepingIgnored) {
        getBukkitPlayer().setSleepingIgnored(sleepingIgnored);
    }

    @Override
    default boolean isSleepingIgnored() {
        return getBukkitPlayer().isSleepingIgnored();
    }

    @Deprecated
    @Override
    default void playNote(@Nonnull Location location, byte b, byte b1) {
        getBukkitPlayer().playNote(location, b, b1);
    }

    @Override
    default void playNote(@Nonnull Location location, @Nonnull Instrument instrument, @Nonnull Note note) {
        getBukkitPlayer().playNote(location, instrument, note);
    }

    default void playSound(@Nonnull Sound sound, float volume, float pitch) {
        getBukkitPlayer().playSound(getLocation(), sound, volume, pitch);
    }

    @Override
    default void playSound(@Nonnull Location location, @Nonnull Sound sound, float volume, float pitch) {
        getBukkitPlayer().playSound(location, sound, volume, pitch);
    }

    @Deprecated
    @Override
    default void playSound(@Nonnull Location location, @Nonnull String sound, float volume, float pitch) {
        getBukkitPlayer().playSound(location, sound, volume, pitch);
    }

    @Deprecated
    @Override
    default void playEffect(@Nonnull Location location, @Nonnull Effect effect, int amount) {
        getBukkitPlayer().playEffect(location, effect, amount);
    }

    @Override
    default <T> void playEffect(@Nonnull Location location, @Nonnull Effect effect, @Nullable T t) {
        getBukkitPlayer().playEffect(location, effect, t);
    }

    @Deprecated
    @Override
    default void sendBlockChange(@Nonnull Location location, @Nonnull Material material, byte data) {
        getBukkitPlayer().sendBlockChange(location, material, data);
    }

    @Deprecated
    @Override
    default boolean sendChunkChange(@Nonnull Location location, int x, int y, int z, @Nonnull byte[] bytes) {
        return getBukkitPlayer().sendChunkChange(location, x, y, z, bytes);
    }

    @Override
    default void sendMap(@Nonnull MapView mapView) {
        getBukkitPlayer().sendMap(mapView);
    }

    @Override
    default void updateInventory() {
        getBukkitPlayer().updateInventory();
    }

    @Override
    default void setPlayerTime(long time, boolean b) {
        getBukkitPlayer().setPlayerTime(time, b);
    }

    @Override
    default long getPlayerTime() {
        return getBukkitPlayer().getPlayerTime();
    }

    @Override
    default long getPlayerTimeOffset() {
        return getBukkitPlayer().getPlayerTimeOffset();
    }

    @Override
    default boolean isPlayerTimeRelative() {
        return getBukkitPlayer().isPlayerTimeRelative();
    }

    @Override
    default void resetPlayerTime() {
        getBukkitPlayer().resetPlayerTime();
    }

    @Override
    default void setPlayerWeather(@Nonnull WeatherType weatherType) {
        getBukkitPlayer().setPlayerWeather(weatherType);
    }

    @Nullable
    @Override
    default WeatherType getPlayerWeather() {
        return getBukkitPlayer().getPlayerWeather();
    }

    @Override
    default void resetPlayerWeather() {
        getBukkitPlayer().resetPlayerWeather();
    }

    @Override
    default void giveExpLevels(int levels) {
        getBukkitPlayer().giveExpLevels(levels);
    }

    @Override
    default float getExp() {
        return getBukkitPlayer().getExp();
    }

    @Override
    default void setExp(float exp) {
        getBukkitPlayer().setExp(exp);
    }

    @Override
    default int getLevel() {
        return getBukkitPlayer().getLevel();
    }

    @Override
    default void setLevel(int level) {
        getBukkitPlayer().setLevel(level);
    }

    @Override
    default int getTotalExperience() {
        return getBukkitPlayer().getTotalExperience();
    }

    @Override
    default void setTotalExperience(int experience) {
        getBukkitPlayer().setTotalExperience(experience);
    }

    @Override
    default void sendExperienceChange(float v) {
        getBukkitPlayer().sendExperienceChange(v);
    }

    @Override
    default void sendExperienceChange(float v, int experience) {
        getBukkitPlayer().sendExperienceChange(v, experience);
    }

    @Override
    default float getExhaustion() {
        return getBukkitPlayer().getExhaustion();
    }

    @Override
    default void setExhaustion(float exhaustion) {
        getBukkitPlayer().setExhaustion(exhaustion);
    }

    @Override
    default float getSaturation() {
        return getBukkitPlayer().getSaturation();
    }

    @Override
    default void setSaturation(float saturation) {
        getBukkitPlayer().setSaturation(saturation);
    }

    @Override
    default int getFoodLevel() {
        return getBukkitPlayer().getFoodLevel();
    }

    @Override
    default void setFoodLevel(int foodLevel) {
        getBukkitPlayer().setFoodLevel(foodLevel);
    }

    @Override
    default boolean getAllowFlight() {
        return getBukkitPlayer().getAllowFlight();
    }

    @Override
    default void setAllowFlight(boolean flight) {
        getBukkitPlayer().setAllowFlight(flight);
    }

    @Deprecated
    @Override
    default void hidePlayer(@Nonnull Player player) {
        getBukkitPlayer().hidePlayer(player);
    }

    @Override
    default void hidePlayer(@Nonnull Plugin plugin, @Nonnull Player player) {
        getBukkitPlayer().hidePlayer(plugin, player);
    }

    @Deprecated
    @Override
    default void showPlayer(@Nonnull Player player) {
        getBukkitPlayer().showPlayer(player);
    }

    @Override
    default void showPlayer(@Nonnull Plugin plugin, @Nonnull Player player) {
        getBukkitPlayer().showPlayer(plugin, player);
    }

    @Override
    default boolean canSee(@Nonnull Player player) {
        return getBukkitPlayer().canSee(player);
    }

    @Override
    default boolean isFlying() {
        return getBukkitPlayer().isFlying();
    }

    @Override
    default void setFlying(boolean flying) {
        getBukkitPlayer().setFlying(flying);
    }

    @Override
    default void setFlySpeed(float speed) throws IllegalArgumentException {
        getBukkitPlayer().setFlySpeed(speed);
    }

    @Override
    default void setWalkSpeed(float speed) throws IllegalArgumentException {
        getBukkitPlayer().setWalkSpeed(speed);
    }

    @Override
    default float getFlySpeed() {
        return getBukkitPlayer().getFlySpeed();
    }

    @Override
    default float getWalkSpeed() {
        return getBukkitPlayer().getWalkSpeed();
    }

    @Deprecated
    @Override
    default void setTexturePack(@Nonnull String url) {
        getBukkitPlayer().setTexturePack(url);
    }

    @Override
    default void setResourcePack(@Nonnull String url) {
        getBukkitPlayer().setResourcePack(url);
    }

    @Override
    default void setResourcePack(@Nonnull String url, @Nonnull byte[] bytes) {
        getBukkitPlayer().setResourcePack(url, bytes);
    }

    @Override
    default void setScoreboard(@Nonnull Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        getBukkitPlayer().setScoreboard(scoreboard);
    }

    @Override
    default boolean isHealthScaled() {
        return getBukkitPlayer().isHealthScaled();
    }

    @Override
    default void setHealthScaled(boolean healthScaled) {
        getBukkitPlayer().setHealthScaled(healthScaled);
    }

    @Override
    default void setHealthScale(double healthScale) throws IllegalArgumentException {
        getBukkitPlayer().setHealthScale(healthScale);
    }

    @Override
    default double getHealthScale() {
        return getBukkitPlayer().getHealthScale();
    }

    @Override
    @Nullable
    default Entity getSpectatorTarget() {
        return getBukkitPlayer().getSpectatorTarget();
    }

    @Override
    default void setSpectatorTarget(@Nullable Entity entity) {
        getBukkitPlayer().setSpectatorTarget(entity);
    }

    @Override
    default void sendTitle(@Nullable String title, @Nullable String subtitle) {
        getBukkitPlayer().sendTitle(title, subtitle, 70, 0, 10);
    }

    @Override
    default void sendTitle(@Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut) {
        getBukkitPlayer().sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    default void resetTitle() {
        getBukkitPlayer().resetTitle();
    }

    @Override
    default void spawnParticle(@Nonnull Particle particle, @Nonnull Location location, int i) {
        getBukkitPlayer().spawnParticle(particle, location, i);
    }

    @Override
    default void spawnParticle(@Nonnull Particle particle, double v, double v1, double v2, int i) {
        getBukkitPlayer().spawnParticle(particle, v, v1, v2, i);
    }

    @Override
    default <T> void spawnParticle(@Nonnull Particle particle, @Nonnull Location location, int i, @Nullable T t) {
        getBukkitPlayer().spawnParticle(particle, location, i, t);
    }

    @Override
    default <T> void spawnParticle(@Nonnull Particle particle, double v, double v1, double v2, int i, @Nullable T t) {
        getBukkitPlayer().spawnParticle(particle, v, v1, v2, i, t);
    }

    @Override
    default void spawnParticle(@Nonnull Particle particle, @Nonnull Location location, int i, double v, double v1, double v2) {
        getBukkitPlayer().spawnParticle(particle, location, i, v, v1, v2);
    }

    @Override
    default void spawnParticle(@Nonnull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5) {
        getBukkitPlayer().spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
    }

    @Override
    default <T> void spawnParticle(@Nonnull Particle particle, @Nonnull Location location, int i, double v, double v1, double v2, @Nullable T t) {
        getBukkitPlayer().spawnParticle(particle, location, i, v, v1, v2, t);
    }

    @Override
    default <T> void spawnParticle(@Nonnull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, @Nullable T t) {
        getBukkitPlayer().spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
    }

    @Override
    default void spawnParticle(@Nonnull Particle particle, @Nonnull Location location, int i, double v, double v1, double v2, double v3) {
        getBukkitPlayer().spawnParticle(particle, location, i, v, v1, v2, v3);
    }

    @Override
    default void spawnParticle(@Nonnull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6) {
        getBukkitPlayer().spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
    }

    @Override
    default <T> void spawnParticle(@Nonnull Particle particle, @Nonnull Location location, int i, double v, double v1, double v2, double v3, @Nullable T t) {
        getBukkitPlayer().spawnParticle(particle, location, i, v, v1, v2, v3, t);
    }

    @Override
    default <T> void spawnParticle(@Nonnull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6, @Nullable T t) {
        getBukkitPlayer().spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t);
    }

    @Nonnull
    @Override
    default AdvancementProgress getAdvancementProgress(@Nonnull Advancement advancement) {
        return getBukkitPlayer().getAdvancementProgress(advancement);
    }

    @Override
    default int getClientViewDistance() {
        return getBukkitPlayer().getClientViewDistance();
    }

    @Override
    @Nonnull
    default String getLocale() {
        return getBukkitPlayer().getLocale();
    }

    @Override
    default void updateCommands() {
        getBukkitPlayer().updateCommands();
    }

    @Override
    default void openBook(@Nonnull ItemStack itemStack) {
        getBukkitPlayer().openBook(itemStack);
    }

    @Override
    @Nonnull
    default Spigot spigot() {
        return getBukkitPlayer().spigot();
    }

    @Override
    default boolean isOnline() {
        return getBukkitPlayer().isOnline();
    }

    @Override
    default boolean isBanned() {
        return getBukkitPlayer().isBanned();
    }

    @Override
    default boolean isWhitelisted() {
        return getBukkitPlayer().isWhitelisted();
    }

    @Override
    default void setWhitelisted(boolean whitelisted) {
        getBukkitPlayer().setWhitelisted(whitelisted);
    }

    @Override
    @Nullable
    default Player getPlayer() {
        return getBukkitPlayer();
    }

    @Override
    default long getFirstPlayed() {
        return getBukkitPlayer().getFirstPlayed();
    }

    @Override
    default long getLastPlayed() {
        return getBukkitPlayer().getLastPlayed();
    }

    @Override
    default boolean hasPlayedBefore() {
        return getBukkitPlayer().hasPlayedBefore();
    }

    @Override
    default void incrementStatistic(@Nonnull Statistic statistic) throws IllegalArgumentException {
        getBukkitPlayer().incrementStatistic(statistic);
    }

    @Override
    default void decrementStatistic(@Nonnull Statistic statistic) throws IllegalArgumentException {
        getBukkitPlayer().decrementStatistic(statistic);
    }

    @Override
    default void incrementStatistic(@Nonnull Statistic statistic, int i) throws IllegalArgumentException {
        getBukkitPlayer().incrementStatistic(statistic, i);
    }

    @Override
    default void decrementStatistic(@Nonnull Statistic statistic, int i) throws IllegalArgumentException {
        getBukkitPlayer().decrementStatistic(statistic, i);
    }

    @Override
    default void setStatistic(@Nonnull Statistic statistic, int i) throws IllegalArgumentException {
        getBukkitPlayer().setStatistic(statistic, i);
    }

    @Override
    default int getStatistic(@Nonnull Statistic statistic) throws IllegalArgumentException {
        return getBukkitPlayer().getStatistic(statistic);
    }

    @Override
    default void incrementStatistic(@Nonnull Statistic statistic, @Nonnull Material material) throws IllegalArgumentException {
        getBukkitPlayer().incrementStatistic(statistic, material);
    }

    @Override
    default void decrementStatistic(@Nonnull Statistic statistic, @Nonnull Material material) throws IllegalArgumentException {
        getBukkitPlayer().decrementStatistic(statistic, material);
    }

    @Override
    default int getStatistic(@Nonnull Statistic statistic, @Nonnull Material material) throws IllegalArgumentException {
        return getBukkitPlayer().getStatistic(statistic, material);
    }

    @Override
    default void incrementStatistic(@Nonnull Statistic statistic, @Nonnull Material material, int i) throws IllegalArgumentException {
        getBukkitPlayer().incrementStatistic(statistic, material, i);
    }

    @Override
    default void decrementStatistic(@Nonnull Statistic statistic, @Nonnull Material material, int i) throws IllegalArgumentException {
        getBukkitPlayer().decrementStatistic(statistic, material, i);
    }

    @Override
    default void setStatistic(@Nonnull Statistic statistic, @Nonnull Material material, int i) throws IllegalArgumentException {
        getBukkitPlayer().setStatistic(statistic, i);
    }

    @Override
    default void incrementStatistic(@Nonnull Statistic statistic, @Nonnull EntityType entityType) throws IllegalArgumentException {
        getBukkitPlayer().incrementStatistic(statistic, entityType);
    }

    @Override
    default void decrementStatistic(@Nonnull Statistic statistic, @Nonnull EntityType entityType) throws IllegalArgumentException {
        getBukkitPlayer().decrementStatistic(statistic, entityType);
    }

    @Override
    default int getStatistic(@Nonnull Statistic statistic, @Nonnull EntityType entityType) throws IllegalArgumentException {
        return getBukkitPlayer().getStatistic(statistic, entityType);
    }

    @Override
    default void incrementStatistic(@Nonnull Statistic statistic, @Nonnull EntityType entityType, int i) throws IllegalArgumentException {
        getBukkitPlayer().incrementStatistic(statistic, entityType, i);
    }

    @Override
    default void decrementStatistic(@Nonnull Statistic statistic, @Nonnull EntityType entityType, int i) {
        getBukkitPlayer().decrementStatistic(statistic, entityType, i);
    }

    @Override
    default void setStatistic(@Nonnull Statistic statistic, @Nonnull EntityType entityType, int i) {
        getBukkitPlayer().setStatistic(statistic, entityType, i);
    }

    @Nonnull
    @Override
    default Map<String, Object> serialize() {
        return getBukkitPlayer().serialize();
    }

    @Override
    default boolean isConversing() {
        return getBukkitPlayer().isConversing();
    }

    @Override
    default void acceptConversationInput(@Nonnull String conversationInput) {
        getBukkitPlayer().acceptConversationInput(conversationInput);
    }

    @Override
    default boolean beginConversation(@Nonnull org.bukkit.conversations.Conversation conversation) {
        return getBukkitPlayer().beginConversation(conversation);
    }

    @Override
    default void abandonConversation(@Nonnull org.bukkit.conversations.Conversation conversation) {
        getBukkitPlayer().abandonConversation(conversation);
    }

    @Override
    default void abandonConversation(@Nonnull org.bukkit.conversations.Conversation conversation, @Nonnull ConversationAbandonedEvent conversationAbandonedEvent) {
        getBukkitPlayer().abandonConversation(conversation, conversationAbandonedEvent);
    }

    @Override
    @Nonnull
    default String getName() {
        return getBukkitPlayer().getName();
    }

    @Nonnull
    @Override
    default PlayerInventory getInventory() {
        return getBukkitPlayer().getInventory();
    }

    @Override
    @Nonnull
    default Inventory getEnderChest() {
        return getBukkitPlayer().getEnderChest();
    }

    @Nonnull
    @Override
    default MainHand getMainHand() {
        return getBukkitPlayer().getMainHand();
    }

    @Override
    default boolean setWindowProperty(@Nonnull InventoryView.Property property, int i) {
        return getBukkitPlayer().setWindowProperty(property, i);
    }

    @Nonnull
    @Override
    default InventoryView getOpenInventory() {
        return getBukkitPlayer().getOpenInventory();
    }

    @Nullable
    @Override
    default InventoryView openInventory(@Nonnull Inventory inventory) {
        return getBukkitPlayer().openInventory(inventory);
    }

    @Nullable
    @Override
    default InventoryView openWorkbench(@Nullable Location location, boolean b) {
        return getBukkitPlayer().openWorkbench(location, b);
    }

    @Nullable
    @Override
    default InventoryView openEnchanting(@Nullable Location location, boolean b) {
        return getBukkitPlayer().openEnchanting(location, b);
    }

    @Override
    default void openInventory(@Nonnull InventoryView inventoryView) {
        getBukkitPlayer().openInventory(inventoryView);
    }

    @Nullable
    @Override
    default InventoryView openMerchant(@Nonnull Villager villager, boolean b) {
        return getBukkitPlayer().openMerchant(villager, b);
    }

    @Nullable
    @Override
    default InventoryView openMerchant(@Nonnull Merchant merchant, boolean b) {
        return getBukkitPlayer().openMerchant(merchant, b);
    }

    @Override
    default void closeInventory() {
        if (Bukkit.isPrimaryThread()) getBukkitPlayer().closeInventory();
        else Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> getBukkitPlayer().closeInventory());
    }

    @Deprecated
    @Override
    @Nonnull
    default ItemStack getItemInHand() {
        return getBukkitPlayer().getItemInHand();
    }

    @Deprecated
    @Override
    default void setItemInHand(@Nullable ItemStack itemStack) {
        getBukkitPlayer().setItemInHand(itemStack);
    }

    @Override
    @Nonnull
    default ItemStack getItemOnCursor() {
        return getBukkitPlayer().getItemOnCursor();
    }

    @Override
    default void setItemOnCursor(@Nullable ItemStack itemStack) {
        getBukkitPlayer().setItemOnCursor(itemStack);
    }

    @Override
    default boolean hasCooldown(@Nonnull Material material) {
        return getBukkitPlayer().hasCooldown(material);
    }

    @Override
    default int getCooldown(@Nonnull Material material) {
        return getBukkitPlayer().getCooldown(material);
    }

    @Override
    default int getSleepTicks() {
        return getBukkitPlayer().getSleepTicks();
    }

    @Override
    @Nullable
    default Location getBedSpawnLocation() {
        return getBukkitPlayer().getBedSpawnLocation();
    }

    @Override
    default void setBedSpawnLocation(@Nullable Location location) {
        getBukkitPlayer().setBedSpawnLocation(location);
    }

    @Override
    default void setBedSpawnLocation(@Nullable Location location, boolean b) {
        getBukkitPlayer().setBedSpawnLocation(location, b);
    }

    @Override
    default boolean sleep(@Nonnull Location location, boolean b) {
        return getBukkitPlayer().sleep(location, b);
    }

    @Override
    default void wakeup(boolean b) {
        getBukkitPlayer().wakeup(b);
    }

    @Override
    @Nonnull
    default Location getBedLocation() {
        return getBukkitPlayer().getBedLocation();
    }

    @Nonnull
    @Override
    default GameMode getGameMode() {
        return getBukkitPlayer().getGameMode();
    }

    @Override
    default void setGameMode(@Nonnull GameMode gameMode) {
        if (Bukkit.isPrimaryThread()) getBukkitPlayer().setGameMode(gameMode);
        else Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> getBukkitPlayer().setGameMode(gameMode));
    }

    @Override
    default boolean isBlocking() {
        return getBukkitPlayer().isBlocking();
    }

    @Override
    default boolean isHandRaised() {
        return getBukkitPlayer().isHandRaised();
    }

    @Override
    default int getExpToLevel() {
        return getBukkitPlayer().getExpToLevel();
    }

    @Override
    default boolean discoverRecipe(@Nonnull NamespacedKey recipe) {
        return getBukkitPlayer().discoverRecipe(recipe);
    }

    @Override
    default int discoverRecipes(@Nonnull Collection<NamespacedKey> recipes) {
        return getBukkitPlayer().discoverRecipes(recipes);
    }

    @Override
    default boolean undiscoverRecipe(@Nonnull NamespacedKey recipe) {
        return getBukkitPlayer().undiscoverRecipe(recipe);
    }

    @Override
    default int undiscoverRecipes(@Nonnull Collection<NamespacedKey> recipes) {
        return getBukkitPlayer().undiscoverRecipes(recipes);
    }

    @Deprecated
    @Override
    @Nullable
    default Entity getShoulderEntityLeft() {
        return getBukkitPlayer().getShoulderEntityLeft();
    }

    @Deprecated
    @Override
    default void setShoulderEntityLeft(@Nullable Entity entity) {
        getBukkitPlayer().setShoulderEntityLeft(entity);
    }

    @Deprecated
    @Override
    @Nullable
    default Entity getShoulderEntityRight() {
        return getBukkitPlayer().getShoulderEntityRight();
    }

    @Deprecated
    @Override
    default void setShoulderEntityRight(@Nullable Entity entity) {
        getBukkitPlayer().setShoulderEntityRight(entity);
    }

    @Override
    default double getEyeHeight() {
        return getBukkitPlayer().getEyeHeight();
    }

    @Override
    default double getEyeHeight(boolean b) {
        return getBukkitPlayer().getEyeHeight(b);
    }

    @Override
    @Nonnull
    default Location getEyeLocation() {
        return getBukkitPlayer().getEyeLocation();
    }

    @Override
    @Nonnull
    default List<Block> getLineOfSight(@Nullable Set<Material> set, int range) {
        return getBukkitPlayer().getLineOfSight(set, range);
    }

    @Override
    @Nonnull
    default Block getTargetBlock(@Nullable Set<Material> set, int range) {
        return getBukkitPlayer().getTargetBlock(set, range);
    }

    @Override
    @Nonnull
    default List<Block> getLastTwoTargetBlocks(@Nullable Set<Material> set, int range) {
        return getBukkitPlayer().getLastTwoTargetBlocks(set, range);
    }

    @Override
    @Nullable
    default Block getTargetBlockExact(int range) {
        return getBukkitPlayer().getTargetBlockExact(range);
    }

    @Override
    @Nullable
    default Block getTargetBlockExact(int range, @Nonnull FluidCollisionMode fluidCollisionMode) {
        return getBukkitPlayer().getTargetBlockExact(range, fluidCollisionMode);
    }

    @Nullable
    @Override
    default RayTraceResult rayTraceBlocks(double range) {
        return getBukkitPlayer().rayTraceBlocks(range);
    }

    @Nullable
    @Override
    default RayTraceResult rayTraceBlocks(double range, @Nonnull FluidCollisionMode fluidCollisionMode) {
        return getBukkitPlayer().rayTraceBlocks(range, fluidCollisionMode);
    }

    @Override
    default void giveExp(int amount) {
        getBukkitPlayer().giveExp(amount);
    }

    @Override
    default int getRemainingAir() {
        return getBukkitPlayer().getRemainingAir();
    }

    @Override
    default void setRemainingAir(int air) {
        getBukkitPlayer().setRemainingAir(air);
    }

    @Override
    default int getMaximumAir() {
        return getBukkitPlayer().getMaximumAir();
    }

    @Override
    default void setMaximumAir(int air) {
        getBukkitPlayer().setMaximumAir(air);
    }

    @Override
    default int getMaximumNoDamageTicks() {
        return getBukkitPlayer().getMaximumNoDamageTicks();
    }

    @Override
    default void setMaximumNoDamageTicks(int ticks) {
        getBukkitPlayer().setMaximumAir(ticks);
    }

    @Override
    default double getLastDamage() {
        return getBukkitPlayer().getLastDamage();
    }

    @Override
    default void setLastDamage(double damage) {
        getBukkitPlayer().setLastDamage(damage);
    }

    @Override
    default int getNoDamageTicks() {
        return getBukkitPlayer().getNoDamageTicks();
    }

    @Override
    default void setNoDamageTicks(int ticks) {
        getBukkitPlayer().setNoDamageTicks(ticks);
    }

    @Override
    default boolean addPotionEffect(@Nonnull PotionEffect potionEffect) {
        if (Bukkit.isPrimaryThread()) return getBukkitPlayer().addPotionEffect(potionEffect);
        else {
            Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> getBukkitPlayer().addPotionEffect(potionEffect));
        }
        return true;
    }

    @Override
    default boolean addPotionEffect(@Nonnull PotionEffect potionEffect, boolean b) {
        if (Bukkit.isPrimaryThread()) return getBukkitPlayer().addPotionEffect(potionEffect, b);
        else {
            Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> getBukkitPlayer().addPotionEffect(potionEffect, b));
        }
        return true;
    }

    @Override
    default boolean addPotionEffects(@Nonnull Collection<PotionEffect> collection) {
        if (Bukkit.isPrimaryThread()) return getBukkitPlayer().addPotionEffects(collection);
        else {
            Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> getBukkitPlayer().addPotionEffects(collection));
        }
        return true;
    }

    @Override
    default boolean hasPotionEffect(@Nonnull PotionEffectType potionEffectType) {
        return getBukkitPlayer().hasPotionEffect(potionEffectType);
    }

    @Nullable
    @Override
    default PotionEffect getPotionEffect(@Nonnull PotionEffectType potionEffectType) {
        return getBukkitPlayer().getPotionEffect(potionEffectType);
    }

    @Override
    default void removePotionEffect(@Nonnull PotionEffectType potionEffectType) {
        if (Bukkit.isPrimaryThread()) getBukkitPlayer().removePotionEffect(potionEffectType);
        else {
            Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> getBukkitPlayer().removePotionEffect(potionEffectType));
        }
    }

    @Nonnull
    @Override
    default Collection<PotionEffect> getActivePotionEffects() {
        return getBukkitPlayer().getActivePotionEffects();
    }

    @Override
    default boolean hasLineOfSight(@Nonnull Entity entity) {
        return getBukkitPlayer().hasLineOfSight(entity);
    }

    @Override
    default boolean getRemoveWhenFarAway() {
        return getBukkitPlayer().getRemoveWhenFarAway();
    }

    @Override
    default void setRemoveWhenFarAway(boolean remove) {
        getBukkitPlayer().setRemoveWhenFarAway(remove);
    }

    @Nullable
    @Override
    default EntityEquipment getEquipment() {
        return getBukkitPlayer().getEquipment();
    }

    @Override
    default void setCanPickupItems(boolean pickup) {
        getBukkitPlayer().setCanPickupItems(pickup);
    }

    @Override
    default boolean getCanPickupItems() {
        return getBukkitPlayer().getCanPickupItems();
    }

    @Override
    default boolean isLeashed() {
        return getBukkitPlayer().isLeashed();
    }

    @Override
    @Nonnull
    default Entity getLeashHolder() throws IllegalStateException {
        return getBukkitPlayer().getLeashHolder();
    }

    @Override
    default boolean setLeashHolder(@Nullable Entity entity) {
        return getBukkitPlayer().setLeashHolder(entity);
    }

    @Override
    default boolean isGliding() {
        return getBukkitPlayer().isGliding();
    }

    @Override
    default void setGliding(boolean gliding) {
        getBukkitPlayer().setGliding(gliding);
    }

    @Override
    default boolean isSwimming() {
        return getBukkitPlayer().isSwimming();
    }

    @Override
    default void setSwimming(boolean swimming) {
        getBukkitPlayer().setSwimming(swimming);
    }

    @Override
    default boolean isRiptiding() {
        return getBukkitPlayer().isRiptiding();
    }

    @Override
    default boolean isSleeping() {
        return getBukkitPlayer().isSleeping();
    }

    @Override
    default void setAI(boolean ai) {
        getBukkitPlayer().setAI(ai);
    }

    @Override
    default boolean hasAI() {
        return getBukkitPlayer().hasAI();
    }

    @Override
    default void setCollidable(boolean collidable) {
        getBukkitPlayer().setCollidable(collidable);
    }

    @Override
    default boolean isCollidable() {
        return getBukkitPlayer().isCollidable();
    }

    @Nullable
    @Override
    default <T> T getMemory(@Nonnull MemoryKey<T> memoryKey) {
        return getBukkitPlayer().getMemory(memoryKey);
    }

    @Override
    default <T> void setMemory(@Nonnull MemoryKey<T> memoryKey, @Nullable T memory) {
        getBukkitPlayer().setMemory(memoryKey, memory);
    }

    @Nullable
    @Override
    default AttributeInstance getAttribute(@Nonnull Attribute attribute) {
        return getBukkitPlayer().getAttribute(attribute);
    }

    @Override
    default void damage(double damage) {
        getBukkitPlayer().damage(damage);
    }

    @Override
    default void damage(double damage, @Nullable Entity entity) {
        getBukkitPlayer().damage(damage, entity);
    }

    @Override
    default double getHealth() {
        return getBukkitPlayer().getHealth();
    }

    @Override
    default void setHealth(double health) {
        getBukkitPlayer().setHealth(health);
    }

    @Override
    default double getAbsorptionAmount() {
        return getBukkitPlayer().getAbsorptionAmount();
    }

    @Override
    default void setAbsorptionAmount(double amount) {
        getBukkitPlayer().setAbsorptionAmount(amount);
    }

    @Override
    default double getMaxHealth() {
        AttributeInstance attribute = getAttribute(Attribute.GENERIC_MAX_HEALTH);
        return attribute != null ? attribute.getValue() : 20;
    }

    @Override
    default void setMaxHealth(double health) {
        AttributeInstance attribute = getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute != null) attribute.setBaseValue(health);
    }

    @Override
    default void resetMaxHealth() {
        AttributeInstance attribute = getAttribute(Attribute.GENERIC_MAX_HEALTH);
        setMaxHealth(attribute != null ? attribute.getDefaultValue() : 20);
    }

    @Override
    @Nonnull
    default Location getLocation() {
        return getBukkitPlayer().getLocation();
    }

    @Override
    @Nullable
    default Location getLocation(@Nullable Location location) {
        return getBukkitPlayer().getLocation(location);
    }

    @Nonnull
    default Location getTargetLocation(double range) {
        return getLocation().clone().add(getLocation().getDirection().multiply(range));
    }

    @Override
    default void setVelocity(@Nonnull Vector vector) {
        getBukkitPlayer().setVelocity(vector);
    }

    @Nonnull
    @Override
    default Vector getVelocity() {
        return getBukkitPlayer().getVelocity();
    }

    @Override
    default double getHeight() {
        return getBukkitPlayer().getHeight();
    }

    @Override
    default double getWidth() {
        return getBukkitPlayer().getWidth();
    }

    @Nonnull
    @Override
    default BoundingBox getBoundingBox() {
        return getBukkitPlayer().getBoundingBox();
    }

    @Override
    default boolean isOnGround() {
        return getBukkitPlayer().isOnGround();
    }

    @Nonnull
    @Override
    default World getWorld() {
        return getBukkitPlayer().getWorld();
    }

    @Override
    default void setRotation(float yaw, float pitch) {
        getBukkitPlayer().setRotation(yaw, pitch);
    }

    @Override
    default boolean teleport(@Nonnull Location location) {
        if (Bukkit.isPrimaryThread()) return getBukkitPlayer().teleport(location);
        else Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> getBukkitPlayer().teleport(location));
        return true;
    }

    @Override
    default boolean teleport(@Nonnull Location location, @Nonnull PlayerTeleportEvent.TeleportCause teleportCause) {
        if (Bukkit.isPrimaryThread()) return getBukkitPlayer().teleport(location, teleportCause);
        else {
            Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> getBukkitPlayer().teleport(location, teleportCause));
        }
        return true;
    }

    @Override
    default boolean teleport(@Nonnull Entity entity) {
        if (Bukkit.isPrimaryThread()) return getBukkitPlayer().teleport(entity);
        else Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> getBukkitPlayer().teleport(entity));
        return true;
    }

    @Override
    default boolean teleport(@Nonnull Entity entity, @Nonnull PlayerTeleportEvent.TeleportCause teleportCause) {
        if (Bukkit.isPrimaryThread()) return getBukkitPlayer().teleport(entity, teleportCause);
        else {
            Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> getBukkitPlayer().teleport(entity, teleportCause));
        }
        return true;
    }

    @Override
    @Nonnull
    default List<Entity> getNearbyEntities(double x, double y, double z) {
        return getBukkitPlayer().getNearbyEntities(x, y, z);
    }

    @Override
    default int getEntityId() {
        return getBukkitPlayer().getEntityId();
    }

    @Override
    default int getFireTicks() {
        return getBukkitPlayer().getFireTicks();
    }

    @Override
    default int getMaxFireTicks() {
        return getBukkitPlayer().getMaxFireTicks();
    }

    @Override
    default void setFireTicks(int ticks) {
        getBukkitPlayer().setFireTicks(ticks);
    }

    @Override
    default void remove() {
        getBukkitPlayer().remove();
    }

    @Override
    default boolean isDead() {
        return getBukkitPlayer().isDead();
    }

    @Override
    default boolean isValid() {
        return getBukkitPlayer().isValid();
    }

    @Nonnull
    @Override
    default Server getServer() {
        return getBukkitPlayer().getServer();
    }

    @Deprecated
    @Override
    default boolean isPersistent() {
        return getBukkitPlayer().isPersistent();
    }

    @Deprecated
    @Override
    default void setPersistent(boolean persistent) {
        getBukkitPlayer().setPersistent(persistent);
    }

    @Deprecated
    @Override
    @Nullable
    default Entity getPassenger() {
        return getBukkitPlayer().getPassenger();
    }

    @Deprecated
    @Override
    default boolean setPassenger(@Nonnull Entity entity) {
        return getBukkitPlayer().setPassenger(entity);
    }

    @Override
    @Nonnull
    default List<Entity> getPassengers() {
        return getBukkitPlayer().getPassengers();
    }

    @Override
    default boolean addPassenger(@Nonnull Entity entity) {
        return getBukkitPlayer().addPassenger(entity);
    }

    @Override
    default boolean removePassenger(@Nonnull Entity entity) {
        return getBukkitPlayer().removePassenger(entity);
    }

    @Override
    default boolean isEmpty() {
        return getBukkitPlayer().isEmpty();
    }

    @Override
    default boolean eject() {
        return getBukkitPlayer().eject();
    }

    @Override
    default float getFallDistance() {
        return getBukkitPlayer().getFallDistance();
    }

    @Override
    default void setFallDistance(float fallDistance) {
        getBukkitPlayer().setFallDistance(fallDistance);
    }

    @Override
    default void setLastDamageCause(@Nullable EntityDamageEvent entityDamageEvent) {
        getBukkitPlayer().setLastDamageCause(entityDamageEvent);
    }

    @Nullable
    @Override
    default EntityDamageEvent getLastDamageCause() {
        return getBukkitPlayer().getLastDamageCause();
    }

    @Override
    @Nonnull
    default UUID getUniqueId() {
        return getBukkitPlayer().getUniqueId();
    }

    @Override
    default int getTicksLived() {
        return getBukkitPlayer().getTicksLived();
    }

    @Override
    default void setTicksLived(int ticks) {
        getBukkitPlayer().setTicksLived(ticks);
    }

    @Override
    default void playEffect(@Nonnull EntityEffect entityEffect) {
        getBukkitPlayer().playEffect(entityEffect);
    }

    @Nonnull
    @Override
    default EntityType getType() {
        return getBukkitPlayer().getType();
    }

    @Override
    default boolean isInsideVehicle() {
        return getBukkitPlayer().isInsideVehicle();
    }

    @Override
    default boolean leaveVehicle() {
        return getBukkitPlayer().leaveVehicle();
    }

    @Override
    @Nullable
    default Entity getVehicle() {
        return getBukkitPlayer().getVehicle();
    }

    @Override
    default void setCustomNameVisible(boolean visible) {
        getBukkitPlayer().setCustomNameVisible(visible);
    }

    @Override
    default boolean isCustomNameVisible() {
        return getBukkitPlayer().isCustomNameVisible();
    }

    @Override
    default void setGlowing(boolean glowing) {
        getBukkitPlayer().setGlowing(glowing);
    }

    @Override
    default boolean isGlowing() {
        return getBukkitPlayer().isGlowing();
    }

    @Override
    default void setInvulnerable(boolean invulnerable) {
        getBukkitPlayer().setInvulnerable(invulnerable);
    }

    @Override
    default boolean isInvulnerable() {
        return getBukkitPlayer().isInvulnerable();
    }

    @Override
    default boolean isSilent() {
        return getBukkitPlayer().isSilent();
    }

    @Override
    default void setSilent(boolean silent) {
        getBukkitPlayer().setSilent(silent);
    }

    @Override
    default boolean hasGravity() {
        return getBukkitPlayer().hasGravity();
    }

    @Override
    default void setGravity(boolean gravity) {
        getBukkitPlayer().setGravity(gravity);
    }

    @Override
    default int getPortalCooldown() {
        return getBukkitPlayer().getPortalCooldown();
    }

    @Override
    default void setPortalCooldown(int cooldown) {
        getBukkitPlayer().setPortalCooldown(cooldown);
    }

    @Nonnull
    @Override
    default Set<String> getScoreboardTags() {
        return getBukkitPlayer().getScoreboardTags();
    }

    @Override
    default boolean addScoreboardTag(@Nonnull String tag) {
        return getBukkitPlayer().addScoreboardTag(tag);
    }

    @Override
    default boolean removeScoreboardTag(@Nonnull String tag) {
        return getBukkitPlayer().removeScoreboardTag(tag);
    }

    @Nonnull
    @Override
    default PistonMoveReaction getPistonMoveReaction() {
        return getBukkitPlayer().getPistonMoveReaction();
    }

    @Override
    @Nonnull
    default BlockFace getFacing() {
        return getBukkitPlayer().getFacing();
    }

    @Nonnull
    @Override
    default Pose getPose() {
        return getBukkitPlayer().getPose();
    }

    @Override
    @Nullable
    default String getCustomName() {
        return getBukkitPlayer().getCustomName();
    }

    @Override
    default void setCustomName(@Nullable String customName) {
        getBukkitPlayer().setCustomName(customName);
    }

    @Override
    default void setMetadata(@Nonnull String metadata, @Nonnull MetadataValue metadataValue) {
        getBukkitPlayer().setMetadata(metadata, metadataValue);
    }

    @Override
    @Nonnull
    default List<MetadataValue> getMetadata(@Nonnull String metadata) {
        return getBukkitPlayer().getMetadata(metadata);
    }

    @Override
    default boolean hasMetadata(@Nonnull String metadata) {
        return getBukkitPlayer().hasMetadata(metadata);
    }

    @Override
    default void removeMetadata(@Nonnull String metadata, @Nonnull Plugin plugin) {
        getBukkitPlayer().removeMetadata(metadata, plugin);
    }

    @Deprecated
    @Override
    default boolean isPermissionSet(@Nonnull String permission) {
        return getBukkitPlayer().isPermissionSet(permission);
    }

    @Deprecated
    @Override
    default boolean isPermissionSet(@Nonnull Permission permission) {
        return getBukkitPlayer().isPermissionSet(permission);
    }

    @Deprecated
    @Override
    default boolean hasPermission(@Nonnull String permission) {
        return getBukkitPlayer().hasPermission(permission);
    }

    @Deprecated
    @Override
    default boolean hasPermission(@Nonnull Permission permission) {
        return getBukkitPlayer().hasPermission(permission);
    }

    @Deprecated
    @Nonnull
    @Override
    default PermissionAttachment addAttachment(@Nonnull Plugin plugin, @Nonnull String s, boolean b) {
        return getBukkitPlayer().addAttachment(plugin, s, b);
    }

    @Deprecated
    @Nonnull
    @Override
    default PermissionAttachment addAttachment(@Nonnull Plugin plugin) {
        return getBukkitPlayer().addAttachment(plugin);
    }

    @Deprecated
    @Nullable
    @Override
    default PermissionAttachment addAttachment(@Nonnull Plugin plugin, @Nonnull String s, boolean b, int i) {
        return getBukkitPlayer().addAttachment(plugin, s, b, i);
    }

    @Deprecated
    @Nullable
    @Override
    default PermissionAttachment addAttachment(@Nonnull Plugin plugin, int i) {
        return getBukkitPlayer().addAttachment(plugin, i);
    }

    @Deprecated
    @Override
    default void removeAttachment(@Nonnull PermissionAttachment permissionAttachment) {
        getBukkitPlayer().removeAttachment(permissionAttachment);
    }

    @Deprecated
    @Override
    default void recalculatePermissions() {
        getBukkitPlayer().recalculatePermissions();
    }

    @Deprecated
    @Nonnull
    @Override
    default Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return getBukkitPlayer().getEffectivePermissions();
    }

    @Deprecated
    @Override
    default boolean isOp() {
        return getBukkitPlayer().isOp();
    }

    @Deprecated
    @Override
    default void setOp(boolean op) {
        getBukkitPlayer().setOp(op);
    }

    @Nonnull
    @Override
    default PersistentDataContainer getPersistentDataContainer() {
        return getBukkitPlayer().getPersistentDataContainer();
    }

    @Override
    default void sendPluginMessage(@Nonnull Plugin plugin, @Nonnull String channel, @Nonnull byte[] bytes) {
        getBukkitPlayer().sendPluginMessage(plugin, channel, bytes);
    }

    @Nonnull
    @Override
    default Set<String> getListeningPluginChannels() {
        return getBukkitPlayer().getListeningPluginChannels();
    }

    @Nonnull
    @Override
    default <T extends Projectile> T launchProjectile(@Nonnull Class<? extends T> projectile) {
        return getBukkitPlayer().launchProjectile(projectile);
    }

    @Nonnull
    @Override
    default <T extends Projectile> T launchProjectile(@Nonnull Class<? extends T> projectile, @Nullable Vector vector) {
        return getBukkitPlayer().launchProjectile(projectile, vector);
    }

    @Nonnull
    Player getBukkitPlayer();

    @Override
    default int getId() {
        return getBukkitPlayer().getEntityId();
    }

    @Nullable
    default TNLPlayer getKiller() {
        return getBukkitPlayer().getKiller() == null ? null : cast(getBukkitPlayer().getKiller());
    }

    @Nonnull
    default Sidebar getSidebar() {
        if (!getVirtualStorage().compareInstance("sidebar", Sidebar.class)) {
            getVirtualStorage().put("sidebar", new Sidebar(this));
        }
        return getVirtualStorage().get("sidebar", Sidebar.class).nonnull();
    }

    @Nonnull
    default Team getTeam() {
        if (!getVirtualStorage().compareInstance("team", Team.class)) getVirtualStorage().put("team", Team.NONE);
        return getVirtualStorage().get("team", Team.class).nonnull();
    }

    default void setTeam(@Nonnull Team team) {
        getVirtualStorage().put("team", team);
        updateTeam();
    }

    default void updateTeam() {
        getTeam().getTeam().addEntry(getName());
        if (Settings.CUSTOM_TEAMS.getValue()) {
            setDisplayName(getTeam().getPrefix() + getTeam().getColor() + getName() + getTeam().getSuffix());
        }
        for (TNLPlayer all : TNLListener.getInstance().getOnlinePlayers()) {
            for (org.bukkit.scoreboard.Team team : Team.getScoreboard().getTeams()) {
                org.bukkit.scoreboard.Team allTeam = all.getScoreboard().getTeam(team.getName());
                if (allTeam == null) allTeam = all.getScoreboard().registerNewTeam(team.getName());
                if (Settings.CUSTOM_TEAMS.getValue()) {
                    allTeam.setDisplayName(team.getDisplayName());
                    allTeam.setPrefix(team.getPrefix());
                    allTeam.setSuffix(team.getSuffix());
                    allTeam.setColor(team.getColor());
                }
                for (String entry : team.getEntries()) if (!allTeam.hasEntry(entry)) allTeam.addEntry(entry);
                for (org.bukkit.scoreboard.Team.Option option : org.bukkit.scoreboard.Team.Option.values()) {
                    allTeam.setOption(option, team.getOption(option));
                }
                allTeam.setCanSeeFriendlyInvisibles(team.canSeeFriendlyInvisibles());
                allTeam.setAllowFriendlyFire(team.allowFriendlyFire());
            }
        }
    }

    @Nonnull
    default Permissions getPermissionManager() {
        if (!(getVirtualStorage().compareInstance("permissions", Permissions.class))) {
            getVirtualStorage().put("permissions", new PermissionManager(this));
        }
        return getVirtualStorage().get("permissions", Permissions.class).nonnull();
    }

    @Nonnull
    default String getWorldAlias() {
        return getWorldAlias(getWorld());
    }

    @Nonnull
    default String getWorldAlias(@Nonnull World world) {
        if (!getVirtualStorage().containsKey("alias-" + world.getName())) return world.getName();
        return getVirtualStorage().get("alias-" + world.getName(), String.class).nonnull();
    }

    default void setWorldAlias(@Nonnull String alias) {
        setWorldAlias(getWorld(), alias);
    }

    default void setWorldAlias(@Nonnull World world, @Nonnull String alias) {
        getVirtualStorage().put("alias-" + world.getName(), alias);
    }

    void openVirtualSignEditor(@Nonnull SignMenu signMenu);

    default void openSignEditor(@Nonnull Location location) {
        sendPacket(TNLOpenSign.create(location));
    }

    @Nonnull
    <NetworkManager> NetworkManager getNetworkManager();

    @Nonnull
    <PlayerConnection> PlayerConnection getPlayerConnection();

    @Nonnull
    <WorldServer> WorldServer getWorldServer();

    @Nonnull
    <CraftPlayer> CraftPlayer getCraftPlayer();

    @Nonnull
    <EntityPlayer> EntityPlayer getEntityPlayer();

    @Nonnull
    default Language getLanguage() {
        return Language.fromLocale(getLocale());
    }

    @Nonnull
    default Scoreboard getScoreboard() {
        if (Bukkit.getScoreboardManager() == null || getBukkitPlayer().getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            getBukkitPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
        return getBukkitPlayer().getScoreboard();
    }

    default void sendBlockChange(@Nonnull Location location, @Nonnull BlockFace blockFace) {
        Block relative = location.getBlock().getRelative(blockFace);
        sendBlockChange(relative.getLocation());
    }

    default void sendBlockChange(@Nonnull Location location) {
        sendBlockChange(location, location.getBlock().getBlockData());
    }

    @Nonnull
    @Override
    default GameProfile getGameProfile() {
        if (!getVirtualStorage().compareInstance("GameProfile", GameProfile.class)) {
            getVirtualStorage().put("GameProfile", new GameProfile(getUniqueId(), getName()));
        }
        return getVirtualStorage().get("GameProfile", GameProfile.class).nonnull();
    }

    @Override
    int getPing();

    @Override
    void setPing(int ping);

    void sendPacket(@Nonnull Object packet);

    default void sendPackets(@Nonnull Object... packets) {
        sendPackets(Arrays.asList(packets));
    }

    default void sendPackets(@Nonnull List<Object> packets) {
        for (Object packet : packets) sendPacket(packet);
    }

    @Deprecated
    @Override
    default void sendMessage(@Nonnull String[] messages) {
        getBukkitPlayer().sendMessage(messages);
    }

    @Override
    default void sendMessage(@Nonnull String message) {
        sendMessage(message, true);
    }

    default void sendMessage(@Nonnull String message, boolean validate) {
        if (validate) message = ChatComponent.getText(message);
        getBukkitPlayer().sendMessage(message);
    }

    default void sendMessage(@Nonnull ChatComponent component) {
        sendMessage(component.getText(), false);
    }

    default void sendMessage(@Nonnull ChatComponent component, @Nonnull Placeholder... placeholders) {
        sendMessage(component.getText(placeholders), false);
    }

    default void sendMessage(@Nonnull String message, @Nonnull Placeholder... placeholders) {
        sendMessage(ChatComponent.getText(message, placeholders), false);
    }

    default void sendMessage(@Nonnull MessageKey messageKey, @Nonnull Placeholder... placeholders) {
        Language language = Language.ROOT;
        if (!messageKey.isSystemMessage()) language = getLanguage();
        LanguageKey languageKey = new LanguageKey(language, messageKey);
        ChatComponent component = Message.valueOf(languageKey);
        if (component != null) sendMessage(component.getText(placeholders), false);
        else {
            Logger.error.println("§cUnknown component§8: §4" + languageKey.getMessageKey().getKey() + " §8(§4" + languageKey.getLanguage().getName() + "§8)");
            sendMessage("%prefix% §cUnknown component§8: §4" + languageKey.getMessageKey() + " §8(§4" + languageKey.getLanguage().getName() + "§8)");
        }
    }

    @Override
    default void kickPlayer(@Nullable String kickMessage) {
        disconnect(kickMessage == null ? "" : kickMessage);
    }

    default void disconnect() {
        disconnect(MessageKey.KICKED, "\n§cDisconnected");
    }

    default void disconnect(@Nonnull String kickMessage) {
        disconnect(kickMessage, true);
    }

    default void disconnect(@Nonnull String kickMessage, boolean validate) {
        String message = (validate ? ChatComponent.getText(kickMessage) : kickMessage);
        if (Bukkit.isPrimaryThread()) getBukkitPlayer().kickPlayer(message);
        else Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> getBukkitPlayer().kickPlayer(message));
    }

    default void disconnect(@Nonnull MessageKey messageKey, @Nonnull Placeholder... placeholders) {
        disconnect(messageKey, "", placeholders);
    }

    default void disconnect(@Nonnull MessageKey messageKey, @Nonnull String append, @Nonnull Placeholder... placeholders) {
        Language language = Language.ROOT;
        if (!messageKey.isSystemMessage()) language = getLanguage();
        LanguageKey languageKey = new LanguageKey(language, messageKey);
        ChatComponent component = Message.valueOf(languageKey);
        if (component != null) disconnect(component.getText(placeholders) + append, false);
        else {
            Logger.error.println("§cUnknown component§8: §4" + languageKey);
            disconnect("§cUnknown component§8: §4" + languageKey, false);
        }
    }

    @Nullable
    default Conversation getConversation() {
        if (getVirtualStorage().compareInstance("current-conversation", Conversation.class)) {
            return getVirtualStorage().get("current-conversation", Conversation.class).nonnull();
        }
        return null;
    }

    default void startConversation(@Nonnull Conversation conversation) {
        getVirtualStorage().put("current-conversation", conversation);
    }

    default void stopConversation() {
        getVirtualStorage().remove("current-conversation");
    }

    default boolean isInConversation() {
        return getVirtualStorage().containsKey("current-conversation");
    }

    default void sendDemoScreen(@Nonnull DemoEvent demoEvent) {
        sendPacket(TNLGameStateChange.create(5, demoEvent.getId()));
    }

    enum DemoEvent {
        WELCOME_SCREEN(0),
        MOVEMENT_CONTROLS(101),
        JUMP_CONTROL(102),
        INVENTORY_CONTROL(103),
        DEMO_OVER(104);

        private final int id;

        DemoEvent(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    default void setInstantRespawn(boolean instant) {
        sendPacket(TNLGameStateChange.create(11, instant ? 1 : 0));
    }

    default void playMobAppearance() {
        sendPacket(TNLGameStateChange.create(10, 0));
    }

    default void playPufferfishSting() {
        sendPacket(TNLGameStateChange.create(9, 0));
    }

    void exitCombat();

    void enterCombat();

    default void saveInventoryContents(@Nonnull String id) {
        saveInventoryContents(id, getInventory().getContents());
    }

    default void saveInventoryContents(@Nonnull String id, @Nonnull ItemStack[] contents) {
        try {
            if (new InventorySaveEvent(this, id).call()) {
                File file = new File(new File("plugins/Listener/Inventories/"), getBukkitPlayer().getUniqueId() + ".yml");
                FileCreator.create(file);
                YamlConfiguration inventory = YamlConfiguration.loadConfiguration(file);
                inventory.set(id, Arrays.asList(contents));
                inventory.save(file);
            }
        } catch (IOException e) {
            Logger.error.println(e);
        }
    }

    default void loadInventoryContents(@Nonnull String id) {
        ItemStack[] contents = getInventoryContents(id);
        if (contents.length > 0 && new InventoryLoadEvent(this, id).call()) {
            getInventory().clear();
            getInventory().setContents(contents);
        }
    }

    @Nonnull
    default ItemStack[] getInventoryContents(@Nonnull String id) {
        ItemStack[] items = new ItemStack[]{};
        File file = new File(new File("plugins/Listener/Inventories/"), getBukkitPlayer().getUniqueId() + ".yml");
        try {
            if (file.exists()) {
                YamlConfiguration inventory = YamlConfiguration.loadConfiguration(file);
                if (inventory.isSet(id)) {
                    List<?> contents = inventory.getList(id);
                    if (contents != null) {
                        items = new ItemStack[contents.size()];
                        for (int i = 0; i < contents.size(); i++) items[i] = (ItemStack) contents.get(i);
                    }
                }
            }
        } catch (Exception e) {
            Logger.error.println(e);
        }
        return items;
    }

    default void bungeeConnect(@Nonnull net.nonswag.tnl.listener.api.server.Server server) {
        try {
            if (server.getStatus().isOnline()) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                dataOutputStream.writeUTF("Connect");
                dataOutputStream.writeUTF(server.getName());
                sendPluginMessage(Bootstrap.getInstance(), "BungeeCord", byteArrayOutputStream.toByteArray());
                sendMessage("%prefix% §aConnecting you to server §6" + server.getName());
            } else sendMessage("%prefix% §cThe server §4" + server.getName() + "§c is Offline");
        } catch (Exception e) {
            Logger.error.println(e);
            sendMessage("%prefix% §cFailed to connect you to server §4" + server.getName());
        }
    }

    @Nullable
    default GUI getGUI() {
        if (getVirtualStorage().compareInstance("current-gui", GUI.class)) {
            return getVirtualStorage().get("current-gui", GUI.class).nonnull();
        }
        return null;
    }

    default void closeGUI() {
        if (getGUI() != null) {
            getVirtualStorage().remove("current-gui");
            sendPacket(TNLCloseWindow.create());
        }
    }

    @Nullable
    default SignMenu getSignMenu() {
        if (getVirtualStorage().compareInstance("current-sign", SignMenu.class)) {
            return getVirtualStorage().get("current-sign", SignMenu.class).nonnull();
        }
        return null;
    }

    default void closeSignMenu() {
        if (getSignMenu() != null) {
            getVirtualStorage().remove("current-sign");
            sendPacket(TNLCloseWindow.create());
        }
    }

    default void setCooldown(@Nonnull GUIItem item, int cooldown) {
        if (item.getBuilder().hasValue()) setCooldown(item.getBuilder().nonnull(), cooldown);
    }

    default void setCooldown(@Nonnull TNLItem item, int cooldown) {
        setCooldown(item.getItemStack(), cooldown);
    }

    default void setCooldown(@Nonnull ItemStack itemStack, int cooldown) {
        setCooldown(itemStack.getType(), cooldown);
    }

    @Override
    default void setCooldown(@Nonnull Material material, int cooldown) {
        if (Bukkit.isPrimaryThread()) getBukkitPlayer().setCooldown(material, cooldown);
        else Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> getBukkitPlayer().setCooldown(material, cooldown));
    }

    default void openGUI(@Nonnull GUI gui) {
        if (!gui.getViewers().contains(this)) gui.getViewers().add(this);
        getVirtualStorage().put("current-gui", gui);
        sendPacket(TNLOpenWindow.create(gui.getSize() / 9, gui.getTitle()));
        if(gui.isPlaySounds()) playSound(getLocation(), Sound.BLOCK_CHEST_OPEN, 0.6f, 1);
        updateGUI();
    }

    default void updateGUI() {
        GUI gui = getGUI();
        if (gui != null) sendPacket(TNLWindowItems.create(gui.items()));
    }

    void hideTabListName(@Nonnull TNLPlayer[] players);

    default void disguise(@Nonnull Generic<?> entity, @Nonnull List<TNLPlayer> receivers) {
        for (TNLPlayer receiver : receivers) disguise(entity, receiver);
    }

    default void disguise(@Nonnull Generic<?> entity, @Nonnull TNLPlayer receiver) {
        disguise(entity, TNLListener.getInstance().getOnlinePlayers());
    }

    default void disguise(@Nonnull Generic<?> entity) {
        disguise(entity, TNLListener.getInstance().getOnlinePlayers());
    }

    void sendBossBar(@Nonnull TNLBossBar<?> TNLBossBar);

    void updateBossBar(@Nonnull TNLBossBar<?> TNLBossBar);

    void hideBossBar(@Nonnull TNLBossBar<?> TNLBossBar);

    void sendBossBar(@Nonnull TNLBossBar<?> TNLBossBar, long millis);

    default void sendTitle(@Nonnull Title title) {
        sendTitle(title.getTitle(), title.getSubtitle(), title.getTimeIn(), title.getTimeStay(), title.getTimeOut());
    }

    default void sendTitle(@Nonnull Title.Animation animation) {
        new Thread(() -> {
            try {
                String[] split = animation.getTitle().getTitle().split("");
                String spaces = "          ";
                do {
                    spaces = spaces.replaceFirst(" ", "");
                    sendTitle((animation.getDesign().getSecondaryColor() +
                                    "- " +
                                    animation.getDesign().getPrimaryColor() +
                                    String.join(spaces, split) +
                                    animation.getDesign().getSecondaryColor() +
                                    " -"),
                            animation.getDesign().getExtraColor() + animation.getTitle().getSubtitle(),
                            animation.getTitle().getTimeIn(),
                            animation.getTitle().getTimeStay(),
                            animation.getTitle().getTimeOut());
                    Thread.sleep(50);
                } while (!spaces.isEmpty());
            } catch (Exception e) {
                Logger.error.println(e);
            }
        }).start();
    }

    void sendActionbar(@Nonnull String actionbar);

    void setArrowCount(int arrows);

    default void hidePlayer(@Nonnull TNLPlayer player, @Nonnull Plugin plugin) {
        player.getBukkitPlayer().hidePlayer(plugin, player.getBukkitPlayer());
    }

    default void hidePlayer(@Nonnull TNLPlayer player) {
        sendPacket(TNLEntityDestroy.create(player));
    }

    default void showPlayer(@Nonnull TNLPlayer player, @Nonnull Plugin plugin) {
        player.getBukkitPlayer().showPlayer(plugin, player.getBukkitPlayer());
    }

    default void showPlayer(@Nonnull TNLPlayer player) {
        sendPacket(TNLPlayerInfo.create(player, TNLPlayerInfo.Action.ADD_PLAYER));
    }

    default void setGlowing(boolean glowing, @Nonnull TNLPlayer... players) {
        for (TNLPlayer player : players) setGlowing(glowing, player);
    }

    void setGlowing(boolean glowing, @Nonnull TNLPlayer player);

    void setGlowing(@Nonnull Entity entity, boolean glowing);

    void uninject();

    void inject();

    @Nullable
    static TNLPlayer cast(@Nonnull String name) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) return cast(player);
        return null;
    }

    @Nullable
    static TNLPlayer cast(@Nonnull UUID uniqueId) {
        Player player = Bukkit.getPlayer(uniqueId);
        if (player != null) return cast(player);
        return null;
    }

    @Nonnull
    static TNLPlayer cast(@Nonnull HumanEntity humanEntity) {
        return cast(((Player) humanEntity));
    }

    @Nonnull
    static TNLPlayer cast(@Nonnull Player player) {
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4) || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5)) {
            return net.nonswag.tnl.listener.api.player.v1_16.R3.NMSPlayer.cast(player);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            return net.nonswag.tnl.listener.api.player.v1_15.R1.NMSPlayer.cast(player);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_10)) {
            return net.nonswag.tnl.listener.api.player.v1_7.R4.NMSPlayer.cast(player);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            return net.nonswag.tnl.listener.api.player.v1_7.R1.NMSPlayer.cast(player);
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }

    @Override
    String toString();
}
