package net.nonswag.tnl.listener.v1_15_R1.api.player;

import com.sun.istack.internal.NotNull;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_15_R1.*;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.api.actionbar.ActionBar;
import net.nonswag.tnl.listener.api.permission.Permissions;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.title.Title;
import net.nonswag.tnl.listener.enumerations.ProtocolVersion;
import net.nonswag.tnl.listener.v1_15_R1.TNLListener;
import net.nonswag.tnl.listener.v1_15_R1.api.bossbar.BossBar;
import net.nonswag.tnl.listener.v1_15_R1.api.scoreboard.Sidebar;
import net.nonswag.tnl.listener.v1_15_R1.eventhandler.InventoryLoadedEvent;
import net.nonswag.tnl.listener.v1_15_R1.eventhandler.InventorySafeEvent;
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
        return getPlayer().isOnline();
    }

    public boolean isBanned() {
        return getPlayer().isBanned();
    }

    public boolean isWhitelisted() {
        return getPlayer().isWhitelisted();
    }

    public void setWhitelisted(boolean b) {
        getPlayer().setWhitelisted(b);
    }

    @Nonnull
    public Player getPlayer() {
        return player;
    }

    public long getFirstPlayed() {
        return getPlayer().getFirstPlayed();
    }

    public long getLastPlayed() {
        return getPlayer().getLastPlayed();
    }

    public boolean hasPlayedBefore() {
        return getPlayer().hasPlayedBefore();
    }

    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
        getPlayer().incrementStatistic(statistic);
    }

    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
        getPlayer().decrementStatistic(statistic);
    }

    public void incrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {
        getPlayer().incrementStatistic(statistic, i);
    }

    public void decrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {
        getPlayer().decrementStatistic(statistic, i);
    }

    public void setStatistic(Statistic statistic, int i) throws IllegalArgumentException {
        getPlayer().setStatistic(statistic, i);
    }

    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
        return getPlayer().getStatistic(statistic);
    }

    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        getPlayer().incrementStatistic(statistic, material);
    }

    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        getPlayer().decrementStatistic(statistic, material);
    }

    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        return getPlayer().getStatistic(statistic, material);
    }

    public void incrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
        getPlayer().incrementStatistic(statistic, material, i);
    }

    public void decrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
        getPlayer().decrementStatistic(statistic, material, i);
    }

    public void setStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
        getPlayer().setStatistic(statistic, material, i);
    }

    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        getPlayer().incrementStatistic(statistic, entityType);
    }

    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        getPlayer().decrementStatistic(statistic, entityType);
    }

    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        return getPlayer().getStatistic(statistic, entityType);
    }

    public void incrementStatistic(Statistic statistic, EntityType entityType, int i) throws IllegalArgumentException {
        getPlayer().incrementStatistic(statistic, entityType, i);
    }

    public void decrementStatistic(Statistic statistic, EntityType entityType, int i) {
        getPlayer().decrementStatistic(statistic, entityType, i);
    }

    public void setStatistic(Statistic statistic, EntityType entityType, int i) {
        getPlayer().setStatistic(statistic, entityType, i);
    }

    public void sendPacket(@Nonnull Packet<?> packet) {
        if (Bukkit.isPrimaryThread()) {
            getPlayerConnection().sendPacket(packet);
        } else {
            NMSMain.runTask(() -> getPlayerConnection().sendPacket(packet));
        }
    }

    public void sendPackets(@Nonnull Packet<?>... packets) {
        for (Packet<?> packet : packets) {
            sendPacket(packet);
        }
    }

    public double getMaxHealth() {
        return getEntityPlayer().getMaxHealth();
    }

    public void sendMessage(String s) {
        getPlayer().sendMessage(s);
    }

    public void sendMessage(String[] strings) {
        getPlayer().sendMessage(strings);
    }

    public void disconnect() {
        disconnect(NMSMain.getPrefix() + "\n§cDisconnected");
    }

    public void disconnect(String kickMessage) {
        if (Bukkit.isPrimaryThread()) {
            if (!getPlayerConnection().processedDisconnect) {
                getPlayerConnection().disconnect(kickMessage);
            }
        } else {
            NMSMain.runTask(() -> {
                if (!getPlayerConnection().processedDisconnect) {
                    getPlayerConnection().disconnect(kickMessage);
                }
            });
        }
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
        return getPlayer().getName();
    }

    public PlayerInventory getInventory() {
        return getPlayer().getInventory();
    }

    public Inventory getEnderChest() {
        return getPlayer().getEnderChest();
    }

    public MainHand getMainHand() {
        return getPlayer().getMainHand();
    }

    public boolean setWindowProperty(InventoryView.Property property, int i) {
        return getPlayer().setWindowProperty(property, i);
    }

    public InventoryView getOpenInventory() {
        return getPlayer().getOpenInventory();
    }

    public InventoryView openInventory(Inventory inventory) {
        return getPlayer().openInventory(inventory);
    }

    public InventoryView openWorkbench(Location location, boolean b) {
        return getPlayer().openWorkbench(location, b);
    }

    public InventoryView openEnchanting(Location location, boolean b) {
        return getPlayer().openEnchanting(location, b);
    }

    public void openInventory(InventoryView inventoryView) {
        getPlayer().openInventory(inventoryView);
    }

    public InventoryView openMerchant(Villager villager, boolean b) {
        return getPlayer().openMerchant(villager, b);
    }

    public InventoryView openMerchant(Merchant merchant, boolean b) {
        return getPlayer().openMerchant(merchant, b);
    }

    public void closeInventory() {
        getPlayer().closeInventory();
    }

    public void setItemOnCursor(ItemStack itemStack) {
        getPlayer().setItemOnCursor(itemStack);
    }

    public int getCooldown(Material material) {
        return getPlayer().getCooldown(material);
    }

    public void setCooldown(Material material, int i) {
        getPlayer().setCooldown(material, i);
    }

    public int getSleepTicks() {
        return getPlayer().getSleepTicks();
    }

    public Location getBedSpawnLocation() {
        return getPlayer().getBedSpawnLocation();
    }

    public void setBedSpawnLocation(Location location) {
        getPlayer().setBedSpawnLocation(location);
    }

    public void setBedSpawnLocation(Location location, boolean b) {
        getPlayer().setBedSpawnLocation(location, b);
    }

    public boolean sleep(Location location, boolean b) {
        return getPlayer().sleep(location, b);
    }

    public void wakeup(boolean b) {
        getPlayer().wakeup(b);
    }

    public Location getBedLocation() {
        return getPlayer().getBedLocation();
    }

    public GameMode getGameMode() {
        return getPlayer().getGameMode();
    }

    public void setGameMode(GameMode gameMode) {
        getPlayer().setGameMode(gameMode);
    }

    public boolean isBlocking() {
        return getPlayer().isBlocking();
    }

    public boolean isHandRaised() {
        return getPlayer().isHandRaised();
    }

    public int getExpToLevel() {
        return getPlayer().getExpToLevel();
    }

    public float getAttackCooldown() {
        return getPlayer().getAttackCooldown();
    }

    public boolean discoverRecipe(NamespacedKey namespacedKey) {
        return getPlayer().discoverRecipe(namespacedKey);
    }

    public int discoverRecipes(Collection<NamespacedKey> collection) {
        return getPlayer().discoverRecipes(collection);
    }

    public boolean undiscoverRecipe(NamespacedKey namespacedKey) {
        return getPlayer().undiscoverRecipe(namespacedKey);
    }

    public int undiscoverRecipes(Collection<NamespacedKey> collection) {
        return getPlayer().undiscoverRecipes(collection);
    }

    public void saveInventory(@NotNull String id) {
        mkdirInventories();
        File file = new File("plugins/TNLListener/" + getPlayer().getUniqueId() + ".yml");
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    NMSMain.stacktrace(new NoSuchFileException("Failed to create file"));
                }
            }
            if (file.exists()) {
                YamlConfiguration inventory = YamlConfiguration.loadConfiguration(file);
                ItemStack[] contents = getPlayer().getInventory().getContents();
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

    public void disguise(@Nonnull net.minecraft.server.v1_15_R1.EntityLiving entity, List<TNLPlayer> receivers) {
        for (TNLPlayer receiver : receivers) {
            disguise(entity, receiver);
        }
    }

    public void disguise(@Nonnull net.minecraft.server.v1_15_R1.EntityLiving entity, TNLPlayer receiver) {
        if (!this.equals(receiver)) {
            receiver.sendPacket(new PacketPlayOutEntityDestroy(this.getEntityId()));
            entity.setLocation(getLocation().getX(), getLocation().getY(), getLocation().getZ(), getLocation().getYaw(), getLocation().getPitch());
            entity.world = this.getWorldServer();
            Reflection.setField(entity, net.minecraft.server.v1_15_R1.Entity.class, "id", this.getEntityId());
            receiver.sendPacket(new PacketPlayOutSpawnEntityLiving(entity));
        }
    }

    public void disguise(@Nonnull net.minecraft.server.v1_15_R1.EntityLiving entity) {
        disguise(entity, TNLListener.getOnlinePlayers());
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

    public void sendBossBar(@Nonnull BossBar bossBar) {
        if (!getBossBars(getUniqueId()).contains(bossBar.getId())) {
            List<String> bars = getBossBars(getUniqueId());
            bars.add(bossBar.getId());
            getBossBars().put(getUniqueId(), bars);
        }
        sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.ADD, bossBar.getBossBar().getHandle()));
        updateBossBar(bossBar);
    }

    public void updateBossBar(@Nonnull BossBar bossBar) {
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

    public void hideBossBar(@Nonnull BossBar bossBar) {
        sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.REMOVE, bossBar.getBossBar().getHandle()));
        List<String> bars = getBossBars(getUniqueId());
        bars.remove(bossBar.getId());
        getBossBars().put(getUniqueId(), bars);
        BossBar.removeBossBar(bossBar.getId());
    }

    public void sendBossBar(@Nonnull BossBar bossBar, long millis) {
        if (getBossHashMap().get(getPlayer().getUniqueId()) != null) {
            hideBossBar(getBossHashMap().get(getPlayer().getUniqueId()));
        }
        getBossHashMap().put(getPlayer().getUniqueId(), bossBar);
        sendBossBar(bossBar);
        new Thread(() -> {
            try {
                Thread.sleep(millis);
            } catch (Throwable ignored) {
            }
            if (getBossHashMap().get(getPlayer().getUniqueId()) != null
                    && getBossHashMap().get(getPlayer().getUniqueId()).equals(bossBar)) {
                hideBossBar(getBossHashMap().get(getPlayer().getUniqueId()));
            }
        }).start();
    }

    public void sendTitle(@Nonnull Title title) {
        sendTitle(title.getTitle(),
                title.getSubtitle(),
                title.getTimeIn(),
                title.getTimeStay(),
                title.getTimeOut());
    }

    public void sendTitle(@Nonnull Title.Animation animation) {
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
                System.out.println(String.join(spaces, split));
            } catch (Throwable t) {
                NMSMain.stacktrace(t);
            }
        }).start();
    }

    public void sendTitle(@Nullable String title,
                          @Nonnull String subTitle,
                          int timeIn,
                          int timeStay,
                          int timeOut) {
        getPlayer().sendTitle(title, subTitle, timeIn, timeStay, timeOut);
    }

    public void sendActionBar(@Nonnull ActionBar actionBar) {
        sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + actionBar.getText() + "\"}"), ChatMessageType.a((byte) 2)));
    }

    public CraftPlayer getCraftPlayer() {
        return ((CraftPlayer) getPlayer());
    }

    public EntityPlayer getEntityPlayer() {
        return getCraftPlayer().getHandle();
    }

    public void bungeeConnect(@Nonnull net.nonswag.tnl.listener.api.server.Server server) {
        try {
            if (server.isOnline()) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                dataOutputStream.writeUTF("Connect");
                dataOutputStream.writeUTF(server.getName());
                getPlayer().sendPluginMessage(NMSMain.getPlugin(), "BungeeCord", byteArrayOutputStream.toByteArray());
                getPlayer().sendMessage(NMSMain.getPrefix() + " §aConnecting you to server §6" + server.getName());
            } else {
                getPlayer().sendMessage(NMSMain.getPrefix() + " §cThe server §4" + server.getName() + "§c is Offline");
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
            getPlayer().sendMessage(NMSMain.getPrefix() + " §cFailed to connect you to server §4" + server.getName());
        }
    }

    public Sidebar getSidebar() {
        return Sidebar.getSidebar(player);
    }

    public void loadInventory(@NotNull String id) {
        File file = new File("plugins/TNLListener/" + getPlayer().getUniqueId() + ".yml");
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
        return getPlayer().getDisplayName();
    }

    public void setDisplayName(String s) {
        getPlayer().setDisplayName(s);
    }

    public String getPlayerListName() {
        return getPlayer().getPlayerListName();
    }

    public void setPlayerListName(String s) {
        getPlayer().setPlayerListName(s);
    }

    public String getPlayerListHeader() {
        return getPlayer().getPlayerListHeader();
    }

    public String getPlayerListFooter() {
        return getPlayer().getPlayerListFooter();
    }

    public void setPlayerListHeader(String s) {
        getPlayer().setPlayerListHeader(s);
    }

    public void setPlayerListFooter(String s) {
        getPlayer().setPlayerListFooter(s);
    }

    public void setPlayerListHeaderFooter(String s, String s1) {
        getPlayer().setPlayerListHeaderFooter(s, s1);
    }

    public void setCompassTarget(Location location) {
        getPlayer().setCompassTarget(location);
    }

    public Location getCompassTarget() {
        return getPlayer().getCompassTarget();
    }

    public InetSocketAddress getAddress() {
        return getPlayer().getAddress();
    }

    public boolean isConversing() {
        return getPlayer().isConversing();
    }

    public void acceptConversationInput(String s) {
        getPlayer().acceptConversationInput(s);
    }

    public boolean beginConversation(Conversation conversation) {
        return getPlayer().beginConversation(conversation);
    }

    public void abandonConversation(Conversation conversation) {
        getPlayer().abandonConversation(conversation);
    }

    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent conversationAbandonedEvent) {
        getPlayer().abandonConversation(conversation, conversationAbandonedEvent);
    }

    public void sendRawMessage(String s) {
        getPlayer().sendRawMessage(s);
    }

    public void sendRawMessage(String s, ChatMessageType chatMessageType) {
        sendPacket(new PacketPlayOutChat(new ChatMessage(s, chatMessageType)));
    }

    public void kickPlayer(String s) {
        getPlayer().kickPlayer(s);
    }

    public void chat(String s) {
        getPlayer().chat(s);
    }

    public boolean performCommand(String s) {
        return getPlayer().performCommand(s);
    }

    public boolean isSneaking() {
        return getPlayer().isSneaking();
    }

    public void setSneaking(boolean b) {
        getPlayer().setSneaking(b);
    }

    public boolean isSprinting() {
        return getPlayer().isSprinting();
    }

    public void setSprinting(boolean b) {
        getPlayer().setSprinting(b);
    }

    public void saveData() {
        getPlayer().saveData();
    }

    public void loadData() {
        getPlayer().loadData();
    }

    public void setSleepingIgnored(boolean b) {
        getPlayer().setSleepingIgnored(b);
    }

    public boolean isSleepingIgnored() {
        return getPlayer().isSleepingIgnored();
    }

    public void playNote(Location location, Instrument instrument, Note note) {
        getPlayer().playNote(location, instrument, note);
    }

    public void playSound(Location location, Sound sound, float v, float v1) {
        getPlayer().playSound(location, sound, v, v1);
    }

    public void playSound(Location location, String s, float v, float v1) {
        getPlayer().playSound(location, s, v, v1);
    }

    public void playSound(Location location, Sound sound, SoundCategory soundCategory, float v, float v1) {
        getPlayer().playSound(location, sound, soundCategory, v, v1);
    }

    public void playSound(Location location, String s, SoundCategory soundCategory, float v, float v1) {
        getPlayer().playSound(location, s, soundCategory, v, v1);
    }

    public void stopSound(Sound sound) {
        getPlayer().stopSound(sound);
    }

    public void stopSound(String s) {
        getPlayer().stopSound(s);
    }

    public void stopSound(Sound sound, SoundCategory soundCategory) {
        getPlayer().stopSound(sound, soundCategory);
    }

    public void stopSound(String s, SoundCategory soundCategory) {
        getPlayer().stopSound(s, soundCategory);
    }

    public <T> void playEffect(Location location, Effect effect, T t) {
        getPlayer().playEffect(location, effect, t);
    }

    public void sendBlockChange(Location location, BlockData blockData) {
        getPlayer().sendBlockChange(location, blockData);
    }

    public void sendSignChange(Location location, String[] strings) throws IllegalArgumentException {
        getPlayer().sendSignChange(location, strings);
    }

    public void sendSignChange(Location location, String[] strings, DyeColor dyeColor) throws IllegalArgumentException {
        getPlayer().sendSignChange(location, strings, dyeColor);
    }

    public void sendMap(MapView mapView) {
        getPlayer().sendMap(mapView);
    }

    public void updateInventory() {
        getPlayer().updateInventory();
    }

    public void setPlayerTime(long l, boolean b) {
        getPlayer().setPlayerTime(l, b);
    }

    public long getPlayerTime() {
        return getPlayer().getPlayerTime();
    }

    public long getPlayerTimeOffset() {
        return getPlayer().getPlayerTimeOffset();
    }

    public boolean isPlayerTimeRelative() {
        return getPlayer().isPlayerTimeRelative();
    }

    public void resetPlayerTime() {
        getPlayer().resetPlayerTime();
    }

    public void setPlayerWeather(WeatherType weatherType) {
        getPlayer().setPlayerWeather(weatherType);
    }

    public WeatherType getPlayerWeather() {
        return getPlayer().getPlayerWeather();
    }

    public void resetPlayerWeather() {
        getPlayer().resetPlayerWeather();
    }

    public void giveExp(int i) {
        getPlayer().giveExp(i);
    }

    public void giveExpLevels(int i) {
        getPlayer().giveExpLevels(i);
    }

    public float getExp() {
        return getPlayer().getExp();
    }

    public void setExp(float v) {
        getPlayer().setExp(v);
    }

    public int getLevel() {
        return getPlayer().getLevel();
    }

    public void setLevel(int i) {
        getPlayer().setLevel(i);
    }

    public int getTotalExperience() {
        return getPlayer().getTotalExperience();
    }

    public void setTotalExperience(int i) {
        getPlayer().setTotalExperience(i);
    }

    public void sendExperienceChange(float v) {
        getPlayer().sendExperienceChange(v);
    }

    public void sendExperienceChange(float v, int i) {
        getPlayer().sendExperienceChange(v, i);
    }

    public float getExhaustion() {
        return getPlayer().getExhaustion();
    }

    public void setExhaustion(float v) {
        getPlayer().setExhaustion(v);
    }

    public float getSaturation() {
        return getPlayer().getSaturation();
    }

    public void setSaturation(float v) {
        getPlayer().setSaturation(v);
    }

    public int getFoodLevel() {
        return getPlayer().getFoodLevel();
    }

    public void setFoodLevel(int i) {
        getPlayer().setFoodLevel(i);
    }

    public boolean getAllowFlight() {
        return getPlayer().getAllowFlight();
    }

    public void setAllowFlight(boolean b) {
        getPlayer().setAllowFlight(b);
    }

    public void hidePlayer(@Nonnull TNLPlayer player) {
        hidePlayer(NMSMain.getPlugin(), player);
    }

    public void hidePlayer(@Nonnull Plugin plugin, @Nonnull TNLPlayer player) {
        this.getPlayer().hidePlayer(plugin, player.getPlayer());
    }

    public void showPlayer(@Nonnull TNLPlayer player) {
        showPlayer(NMSMain.getPlugin(), player);
    }

    public void showPlayer(@Nonnull Plugin plugin, @Nonnull TNLPlayer player) {
        this.getPlayer().showPlayer(plugin, player.getPlayer());
    }

    public boolean canSee(@Nonnull TNLPlayer player) {
        return this.getPlayer().canSee(player.getPlayer());
    }

    public boolean isFlying() {
        return getPlayer().isFlying();
    }

    public void setFlying(boolean b) {
        getPlayer().setFlying(b);
    }

    public void setFlySpeed(float v) throws IllegalArgumentException {
        getPlayer().setFlySpeed(v);
    }

    public void setWalkSpeed(float v) throws IllegalArgumentException {
        getPlayer().setWalkSpeed(v);
    }

    public float getFlySpeed() {
        return getPlayer().getFlySpeed();
    }

    public float getWalkSpeed() {
        return getPlayer().getWalkSpeed();
    }

    public void setResourcePack(String s) {
        getPlayer().setResourcePack(s);
    }

    public void setResourcePack(String s, byte[] bytes) {
        getPlayer().setResourcePack(s, bytes);
    }

    public org.bukkit.scoreboard.Scoreboard getScoreboard() {
        return getPlayer().getScoreboard();
    }

    public void setScoreboard(org.bukkit.scoreboard.Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        getPlayer().setScoreboard(scoreboard);
    }

    public boolean isHealthScaled() {
        return getPlayer().isHealthScaled();
    }

    public void setHealthScaled(boolean b) {
        getPlayer().setHealthScaled(b);
    }

    public void setHealthScale(double v) throws IllegalArgumentException {
        getPlayer().setHealthScale(v);
    }

    public double getHealthScale() {
        return getPlayer().getHealthScale();
    }

    public Entity getSpectatorTarget() {
        return getPlayer().getSpectatorTarget();
    }

    public void setSpectatorTarget(Entity entity) {
        getPlayer().setSpectatorTarget(entity);
    }

    public void resetTitle() {
        getPlayer().resetTitle();
    }

    public void spawnParticle(Particle particle, Location location, int i) {
        getPlayer().spawnParticle(particle, location, i);
    }

    public void spawnParticle(Particle particle, double v, double v1, double v2, int i) {
        getPlayer().spawnParticle(particle, v, v1, v2, i);
    }

    public <T> void spawnParticle(Particle particle, Location location, int i, T t) {
        getPlayer().spawnParticle(particle, location, i, t);
    }

    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, T t) {
        getPlayer().spawnParticle(particle, v, v1, v2, i, t);
    }

    public void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2) {
        getPlayer().spawnParticle(particle, location, i, v, v1, v2);
    }

    public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5) {
        getPlayer().spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
    }

    public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, T t) {
        getPlayer().spawnParticle(particle, location, i, v, v1, v2, t);
    }

    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, T t) {
        getPlayer().spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
    }

    public void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3) {
        getPlayer().spawnParticle(particle, location, i, v, v1, v2, v3);
    }

    public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6) {
        getPlayer().spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
    }

    public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3, T t) {
        getPlayer().spawnParticle(particle, location, i, v, v1, v2, v3, t);
    }

    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6, T t) {
        getPlayer().spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t);
    }

    public AdvancementProgress getAdvancementProgress(Advancement advancement) {
        return getPlayer().getAdvancementProgress(advancement);
    }

    public int getClientViewDistance() {
        return getPlayer().getClientViewDistance();
    }

    public String getLocale() {
        return getPlayer().getLocale();
    }

    public void updateCommands() {
        getPlayer().updateCommands();
    }

    public void openBook(ItemStack itemStack) {
        getPlayer().openBook(itemStack);
    }

    public Location getLocation() {
        return getPlayer().getLocation();
    }

    public Location getLocation(Location location) {
        return getPlayer().getLocation(location);
    }

    public Location getTargetLocation(int i) {
        return getLocation().clone().add(getPlayer().getLocation().getDirection().multiply(3));
    }

    public void setVelocity(Vector vector) {
        getPlayer().setVelocity(vector);
    }

    public Vector getVelocity() {
        return getPlayer().getVelocity();
    }

    public double getHeight() {
        return getPlayer().getHeight();
    }

    public double getWidth() {
        return getPlayer().getWidth();
    }

    public BoundingBox getBoundingBox() {
        return getPlayer().getBoundingBox();
    }

    public boolean isOnGround() {
        return getPlayer().isOnGround();
    }

    public World getWorld() {
        return getPlayer().getWorld();
    }

    public WorldServer getWorldServer() {
        return ((CraftWorld) getPlayer().getWorld()).getHandle();
    }

    public void setRotation(float v, float v1) {
        getPlayer().setRotation(v, v1);
    }

    public boolean teleport(Location location) {
        return getPlayer().teleport(location);
    }

    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
        return getPlayer().teleport(location, teleportCause);
    }

    public boolean teleport(Entity entity) {
        return getPlayer().teleport(entity);
    }

    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause teleportCause) {
        return getPlayer().teleport(entity, teleportCause);
    }

    public List<Entity> getNearbyEntities(double v, double v1, double v2) {
        return getPlayer().getNearbyEntities(v, v1, v2);
    }

    public int getEntityId() {
        return getPlayer().getEntityId();
    }

    public int getFireTicks() {
        return getPlayer().getFireTicks();
    }

    public int getMaxFireTicks() {
        return getPlayer().getMaxFireTicks();
    }

    public void setFireTicks(int i) {
        getPlayer().setFireTicks(i);
    }

    public void remove() {
        getPlayer().remove();
    }

    public boolean isDead() {
        return getPlayer().isDead();
    }

    public boolean isValid() {
        return getPlayer().isValid();
    }

    public Server getServer() {
        return getPlayer().getServer();
    }

    public List<Entity> getPassengers() {
        return getPlayer().getPassengers();
    }

    public boolean addPassenger(Entity entity) {
        return getPlayer().addPassenger(entity);
    }

    public boolean removePassenger(Entity entity) {
        return getPlayer().removePassenger(entity);
    }

    public boolean isEmpty() {
        return getPlayer().isEmpty();
    }

    public boolean eject() {
        return getPlayer().eject();
    }

    public float getFallDistance() {
        return getPlayer().getFallDistance();
    }

    public void setFallDistance(float v) {
        getPlayer().setFallDistance(v);
    }

    public void setLastDamageCause(EntityDamageEvent entityDamageEvent) {
        getPlayer().setLastDamageCause(entityDamageEvent);
    }

    public EntityDamageEvent getLastDamageCause() {
        return getPlayer().getLastDamageCause();
    }

    public UUID getUniqueId() {
        return getPlayer().getUniqueId();
    }

    public int getTicksLived() {
        return getPlayer().getTicksLived();
    }

    public void setTicksLived(int i) {
        getPlayer().setTicksLived(i);
    }

    public void playEffect(EntityEffect entityEffect) {
        getPlayer().playEffect(entityEffect);
    }

    public EntityType getType() {
        return getPlayer().getType();
    }

    public boolean isInsideVehicle() {
        return getPlayer().isInsideVehicle();
    }

    public boolean leaveVehicle() {
        return getPlayer().leaveVehicle();
    }

    public Entity getVehicle() {
        return getPlayer().getVehicle();
    }

    public ProtocolVersion getVersion() {
        return VersionCache.getVersion(this);
    }

    public void setCustomNameVisible(boolean b) {
        getPlayer().setCustomNameVisible(b);
    }

    public boolean isCustomNameVisible() {
        return getPlayer().isCustomNameVisible();
    }

    public void setGlowing(boolean b) {
        getPlayer().setGlowing(b);
    }

    public boolean isGlowing() {
        return getPlayer().isGlowing();
    }

    public void setInvulnerable(boolean b) {
        getPlayer().setInvulnerable(b);
    }

    public boolean isInvulnerable() {
        return getPlayer().isInvulnerable();
    }

    public boolean isSilent() {
        return getPlayer().isSilent();
    }

    public void setSilent(boolean b) {
        getPlayer().setSilent(b);
    }

    public boolean hasGravity() {
        return getPlayer().hasGravity();
    }

    public void setGravity(boolean b) {
        getPlayer().setGravity(b);
    }

    public int getPortalCooldown() {
        return getPlayer().getPortalCooldown();
    }

    public void setPortalCooldown(int i) {
        getPlayer().setPortalCooldown(i);
    }

    public Set<String> getScoreboardTags() {
        return getPlayer().getScoreboardTags();
    }

    public boolean addScoreboardTag(String s) {
        return getPlayer().addScoreboardTag(s);
    }

    public boolean removeScoreboardTag(String s) {
        return getPlayer().removeScoreboardTag(s);
    }

    public PistonMoveReaction getPistonMoveReaction() {
        return getPlayer().getPistonMoveReaction();
    }

    public BlockFace getFacing() {
        return getPlayer().getFacing();
    }

    public Pose getPose() {
        return getPlayer().getPose();
    }

    public BackFlip backflip() {
        return new BackFlip(this);
    }

    public Map<String, Object> serialize() {
        return getPlayer().serialize();
    }

    public double getEyeHeight() {
        return getPlayer().getEyeHeight();
    }

    public double getEyeHeight(boolean b) {
        return getPlayer().getEyeHeight(b);
    }

    public Location getEyeLocation() {
        return getPlayer().getEyeLocation();
    }

    public List<Block> getLineOfSight(Set<Material> set, int i) {
        return getPlayer().getLineOfSight(set, i);
    }

    public Block getTargetBlock(Set<Material> set, int i) {
        return getPlayer().getTargetBlock(set, i);
    }

    public List<Block> getLastTwoTargetBlocks(Set<Material> set, int i) {
        return getPlayer().getLastTwoTargetBlocks(set, i);
    }

    public Block getTargetBlockExact(int i) {
        return getPlayer().getTargetBlockExact(i);
    }

    public Block getTargetBlockExact(int i, FluidCollisionMode fluidCollisionMode) {
        return getPlayer().getTargetBlockExact(i, fluidCollisionMode);
    }

    public RayTraceResult rayTraceBlocks(double v) {
        return getPlayer().rayTraceBlocks(v);
    }

    public RayTraceResult rayTraceBlocks(double v, FluidCollisionMode fluidCollisionMode) {
        return getPlayer().rayTraceBlocks(v, fluidCollisionMode);
    }

    public int getRemainingAir() {
        return getPlayer().getRemainingAir();
    }

    public void setRemainingAir(int i) {
        getPlayer().setRemainingAir(i);
    }

    public int getMaximumAir() {
        return getPlayer().getMaximumAir();
    }

    public void setMaximumAir(int i) {
        getPlayer().setMaximumNoDamageTicks(i);
    }

    public int getMaximumNoDamageTicks() {
        return getPlayer().getMaximumNoDamageTicks();
    }

    public void setMaximumNoDamageTicks(int i) {
        getPlayer().setMaximumNoDamageTicks(i);
    }

    public double getLastDamage() {
        return getPlayer().getLastDamage();
    }

    public void setLastDamage(double v) {
        getPlayer().setLastDamage(v);
    }

    public int getNoDamageTicks() {
        return getPlayer().getNoDamageTicks();
    }

    public void setNoDamageTicks(int i) {
        getPlayer().setNoDamageTicks(i);
    }

    @Nullable
    public TNLPlayer getKiller() {
        if (getPlayer().getKiller() != null) {
            return TNLPlayer.cast(getPlayer().getKiller());
        }
        return null;
    }

    public boolean addPotionEffect(PotionEffect potionEffect) {
        return getPlayer().addPotionEffect(potionEffect);
    }

    public boolean addPotionEffects(Collection<PotionEffect> collection) {
        return getPlayer().addPotionEffects(collection);
    }

    public boolean hasPotionEffect(PotionEffectType potionEffectType) {
        return getPlayer().hasPotionEffect(potionEffectType);
    }

    public PotionEffect getPotionEffect(PotionEffectType potionEffectType) {
        return getPlayer().getPotionEffect(potionEffectType);
    }

    public void removePotionEffect(PotionEffectType potionEffectType) {
        getPlayer().removePotionEffect(potionEffectType);
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        return getPlayer().getActivePotionEffects();
    }

    public boolean hasLineOfSight(Entity entity) {
        return getPlayer().hasLineOfSight(entity);
    }

    public boolean getRemoveWhenFarAway() {
        return getPlayer().getRemoveWhenFarAway();
    }

    public void setRemoveWhenFarAway(boolean b) {
        getPlayer().setRemoveWhenFarAway(b);
    }

    public EntityEquipment getEquipment() {
        return getPlayer().getEquipment();
    }

    public void setCanPickupItems(boolean b) {
        getPlayer().setCanPickupItems(b);
    }

    public boolean getCanPickupItems() {
        return getPlayer().getCanPickupItems();
    }

    public boolean isLeashed() {
        return getPlayer().isLeashed();
    }

    public Entity getLeashHolder() throws IllegalStateException {
        return getPlayer().getLeashHolder();
    }

    public boolean setLeashHolder(Entity entity) {
        return getPlayer().setLeashHolder(entity);
    }

    public boolean isGliding() {
        return getPlayer().isGliding();
    }

    public void setGliding(boolean b) {
        getPlayer().setGliding(b);
    }

    public boolean isSwimming() {
        return getPlayer().isSwimming();
    }

    public void setSwimming(boolean b) {
        getPlayer().setSwimming(b);
    }

    public boolean isRiptiding() {
        return getPlayer().isRiptiding();
    }

    public boolean isSleeping() {
        return getPlayer().isSleeping();
    }

    public void setAI(boolean b) {
        getPlayer().setAI(b);
    }

    public boolean hasAI() {
        return getPlayer().hasAI();
    }

    public void attack(Entity entity) {
        getPlayer().attack(entity);
    }

    public void swingMainHand() {
        getPlayer().swingMainHand();
    }

    public void swingOffHand() {
        getPlayer().swingOffHand();
    }

    public void setCollidable(boolean b) {
        getPlayer().setCollidable(b);
    }

    public boolean isCollidable() {
        return getPlayer().isCollidable();
    }

    public <T> T getMemory(MemoryKey<T> memoryKey) {
        return getPlayer().getMemory(memoryKey);
    }

    public <T> void setMemory(MemoryKey<T> memoryKey, T t) {
        getPlayer().setMemory(memoryKey, t);
    }

    public AttributeInstance getAttribute(Attribute attribute) {
        return getPlayer().getAttribute(attribute);
    }

    public void damage(double v) {
        getPlayer().damage(v);
    }

    public void damage(double v, Entity entity) {
        getPlayer().damage(v, entity);
    }

    public double getHealth() {
        return getPlayer().getHealth();
    }

    public void setHealth(double v) {
        getPlayer().setHealth(v);
    }

    public double getAbsorptionAmount() {
        return getPlayer().getAbsorptionAmount();
    }

    public void setAbsorptionAmount(double v) {
        getPlayer().setAbsorptionAmount(v);
    }

    public String getCustomName() {
        return getPlayer().getCustomName();
    }

    public void setCustomName(String s) {
        getPlayer().setCustomName(s);
    }

    public void setMetadata(String s, MetadataValue metadataValue) {
        getPlayer().setMetadata(s, metadataValue);
    }

    public List<MetadataValue> getMetadata(String s) {
        return getPlayer().getMetadata(s);
    }

    public boolean hasMetadata(String s) {
        return getPlayer().hasMetadata(s);
    }

    public void removeMetadata(String s, Plugin plugin) {
        getPlayer().removeMetadata(s, plugin);
    }

    public boolean isPermissionSet(String s) {
        return getPlayer().isPermissionSet(s);
    }

    public boolean isPermissionSet(Permission permission) {
        return getPlayer().isPermissionSet(permission);
    }

    public boolean hasPermission(String s) {
        return getPlayer().hasPermission(s);
    }

    public boolean hasPermission(Permission permission) {
        return getPlayer().hasPermission(permission);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
        return getPlayer().addAttachment(plugin, s, b);
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        return getPlayer().addAttachment(plugin);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
        return getPlayer().addAttachment(plugin, s, b, i);
    }

    public PermissionAttachment addAttachment(Plugin plugin, int i) {
        return getPlayer().addAttachment(plugin, i);
    }

    public void removeAttachment(PermissionAttachment permissionAttachment) {
        getPlayer().removeAttachment(permissionAttachment);
    }

    public void recalculatePermissions() {
        getPlayer().recalculatePermissions();
    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return getPlayer().getEffectivePermissions();
    }

    public boolean isOp() {
        return getPlayer().isOp();
    }

    public void setOp(boolean b) {
        getPlayer().setOp(b);
    }

    public PersistentDataContainer getPersistentDataContainer() {
        return getPlayer().getPersistentDataContainer();
    }

    public void sendPluginMessage(Plugin plugin, String s, byte[] bytes) {
        getPlayer().sendPluginMessage(plugin, s, bytes);
    }

    public Set<String> getListeningPluginChannels() {
        return getPlayer().getListeningPluginChannels();
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass) {
        return getPlayer().launchProjectile(aClass);
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass, Vector vector) {
        return getPlayer().launchProjectile(aClass, vector);
    }

    @Override
    public String toString() {
        return "TNLPlayer{" +
                "player=" + player +
                ", tnlOptionPacketScoreboard=" + tnlOptionPacketScoreboard +
                ", tnlOptionPacketTeam=" + tnlOptionPacketTeam +
                ", virtualStorage=" + virtualStorage +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TNLPlayer player = (TNLPlayer) o;
        return getUniqueId().equals(player.getUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId());
    }
}
