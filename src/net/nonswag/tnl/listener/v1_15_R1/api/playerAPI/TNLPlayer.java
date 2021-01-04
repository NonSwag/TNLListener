package net.nonswag.tnl.listener.v1_15_R1.api.playerAPI;

import com.sun.istack.internal.NotNull;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_15_R1.*;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.api.actionbarAPI.ActionBar;
import net.nonswag.tnl.listener.api.permissionAPI.Permissions;
import net.nonswag.tnl.listener.api.titleAPI.Title;
import net.nonswag.tnl.listener.enumerations.ProtocolVersion;
import net.nonswag.tnl.listener.v1_15_R1.TNLListener;
import net.nonswag.tnl.listener.v1_15_R1.api.bossbarAPI.BossBar;
import net.nonswag.tnl.listener.v1_15_R1.api.scoreboardAPI.Sidebar;
import net.nonswag.tnl.listener.v1_15_R1.eventHandler.InventoryLoadedEvent;
import net.nonswag.tnl.listener.v1_15_R1.eventHandler.InventorySafeEvent;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.InetSocketAddress;
import java.nio.file.NoSuchFileException;
import java.util.*;

public final class TNLPlayer {

    @Nonnull private final Player player;
    @Nonnull private final Scoreboard tnlOptionPacketScoreboard = new Scoreboard();
    @Nonnull private final ScoreboardTeam tnlOptionPacketTeam = new ScoreboardTeam(getTnlOptionPacketScoreboard(), "TNLOptionPacket");
    @Nonnull private final HashMap<String, Object> virtualStorage = new HashMap<>();

    @Deprecated
    public TNLPlayer(@Nonnull Player player) {
        this.player = player;
    }

    @Nonnull
    public static TNLPlayer cast(@Nonnull Player player) {
        if (!TNLListener.getPlayerHashMap().containsKey(player)) {
            TNLListener.getPlayerHashMap().put(player, new TNLPlayer(player));
        }
        return TNLListener.getPlayerHashMap().get(player);
    }

    @Nonnull
    public static TNLPlayer cast(@Nonnull CraftPlayer craftPlayer) {
        return cast((Player) craftPlayer);
    }

    @Nullable
    public static TNLPlayer cast(@Nonnull CommandSender sender) {
        if (sender instanceof Player) {
            return cast((Player) sender);
        }
        return null;
    }

    @Nullable
    public static TNLPlayer cast(@Nonnull HumanEntity humanEntity) {
        if (humanEntity instanceof Player) {
            return cast((Player) humanEntity);
        }
        return null;
    }

    @Nullable
    public static TNLPlayer cast(@Nonnull Entity entity) {
        if (entity instanceof Player) {
            return cast((Player) entity);
        }
        return null;
    }

    @Nullable
    public static TNLPlayer cast(@Nonnull LivingEntity livingEntity) {
        if (livingEntity instanceof Player) {
            return cast((Player) livingEntity);
        }
        return null;
    }

    @Nullable
    public static TNLPlayer cast(@Nonnull String string) {
        Player player = Bukkit.getPlayer(string);
        if (player != null) {
            return cast(player);
        }
        return null;
    }

    @Nullable
    public static TNLPlayer cast(@Nonnull Object object) {
        if (object instanceof Player) {
            return cast(((Player) object));
        }
        return null;
    }

    @Nonnull
    public static TNLPlayer cast(@Nonnull TNLPlayer player) {
        return player;
    }

    @Nonnull
    public HashMap<String, Object> getVirtualStorage() {
        return virtualStorage;
    }

    @Nonnull
    public Scoreboard getTnlOptionPacketScoreboard() {
        return tnlOptionPacketScoreboard;
    }

    @Nonnull
    public ScoreboardTeam getTnlOptionPacketTeam() {
        return tnlOptionPacketTeam;
    }

    public PlayerConnection getPlayerConnection() {
        return getEntityPlayer().playerConnection;
    }

    public NetworkManager getNetworkManager() {
        return getPlayerConnection().networkManager;
    }

    public Channel getChannel() {
        return getNetworkManager().channel;
    }

    public ChannelPipeline getPipeline() {
        return getChannel().pipeline();
    }

    public int getPing() {
        return getEntityPlayer().ping;
    }

    public void getProtocolVersion() {

    }

    public boolean isOnline() {
        return player.isOnline();
    }

    public boolean isBanned() {
        return player.isBanned();
    }

    public boolean isWhitelisted() {
        return player.isWhitelisted();
    }

    public void setWhitelisted(boolean b) {
        player.setWhitelisted(b);
    }

    @Nonnull
    public Player getPlayer() {
        return player;
    }

    public long getFirstPlayed() {
        return player.getFirstPlayed();
    }

    public long getLastPlayed() {
        return player.getLastPlayed();
    }

    public boolean hasPlayedBefore() {
        return player.hasPlayedBefore();
    }

    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
        player.incrementStatistic(statistic);
    }

    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
        player.decrementStatistic(statistic);
    }

    public void incrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {
        player.incrementStatistic(statistic, i);
    }

    public void decrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {
        player.decrementStatistic(statistic, i);
    }

    public void setStatistic(Statistic statistic, int i) throws IllegalArgumentException {
        player.setStatistic(statistic, i);
    }

    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
        return player.getStatistic(statistic);
    }

    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        player.incrementStatistic(statistic, material);
    }

    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        player.decrementStatistic(statistic, material);
    }

    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        return player.getStatistic(statistic, material);
    }

    public void incrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
        player.incrementStatistic(statistic, material, i);
    }

    public void decrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
        player.decrementStatistic(statistic, material, i);
    }

    public void setStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
        player.setStatistic(statistic, material, i);
    }

    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        player.incrementStatistic(statistic, entityType);
    }

    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        player.decrementStatistic(statistic, entityType);
    }

    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        return player.getStatistic(statistic, entityType);
    }

    public void incrementStatistic(Statistic statistic, EntityType entityType, int i) throws IllegalArgumentException {
        player.incrementStatistic(statistic, entityType, i);
    }

    public void decrementStatistic(Statistic statistic, EntityType entityType, int i) {
        player.decrementStatistic(statistic, entityType, i);
    }

    public void setStatistic(Statistic statistic, EntityType entityType, int i) {
        player.setStatistic(statistic, entityType, i);
    }

    public void sendPacket(Packet<?> packet) {
        NMSMain.runTask(() -> getPlayerConnection().sendPacket(packet));
    }

    public void sendMessage(String s) {
        player.sendMessage(s);
    }

    public void sendMessage(String[] strings) {
        player.sendMessage(strings);
    }

    public void disconnect() {
        disconnect(NMSMain.getPrefix() + "\n§cDisconnected");
    }

    public void disconnect(String kickMessage) {
        NMSMain.runTask(() -> {
            if (!getPlayerConnection().processedDisconnect) {
                getPlayerConnection().disconnect(kickMessage);
            }
        });
    }

    public List<String> getPermissions() {
        return Permissions.getPermissions(getPlayer());
    }

    public void addPermission(String permission) {
        Permissions.add(getPlayer(), permission);
    }

    public void addPermissions(String... permissions) {
        Permissions.add(getPlayer(), permissions);
    }

    public void removePermission(String permission) {
        Permissions.remove(getPlayer(), permission);
    }

    public void removePermissions(String... permissions) {
        Permissions.remove(getPlayer(), permissions);
    }

    public String getWorldAlias() {
        return getWorldAlias(getWorld());
    }

    public void setRules(ScoreboardTeamBase.EnumNameTagVisibility rule, ScoreboardTeamBase.EnumTeamPush rule1) {
        getTnlOptionPacketScoreboard().addPlayerToTeam(getPlayer().getName(), getTnlOptionPacketTeam());
        if (rule != null) {
            getTnlOptionPacketTeam().setNameTagVisibility(rule);
        }
        if (rule1 != null) {
            getTnlOptionPacketTeam().setCollisionRule(rule1);
        }
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(getTnlOptionPacketTeam(), 0);
        sendPacket(packet);
    }

    private static HashMap<World, String> getWorldAliasHashMap() {
        return TNLListener.getWorldAliasHashMap();
    }

    public static String getWorldAlias(World world) {
        return getWorldAliasHashMap().getOrDefault(world, world.getName());
    }

    public static void setWorldAlias(World world, String alias) {
        getWorldAliasHashMap().put(world, alias);
    }

    public String getName() {
        return player.getName();
    }

    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    public Inventory getEnderChest() {
        return player.getEnderChest();
    }

    public MainHand getMainHand() {
        return player.getMainHand();
    }

    public boolean setWindowProperty(InventoryView.Property property, int i) {
        return player.setWindowProperty(property, i);
    }

    public InventoryView getOpenInventory() {
        return player.getOpenInventory();
    }

    public InventoryView openInventory(Inventory inventory) {
        return player.openInventory(inventory);
    }

    public InventoryView openWorkbench(Location location, boolean b) {
        return player.openWorkbench(location, b);
    }

    public InventoryView openEnchanting(Location location, boolean b) {
        return player.openEnchanting(location, b);
    }

    public void openInventory(InventoryView inventoryView) {
        player.openInventory(inventoryView);
    }

    public InventoryView openMerchant(Villager villager, boolean b) {
        return player.openMerchant(villager, b);
    }

    public InventoryView openMerchant(Merchant merchant, boolean b) {
        return player.openMerchant(merchant, b);
    }

    public void closeInventory() {
        player.closeInventory();
    }

    public void setItemOnCursor(ItemStack itemStack) {
        player.setItemOnCursor(itemStack);
    }

    public int getCooldown(Material material) {
        return player.getCooldown(material);
    }

    public void setCooldown(Material material, int i) {
        player.setCooldown(material, i);
    }

    public int getSleepTicks() {
        return player.getSleepTicks();
    }

    public Location getBedSpawnLocation() {
        return player.getBedSpawnLocation();
    }

    public void setBedSpawnLocation(Location location) {
        player.setBedSpawnLocation(location);
    }

    public void setBedSpawnLocation(Location location, boolean b) {
        player.setBedSpawnLocation(location, b);
    }

    public boolean sleep(Location location, boolean b) {
        return player.sleep(location, b);
    }

    public void wakeup(boolean b) {
        player.wakeup(b);
    }

    public Location getBedLocation() {
        return player.getBedLocation();
    }

    public GameMode getGameMode() {
        return player.getGameMode();
    }

    public void setGameMode(GameMode gameMode) {
        player.setGameMode(gameMode);
    }

    public boolean isBlocking() {
        return player.isBlocking();
    }

    public boolean isHandRaised() {
        return player.isHandRaised();
    }

    public int getExpToLevel() {
        return player.getExpToLevel();
    }

    public float getAttackCooldown() {
        return player.getAttackCooldown();
    }

    public boolean discoverRecipe(NamespacedKey namespacedKey) {
        return player.discoverRecipe(namespacedKey);
    }

    public int discoverRecipes(Collection<NamespacedKey> collection) {
        return player.discoverRecipes(collection);
    }

    public boolean undiscoverRecipe(NamespacedKey namespacedKey) {
        return player.undiscoverRecipe(namespacedKey);
    }

    public int undiscoverRecipes(Collection<NamespacedKey> collection) {
        return player.undiscoverRecipes(collection);
    }

    public void saveInventory(@NotNull String id) {
        mkdirInventories();
        File file = new File("plugins/TNLListener/" + player.getUniqueId() + ".yml");
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    NMSMain.stacktrace(new NoSuchFileException("Failed to create file"));
                }
            }
            if (file.exists()) {
                YamlConfiguration inventory = YamlConfiguration.loadConfiguration(file);
                ItemStack[] contents = player.getInventory().getContents();
                inventory.set(id, Arrays.asList(contents));
                inventory.save(file);
                NMSMain.callEvent(new InventorySafeEvent(!Bukkit.isPrimaryThread(), this, id));
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public void hideTabListName(@Nullable Player[] players) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        if(players == null) {
            for(Player all : Bukkit.getOnlinePlayers()) {
                sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
            }
        } else {
            for(Player all : players) {
                sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
            }
        }
    }

    private static final HashMap<UUID, List<String>> bossBars = new HashMap<>();
    private static final HashMap<UUID, BossBar> bossHashMap = new HashMap<>();

    private static HashMap<UUID, BossBar> getBossHashMap() {
        return bossHashMap;
    }

    public static HashMap<UUID, List<String>> getBossBars() {
        return bossBars;
    }

    public static List<String> getBossBars(UUID uniqueId) {
        return bossBars.getOrDefault(uniqueId, new ArrayList<>());
    }

    public void sendBossBar(BossBar bossBar) {
        if (!getBossBars(getUniqueId()).contains(bossBar.getId())) {
            List<String> bars = getBossBars(getUniqueId());
            bars.add(bossBar.getId());
            getBossBars().put(getUniqueId(), bars);
        }
        sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.ADD, bossBar.getBossBar().getHandle()));
        updateBossBar(bossBar);
    }

    public void updateBossBar(BossBar bossBar) {
        if (!getBossBars(getUniqueId()).contains(bossBar.getId())) {
            List<String> bars = getBossBars(getUniqueId());
            bars.add(bossBar.getId());
            getBossBars().put(getUniqueId(), bars);
        }
        sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.UPDATE_NAME, bossBar.getBossBar().getHandle()));
        sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.UPDATE_PCT, bossBar.getBossBar().getHandle()));
        sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.UPDATE_PROPERTIES, bossBar.getBossBar().getHandle()));
        sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.UPDATE_STYLE, bossBar.getBossBar().getHandle()));
    }

    public void hideBossBar(BossBar bossBar) {
        sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.REMOVE, bossBar.getBossBar().getHandle()));
        List<String> bars = getBossBars(getUniqueId());
        bars.remove(bossBar.getId());
        getBossBars().put(getUniqueId(), bars);
        BossBar.removeBossBar(bossBar.getId());
    }

    public void sendBossBar(BossBar bossBar, long millis) {
        if (getBossHashMap().get(player.getUniqueId()) != null) {
            hideBossBar(getBossHashMap().get(player.getUniqueId()));
        }
        getBossHashMap().put(player.getUniqueId(), bossBar);
        sendBossBar(bossBar);
        new Thread(() -> {
            try {
                Thread.sleep(millis);
            } catch (Throwable ignored) {
            }
            if (getBossHashMap().get(player.getUniqueId()) != null
                    && getBossHashMap().get(player.getUniqueId()).equals(bossBar)) {
                hideBossBar(getBossHashMap().get(player.getUniqueId()));
            }
        }).start();
    }

    public void sendTitle(Title title) {
        NMSMain.runTask(() -> this.player.sendTitle(title.getTitle(),
                title.getSubtitle(),
                title.getTimeIn(),
                title.getTimeStay(),
                title.getTimeOut()));
    }

    public void sendActionBar(ActionBar actionBar) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + actionBar.getText() + "\"}"), ChatMessageType.a((byte) 2));
        sendPacket(packet);
    }

    public CraftPlayer getCraftPlayer() {
        return ((CraftPlayer) getPlayer());
    }

    public EntityPlayer getEntityPlayer() {
        return getCraftPlayer().getHandle();
    }

    public void bungeeConnect(net.nonswag.tnl.listener.api.serverAPI.Server server) {
        try {
            if (server.isOnline()) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                dataOutputStream.writeUTF("Connect");
                dataOutputStream.writeUTF(server.getName());
                player.sendPluginMessage(NMSMain.getPlugin(), "BungeeCord", byteArrayOutputStream.toByteArray());
                player.sendMessage(NMSMain.getPrefix() + " §aConnecting you to server §6" + server.getName());
            } else {
                player.sendMessage(NMSMain.getPrefix() + " §cThe server §4" + server.getName() + "§c is Offline");
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
            player.sendMessage(NMSMain.getPrefix() + " §cFailed to connect you to server §4" + server.getName());
        }
    }

    public Sidebar getSidebar() {
        return Sidebar.getSidebar(player);
    }

    public void loadInventory(@NotNull String id) {
        File file = new File("plugins/TNLListener/" + player.getUniqueId() + ".yml");
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
                NMSMain.callEvent(new InventoryLoadedEvent(!Bukkit.isPrimaryThread(), this, id));
            } else {
                NMSMain.stacktrace("Unknown InventoryID '" + id + "' for player '" + getName() + "'");
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    private void mkdirInventories() {
        File file = new File("plugins/TNLListener/");
        if (!file.exists()) {
            if (file.mkdirs()) {
                NMSMain.print("Successfully created folder '" + file.getAbsolutePath() + "'");
            } else {
                NMSMain.stacktrace("Failed to create folder '" + file.getAbsolutePath() + "'",
                        "Check if your software runs with the permission '777', 'root' or higher",
                        "Cloud and any kind of Remote software may cause issues if the server loads from a template");
            }
        }
    }

    public String getDisplayName() {
        return player.getDisplayName();
    }

    public void setDisplayName(String s) {
        player.setDisplayName(s);
    }

    public String getPlayerListName() {
        return player.getPlayerListName();
    }

    public void setPlayerListName(String s) {
        player.setPlayerListName(s);
    }

    public String getPlayerListHeader() {
        return player.getPlayerListHeader();
    }

    public String getPlayerListFooter() {
        return player.getPlayerListFooter();
    }

    public void setPlayerListHeader(String s) {
        player.setPlayerListHeader(s);
    }

    public void setPlayerListFooter(String s) {
        player.setPlayerListFooter(s);
    }

    public void setPlayerListHeaderFooter(String s, String s1) {
        player.setPlayerListHeaderFooter(s, s1);
    }

    public void setCompassTarget(Location location) {
        player.setCompassTarget(location);
    }

    public Location getCompassTarget() {
        return player.getCompassTarget();
    }

    public InetSocketAddress getAddress() {
        return player.getAddress();
    }

    public boolean isConversing() {
        return player.isConversing();
    }

    public void acceptConversationInput(String s) {
        player.acceptConversationInput(s);
    }

    public boolean beginConversation(Conversation conversation) {
        return player.beginConversation(conversation);
    }

    public void abandonConversation(Conversation conversation) {
        player.abandonConversation(conversation);
    }

    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent conversationAbandonedEvent) {
        player.abandonConversation(conversation, conversationAbandonedEvent);
    }

    public void sendRawMessage(String s) {
        player.sendRawMessage(s);
    }

    public void sendRawMessage(String s, ChatMessageType chatMessageType) {
        sendPacket(new PacketPlayOutChat(new ChatMessage(s, chatMessageType)));
    }

    public void kickPlayer(String s) {
        player.kickPlayer(s);
    }

    public void chat(String s) {
        player.chat(s);
    }

    public boolean performCommand(String s) {
        return player.performCommand(s);
    }

    public boolean isSneaking() {
        return player.isSneaking();
    }

    public void setSneaking(boolean b) {
        player.setSneaking(b);
    }

    public boolean isSprinting() {
        return player.isSprinting();
    }

    public void setSprinting(boolean b) {
        player.setSprinting(b);
    }

    public void saveData() {
        player.saveData();
    }

    public void loadData() {
        player.loadData();
    }

    public void setSleepingIgnored(boolean b) {
        player.setSleepingIgnored(b);
    }

    public boolean isSleepingIgnored() {
        return player.isSleepingIgnored();
    }

    public void playNote(Location location, Instrument instrument, Note note) {
        player.playNote(location, instrument, note);
    }

    public void playSound(Location location, Sound sound, float v, float v1) {
        player.playSound(location, sound, v, v1);
    }

    public void playSound(Location location, String s, float v, float v1) {
        player.playSound(location, s, v, v1);
    }

    public void playSound(Location location, Sound sound, SoundCategory soundCategory, float v, float v1) {
        player.playSound(location, sound, soundCategory, v, v1);
    }

    public void playSound(Location location, String s, SoundCategory soundCategory, float v, float v1) {
        player.playSound(location, s, soundCategory, v, v1);
    }

    public void stopSound(Sound sound) {
        player.stopSound(sound);
    }

    public void stopSound(String s) {
        player.stopSound(s);
    }

    public void stopSound(Sound sound, SoundCategory soundCategory) {
        player.stopSound(sound, soundCategory);
    }

    public void stopSound(String s, SoundCategory soundCategory) {
        player.stopSound(s, soundCategory);
    }

    public <T> void playEffect(Location location, Effect effect, T t) {
        player.playEffect(location, effect, t);
    }

    public void sendBlockChange(Location location, BlockData blockData) {
        player.sendBlockChange(location, blockData);
    }

    public void sendSignChange(Location location, String[] strings) throws IllegalArgumentException {
        player.sendSignChange(location, strings);
    }

    public void sendSignChange(Location location, String[] strings, DyeColor dyeColor) throws IllegalArgumentException {
        player.sendSignChange(location, strings, dyeColor);
    }

    public void sendMap(MapView mapView) {
        player.sendMap(mapView);
    }

    public void updateInventory() {
        player.updateInventory();
    }

    public void setPlayerTime(long l, boolean b) {
        player.setPlayerTime(l, b);
    }

    public long getPlayerTime() {
        return player.getPlayerTime();
    }

    public long getPlayerTimeOffset() {
        return player.getPlayerTimeOffset();
    }

    public boolean isPlayerTimeRelative() {
        return player.isPlayerTimeRelative();
    }

    public void resetPlayerTime() {
        player.resetPlayerTime();
    }

    public void setPlayerWeather(WeatherType weatherType) {
        player.setPlayerWeather(weatherType);
    }

    public WeatherType getPlayerWeather() {
        return player.getPlayerWeather();
    }

    public void resetPlayerWeather() {
        player.resetPlayerWeather();
    }

    public void giveExp(int i) {
        player.giveExp(i);
    }

    public void giveExpLevels(int i) {
        player.giveExpLevels(i);
    }

    public float getExp() {
        return player.getExp();
    }

    public void setExp(float v) {
        player.setExp(v);
    }

    public int getLevel() {
        return player.getLevel();
    }

    public void setLevel(int i) {
        player.setLevel(i);
    }

    public int getTotalExperience() {
        return player.getTotalExperience();
    }

    public void setTotalExperience(int i) {
        player.setTotalExperience(i);
    }

    public void sendExperienceChange(float v) {
        player.sendExperienceChange(v);
    }

    public void sendExperienceChange(float v, int i) {
        player.sendExperienceChange(v, i);
    }

    public float getExhaustion() {
        return player.getExhaustion();
    }

    public void setExhaustion(float v) {
        player.setExhaustion(v);
    }

    public float getSaturation() {
        return player.getSaturation();
    }

    public void setSaturation(float v) {
        player.setSaturation(v);
    }

    public int getFoodLevel() {
        return player.getFoodLevel();
    }

    public void setFoodLevel(int i) {
        player.setFoodLevel(i);
    }

    public boolean getAllowFlight() {
        return player.getAllowFlight();
    }

    public void setAllowFlight(boolean b) {
        player.setAllowFlight(b);
    }

    public void hidePlayer(Plugin plugin, Player player) {
        this.player.hidePlayer(plugin, player);
    }

    public void showPlayer(Plugin plugin, Player player) {
        this.player.showPlayer(plugin, player);
    }

    public boolean canSee(Player player) {
        return this.player.canSee(player);
    }

    public boolean isFlying() {
        return player.isFlying();
    }

    public void setFlying(boolean b) {
        player.setFlying(b);
    }

    public void setFlySpeed(float v) throws IllegalArgumentException {
        player.setFlySpeed(v);
    }

    public void setWalkSpeed(float v) throws IllegalArgumentException {
        player.setWalkSpeed(v);
    }

    public float getFlySpeed() {
        return player.getFlySpeed();
    }

    public float getWalkSpeed() {
        return player.getWalkSpeed();
    }

    public void setResourcePack(String s) {
        player.setResourcePack(s);
    }

    public void setResourcePack(String s, byte[] bytes) {
        player.setResourcePack(s, bytes);
    }

    public org.bukkit.scoreboard.Scoreboard getScoreboard() {
        return player.getScoreboard();
    }

    public void setScoreboard(org.bukkit.scoreboard.Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        player.setScoreboard(scoreboard);
    }

    public boolean isHealthScaled() {
        return player.isHealthScaled();
    }

    public void setHealthScaled(boolean b) {
        player.setHealthScaled(b);
    }

    public void setHealthScale(double v) throws IllegalArgumentException {
        player.setHealthScale(v);
    }

    public double getHealthScale() {
        return player.getHealthScale();
    }

    public Entity getSpectatorTarget() {
        return player.getSpectatorTarget();
    }

    public void setSpectatorTarget(Entity entity) {
        player.setSpectatorTarget(entity);
    }

    public void sendTitle(String s, String s1, int i, int i1, int i2) {
        player.sendTitle(s, s1, i, i1, i2);
    }

    public void resetTitle() {
        player.resetTitle();
    }

    public void spawnParticle(Particle particle, Location location, int i) {
        player.spawnParticle(particle, location, i);
    }

    public void spawnParticle(Particle particle, double v, double v1, double v2, int i) {
        player.spawnParticle(particle, v, v1, v2, i);
    }

    public <T> void spawnParticle(Particle particle, Location location, int i, T t) {
        player.spawnParticle(particle, location, i, t);
    }

    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, T t) {
        player.spawnParticle(particle, v, v1, v2, i, t);
    }

    public void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2) {
        player.spawnParticle(particle, location, i, v, v1, v2);
    }

    public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5) {
        player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
    }

    public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, T t) {
        player.spawnParticle(particle, location, i, v, v1, v2, t);
    }

    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, T t) {
        player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
    }

    public void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3) {
        player.spawnParticle(particle, location, i, v, v1, v2, v3);
    }

    public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6) {
        player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
    }

    public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3, T t) {
        player.spawnParticle(particle, location, i, v, v1, v2, v3, t);
    }

    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6, T t) {
        player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t);
    }

    public AdvancementProgress getAdvancementProgress(Advancement advancement) {
        return player.getAdvancementProgress(advancement);
    }

    public int getClientViewDistance() {
        return player.getClientViewDistance();
    }

    public String getLocale() {
        return player.getLocale();
    }

    public void updateCommands() {
        player.updateCommands();
    }

    public void openBook(ItemStack itemStack) {
        player.openBook(itemStack);
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public Location getLocation(Location location) {
        return player.getLocation();
    }

    public void setVelocity(Vector vector) {
        player.setVelocity(vector);
    }

    public Vector getVelocity() {
        return player.getVelocity();
    }

    public double getHeight() {
        return player.getHeight();
    }

    public double getWidth() {
        return player.getWidth();
    }

    public BoundingBox getBoundingBox() {
        return player.getBoundingBox();
    }

    public boolean isOnGround() {
        return player.isOnGround();
    }

    public World getWorld() {
        return player.getWorld();
    }

    public WorldServer getWorldServer() {
        return ((CraftWorld) player.getWorld()).getHandle();
    }

    public void setRotation(float v, float v1) {
        player.setRotation(v, v1);
    }

    public boolean teleport(Location location) {
        return player.teleport(location);
    }

    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
        return player.teleport(location, teleportCause);
    }

    public boolean teleport(Entity entity) {
        return player.teleport(entity);
    }

    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause teleportCause) {
        return player.teleport(entity, teleportCause);
    }

    public List<Entity> getNearbyEntities(double v, double v1, double v2) {
        return player.getNearbyEntities(v, v1, v2);
    }

    public int getEntityId() {
        return player.getEntityId();
    }

    public int getFireTicks() {
        return player.getFireTicks();
    }

    public int getMaxFireTicks() {
        return player.getMaxFireTicks();
    }

    public void setFireTicks(int i) {
        player.setFireTicks(i);
    }

    public void remove() {
        player.remove();
    }

    public boolean isDead() {
        return player.isDead();
    }

    public boolean isValid() {
        return player.isValid();
    }

    public Server getServer() {
        return player.getServer();
    }

    public List<Entity> getPassengers() {
        return player.getPassengers();
    }

    public boolean addPassenger(Entity entity) {
        return player.addPassenger(entity);
    }

    public boolean removePassenger(Entity entity) {
        return player.removePassenger(entity);
    }

    public boolean isEmpty() {
        return player.isEmpty();
    }

    public boolean eject() {
        return player.eject();
    }

    public float getFallDistance() {
        return player.getFallDistance();
    }

    public void setFallDistance(float v) {
        player.setFallDistance(v);
    }

    public void setLastDamageCause(EntityDamageEvent entityDamageEvent) {
        player.setLastDamageCause(entityDamageEvent);
    }

    public EntityDamageEvent getLastDamageCause() {
        return player.getLastDamageCause();
    }

    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    public int getTicksLived() {
        return player.getTicksLived();
    }

    public void setTicksLived(int i) {
        player.setTicksLived(i);
    }

    public void playEffect(EntityEffect entityEffect) {
        player.playEffect(entityEffect);
    }

    public EntityType getType() {
        return player.getType();
    }

    public boolean isInsideVehicle() {
        return player.isInsideVehicle();
    }

    public boolean leaveVehicle() {
        return player.leaveVehicle();
    }

    public Entity getVehicle() {
        return player.getVehicle();
    }

    public ProtocolVersion getVersion() {
        return VersionCache.getVersion(this);
    }

    public void setCustomNameVisible(boolean b) {
        player.setCustomNameVisible(b);
    }

    public boolean isCustomNameVisible() {
        return player.isCustomNameVisible();
    }

    public void setGlowing(boolean b) {
        player.setGlowing(b);
    }

    public boolean isGlowing() {
        return player.isGlowing();
    }

    public void setInvulnerable(boolean b) {
        player.setInvulnerable(b);
    }

    public boolean isInvulnerable() {
        return player.isInvulnerable();
    }

    public boolean isSilent() {
        return player.isSilent();
    }

    public void setSilent(boolean b) {
        player.setSilent(b);
    }

    public boolean hasGravity() {
        return player.hasGravity();
    }

    public void setGravity(boolean b) {
        player.setGravity(b);
    }

    public int getPortalCooldown() {
        return player.getPortalCooldown();
    }

    public void setPortalCooldown(int i) {
        player.setPortalCooldown(i);
    }

    public Set<String> getScoreboardTags() {
        return player.getScoreboardTags();
    }

    public boolean addScoreboardTag(String s) {
        return player.addScoreboardTag(s);
    }

    public boolean removeScoreboardTag(String s) {
        return player.removeScoreboardTag(s);
    }

    public PistonMoveReaction getPistonMoveReaction() {
        return player.getPistonMoveReaction();
    }

    public BlockFace getFacing() {
        return player.getFacing();
    }

    public Pose getPose() {
        return player.getPose();
    }

    public BackFlip backflip() {
        return new BackFlip(this);
    }

    public Map<String, Object> serialize() {
        return player.serialize();
    }

    public double getEyeHeight() {
        return player.getEyeHeight();
    }

    public double getEyeHeight(boolean b) {
        return player.getEyeHeight(b);
    }

    public Location getEyeLocation() {
        return player.getEyeLocation();
    }

    public List<Block> getLineOfSight(Set<Material> set, int i) {
        return player.getLineOfSight(set, i);
    }

    public Block getTargetBlock(Set<Material> set, int i) {
        return player.getTargetBlock(set, i);
    }

    public List<Block> getLastTwoTargetBlocks(Set<Material> set, int i) {
        return player.getLastTwoTargetBlocks(set, i);
    }

    public Block getTargetBlockExact(int i) {
        return player.getTargetBlockExact(i);
    }

    public Block getTargetBlockExact(int i, FluidCollisionMode fluidCollisionMode) {
        return player.getTargetBlockExact(i, fluidCollisionMode);
    }

    public RayTraceResult rayTraceBlocks(double v) {
        return player.rayTraceBlocks(v);
    }

    public RayTraceResult rayTraceBlocks(double v, FluidCollisionMode fluidCollisionMode) {
        return player.rayTraceBlocks(v, fluidCollisionMode);
    }

    public int getRemainingAir() {
        return player.getRemainingAir();
    }

    public void setRemainingAir(int i) {
        player.setRemainingAir(i);
    }

    public int getMaximumAir() {
        return player.getMaximumAir();
    }

    public void setMaximumAir(int i) {
        player.setMaximumNoDamageTicks(i);
    }

    public int getMaximumNoDamageTicks() {
        return player.getMaximumNoDamageTicks();
    }

    public void setMaximumNoDamageTicks(int i) {
        player.setMaximumNoDamageTicks(i);
    }

    public double getLastDamage() {
        return player.getLastDamage();
    }

    public void setLastDamage(double v) {
        player.setLastDamage(v);
    }

    public int getNoDamageTicks() {
        return player.getNoDamageTicks();
    }

    public void setNoDamageTicks(int i) {
        player.setNoDamageTicks(i);
    }

    @Nullable
    public TNLPlayer getKiller() {
        if (player.getKiller() != null) {
            return TNLPlayer.cast(player.getKiller());
        }
        return null;
    }

    public boolean addPotionEffect(PotionEffect potionEffect) {
        return player.addPotionEffect(potionEffect);
    }

    public boolean addPotionEffects(Collection<PotionEffect> collection) {
        return player.addPotionEffects(collection);
    }

    public boolean hasPotionEffect(PotionEffectType potionEffectType) {
        return player.hasPotionEffect(potionEffectType);
    }

    public PotionEffect getPotionEffect(PotionEffectType potionEffectType) {
        return player.getPotionEffect(potionEffectType);
    }

    public void removePotionEffect(PotionEffectType potionEffectType) {
        player.removePotionEffect(potionEffectType);
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        return player.getActivePotionEffects();
    }

    public boolean hasLineOfSight(Entity entity) {
        return player.hasLineOfSight(entity);
    }

    public boolean getRemoveWhenFarAway() {
        return player.getRemoveWhenFarAway();
    }

    public void setRemoveWhenFarAway(boolean b) {
        player.setRemoveWhenFarAway(b);
    }

    public EntityEquipment getEquipment() {
        return player.getEquipment();
    }

    public void setCanPickupItems(boolean b) {
        player.setCanPickupItems(b);
    }

    public boolean getCanPickupItems() {
        return player.getCanPickupItems();
    }

    public boolean isLeashed() {
        return player.isLeashed();
    }

    public Entity getLeashHolder() throws IllegalStateException {
        return player.getLeashHolder();
    }

    public boolean setLeashHolder(Entity entity) {
        return player.setLeashHolder(entity);
    }

    public boolean isGliding() {
        return player.isGliding();
    }

    public void setGliding(boolean b) {
        player.setGliding(b);
    }

    public boolean isSwimming() {
        return player.isSwimming();
    }

    public void setSwimming(boolean b) {
        player.setSwimming(b);
    }

    public boolean isRiptiding() {
        return player.isRiptiding();
    }

    public boolean isSleeping() {
        return player.isSleeping();
    }

    public void setAI(boolean b) {
        player.setAI(b);
    }

    public boolean hasAI() {
        return player.hasAI();
    }

    public void attack(Entity entity) {
        player.attack(entity);
    }

    public void swingMainHand() {
        player.swingMainHand();
    }

    public void swingOffHand() {
        player.swingOffHand();
    }

    public void setCollidable(boolean b) {
        player.setCollidable(b);
    }

    public boolean isCollidable() {
        return player.isCollidable();
    }

    public <T> T getMemory(MemoryKey<T> memoryKey) {
        return player.getMemory(memoryKey);
    }

    public <T> void setMemory(MemoryKey<T> memoryKey, T t) {
        player.setMemory(memoryKey, t);
    }

    public AttributeInstance getAttribute(Attribute attribute) {
        return player.getAttribute(attribute);
    }

    public void damage(double v) {
        player.damage(v);
    }

    public void damage(double v, Entity entity) {
        player.damage(v, entity);
    }

    public double getHealth() {
        return player.getHealth();
    }

    public void setHealth(double v) {
        player.setHealth(v);
    }

    public double getAbsorptionAmount() {
        return player.getAbsorptionAmount();
    }

    public void setAbsorptionAmount(double v) {
        player.setAbsorptionAmount(v);
    }

    public String getCustomName() {
        return player.getCustomName();
    }

    public void setCustomName(String s) {
        player.setCustomName(s);
    }

    public void setMetadata(String s, MetadataValue metadataValue) {
        player.setMetadata(s, metadataValue);
    }

    public List<MetadataValue> getMetadata(String s) {
        return player.getMetadata(s);
    }

    public boolean hasMetadata(String s) {
        return player.hasMetadata(s);
    }

    public void removeMetadata(String s, Plugin plugin) {
        player.removeMetadata(s, plugin);
    }

    public boolean isPermissionSet(String s) {
        return player.isPermissionSet(s);
    }

    public boolean isPermissionSet(Permission permission) {
        return player.isPermissionSet(permission);
    }

    public boolean hasPermission(String s) {
        return player.hasPermission(s);
    }

    public boolean hasPermission(Permission permission) {
        return player.hasPermission(permission);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
        return player.addAttachment(plugin, s, b);
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        return player.addAttachment(plugin);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
        return player.addAttachment(plugin, s, b, i);
    }

    public PermissionAttachment addAttachment(Plugin plugin, int i) {
        return player.addAttachment(plugin, i);
    }

    public void removeAttachment(PermissionAttachment permissionAttachment) {
        player.removeAttachment(permissionAttachment);
    }

    public void recalculatePermissions() {
        player.recalculatePermissions();
    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return player.getEffectivePermissions();
    }

    public boolean isOp() {
        return player.isOp();
    }

    public void setOp(boolean b) {
        player.setOp(b);
    }

    public PersistentDataContainer getPersistentDataContainer() {
        return player.getPersistentDataContainer();
    }

    public void sendPluginMessage(Plugin plugin, String s, byte[] bytes) {
        player.sendPluginMessage(plugin, s, bytes);
    }

    public Set<String> getListeningPluginChannels() {
        return player.getListeningPluginChannels();
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass) {
        return player.launchProjectile(aClass);
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass, Vector vector) {
        return player.launchProjectile(aClass, vector);
    }
}
