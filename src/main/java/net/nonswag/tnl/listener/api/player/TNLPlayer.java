package net.nonswag.tnl.listener.api.player;

import net.nonswag.tnl.listener.Bootstrap;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.bossbar.TNLBossBar;
import net.nonswag.tnl.listener.api.conversation.Conversation;
import net.nonswag.tnl.listener.api.entity.TNLEntity;
import net.nonswag.tnl.listener.api.file.FileCreator;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.message.ChatComponent;
import net.nonswag.tnl.listener.api.message.Language;
import net.nonswag.tnl.listener.api.message.MessageKey;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.object.Generic;
import net.nonswag.tnl.listener.api.packet.TNLEntityDestroy;
import net.nonswag.tnl.listener.api.packet.TNLGameStateChange;
import net.nonswag.tnl.listener.api.packet.TNLPlayerInfo;
import net.nonswag.tnl.listener.api.permission.PermissionManager;
import net.nonswag.tnl.listener.api.permission.Permissions;
import net.nonswag.tnl.listener.api.scoreboard.Sidebar;
import net.nonswag.tnl.listener.api.scoreboard.Team;
import net.nonswag.tnl.listener.api.sign.SignMenu;
import net.nonswag.tnl.listener.api.title.Title;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import net.nonswag.tnl.listener.events.InventoryLoadedEvent;
import net.nonswag.tnl.listener.events.InventorySafeEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
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

public interface TNLPlayer extends TNLEntity {

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

    @Nullable
    WeatherType getPlayerWeather();

    @Nonnull
    org.bukkit.block.Block getTargetBlock(@Nonnull Set<Material> set, int i);

    @Nonnull
    List<org.bukkit.block.Block> getLastTwoTargetBlocks(@Nonnull Set<Material> set, int i);

    @Nullable
    org.bukkit.block.Block getTargetBlockExact(int i);

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
    default Sidebar getSidebar() {
        if (!getVirtualStorage().containsKey("sidebar") || !(getVirtualStorage().get("sidebar") instanceof Sidebar)) {
            getVirtualStorage().put("sidebar", new Sidebar(this));
        }
        return ((Sidebar) getVirtualStorage().get("sidebar"));
    }

    @Nonnull
    default Team getTeam() {
        if (!getVirtualStorage().containsKey("team") || !(getVirtualStorage().get("team") instanceof Team)) {
            getVirtualStorage().put("team", Team.NONE);
        }
        return ((Team) getVirtualStorage().get("team"));
    }

    default void setTeam(@Nonnull Team team) {
        getVirtualStorage().put("team", team);
        updateTeam();
    }

    default void updateTeam() {
        getTeam().getTeam().addEntry(getName());
        setDisplayName(getTeam().getPrefix() + getTeam().getColor() + getName() + getTeam().getSuffix());
        for (TNLPlayer all : TNLListener.getInstance().getOnlinePlayers()) {
            for (org.bukkit.scoreboard.Team team : Team.getScoreboard().getTeams()) {
                org.bukkit.scoreboard.Team allTeam = all.getScoreboard().getTeam(team.getName());
                if (allTeam == null) {
                    allTeam = all.getScoreboard().registerNewTeam(team.getName());
                }
                allTeam.setDisplayName(team.getDisplayName());
                allTeam.setPrefix(team.getPrefix());
                allTeam.setSuffix(team.getSuffix());
                allTeam.setColor(team.getColor());
                for (String entry : team.getEntries()) {
                    if (!allTeam.hasEntry(entry)) {
                        allTeam.addEntry(entry);
                    }
                }
                for (org.bukkit.scoreboard.Team.Option option : org.bukkit.scoreboard.Team.Option.values()) {
                    allTeam.setOption(option, team.getOption(option));
                }
                allTeam.setCanSeeFriendlyInvisibles(team.canSeeFriendlyInvisibles());
                allTeam.setAllowFriendlyFire(team.allowFriendlyFire());
            }
        }
    }

    @Nonnull
    List<MetadataValue> getMetadata(@Nonnull String s);

    @Nonnull
    Set<String> getListeningPluginChannels();

    @Nonnull
    Player getBukkitPlayer();

    @Nonnull
    default Permissions getPermissionManager() {
        if (!getVirtualStorage().containsKey("permissions") || !(getVirtualStorage().get("permissions") instanceof Permissions)) {
            getVirtualStorage().put("permissions", new PermissionManager(this));
        }
        return ((Permissions) getVirtualStorage().get("permissions"));
    }

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
    HashMap<String, Object> getVirtualStorage();

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
    default Scoreboard getScoreboard() {
        if (getBukkitPlayer().getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            getBukkitPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
        return getBukkitPlayer().getScoreboard();
    }

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

    default void sendBlockChange(@Nonnull Location location, @Nonnull BlockFace blockFace) {
        Block relative = location.getBlock().getRelative(blockFace);
        sendBlockChange(relative.getLocation());
    }

    void sendBlockChange(@Nonnull Location location, @Nonnull BlockData block);

    default void sendBlockChange(@Nonnull Location location) {
        sendBlockChange(location, location.getBlock().getBlockData());
    }

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

    @Nullable
    default Conversation getConversation() {
        return TNLListener.getInstance().getConversationHashMap().get(getUniqueId());
    }

    default void startConversation(@Nonnull Conversation conversation) {
        TNLListener.getInstance().getConversationHashMap().put(getUniqueId(), conversation);
    }

    default void stopConversation() {
        TNLListener.getInstance().getConversationHashMap().remove(getUniqueId());
    }

    default boolean isInConversation() {
        return TNLListener.getInstance().getConversationHashMap().containsKey(getUniqueId());
    }

    default void sendDemoScreen() {
        sendPacket(TNLGameStateChange.create(5, 0));
    }

    boolean setWindowProperty(@Nonnull InventoryView.Property property, int i);

    void openInventory(@Nonnull InventoryView inventoryView);

    void closeInventory();

    void setItemOnCursor(@Nonnull ItemStack itemStack);

    int getCooldown(@Nonnull Material material);

    void setCooldown(@Nonnull Material material, int i);

    void setCooldown(@Nonnull ItemStack itemStack, int i);

    int getSleepTicks();

    void exitCombat();

    void enterCombat();

    void setDisplayName(@Nonnull String s);

    void setBedSpawnLocation(@Nonnull Location location);

    void setBedSpawnLocation(@Nonnull Location location, boolean b);

    boolean sleep(@Nonnull Location location, boolean b);

    void wakeup(boolean b);

    boolean isBlocking();

    boolean isHandRaised();

    int getExpToLevel();

    float getAttackCooldown();

    default void saveInventory(@Nonnull String id) {
        try {
            File file = new File(new File("plugins/Listener/Inventories/"), getBukkitPlayer().getUniqueId() + ".yml");
            FileCreator.create(file);
            YamlConfiguration inventory = YamlConfiguration.loadConfiguration(file);
            ItemStack[] contents = getBukkitPlayer().getInventory().getContents();
            inventory.set(id, Arrays.asList(contents));
            inventory.save(file);
            Bukkit.getPluginManager().callEvent(new InventorySafeEvent(this, id));
        } catch (IOException e) {
            Logger.error.println(e);
        }
    }

    default void loadInventory(@Nonnull String id) {
        File file = new File(new File("plugins/Listener/Inventories/"), getBukkitPlayer().getUniqueId() + ".yml");
        try {
            if (file.exists()) {
                YamlConfiguration inventory = YamlConfiguration.loadConfiguration(file);
                if (inventory.isSet(id)) {
                    List<?> contents = inventory.getList(id);
                    if (contents != null) {
                        for (int i = 0; i < contents.size(); i++) {
                            this.getInventory().setItem(i, ((ItemStack) contents.get(i)));
                        }
                    } else {
                        this.getInventory().clear();
                    }
                } else {
                    this.getInventory().clear();
                }
                Bukkit.getPluginManager().callEvent(new InventoryLoadedEvent(this, id));
            }
        } catch (Exception e) {
            Logger.error.println(e);
        }
    }

    @Nullable
    default Inventory getInventory(@Nonnull String id) {
        throw new UnsupportedOperationException("method is not supported");
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
            } else {
                sendMessage("%prefix% §cThe server §4" + server.getName() + "§c is Offline");
            }
        } catch (Exception e) {
            Logger.error.println(e);
            sendMessage("%prefix% §cFailed to connect you to server §4" + server.getName());
        }
    }

    void hideTabListName(@Nonnull TNLPlayer[] players);

    void disguise(@Nonnull Generic<?> entity, @Nonnull List<TNLPlayer> receivers);

    void disguise(@Nonnull Generic<?> entity, @Nonnull TNLPlayer receiver);

    void disguise(@Nonnull Generic<?> entity);

    void sendBossBar(@Nonnull TNLBossBar<?> TNLBossBar);

    void updateBossBar(@Nonnull TNLBossBar<?> TNLBossBar);

    void hideBossBar(@Nonnull TNLBossBar<?> TNLBossBar);

    void sendBossBar(@Nonnull TNLBossBar<?> TNLBossBar, long millis);

    void sendTitle(@Nonnull Title title);

    void sendTitle(@Nonnull Title.Animation animation);

    void sendActionbar(@Nonnull String actionbar);

    void setPlayerListName(@Nonnull String s);

    void setPlayerListHeader(@Nonnull String s);

    void setPlayerListFooter(@Nonnull String s);

    void setPlayerListHeaderFooter(@Nonnull String s, @Nonnull String s1);

    void setCompassTarget(@Nonnull Location location);

    default void chat(@Nonnull String s) {
        getBukkitPlayer().chat(s);
    }

    default boolean performCommand(@Nonnull String s) {
        return getBukkitPlayer().performCommand(s);
    }

    default boolean isSneaking() {
        return getBukkitPlayer().isSneaking();
    }

    default void setSneaking(boolean b) {
        getBukkitPlayer().setSneaking(b);
    }

    default boolean isSprinting() {
        return getBukkitPlayer().isSprinting();
    }

    default void setSprinting(boolean b) {
        getBukkitPlayer().setSprinting(b);
    }

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

    @Nullable
    static TNLPlayer cast(@Nonnull String name) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            return cast(player);
        }
        return null;
    }

    @Nullable
    static TNLPlayer cast(@Nonnull UUID uniqueId) {
        Player player = Bukkit.getPlayer(uniqueId);
        if (player != null) {
            return cast(player);
        }
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
