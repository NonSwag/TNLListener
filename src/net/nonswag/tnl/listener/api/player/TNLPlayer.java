package net.nonswag.tnl.listener.api.player;

import net.nonswag.tnl.listener.api.bossbar.BossBar;
import net.nonswag.tnl.listener.api.message.ChatComponent;
import net.nonswag.tnl.listener.api.message.Language;
import net.nonswag.tnl.listener.api.message.MessageKey;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.object.Generic;
import net.nonswag.tnl.listener.api.permission.Permissions;
import net.nonswag.tnl.listener.api.scoreboard.Sidebar;
import net.nonswag.tnl.listener.api.sign.SignMenu;
import net.nonswag.tnl.listener.api.title.Title;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * TNLPlayer is a more net.minecraft.server based player
 **/

public interface TNLPlayer {

    @Nullable
    Entity getVehicle();

    @Nonnull
    Set<String> getScoreboardTags();

    @Nonnull
    PistonMoveReaction getPistonMoveReaction();

    @Nonnull
    BlockFace getFacing();

    @Nonnull
    Location getEyeLocation();

    @Nonnull
    List<org.bukkit.block.Block> getLineOfSight(@Nonnull Set<Material> set, int i);

    @Nonnull
    WeatherType getPlayerWeather();

    @Nonnull
    org.bukkit.block.Block getTargetBlock(@Nonnull Set<Material> set, int i);

    @Nonnull
    List<org.bukkit.block.Block> getLastTwoTargetBlocks(@Nonnull Set<Material> set, int i);

    @Nullable
    org.bukkit.block.Block getTargetBlockExact(int i);

    @Nonnull
    Sidebar<?, ?> getSidebar();

    @Nullable
    TNLPlayer getKiller();

    @Nullable
    PotionEffect getPotionEffect(@Nonnull PotionEffectType potionEffectType);

    @Nonnull
    Collection<PotionEffect> getActivePotionEffects();

    @Nullable
    EntityEquipment getEquipment();

    @Nullable
    Entity getLeashHolder() throws IllegalStateException;

    @Nullable
    String getCustomName();

    @Nonnull
    List<MetadataValue> getMetadata(@Nonnull String s);

    @Nonnull
    Set<String> getListeningPluginChannels();

    @Nonnull
    Player getBukkitPlayer();

    @Nonnull
    Permissions getPermissionManager();

    @Nonnull
    String getName();

    @Nonnull
    PlayerInventory getInventory();

    @Nonnull
    String getWorldAlias();

    @Nullable
    InventoryView getOpenInventory();

    @Nullable
    InventoryView openInventory(@Nonnull Inventory inventory);

    @Nullable
    InventoryView openWorkbench(@Nonnull Location location, boolean b);

    @Nullable
    InventoryView openEnchanting(@Nonnull Location location, boolean b);

    void openVirtualSignEditor(@Nonnull SignMenu signMenu);

    void openSignEditor(@Nonnull Location location);

    @Nullable
    Entity getSpectatorTarget();

    @Nonnull
    Inventory getEnderChest();

    @Nonnull
    <NetworkManager> NetworkManager getNetworkManager();

    @Nonnull
    <PlayerConnection> PlayerConnection getPlayerConnection();

    @Nonnull
    <ScoreboardTeam> ScoreboardTeam getOptionTeam();

    @Nonnull
    <Scoreboard> Scoreboard getOptionScoreboard();

    @Nonnull
    HashMap<String, Object> getVirtualStorage();

    @Nonnull
    <T> void playEffect(@Nonnull Location location, @Nonnull Effect effect, @Nonnull T t);

    @Nonnull
    Language getLanguage();

    @Nonnull
    String getLocale();

    @Nonnull
    Location getLocation();

    @Nonnull
    Location getLocation(@Nonnull Location location);

    @Nonnull
    Location getTargetLocation(double i);

    @Nonnull
    Vector getVelocity();

    @Nonnull
    World getWorld();

    @Nonnull
    <WorldServer> WorldServer getWorldServer();

    @Nonnull
    List<Entity> getNearbyEntities(double v, double v1, double v2);

    @Nonnull
    List<Entity> getPassengers();

    @Nullable
    EntityDamageEvent getLastDamageCause();

    @Nonnull
    UUID getUniqueId();

    @Nonnull
    String getDisplayName();

    @Nonnull
    String getPlayerListName();

    @Nonnull
    <CraftPlayer> CraftPlayer getCraftPlayer();

    @Nonnull
    <EntityPlayer> EntityPlayer getEntityPlayer();

    @Nonnull
    Location getBedLocation();

    @Nonnull
    GameMode getGameMode();

    void setGameMode(@Nonnull GameMode gamemode);

    @Nullable
    Location getBedSpawnLocation();

    @Nullable
    String getPlayerListHeader();

    @Nullable
    String getPlayerListFooter();

    @Nonnull
    Location getCompassTarget();

    @Nullable
    InetSocketAddress getAddress();

    int getPing();

    boolean isOnline();

    boolean isBanned();

    boolean isWhitelisted();

    void setWhitelisted(boolean whitelisted);

    long getFirstPlayed();

    boolean hasPlayedBefore();

    void sendPacket(@Nonnull Object packet);

    void sendPackets(@Nonnull Object... packets);

    double getMaxHealth();

    void sendMessage(@Nonnull ChatComponent component);

    void sendMessage(@Nonnull ChatComponent component, @Nonnull Placeholder... placeholders);

    void sendMessage(@Nonnull String message, @Nonnull Placeholder... placeholders);

    void sendMessage(@Nonnull MessageKey messageKey, @Nonnull Placeholder... placeholders);

    void disconnect();

    void disconnect(@Nonnull MessageKey messageKey, @Nonnull Placeholder... placeholders);

    void disconnect(@Nonnull MessageKey messageKey, @Nonnull String append, @Nonnull Placeholder... placeholders);

    void disconnect(@Nonnull String kickMessage);

    <EnumTeamPush> void setCollision(@Nonnull EnumTeamPush collision);

    boolean setWindowProperty(@Nonnull InventoryView.Property property, int i);

    void openInventory(@Nonnull InventoryView inventoryView);

    void closeInventory();

    void setItemOnCursor(@Nonnull ItemStack itemStack);

    int getCooldown(@Nonnull Material material);

    void setCooldown(@Nonnull Material material, int i);

    void setCooldown(@Nonnull ItemStack itemStack, int i);

    int getSleepTicks();

    void setDisplayName(@Nonnull String s);

    void setBedSpawnLocation(@Nonnull Location location);

    void setBedSpawnLocation(@Nonnull Location location, boolean b);

    boolean sleep(@Nonnull Location location, boolean b);

    void wakeup(boolean b);

    boolean isBlocking();

    boolean isHandRaised();

    int getExpToLevel();

    float getAttackCooldown();

    void saveInventory(@Nonnull String id);

    void hideTabListName(@Nonnull TNLPlayer[] players);

    @Nonnull
    void disguise(@Nonnull Generic<?> entity, @Nonnull List<TNLPlayer> receivers);

    @Nonnull
    void disguise(@Nonnull Generic<?> entity, @Nonnull TNLPlayer receiver);

    @Nonnull
    void disguise(@Nonnull Generic<?> entity);

    @Nonnull
    void sendBossBar(@Nonnull BossBar<?> bossBar);

    @Nonnull
    void updateBossBar(@Nonnull BossBar<?> bossBar);

    @Nonnull
    void hideBossBar(@Nonnull BossBar<?> bossBar);

    @Nonnull
    void sendBossBar(@Nonnull BossBar<?> bossBar, long millis);

    void sendTitle(@Nonnull Title title);

    void sendTitle(@Nonnull Title.Animation animation);

    void sendActionbar(@Nonnull String actionbar);

    void bungeeConnect(@Nonnull net.nonswag.tnl.listener.api.server.Server server);

    void loadInventory(@Nonnull String id);

    void mkdirInventories();

    void setPlayerListName(@Nonnull String s);

    void setPlayerListHeader(@Nonnull String s);

    void setPlayerListFooter(@Nonnull String s);

    void setPlayerListHeaderFooter(@Nonnull String s, @Nonnull String s1);

    void setCompassTarget(@Nonnull Location location);

    void chat(@Nonnull String s);

    boolean performCommand(@Nonnull String s);

    boolean isSneaking();

    void setSneaking(boolean b);

    boolean isSprinting();

    void setSprinting(boolean b);

    void saveData();

    void loadData();

    void setSleepingIgnored(boolean b);

    boolean isSleepingIgnored();

    void sendMap(@Nonnull MapView mapView);

    void updateInventory();

    void setPlayerTime(long l, boolean b);

    long getPlayerTime();

    long getPlayerTimeOffset();

    boolean isPlayerTimeRelative();

    void resetPlayerTime();

    void setPlayerWeather(@Nonnull WeatherType weatherType);

    void resetPlayerWeather();

    void giveExp(int i);

    void giveExpLevels(int i);

    float getExp();

    void setExp(float v);

    int getLevel();

    void setLevel(int i);

    int getTotalExperience();

    void setTotalExperience(int i);

    void sendExperienceChange(float v);

    void sendExperienceChange(float v, int i);

    float getExhaustion();

    void setExhaustion(float v);

    float getSaturation();

    void setSaturation(float v);

    int getFoodLevel();

    void setFoodLevel(int i);

    boolean getAllowFlight();

    void setAllowFlight(boolean b);

    void setArrowCount(int arrows);

    void hidePlayer(@Nonnull TNLPlayer player);

    void showPlayer(@Nonnull TNLPlayer player);

    boolean isFlying();

    void setFlying(boolean b);

    void setFlySpeed(float v) throws IllegalArgumentException;

    void setWalkSpeed(float v) throws IllegalArgumentException;

    float getFlySpeed();

    float getWalkSpeed();

    void setResourcePack(@Nonnull String s, byte[] bytes);

    double getHealthScale();

    void spectate(@Nonnull Entity entity);

    void spectate();

    void resetTitle();

    int getClientViewDistance();

    void updateCommands();

    void openBook(@Nonnull ItemStack itemStack);

    void setVelocity(@Nonnull Vector vector);

    double getHeight();

    double getWidth();

    boolean isOnGround();

    void setRotation(float v, float v1);

    boolean teleport(@Nonnull Location location);

    boolean teleport(@Nonnull Location location, @Nonnull PlayerTeleportEvent.TeleportCause teleportCause);

    boolean teleport(@Nonnull Entity entity);

    boolean teleport(@Nonnull Entity entity, @Nonnull PlayerTeleportEvent.TeleportCause teleportCause);

    int getEntityId();

    int getFireTicks();

    int getMaxFireTicks();

    void setFireTicks(int i);

    boolean isDead();

    boolean addPassenger(@Nonnull Entity entity);

    boolean removePassenger(@Nonnull Entity entity);

    boolean eject();

    float getFallDistance();

    void setFallDistance(float v);

    void setLastDamageCause(@Nonnull EntityDamageEvent entityDamageEvent);

    int getTicksLived();

    void setTicksLived(int i);

    void playEffect(@Nonnull EntityEffect entityEffect);

    boolean isInsideVehicle();

    boolean leaveVehicle();

    void setCustomNameVisible(boolean b);

    boolean isCustomNameVisible();

    void setGlowing(boolean b);

    void setGlowing(boolean b, @Nonnull TNLPlayer... players);

    void setGlowing(boolean b, @Nonnull Entity entity);

    boolean isGlowing();

    void setInvulnerable(boolean b);

    boolean isInvulnerable();

    boolean hasGravity();

    void setGravity(boolean b);

    int getPortalCooldown();

    void setPortalCooldown(int i);

    boolean addScoreboardTag(@Nonnull String s);

    boolean removeScoreboardTag(@Nonnull String s);

    double getEyeHeight();

    double getEyeHeight(boolean b);

    int getRemainingAir();

    void setRemainingAir(int i);

    void setMaximumAir(int i);

    int getMaximumAir();

    int getMaximumNoDamageTicks();

    void setMaximumNoDamageTicks(int i);

    double getLastDamage();

    void setLastDamage(double v);

    int getNoDamageTicks();

    void setNoDamageTicks(int i);

    boolean addPotionEffect(@Nonnull PotionEffect potionEffect);

    boolean addPotionEffects(@Nonnull Collection<PotionEffect> collection);

    boolean hasPotionEffect(@Nonnull PotionEffectType potionEffectType);

    void removePotionEffect(@Nonnull PotionEffectType potionEffectType);

    boolean hasLineOfSight(@Nonnull Entity entity);

    boolean getRemoveWhenFarAway();

    void setRemoveWhenFarAway(boolean b);

    void setCanPickupItems(boolean b);

    boolean getCanPickupItems();

    boolean isLeashed();

    boolean setLeashHolder(@Nonnull Entity entity);

    boolean isGliding();

    void setGliding(boolean b);

    boolean isSwimming();

    void setSwimming(boolean b);

    boolean isRiptiding();

    boolean isSleeping();

    void attack(@Nonnull Entity entity);

    void swingMainHand();

    void swingOffHand();

    void setCollidable(boolean b);

    boolean isCollidable();

    void damage(double v);

    void damage(double v, @Nullable Entity entity);

    double getHealth();

    void setHealth(double v);

    double getAbsorptionAmount();

    void setAbsorptionAmount(double v);

    void setCustomName(@Nonnull String s);

    void setMetadata(@Nonnull String s, @Nonnull MetadataValue metadataValue);

    boolean hasMetadata(@Nonnull String s);

    void removeMetadata(@Nonnull String s, @Nonnull Plugin plugin);

    void sendPluginMessage(@Nonnull Plugin plugin, @Nonnull String s, byte[] bytes);

    void uninject();

    void inject();
}
