package net.nonswag.tnl.listener.api.player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_15_R1.*;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.actionbar.ActionBar;
import net.nonswag.tnl.listener.api.bossbar.BossBar;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.permission.PermissionManager;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.title.Title;
import net.nonswag.tnl.listener.enumerations.ProtocolVersion;
import net.nonswag.tnl.listener.eventhandler.InventoryLoadedEvent;
import net.nonswag.tnl.listener.eventhandler.InventorySafeEvent;
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

/**
 * TNLPlayer is a more net.minecraft.server based player
 *
 * @see TNLPlayer#backflip()
 * @see TNLPlayer#bungeeConnect(net.nonswag.tnl.listener.api.server.Server)
 * @see TNLPlayer#cast(Entity)
 * @see TNLPlayer#cast(Object)
 * @see TNLPlayer#cast(Player)
 * @see TNLPlayer#cast(String)
 * @see TNLPlayer#cast(TNLPlayer)
 * @see TNLPlayer#cast(CommandSender)
 * @see TNLPlayer#cast(CraftPlayer)
 * @see TNLPlayer#cast(HumanEntity)
 * @see TNLPlayer#cast(LivingEntity)
 * @see TNLPlayer#getVirtualStorage()
 * @see TNLPlayer#updateBossBar(BossBar)
 * @see TNLPlayer#hideBossBar(BossBar)
 * @see TNLPlayer#sendBossBar(BossBar)
 * @see TNLPlayer#sendBossBar(BossBar, long)
 * @see TNLPlayer#getBossBars(UUID)
 * @see TNLPlayer#resetTitle()
 * @see TNLPlayer#sendTitle(Title)
 * @see TNLPlayer#sendTitle(Title.Animation)
 *
 **/

public class TNLPlayer {

    @Nonnull private final Player bukkitPlayer;
    @Nonnull private final Scoreboard tnlOptionPacketScoreboard = new Scoreboard();
    @Nonnull private final ScoreboardTeam tnlOptionPacketTeam = new ScoreboardTeam(getTnlOptionPacketScoreboard(), "TNLOptionPacket");
    @Nonnull private final HashMap<String, Object> virtualStorage = new HashMap<>();
    @Nonnull private final PermissionManager permissionManager;

    protected TNLPlayer(@Nonnull Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        this.permissionManager = new PermissionManager(this);
    }

    @Nonnull
    public static TNLPlayer cast(@Nonnull Player player) {
        if (!TNLListener.getInstance().getPlayerHashMap().containsKey(player)) {
            TNLListener.getInstance().getPlayerHashMap().put(player, new TNLPlayer(player));
        }
        return TNLListener.getInstance().getPlayerHashMap().get(player);
    }

    @Nonnull
    public static TNLPlayer cast(@Nonnull CraftPlayer craftPlayer) {
        return cast((Player) craftPlayer);
    }

    @Nullable
    public static TNLPlayer cast(@Nullable CommandSender sender) {
        if (sender instanceof Player) {
            return cast((Player) sender);
        }
        return null;
    }

    @Nullable
    public static TNLPlayer cast(@Nullable HumanEntity humanEntity) {
        if (humanEntity instanceof Player) {
            return cast((Player) humanEntity);
        }
        return null;
    }

    @Nullable
    public static TNLPlayer cast(@Nullable Entity entity) {
        if (entity instanceof Player) {
            return cast((Player) entity);
        }
        return null;
    }

    @Nullable
    public static TNLPlayer cast(@Nullable LivingEntity livingEntity) {
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
    public static TNLPlayer cast(@Nullable Object object) {
        if (object instanceof Player) {
            return cast(((Player) object));
        }
        return null;
    }

    @Nullable
    public static TNLPlayer cast(@Nullable TNLPlayer player) {
        return player;
    }

    public HashMap<String, Object> getVirtualStorage() {
        return virtualStorage;
    }

    public Scoreboard getTnlOptionPacketScoreboard() {
        return tnlOptionPacketScoreboard;
    }

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

    @Nonnull
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public boolean isOnline() {
        return getBukkitPlayer().isOnline();
    }

    public boolean isBanned() {
        return getBukkitPlayer().isBanned();
    }

    public boolean isWhitelisted() {
        return getBukkitPlayer().isWhitelisted();
    }

    public void setWhitelisted(boolean b) {
        getBukkitPlayer().setWhitelisted(b);
    }

    @Nonnull
    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public long getFirstPlayed() {
        return getBukkitPlayer().getFirstPlayed();
    }

    public boolean hasPlayedBefore() {
        return getBukkitPlayer().hasPlayedBefore();
    }

    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
        getBukkitPlayer().incrementStatistic(statistic);
    }

    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
        getBukkitPlayer().decrementStatistic(statistic);
    }

    public void incrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {
        getBukkitPlayer().incrementStatistic(statistic, i);
    }

    public void decrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {
        getBukkitPlayer().decrementStatistic(statistic, i);
    }

    public void setStatistic(Statistic statistic, int i) throws IllegalArgumentException {
        getBukkitPlayer().setStatistic(statistic, i);
    }

    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
        return getBukkitPlayer().getStatistic(statistic);
    }

    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        getBukkitPlayer().incrementStatistic(statistic, material);
    }

    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        getBukkitPlayer().decrementStatistic(statistic, material);
    }

    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        return getBukkitPlayer().getStatistic(statistic, material);
    }

    public void incrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
        getBukkitPlayer().incrementStatistic(statistic, material, i);
    }

    public void decrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
        getBukkitPlayer().decrementStatistic(statistic, material, i);
    }

    public void setStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
        getBukkitPlayer().setStatistic(statistic, material, i);
    }

    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        getBukkitPlayer().incrementStatistic(statistic, entityType);
    }

    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        getBukkitPlayer().decrementStatistic(statistic, entityType);
    }

    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        return getBukkitPlayer().getStatistic(statistic, entityType);
    }

    public void incrementStatistic(Statistic statistic, EntityType entityType, int i) throws IllegalArgumentException {
        getBukkitPlayer().incrementStatistic(statistic, entityType, i);
    }

    public void decrementStatistic(Statistic statistic, EntityType entityType, int i) {
        getBukkitPlayer().decrementStatistic(statistic, entityType, i);
    }

    public void setStatistic(Statistic statistic, EntityType entityType, int i) {
        getBukkitPlayer().setStatistic(statistic, entityType, i);
    }

    public void sendPacket(Packet<?> packet) {
        if (Bukkit.isPrimaryThread()) {
            getPlayerConnection().sendPacket(packet);
        } else {
            Bukkit.getScheduler().runTask(NMSMain.getInstance(), () -> getPlayerConnection().sendPacket(packet));
        }
    }

    public void sendPackets(Packet<?>... packets) {
        for (Packet<?> packet : packets) {
            sendPacket(packet);
        }
    }

    public double getMaxHealth() {
        return getEntityPlayer().getMaxHealth();
    }

    public void sendMessage(String s) {
        getBukkitPlayer().sendMessage(s);
    }

    public void disconnect() {
        disconnect(TNLListener.getInstance().getPrefix() + "\n§cDisconnected");
    }

    public void disconnect(@Nonnull String kickMessage) {
        if (Bukkit.isPrimaryThread()) {
            if (!getPlayerConnection().processedDisconnect) {
                getPlayerConnection().disconnect(kickMessage);
            }
        } else {
            Bukkit.getScheduler().runTask(NMSMain.getInstance(), () -> {
                if (!getPlayerConnection().processedDisconnect) {
                    getPlayerConnection().disconnect(kickMessage);
                }
            });
        }
    }

    public String getWorldAlias() {
        return getWorldAlias(getWorld());
    }

    public void setRules(ScoreboardTeamBase.EnumNameTagVisibility rule, ScoreboardTeamBase.EnumTeamPush rule1) {
        getTnlOptionPacketScoreboard().addPlayerToTeam(getBukkitPlayer().getName(), getTnlOptionPacketTeam());
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
        return TNLListener.getInstance().getWorldAliasHashMap();
    }

    public static String getWorldAlias(World world) {
        return getWorldAliasHashMap().getOrDefault(world, world.getName());
    }

    public static void setWorldAlias(World world, String alias) {
        getWorldAliasHashMap().put(world, alias);
    }

    public String getName() {
        return getBukkitPlayer().getName();
    }

    public PlayerInventory getInventory() {
        return getBukkitPlayer().getInventory();
    }

    public Inventory getEnderChest() {
        return getBukkitPlayer().getEnderChest();
    }

    public MainHand getMainHand() {
        return getBukkitPlayer().getMainHand();
    }

    public boolean setWindowProperty(InventoryView.Property property, int i) {
        return getBukkitPlayer().setWindowProperty(property, i);
    }

    public InventoryView getOpenInventory() {
        return getBukkitPlayer().getOpenInventory();
    }

    public InventoryView openInventory(Inventory inventory) {
        return getBukkitPlayer().openInventory(inventory);
    }

    public InventoryView openWorkbench(Location location, boolean b) {
        return getBukkitPlayer().openWorkbench(location, b);
    }

    public InventoryView openEnchanting(Location location, boolean b) {
        return getBukkitPlayer().openEnchanting(location, b);
    }

    public void openInventory(InventoryView inventoryView) {
        getBukkitPlayer().openInventory(inventoryView);
    }

    public InventoryView openMerchant(Villager villager, boolean b) {
        return getBukkitPlayer().openMerchant(villager, b);
    }

    public InventoryView openMerchant(Merchant merchant, boolean b) {
        return getBukkitPlayer().openMerchant(merchant, b);
    }

    public void closeInventory() {
        getBukkitPlayer().closeInventory();
    }

    public void setItemOnCursor(ItemStack itemStack) {
        getBukkitPlayer().setItemOnCursor(itemStack);
    }

    public int getCooldown(Material material) {
        return getBukkitPlayer().getCooldown(material);
    }

    public void setCooldown(Material material, int i) {
        getBukkitPlayer().setCooldown(material, i);
    }

    public int getSleepTicks() {
        return getBukkitPlayer().getSleepTicks();
    }

    public Location getBedSpawnLocation() {
        return getBukkitPlayer().getBedSpawnLocation();
    }

    public void setBedSpawnLocation(Location location) {
        getBukkitPlayer().setBedSpawnLocation(location);
    }

    public void setBedSpawnLocation(Location location, boolean b) {
        getBukkitPlayer().setBedSpawnLocation(location, b);
    }

    public boolean sleep(Location location, boolean b) {
        return getBukkitPlayer().sleep(location, b);
    }

    public void wakeup(boolean b) {
        getBukkitPlayer().wakeup(b);
    }

    public Location getBedLocation() {
        return getBukkitPlayer().getBedLocation();
    }

    public GameMode getGameMode() {
        return getBukkitPlayer().getGameMode();
    }

    public void setGameMode(GameMode gameMode) {
        getBukkitPlayer().setGameMode(gameMode);
    }

    public boolean isBlocking() {
        return getBukkitPlayer().isBlocking();
    }

    public boolean isHandRaised() {
        return getBukkitPlayer().isHandRaised();
    }

    public int getExpToLevel() {
        return getBukkitPlayer().getExpToLevel();
    }

    public float getAttackCooldown() {
        return getBukkitPlayer().getAttackCooldown();
    }

    public boolean discoverRecipe(NamespacedKey namespacedKey) {
        return getBukkitPlayer().discoverRecipe(namespacedKey);
    }

    public int discoverRecipes(Collection<NamespacedKey> collection) {
        return getBukkitPlayer().discoverRecipes(collection);
    }

    public boolean undiscoverRecipe(NamespacedKey namespacedKey) {
        return getBukkitPlayer().undiscoverRecipe(namespacedKey);
    }

    public int undiscoverRecipes(Collection<NamespacedKey> collection) {
        return getBukkitPlayer().undiscoverRecipes(collection);
    }

    public void saveInventory(String id) {
        mkdirInventories();
        File file = new File("plugins/TNLListener/" + getBukkitPlayer().getUniqueId() + ".yml");
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    Logger.error.println(new NoSuchFileException("Failed to create file"));
                }
            }
            if (file.exists()) {
                YamlConfiguration inventory = YamlConfiguration.loadConfiguration(file);
                ItemStack[] contents = getBukkitPlayer().getInventory().getContents();
                inventory.set(id, Arrays.asList(contents));
                inventory.save(file);
                Bukkit.getPluginManager().callEvent(new InventorySafeEvent(!Bukkit.isPrimaryThread(), this, id));
            }
        } catch (Exception e) {
            Logger.error.println(e);
        }
    }

    public void hideTabListName(@Nonnull TNLPlayer[] players) {
        for (TNLPlayer all : players) {
            if (!all.equals(this)) {
                sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, all.getEntityPlayer()));
            }
        }
    }

    public void disguise(net.minecraft.server.v1_15_R1.EntityLiving entity, List<TNLPlayer> receivers) {
        for (TNLPlayer receiver : receivers) {
            disguise(entity, receiver);
        }
    }

    public void disguise(net.minecraft.server.v1_15_R1.EntityLiving entity, TNLPlayer receiver) {
        if (!this.equals(receiver)) {
            receiver.sendPacket(new PacketPlayOutEntityDestroy(this.getEntityId()));
            entity.setLocation(getLocation().getX(), getLocation().getY(), getLocation().getZ(), getLocation().getYaw(), getLocation().getPitch());
            entity.world = this.getWorldServer();
            Reflection.setField(entity, net.minecraft.server.v1_15_R1.Entity.class, "id", this.getEntityId());
            receiver.sendPacket(new PacketPlayOutSpawnEntityLiving(entity));
        }
    }

    public void disguise(net.minecraft.server.v1_15_R1.EntityLiving entity) {
        disguise(entity, TNLListener.getInstance().getOnlinePlayers());
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
        if (getBossHashMap().get(getBukkitPlayer().getUniqueId()) != null) {
            hideBossBar(getBossHashMap().get(getBukkitPlayer().getUniqueId()));
        }
        getBossHashMap().put(getBukkitPlayer().getUniqueId(), bossBar);
        sendBossBar(bossBar);
        new Thread(() -> {
            try {
                Thread.sleep(millis);
            } catch (Throwable ignored) {
            }
            if (getBossHashMap().get(getBukkitPlayer().getUniqueId()) != null
                    && getBossHashMap().get(getBukkitPlayer().getUniqueId()).equals(bossBar)) {
                hideBossBar(getBossHashMap().get(getBukkitPlayer().getUniqueId()));
            }
        }).start();
    }

    public void sendTitle(Title title) {
        getBukkitPlayer().sendTitle(title.getTitle(),
                title.getSubtitle(),
                title.getTimeIn(),
                title.getTimeStay(),
                title.getTimeOut());
    }

    public void sendTitle(Title.Animation animation) {
        new Thread(() -> {
            try {
                String[] split = animation.getTitle().getTitle().split("");
                String spaces = "          ";
                do {
                    spaces = spaces.replaceFirst(" ", "");
                    getBukkitPlayer().sendTitle((animation.getDesign().getSecondaryColor() +
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

    public void sendActionBar(ActionBar actionBar) {
        sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + actionBar.getText() + "\"}"), ChatMessageType.a((byte) 2)));
    }

    public CraftPlayer getCraftPlayer() {
        return ((CraftPlayer) getBukkitPlayer());
    }

    public EntityPlayer getEntityPlayer() {
        return getCraftPlayer().getHandle();
    }

    public void bungeeConnect(net.nonswag.tnl.listener.api.server.Server server) {
        try {
            if (server.isOnline()) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                dataOutputStream.writeUTF("Connect");
                dataOutputStream.writeUTF(server.getName());
                getBukkitPlayer().sendPluginMessage(NMSMain.getInstance(), "BungeeCord", byteArrayOutputStream.toByteArray());
                getBukkitPlayer().sendMessage(TNLListener.getInstance().getPrefix() + " §aConnecting you to server §6" + server.getName());
            } else {
                getBukkitPlayer().sendMessage(TNLListener.getInstance().getPrefix() + " §cThe server §4" + server.getName() + "§c is Offline");
            }
        } catch (Exception e) {
            Logger.error.println(e);
            getBukkitPlayer().sendMessage(TNLListener.getInstance().getPrefix() + " §cFailed to connect you to server §4" + server.getName());
        }
    }

    public void loadInventory(String id) {
        File file = new File("plugins/TNLListener/" + getBukkitPlayer().getUniqueId() + ".yml");
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
                Bukkit.getPluginManager().callEvent(new InventoryLoadedEvent(!Bukkit.isPrimaryThread(), this, id));
            } else {
                Logger.error.println("Unknown InventoryID '" + id + "' for player '" + getName() + "'");
            }
        } catch (Exception e) {
            Logger.error.println(e);
        }
    }

    private void mkdirInventories() {
        File file = new File("plugins/TNLListener/");
        if (!file.exists()) {
            if (file.mkdirs()) {
                Logger.info.println("Successfully created folder '" + file.getAbsolutePath() + "'");
            } else {
                Logger.error.println("Failed to create folder '" + file.getAbsolutePath() + "'",
                        "Check if your software runs with the permission '777', 'root' or higher",
                        "Cloud and any kind of Remote software may cause issues if the server loads from a template");
            }
        }
    }

    public String getDisplayName() {
        return getBukkitPlayer().getDisplayName();
    }

    public void setDisplayName(String s) {
        getBukkitPlayer().setDisplayName(s);
    }

    public String getPlayerListName() {
        return getBukkitPlayer().getPlayerListName();
    }

    public void setPlayerListName(String s) {
        getBukkitPlayer().setPlayerListName(s);
    }

    public String getPlayerListHeader() {
        return getBukkitPlayer().getPlayerListHeader();
    }

    public String getPlayerListFooter() {
        return getBukkitPlayer().getPlayerListFooter();
    }

    public void setPlayerListHeader(String s) {
        getBukkitPlayer().setPlayerListHeader(s);
    }

    public void setPlayerListFooter(String s) {
        getBukkitPlayer().setPlayerListFooter(s);
    }

    public void setPlayerListHeaderFooter(String s, String s1) {
        getBukkitPlayer().setPlayerListHeaderFooter(s, s1);
    }

    public void setCompassTarget(Location location) {
        getBukkitPlayer().setCompassTarget(location);
    }

    public Location getCompassTarget() {
        return getBukkitPlayer().getCompassTarget();
    }

    public InetSocketAddress getAddress() {
        return getBukkitPlayer().getAddress();
    }

    public boolean isConversing() {
        return getBukkitPlayer().isConversing();
    }

    public void acceptConversationInput(String s) {
        getBukkitPlayer().acceptConversationInput(s);
    }

    public boolean beginConversation(Conversation conversation) {
        return getBukkitPlayer().beginConversation(conversation);
    }

    public void abandonConversation(Conversation conversation) {
        getBukkitPlayer().abandonConversation(conversation);
    }

    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent conversationAbandonedEvent) {
        getBukkitPlayer().abandonConversation(conversation, conversationAbandonedEvent);
    }

    public void sendRawMessage(String s) {
        getBukkitPlayer().sendRawMessage(s);
    }

    public void sendRawMessage(String s, ChatMessageType chatMessageType) {
        sendPacket(new PacketPlayOutChat(new ChatMessage(s, chatMessageType)));
    }

    public void kickPlayer(String s) {
        getBukkitPlayer().kickPlayer(s);
    }

    public void chat(String s) {
        getBukkitPlayer().chat(s);
    }

    public boolean performCommand(String s) {
        return getBukkitPlayer().performCommand(s);
    }

    public boolean isSneaking() {
        return getBukkitPlayer().isSneaking();
    }

    public void setSneaking(boolean b) {
        getBukkitPlayer().setSneaking(b);
    }

    public boolean isSprinting() {
        return getBukkitPlayer().isSprinting();
    }

    public void setSprinting(boolean b) {
        getBukkitPlayer().setSprinting(b);
    }

    public void saveData() {
        getBukkitPlayer().saveData();
    }

    public void loadData() {
        getBukkitPlayer().loadData();
    }

    public void setSleepingIgnored(boolean b) {
        getBukkitPlayer().setSleepingIgnored(b);
    }

    public boolean isSleepingIgnored() {
        return getBukkitPlayer().isSleepingIgnored();
    }

    public void playNote(Location location, Instrument instrument, Note note) {
        getBukkitPlayer().playNote(location, instrument, note);
    }

    public void playSound(Location location, Sound sound, float v, float v1) {
        getBukkitPlayer().playSound(location, sound, v, v1);
    }

    public void playSound(Location location, String s, float v, float v1) {
        getBukkitPlayer().playSound(location, s, v, v1);
    }

    public void playSound(Location location, Sound sound, SoundCategory soundCategory, float v, float v1) {
        getBukkitPlayer().playSound(location, sound, soundCategory, v, v1);
    }

    public void playSound(Location location, String s, SoundCategory soundCategory, float v, float v1) {
        getBukkitPlayer().playSound(location, s, soundCategory, v, v1);
    }

    public void stopSound(Sound sound) {
        getBukkitPlayer().stopSound(sound);
    }

    public void stopSound(String s) {
        getBukkitPlayer().stopSound(s);
    }

    public void stopSound(Sound sound, SoundCategory soundCategory) {
        getBukkitPlayer().stopSound(sound, soundCategory);
    }

    public void stopSound(String s, SoundCategory soundCategory) {
        getBukkitPlayer().stopSound(s, soundCategory);
    }

    public <T> void playEffect(Location location, Effect effect, T t) {
        getBukkitPlayer().playEffect(location, effect, t);
    }

    public void sendBlockChange(Location location, BlockData blockData) {
        getBukkitPlayer().sendBlockChange(location, blockData);
    }

    public void sendSignChange(Location location, String[] strings) throws IllegalArgumentException {
        getBukkitPlayer().sendSignChange(location, strings);
    }

    public void sendSignChange(Location location, String[] strings, DyeColor dyeColor) throws IllegalArgumentException {
        getBukkitPlayer().sendSignChange(location, strings, dyeColor);
    }

    public void sendMap(MapView mapView) {
        getBukkitPlayer().sendMap(mapView);
    }

    public void updateInventory() {
        getBukkitPlayer().updateInventory();
    }

    public void setPlayerTime(long l, boolean b) {
        getBukkitPlayer().setPlayerTime(l, b);
    }

    public long getPlayerTime() {
        return getBukkitPlayer().getPlayerTime();
    }

    public long getPlayerTimeOffset() {
        return getBukkitPlayer().getPlayerTimeOffset();
    }

    public boolean isPlayerTimeRelative() {
        return getBukkitPlayer().isPlayerTimeRelative();
    }

    public void resetPlayerTime() {
        getBukkitPlayer().resetPlayerTime();
    }

    public void setPlayerWeather(WeatherType weatherType) {
        getBukkitPlayer().setPlayerWeather(weatherType);
    }

    public WeatherType getPlayerWeather() {
        return getBukkitPlayer().getPlayerWeather();
    }

    public void resetPlayerWeather() {
        getBukkitPlayer().resetPlayerWeather();
    }

    public void giveExp(int i) {
        getBukkitPlayer().giveExp(i);
    }

    public void giveExpLevels(int i) {
        getBukkitPlayer().giveExpLevels(i);
    }

    public float getExp() {
        return getBukkitPlayer().getExp();
    }

    public void setExp(float v) {
        getBukkitPlayer().setExp(v);
    }

    public int getLevel() {
        return getBukkitPlayer().getLevel();
    }

    public void setLevel(int i) {
        getBukkitPlayer().setLevel(i);
    }

    public int getTotalExperience() {
        return getBukkitPlayer().getTotalExperience();
    }

    public void setTotalExperience(int i) {
        getBukkitPlayer().setTotalExperience(i);
    }

    public void sendExperienceChange(float v) {
        getBukkitPlayer().sendExperienceChange(v);
    }

    public void sendExperienceChange(float v, int i) {
        getBukkitPlayer().sendExperienceChange(v, i);
    }

    public float getExhaustion() {
        return getBukkitPlayer().getExhaustion();
    }

    public void setExhaustion(float v) {
        getBukkitPlayer().setExhaustion(v);
    }

    public float getSaturation() {
        return getBukkitPlayer().getSaturation();
    }

    public void setSaturation(float v) {
        getBukkitPlayer().setSaturation(v);
    }

    public int getFoodLevel() {
        return getBukkitPlayer().getFoodLevel();
    }

    public void setFoodLevel(int i) {
        getBukkitPlayer().setFoodLevel(i);
    }

    public boolean getAllowFlight() {
        return getBukkitPlayer().getAllowFlight();
    }

    public void setAllowFlight(boolean b) {
        getBukkitPlayer().setAllowFlight(b);
    }

    public void setArrowCount(int arrows) {
        getEntityPlayer().setArrowCount(arrows);
    }

    public void hidePlayer(@Nonnull TNLPlayer player) {
        this.sendPacket(new PacketPlayOutEntityDestroy(player.getEntityId()));
    }

    public void showPlayer(@Nonnull TNLPlayer player) {
        this.sendPacket(new PacketPlayOutSpawnEntityLiving(player.getEntityPlayer()));
    }

    public boolean isFlying() {
        return getBukkitPlayer().isFlying();
    }

    public void setFlying(boolean b) {
        getBukkitPlayer().setFlying(b);
    }

    public void setFlySpeed(float v) throws IllegalArgumentException {
        getBukkitPlayer().setFlySpeed(v);
    }

    public void setWalkSpeed(float v) throws IllegalArgumentException {
        getBukkitPlayer().setWalkSpeed(v);
    }

    public float getFlySpeed() {
        return getBukkitPlayer().getFlySpeed();
    }

    public float getWalkSpeed() {
        return getBukkitPlayer().getWalkSpeed();
    }

    public void setResourcePack(String s, byte[] bytes) {
        getBukkitPlayer().setResourcePack(s, bytes);
    }

    public boolean isHealthScaled() {
        return getBukkitPlayer().isHealthScaled();
    }

    public void setHealthScaled(boolean b) {
        getBukkitPlayer().setHealthScaled(b);
    }

    public void setHealthScale(double v) throws IllegalArgumentException {
        getBukkitPlayer().setHealthScale(v);
    }

    public double getHealthScale() {
        return getBukkitPlayer().getHealthScale();
    }

    @Nullable
    public Entity getSpectatorTarget() {
        return getBukkitPlayer().getSpectatorTarget();
    }

    public void setSpectatorTarget(@Nonnull Entity entity) {
        getBukkitPlayer().setSpectatorTarget(entity);
    }

    public void resetTitle() {
        sendTitle(Title.EMPTY);
    }

    public void spawnParticle(Particle particle, Location location, int i) {
        getBukkitPlayer().spawnParticle(particle, location, i);
    }

    public void spawnParticle(Particle particle, double v, double v1, double v2, int i) {
        getBukkitPlayer().spawnParticle(particle, v, v1, v2, i);
    }

    public <T> void spawnParticle(Particle particle, Location location, int i, T t) {
        getBukkitPlayer().spawnParticle(particle, location, i, t);
    }

    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, T t) {
        getBukkitPlayer().spawnParticle(particle, v, v1, v2, i, t);
    }

    public void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2) {
        getBukkitPlayer().spawnParticle(particle, location, i, v, v1, v2);
    }

    public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5) {
        getBukkitPlayer().spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
    }

    public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, T t) {
        getBukkitPlayer().spawnParticle(particle, location, i, v, v1, v2, t);
    }

    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, T t) {
        getBukkitPlayer().spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
    }

    public void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3) {
        getBukkitPlayer().spawnParticle(particle, location, i, v, v1, v2, v3);
    }

    public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6) {
        getBukkitPlayer().spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
    }

    public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3, T t) {
        getBukkitPlayer().spawnParticle(particle, location, i, v, v1, v2, v3, t);
    }

    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6, T t) {
        getBukkitPlayer().spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t);
    }

    public AdvancementProgress getAdvancementProgress(Advancement advancement) {
        return getBukkitPlayer().getAdvancementProgress(advancement);
    }

    public int getClientViewDistance() {
        return getBukkitPlayer().getClientViewDistance();
    }

    public String getLocale() {
        return getBukkitPlayer().getLocale();
    }

    public void updateCommands() {
        getBukkitPlayer().updateCommands();
    }

    public void openBook(ItemStack itemStack) {
        getBukkitPlayer().openBook(itemStack);
    }

    public Location getLocation() {
        return getBukkitPlayer().getLocation();
    }

    public Location getLocation(Location location) {
        return getBukkitPlayer().getLocation(location);
    }

    public Location getTargetLocation(int i) {
        return getLocation().clone().add(getBukkitPlayer().getLocation().getDirection().multiply(i));
    }

    public void setVelocity(Vector vector) {
        getBukkitPlayer().setVelocity(vector);
    }

    public Vector getVelocity() {
        return getBukkitPlayer().getVelocity();
    }

    public double getHeight() {
        return getBukkitPlayer().getHeight();
    }

    public double getWidth() {
        return getBukkitPlayer().getWidth();
    }

    public BoundingBox getBoundingBox() {
        return getBukkitPlayer().getBoundingBox();
    }

    public boolean isOnGround() {
        return getBukkitPlayer().isOnGround();
    }

    public World getWorld() {
        return getBukkitPlayer().getWorld();
    }

    public WorldServer getWorldServer() {
        return ((CraftWorld) getBukkitPlayer().getWorld()).getHandle();
    }

    public void setRotation(float v, float v1) {
        getBukkitPlayer().setRotation(v, v1);
    }

    public boolean teleport(Location location) {
        return getBukkitPlayer().teleport(location);
    }

    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
        return getBukkitPlayer().teleport(location, teleportCause);
    }

    public boolean teleport(Entity entity) {
        return getBukkitPlayer().teleport(entity);
    }

    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause teleportCause) {
        return getBukkitPlayer().teleport(entity, teleportCause);
    }

    public List<Entity> getNearbyEntities(double v, double v1, double v2) {
        return getBukkitPlayer().getNearbyEntities(v, v1, v2);
    }

    public int getEntityId() {
        return getBukkitPlayer().getEntityId();
    }

    public int getFireTicks() {
        return getBukkitPlayer().getFireTicks();
    }

    public int getMaxFireTicks() {
        return getBukkitPlayer().getMaxFireTicks();
    }

    public void setFireTicks(int i) {
        getBukkitPlayer().setFireTicks(i);
    }

    public void remove() {
        getBukkitPlayer().remove();
    }

    public boolean isDead() {
        return getBukkitPlayer().isDead();
    }

    public boolean isValid() {
        return getBukkitPlayer().isValid();
    }

    public Server getServer() {
        return getBukkitPlayer().getServer();
    }

    public List<Entity> getPassengers() {
        return getBukkitPlayer().getPassengers();
    }

    public boolean addPassenger(Entity entity) {
        return getBukkitPlayer().addPassenger(entity);
    }

    public boolean removePassenger(Entity entity) {
        return getBukkitPlayer().removePassenger(entity);
    }

    public boolean isEmpty() {
        return getBukkitPlayer().isEmpty();
    }

    public boolean eject() {
        return getBukkitPlayer().eject();
    }

    public float getFallDistance() {
        return getBukkitPlayer().getFallDistance();
    }

    public void setFallDistance(float v) {
        getBukkitPlayer().setFallDistance(v);
    }

    public void setLastDamageCause(EntityDamageEvent entityDamageEvent) {
        getBukkitPlayer().setLastDamageCause(entityDamageEvent);
    }

    public EntityDamageEvent getLastDamageCause() {
        return getBukkitPlayer().getLastDamageCause();
    }

    public UUID getUniqueId() {
        return getBukkitPlayer().getUniqueId();
    }

    public int getTicksLived() {
        return getBukkitPlayer().getTicksLived();
    }

    public void setTicksLived(int i) {
        getBukkitPlayer().setTicksLived(i);
    }

    public void playEffect(EntityEffect entityEffect) {
        getBukkitPlayer().playEffect(entityEffect);
    }

    public EntityType getType() {
        return getBukkitPlayer().getType();
    }

    public boolean isInsideVehicle() {
        return getBukkitPlayer().isInsideVehicle();
    }

    public boolean leaveVehicle() {
        return getBukkitPlayer().leaveVehicle();
    }

    public Entity getVehicle() {
        return getBukkitPlayer().getVehicle();
    }

    public ProtocolVersion getVersion() {
        return VersionCache.getVersion(this);
    }

    public void setCustomNameVisible(boolean b) {
        getBukkitPlayer().setCustomNameVisible(b);
    }

    public boolean isCustomNameVisible() {
        return getBukkitPlayer().isCustomNameVisible();
    }

    public void setGlowing(boolean b) {
        getBukkitPlayer().setGlowing(b);
    }

    public boolean isGlowing() {
        return getBukkitPlayer().isGlowing();
    }

    public void setInvulnerable(boolean b) {
        getBukkitPlayer().setInvulnerable(b);
    }

    public boolean isInvulnerable() {
        return getBukkitPlayer().isInvulnerable();
    }

    public boolean isSilent() {
        return getBukkitPlayer().isSilent();
    }

    public void setSilent(boolean b) {
        getBukkitPlayer().setSilent(b);
    }

    public boolean hasGravity() {
        return getBukkitPlayer().hasGravity();
    }

    public void setGravity(boolean b) {
        getBukkitPlayer().setGravity(b);
    }

    public int getPortalCooldown() {
        return getBukkitPlayer().getPortalCooldown();
    }

    public void setPortalCooldown(int i) {
        getBukkitPlayer().setPortalCooldown(i);
    }

    public Set<String> getScoreboardTags() {
        return getBukkitPlayer().getScoreboardTags();
    }

    public boolean addScoreboardTag(String s) {
        return getBukkitPlayer().addScoreboardTag(s);
    }

    public boolean removeScoreboardTag(String s) {
        return getBukkitPlayer().removeScoreboardTag(s);
    }

    public PistonMoveReaction getPistonMoveReaction() {
        return getBukkitPlayer().getPistonMoveReaction();
    }

    public BlockFace getFacing() {
        return getBukkitPlayer().getFacing();
    }

    public Pose getPose() {
        return getBukkitPlayer().getPose();
    }

    public BackFlip backflip() {
        return new BackFlip(this);
    }

    public Map<String, Object> serialize() {
        return getBukkitPlayer().serialize();
    }

    public double getEyeHeight() {
        return getBukkitPlayer().getEyeHeight();
    }

    public double getEyeHeight(boolean b) {
        return getBukkitPlayer().getEyeHeight(b);
    }

    public Location getEyeLocation() {
        return getBukkitPlayer().getEyeLocation();
    }

    public List<Block> getLineOfSight(Set<Material> set, int i) {
        return getBukkitPlayer().getLineOfSight(set, i);
    }

    public Block getTargetBlock(Set<Material> set, int i) {
        return getBukkitPlayer().getTargetBlock(set, i);
    }

    public List<Block> getLastTwoTargetBlocks(Set<Material> set, int i) {
        return getBukkitPlayer().getLastTwoTargetBlocks(set, i);
    }

    public Block getTargetBlockExact(int i) {
        return getBukkitPlayer().getTargetBlockExact(i);
    }

    public Block getTargetBlockExact(int i, FluidCollisionMode fluidCollisionMode) {
        return getBukkitPlayer().getTargetBlockExact(i, fluidCollisionMode);
    }

    public RayTraceResult rayTraceBlocks(double v) {
        return getBukkitPlayer().rayTraceBlocks(v);
    }

    public RayTraceResult rayTraceBlocks(double v, FluidCollisionMode fluidCollisionMode) {
        return getBukkitPlayer().rayTraceBlocks(v, fluidCollisionMode);
    }

    public int getRemainingAir() {
        return getBukkitPlayer().getRemainingAir();
    }

    public void setRemainingAir(int i) {
        getBukkitPlayer().setRemainingAir(i);
    }

    public int getMaximumAir() {
        return getBukkitPlayer().getMaximumAir();
    }

    public void setMaximumAir(int i) {
        getBukkitPlayer().setMaximumNoDamageTicks(i);
    }

    public int getMaximumNoDamageTicks() {
        return getBukkitPlayer().getMaximumNoDamageTicks();
    }

    public void setMaximumNoDamageTicks(int i) {
        getBukkitPlayer().setMaximumNoDamageTicks(i);
    }

    public double getLastDamage() {
        return getBukkitPlayer().getLastDamage();
    }

    public void setLastDamage(double v) {
        getBukkitPlayer().setLastDamage(v);
    }

    public int getNoDamageTicks() {
        return getBukkitPlayer().getNoDamageTicks();
    }

    public void setNoDamageTicks(int i) {
        getBukkitPlayer().setNoDamageTicks(i);
    }

    public TNLPlayer getKiller() {
        if (getBukkitPlayer().getKiller() != null) {
            return TNLPlayer.cast(getBukkitPlayer().getKiller());
        }
        return null;
    }

    public boolean addPotionEffect(PotionEffect potionEffect) {
        return getBukkitPlayer().addPotionEffect(potionEffect);
    }

    public boolean addPotionEffects(Collection<PotionEffect> collection) {
        return getBukkitPlayer().addPotionEffects(collection);
    }

    public boolean hasPotionEffect(PotionEffectType potionEffectType) {
        return getBukkitPlayer().hasPotionEffect(potionEffectType);
    }

    public PotionEffect getPotionEffect(PotionEffectType potionEffectType) {
        return getBukkitPlayer().getPotionEffect(potionEffectType);
    }

    public void removePotionEffect(PotionEffectType potionEffectType) {
        getBukkitPlayer().removePotionEffect(potionEffectType);
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        return getBukkitPlayer().getActivePotionEffects();
    }

    public boolean hasLineOfSight(Entity entity) {
        return getBukkitPlayer().hasLineOfSight(entity);
    }

    public boolean getRemoveWhenFarAway() {
        return getBukkitPlayer().getRemoveWhenFarAway();
    }

    public void setRemoveWhenFarAway(boolean b) {
        getBukkitPlayer().setRemoveWhenFarAway(b);
    }

    public EntityEquipment getEquipment() {
        return getBukkitPlayer().getEquipment();
    }

    public void setCanPickupItems(boolean b) {
        getBukkitPlayer().setCanPickupItems(b);
    }

    public boolean getCanPickupItems() {
        return getBukkitPlayer().getCanPickupItems();
    }

    public boolean isLeashed() {
        return getBukkitPlayer().isLeashed();
    }

    public Entity getLeashHolder() throws IllegalStateException {
        return getBukkitPlayer().getLeashHolder();
    }

    public boolean setLeashHolder(Entity entity) {
        return getBukkitPlayer().setLeashHolder(entity);
    }

    public boolean isGliding() {
        return getBukkitPlayer().isGliding();
    }

    public void setGliding(boolean b) {
        getBukkitPlayer().setGliding(b);
    }

    public boolean isSwimming() {
        return getBukkitPlayer().isSwimming();
    }

    public void setSwimming(boolean b) {
        getBukkitPlayer().setSwimming(b);
    }

    public boolean isRiptiding() {
        return getBukkitPlayer().isRiptiding();
    }

    public boolean isSleeping() {
        return getBukkitPlayer().isSleeping();
    }

    public void setAI(boolean b) {
        getBukkitPlayer().setAI(b);
    }

    public boolean hasAI() {
        return getBukkitPlayer().hasAI();
    }

    public void attack(Entity entity) {
        getBukkitPlayer().attack(entity);
    }

    public void swingMainHand() {
        getBukkitPlayer().swingMainHand();
    }

    public void swingOffHand() {
        getBukkitPlayer().swingOffHand();
    }

    public void setCollidable(boolean b) {
        getBukkitPlayer().setCollidable(b);
    }

    public boolean isCollidable() {
        return getBukkitPlayer().isCollidable();
    }

    public <T> T getMemory(MemoryKey<T> memoryKey) {
        return getBukkitPlayer().getMemory(memoryKey);
    }

    public <T> void setMemory(MemoryKey<T> memoryKey, T t) {
        getBukkitPlayer().setMemory(memoryKey, t);
    }

    public AttributeInstance getAttribute(Attribute attribute) {
        return getBukkitPlayer().getAttribute(attribute);
    }

    public void damage(double v) {
        getBukkitPlayer().damage(v);
    }

    public void damage(double v, Entity entity) {
        getBukkitPlayer().damage(v, entity);
    }

    public double getHealth() {
        return getBukkitPlayer().getHealth();
    }

    public void setHealth(double v) {
        getBukkitPlayer().setHealth(v);
    }

    public double getAbsorptionAmount() {
        return getBukkitPlayer().getAbsorptionAmount();
    }

    public void setAbsorptionAmount(double v) {
        getBukkitPlayer().setAbsorptionAmount(v);
    }

    public String getCustomName() {
        return getBukkitPlayer().getCustomName();
    }

    public void setCustomName(String s) {
        getBukkitPlayer().setCustomName(s);
    }

    public void setMetadata(String s, MetadataValue metadataValue) {
        getBukkitPlayer().setMetadata(s, metadataValue);
    }

    public List<MetadataValue> getMetadata(String s) {
        return getBukkitPlayer().getMetadata(s);
    }

    public boolean hasMetadata(String s) {
        return getBukkitPlayer().hasMetadata(s);
    }

    public void removeMetadata(String s, Plugin plugin) {
        getBukkitPlayer().removeMetadata(s, plugin);
    }

    public void sendPluginMessage(Plugin plugin, String s, byte[] bytes) {
        getBukkitPlayer().sendPluginMessage(plugin, s, bytes);
    }

    public Set<String> getListeningPluginChannels() {
        return getBukkitPlayer().getListeningPluginChannels();
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass) {
        return getBukkitPlayer().launchProjectile(aClass);
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass, Vector vector) {
        return getBukkitPlayer().launchProjectile(aClass, vector);
    }

    @Override
    public String toString() {
        return "TNLPlayer{" +
                "bukkitPlayer=" + bukkitPlayer +
                ", tnlOptionPacketScoreboard=" + tnlOptionPacketScoreboard +
                ", tnlOptionPacketTeam=" + tnlOptionPacketTeam +
                ", virtualStorage=" + virtualStorage +
                ", permissionManager=" + permissionManager +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TNLPlayer player = (TNLPlayer) o;
        return bukkitPlayer.equals(player.bukkitPlayer) && tnlOptionPacketScoreboard.equals(player.tnlOptionPacketScoreboard) && tnlOptionPacketTeam.equals(player.tnlOptionPacketTeam) && virtualStorage.equals(player.virtualStorage) && permissionManager.equals(player.permissionManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bukkitPlayer, tnlOptionPacketScoreboard, tnlOptionPacketTeam, virtualStorage, permissionManager);
    }
}
