package net.nonswag.tnl.listener.api.player.v1_15.R1;

import io.netty.channel.*;
import net.minecraft.server.v1_15_R1.*;
import net.nonswag.tnl.listener.Loader;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.actionbar.ActionBar;
import net.nonswag.tnl.listener.api.bossbar.BossBar;
import net.nonswag.tnl.listener.api.bossbar.v1_15.R1.NMSBossBar;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.message.*;
import net.nonswag.tnl.listener.api.permission.PermissionManager;
import net.nonswag.tnl.listener.api.player.BackFlip;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.sign.SignMenu;
import net.nonswag.tnl.listener.api.title.Title;
import net.nonswag.tnl.listener.events.InventoryLoadedEvent;
import net.nonswag.tnl.listener.events.InventorySafeEvent;
import net.nonswag.tnl.listener.events.PlayerPacketEvent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.InetSocketAddress;
import java.nio.file.NoSuchFileException;
import java.util.*;

public class NMSPlayer implements TNLPlayer {

    @Nonnull
    private static final HashMap<UUID, List<String>> bossBars = new HashMap<>();
    @Nonnull
    private static final HashMap<UUID, NMSBossBar> bossHashMap = new HashMap<>();

    @Nonnull
    private final Player bukkitPlayer;
    @Nonnull
    private final Scoreboard optionScoreboard = new Scoreboard();
    @Nonnull
    private final ScoreboardTeam optionTeam = new ScoreboardTeam(getOptionScoreboard(), "TNLOptionPacket");
    @Nonnull
    private final HashMap<String, Object> virtualStorage = new HashMap<>();
    @Nonnull
    private final PermissionManager permissionManager;

    protected NMSPlayer(@Nonnull Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        this.permissionManager = new PermissionManager(this);
        getOptionScoreboard().addPlayerToTeam(getName(), getOptionTeam());
    }

    @Nonnull
    public static NMSPlayer cast(@Nonnull Player player) {
        if (!TNLListener.getInstance().getPlayerHashMap().containsKey(player)) {
            TNLListener.getInstance().getPlayerHashMap().put(player, new NMSPlayer(player));
        }
        return (NMSPlayer) TNLListener.getInstance().getPlayerHashMap().get(player);
    }

    @Nonnull
    public static NMSPlayer cast(@Nonnull CraftPlayer craftPlayer) {
        return cast((Player) craftPlayer);
    }

    @Nullable
    public static NMSPlayer cast(@Nullable CommandSender sender) {
        if (sender instanceof Player) {
            return cast((Player) sender);
        }
        return null;
    }

    @Nullable
    public static NMSPlayer cast(@Nullable HumanEntity humanEntity) {
        if (humanEntity instanceof Player) {
            return cast((Player) humanEntity);
        }
        return null;
    }

    @Nullable
    public static NMSPlayer cast(@Nullable Entity entity) {
        if (entity instanceof Player) {
            return cast((Player) entity);
        }
        return null;
    }

    @Nullable
    public static NMSPlayer cast(@Nullable LivingEntity livingEntity) {
        if (livingEntity instanceof Player) {
            return cast((Player) livingEntity);
        }
        return null;
    }

    @Nullable
    public static NMSPlayer cast(@Nonnull String string) {
        Player player = Bukkit.getPlayer(string);
        if (player != null) {
            return cast(player);
        }
        return null;
    }

    @Nullable
    public static NMSPlayer cast(@Nullable Object object) {
        if (object instanceof Player) {
            return cast(((Player) object));
        }
        return null;
    }

    @Nonnull
    public static NMSPlayer cast(@Nonnull TNLPlayer player) {
        return ((NMSPlayer) player);
    }

    @Override
    @Nonnull
    public HashMap<String, Object> getVirtualStorage() {
        return virtualStorage;
    }

    @Override
    @Nonnull
    public Scoreboard getOptionScoreboard() {
        return optionScoreboard;
    }

    @Override
    @Nonnull
    public ScoreboardTeam getOptionTeam() {
        return optionTeam;
    }

    @Override
    @Nonnull
    public PlayerConnection getPlayerConnection() {
        return getEntityPlayer().playerConnection;
    }

    @Override
    @Nonnull
    public NetworkManager getNetworkManager() {
        return getPlayerConnection().networkManager;
    }

    @Override
    public int getPing() {
        return getEntityPlayer().ping;
    }

    @Override
    @Nonnull
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    @Override
    public boolean isOnline() {
        return getBukkitPlayer().isOnline();
    }

    @Override
    public boolean isBanned() {
        return getBukkitPlayer().isBanned();
    }

    @Override
    public boolean isWhitelisted() {
        return getBukkitPlayer().isWhitelisted();
    }

    @Override
    public void setWhitelisted(boolean whitelisted) {
        getBukkitPlayer().setWhitelisted(whitelisted);
    }

    @Override
    @Nonnull
    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    @Override
    public long getFirstPlayed() {
        return getBukkitPlayer().getFirstPlayed();
    }

    @Override
    public boolean hasPlayedBefore() {
        return getBukkitPlayer().hasPlayedBefore();
    }

    @Override
    public void sendPackets(@Nonnull Object... packets) {
        for (Object packet : packets) {
            sendPacket(packet);
        }
    }

    @Override
    public void sendPacket(@Nonnull Object packet) {
        if (Bukkit.isPrimaryThread()) {
            getPlayerConnection().sendPacket((Packet<?>) packet);
        } else {
            Bukkit.getScheduler().runTask(Loader.getInstance(), () -> getPlayerConnection().sendPacket((Packet<?>) packet));
        }
    }

    @Override
    public double getMaxHealth() {
        return getEntityPlayer().getMaxHealth();
    }

    @Override
    public void sendMessage(@Nonnull ChatComponent component) {
        sendMessage(component.getText());
    }

    @Override
    public void sendMessage(@Nonnull ChatComponent component, @Nonnull Placeholder... placeholders) {
        sendMessage(component.getText(placeholders));
    }

    @Override
    public void sendMessage(@Nonnull String message, @Nonnull Placeholder... placeholders) {
        getBukkitPlayer().sendMessage(ChatComponent.getText(message, placeholders));
    }

    @Override
    public void sendMessage(@Nonnull MessageKey messageKey, @Nonnull Placeholder... placeholders) {
        Language language = Language.ROOT;
        if (!messageKey.isSystemMessage()) {
            language = Language.fromLocale(getBukkitPlayer().getLocale());
        }
        LanguageKey languageKey = new LanguageKey(language, messageKey);
        ChatComponent component = Message.valueOf(languageKey);
        if (component != null) {
            getBukkitPlayer().sendMessage(component.getText(placeholders));
        } else {
            Logger.error.println("§cUnknown component§8: §4" + languageKey.getMessageKey().getKey() + " §8(§4" + languageKey.getLanguage().getName() + "§8)");
            sendMessage("%prefix% §cUnknown component§8: §4" + languageKey.getMessageKey() + " §8(§4" + languageKey.getLanguage().getName() + "§8)");
        }
    }

    @Override
    public void disconnect() {
        disconnect(MessageKey.KICKED + "\n§cDisconnected");
    }

    @Override
    public void disconnect(@Nonnull MessageKey messageKey, @Nonnull String append, @Nonnull Placeholder... placeholders) {
        Language language = Language.ROOT;
        if (!messageKey.isSystemMessage()) {
            language = Language.fromLocale(getBukkitPlayer().getLocale());
        }
        LanguageKey languageKey = new LanguageKey(language, messageKey);
        ChatComponent component = Message.valueOf(languageKey);
        if (component != null) {
            disconnect(component.getText(placeholders) + append);
        } else {
            Logger.error.println("§cUnknown component§8: §4" + languageKey.toString());
            disconnect("§cUnknown component§8: §4" + languageKey.toString());
        }
    }

    @Override
    public void disconnect(@Nonnull MessageKey messageKey, @Nonnull Placeholder... placeholders) {
        disconnect(messageKey, "", placeholders);
    }

    @Override
    public void disconnect(@Nonnull String kickMessage) {
        if (Bukkit.isPrimaryThread()) {
            if (!getPlayerConnection().processedDisconnect) {
                getPlayerConnection().disconnect(kickMessage);
            }
        } else {
            Bukkit.getScheduler().runTask(Loader.getInstance(), () -> {
                if (!getPlayerConnection().processedDisconnect) {
                    getPlayerConnection().disconnect(kickMessage);
                }
            });
        }
    }

    @Override
    public String getWorldAlias() {
        return getWorldAlias(getWorld());
    }

    @Override
    public <EnumTeamPush> void setCollision(@Nonnull EnumTeamPush collision) {
        getOptionTeam().setCollisionRule((ScoreboardTeamBase.EnumTeamPush) collision);
        sendPacket(new PacketPlayOutScoreboardTeam(getOptionTeam(), 0));
    }

    @Nonnull
    public static String getWorldAlias(@Nonnull World world) {
        return TNLListener.getInstance().getWorldAliasHashMap().getOrDefault(world, world.getName());
    }

    public static void setWorldAlias(@Nonnull World world, @Nonnull String alias) {
        TNLListener.getInstance().getWorldAliasHashMap().put(world, alias);
    }

    @Override
    @Nonnull
    public String getName() {
        return getBukkitPlayer().getName();
    }

    @Override
    @Nonnull
    public PlayerInventory getInventory() {
        return getBukkitPlayer().getInventory();
    }

    @Override
    @Nonnull
    public Inventory getEnderChest() {
        return getBukkitPlayer().getEnderChest();
    }

    @Override
    public boolean setWindowProperty(@Nonnull InventoryView.Property property, int i) {
        return getBukkitPlayer().setWindowProperty(property, i);
    }

    @Override
    @Nullable
    public InventoryView getOpenInventory() {
        return getBukkitPlayer().getOpenInventory();
    }

    @Override
    @Nullable
    public InventoryView openInventory(@Nonnull Inventory inventory) {
        return getBukkitPlayer().openInventory(inventory);
    }

    @Override
    @Nullable
    public InventoryView openWorkbench(@Nonnull Location location, boolean b) {
        return getBukkitPlayer().openWorkbench(location, b);
    }

    @Override
    @Nullable
    public InventoryView openEnchanting(@Nonnull Location location, boolean b) {
        return getBukkitPlayer().openEnchanting(location, b);
    }

    @Override
    public void openSignEditor(@Nonnull Location location) {
        if (location.getBlock().getBlockData() instanceof org.bukkit.block.data.type.Sign) {
            sendPacket(new PacketPlayOutOpenSignEditor(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ())));
        }
    }

    @Override
    public void openVirtualSignEditor(@Nonnull SignMenu signMenu) {
        BlockPosition position = new BlockPosition(signMenu.getLocation().getBlockX(), signMenu.getLocation().getBlockY(), signMenu.getLocation().getBlockZ());
        PacketPlayOutOpenSignEditor editor = new PacketPlayOutOpenSignEditor(position);
        TileEntitySign tileEntitySign = new TileEntitySign();
        tileEntitySign.setLocation(getWorldServer(), position);
        for (int line = 0; line < 4; line++) {
            if (signMenu.getLines().length >= (line + 1)) {
                tileEntitySign.lines[line] = new ChatMessage(signMenu.getLines()[line]);
            }
        }
        getBukkitPlayer().sendBlockChange(signMenu.getLocation(), Material.SPRUCE_SIGN.createBlockData());
        PacketPlayOutTileEntityData updatePacket = tileEntitySign.getUpdatePacket();
        assert updatePacket != null;
        sendPacket(updatePacket);
        sendPacket(editor);
        TNLListener.getInstance().getSignHashMap().put(getUniqueId(), signMenu);
    }

    @Override
    public void openInventory(@Nonnull InventoryView inventoryView) {
        getBukkitPlayer().openInventory(inventoryView);
    }

    @Override
    public void closeInventory() {
        getBukkitPlayer().closeInventory();
    }

    @Override
    public void setItemOnCursor(@Nonnull ItemStack itemStack) {
        getBukkitPlayer().setItemOnCursor(itemStack);
    }

    @Override
    public int getCooldown(@Nonnull Material material) {
        return getBukkitPlayer().getCooldown(material);
    }

    @Override
    public void setCooldown(@Nonnull Material material, int i) {
        if (Bukkit.isPrimaryThread()) {
            getCraftPlayer().setCooldown(material, i);
        } else {
            Bukkit.getScheduler().runTask(Loader.getInstance(), () -> getCraftPlayer().setCooldown(material, i));
        }
    }

    @Override
    public void setCooldown(@Nonnull ItemStack itemStack, int i) {
        sendPacket(new PacketPlayOutSetCooldown(CraftItemStack.asNMSCopy(itemStack).getItem(), i));
    }

    @Override
    public int getSleepTicks() {
        return getBukkitPlayer().getSleepTicks();
    }

    @Override
    @Nullable
    public Location getBedSpawnLocation() {
        return getBukkitPlayer().getBedSpawnLocation();
    }

    @Override
    public void setBedSpawnLocation(@Nonnull Location location) {
        getBukkitPlayer().setBedSpawnLocation(location);
    }

    @Override
    public void setBedSpawnLocation(@Nonnull Location location, boolean b) {
        getBukkitPlayer().setBedSpawnLocation(location, b);
    }

    @Override
    public boolean sleep(@Nonnull Location location, boolean b) {
        return getBukkitPlayer().sleep(location, b);
    }

    @Override
    public void wakeup(boolean b) {
        getBukkitPlayer().wakeup(b);
    }

    @Override
    @Nonnull
    public Location getBedLocation() {
        return getBukkitPlayer().getBedLocation();
    }

    @Nonnull
    @Override
    public GameMode getGameMode() {
        return getCraftPlayer().getGameMode();
    }

    @Override
    public void setGameMode(@Nonnull GameMode gamemode) {
        getCraftPlayer().setGameMode(gamemode);
    }

    @Override
    public boolean isBlocking() {
        return getBukkitPlayer().isBlocking();
    }

    @Override
    public boolean isHandRaised() {
        return getBukkitPlayer().isHandRaised();
    }

    @Override
    public int getExpToLevel() {
        return getBukkitPlayer().getExpToLevel();
    }

    @Override
    public float getAttackCooldown() {
        return getBukkitPlayer().getAttackCooldown();
    }

    @Override
    public void saveInventory(@Nonnull String id) {
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

    @Override
    public void hideTabListName(@Nonnull TNLPlayer[] players) {
        for (TNLPlayer all : players) {
            if (!all.getUniqueId().equals(getUniqueId())) {
                sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((NMSPlayer) all).getEntityPlayer()));
            }
        }
    }

    @Nonnull
    @Override
    public void disguise(@Nonnull net.nonswag.tnl.listener.api.entity.LivingEntity<?> entity) {
        disguise(entity, TNLListener.getInstance().getOnlinePlayers());
    }

    @Nonnull
    @Override
    public void disguise(@Nonnull net.nonswag.tnl.listener.api.entity.LivingEntity<?> entity, @Nonnull List<TNLPlayer> receivers) {
        for (TNLPlayer receiver : receivers) {
            disguise(entity, receiver);
        }
    }

    @Nonnull
    @Override
    public void disguise(@Nonnull net.nonswag.tnl.listener.api.entity.LivingEntity<?> entity, @Nonnull TNLPlayer receiver) {
        if (!receiver.getUniqueId().equals(getUniqueId()) && entity.getParameter() instanceof EntityLiving) {
            receiver.sendPacket(new PacketPlayOutEntityDestroy(this.getEntityId()));
            ((EntityLiving) entity.getParameter()).setLocation(getLocation().getX(), getLocation().getY(), getLocation().getZ(), getLocation().getYaw(), getLocation().getPitch());
            ((EntityLiving) entity.getParameter()).world = this.getWorldServer();
            Reflection.setField(entity, net.minecraft.server.v1_15_R1.Entity.class, "id", this.getEntityId());
            receiver.sendPacket(new PacketPlayOutSpawnEntityLiving(((EntityLiving) entity.getParameter())));
        }
    }

    @Nonnull
    private static HashMap<UUID, NMSBossBar> getBossHashMap() {
        return bossHashMap;
    }

    @Nonnull
    private static HashMap<UUID, List<String>> getBossBars() {
        return bossBars;
    }

    @Nonnull
    public static List<String> getBossBars(@Nonnull UUID uniqueId) {
        return bossBars.getOrDefault(uniqueId, new ArrayList<>());
    }

    @Nonnull
    @Override
    public void sendBossBar(@Nonnull BossBar<?> bossBar, long millis) {
        NMSBossBar.createBossBar(bossBar.getId(), ((NMSBossBar) bossBar));
        if (getBossHashMap().get(getBukkitPlayer().getUniqueId()) != null) {
            hideBossBar(getBossHashMap().get(getBukkitPlayer().getUniqueId()));
        }
        getBossHashMap().put(getBukkitPlayer().getUniqueId(), (NMSBossBar) bossBar);
        sendBossBar(bossBar);
        new Thread(() -> {
            try {
                Thread.sleep(millis);
            } catch (Exception ignored) {
            }
            if (getBossHashMap().get(getBukkitPlayer().getUniqueId()) != null
                    && getBossHashMap().get(getBukkitPlayer().getUniqueId()).equals(bossBar)) {
                hideBossBar(getBossHashMap().get(getBukkitPlayer().getUniqueId()));
            }
        }).start();
    }

    @Nonnull
    @Override
    public void sendBossBar(@Nonnull BossBar<?> bossBar) {
        NMSBossBar.createBossBar(bossBar.getId(), ((NMSBossBar) bossBar));
        if (!getBossBars(getUniqueId()).contains(bossBar.getId())) {
            sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.ADD, ((NMSBossBar) bossBar).getBossBar().getHandle()));
            List<String> bars = getBossBars(getUniqueId());
            bars.add(bossBar.getId());
            getBossBars().put(getUniqueId(), bars);
        }
        updateBossBar(bossBar);
    }

    @Nonnull
    @Override
    public void updateBossBar(@Nonnull BossBar<?> bossBar) {
        NMSBossBar.createBossBar(bossBar.getId(), ((NMSBossBar) bossBar));
        if (!getBossBars(getUniqueId()).contains(bossBar.getId())) {
            sendBossBar(bossBar);
        } else {
            BossBattleServer handle = ((NMSBossBar) bossBar).getBossBar().getHandle();
            sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.UPDATE_NAME, handle));
            sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.UPDATE_PCT, handle));
            sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.UPDATE_PROPERTIES, handle));
            sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.UPDATE_STYLE, handle));
        }
    }

    @Nonnull
    @Override
    public void hideBossBar(@Nonnull BossBar<?> bossBar) {
        NMSBossBar.deleteBossBar(bossBar.getId(), ((NMSBossBar) bossBar));
        sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.REMOVE, ((NMSBossBar) bossBar).getBossBar().getHandle()));
        List<String> bars = getBossBars(getUniqueId());
        bars.remove(bossBar.getId());
        getBossBars().put(getUniqueId(), bars);
        NMSBossBar.removeBossBar(bossBar.getId());
    }

    @Override
    public void sendTitle(@Nonnull Title title) {
        getCraftPlayer().sendTitle(title.getTitle(), title.getSubtitle(), title.getTimeIn(), title.getTimeStay(), title.getTimeOut());
    }

    @Override
    public void sendTitle(@Nonnull Title.Animation animation) {
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

    @Override
    public void sendActionBar(@Nonnull ActionBar actionBar) {
        sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + actionBar.getText() + "\"}"), ChatMessageType.a((byte) 2)));
    }

    @Override
    @Nonnull
    public CraftPlayer getCraftPlayer() {
        return ((CraftPlayer) getBukkitPlayer());
    }

    @Override
    @Nonnull
    public EntityPlayer getEntityPlayer() {
        return getCraftPlayer().getHandle();
    }

    @Override
    public void bungeeConnect(@Nonnull net.nonswag.tnl.listener.api.server.Server server) {
        try {
            if (server.isOnline()) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                dataOutputStream.writeUTF("Connect");
                dataOutputStream.writeUTF(server.getName());
                sendPluginMessage(Loader.getInstance(), "BungeeCord", byteArrayOutputStream.toByteArray());
                sendMessage("%prefix% §aConnecting you to server §6" + server.getName());
            } else {
                sendMessage("%prefix% §cThe server §4" + server.getName() + "§c is Offline");
            }
        } catch (Exception e) {
            Logger.error.println(e);
            sendMessage("%prefix% §cFailed to connect you to server §4" + server.getName());
        }
    }

    @Override
    public void loadInventory(@Nonnull String id) {
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

    @Override
    public void mkdirInventories() {
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

    @Override
    @Nonnull
    public String getDisplayName() {
        return getBukkitPlayer().getDisplayName();
    }

    @Override
    public void setDisplayName(@Nonnull String s) {
        getBukkitPlayer().setDisplayName(s);
    }

    @Override
    @Nonnull
    public String getPlayerListName() {
        return getBukkitPlayer().getPlayerListName();
    }

    @Override
    public void setPlayerListName(@Nonnull String s) {
        getBukkitPlayer().setPlayerListName(s);
    }

    @Override
    @Nullable
    public String getPlayerListHeader() {
        return getBukkitPlayer().getPlayerListHeader();
    }

    @Override
    @Nullable
    public String getPlayerListFooter() {
        return getBukkitPlayer().getPlayerListFooter();
    }

    @Override
    public void setPlayerListHeader(@Nonnull String s) {
        getBukkitPlayer().setPlayerListHeader(s);
    }

    @Override
    public void setPlayerListFooter(@Nonnull String s) {
        getBukkitPlayer().setPlayerListFooter(s);
    }

    @Override
    public void setPlayerListHeaderFooter(@Nonnull String s, @Nonnull String s1) {
        getBukkitPlayer().setPlayerListHeaderFooter(s, s1);
    }

    @Override
    public void setCompassTarget(@Nonnull Location location) {
        getBukkitPlayer().setCompassTarget(location);
    }

    @Override
    @Nonnull
    public Location getCompassTarget() {
        return getBukkitPlayer().getCompassTarget();
    }

    @Override
    @Nullable
    public InetSocketAddress getAddress() {
        return getBukkitPlayer().getAddress();
    }

    @Override
    public void chat(@Nonnull String s) {
        getBukkitPlayer().chat(s);
    }

    @Override
    public boolean performCommand(@Nonnull String s) {
        return getBukkitPlayer().performCommand(s);
    }

    @Override
    public boolean isSneaking() {
        return getBukkitPlayer().isSneaking();
    }

    @Override
    public void setSneaking(boolean b) {
        getBukkitPlayer().setSneaking(b);
    }

    @Override
    public boolean isSprinting() {
        return getBukkitPlayer().isSprinting();
    }

    @Override
    public void setSprinting(boolean b) {
        getBukkitPlayer().setSprinting(b);
    }

    @Override
    public void saveData() {
        getBukkitPlayer().saveData();
    }

    @Override
    public void loadData() {
        getBukkitPlayer().loadData();
    }

    @Override
    public void setSleepingIgnored(boolean b) {
        getBukkitPlayer().setSleepingIgnored(b);
    }

    @Override
    public boolean isSleepingIgnored() {
        return getBukkitPlayer().isSleepingIgnored();
    }

    @Override
    @Nonnull
    public <T> void playEffect(@Nonnull Location location, @Nonnull Effect effect, @Nonnull T t) {
        getBukkitPlayer().playEffect(location, effect, t);
    }

    @Override
    public void sendMap(@Nonnull MapView mapView) {
        getBukkitPlayer().sendMap(mapView);
    }

    @Override
    public void updateInventory() {
        getBukkitPlayer().updateInventory();
    }

    @Override
    public void setPlayerTime(long l, boolean b) {
        getBukkitPlayer().setPlayerTime(l, b);
        sendPacket(new PacketPlayOutUpdateTime(l, l, b));
    }

    @Override
    public long getPlayerTime() {
        return getBukkitPlayer().getPlayerTime();
    }

    @Override
    public long getPlayerTimeOffset() {
        return getBukkitPlayer().getPlayerTimeOffset();
    }

    @Override
    public boolean isPlayerTimeRelative() {
        return getBukkitPlayer().isPlayerTimeRelative();
    }

    @Override
    public void resetPlayerTime() {
        getBukkitPlayer().resetPlayerTime();
        sendPacket(new PacketPlayOutUpdateTime(getWorld().getTime(), getWorld().getTime(), true));
    }

    @Override
    public void setPlayerWeather(@Nonnull WeatherType weatherType) {
        getBukkitPlayer().setPlayerWeather(weatherType);
        if (weatherType.equals(WeatherType.DOWNFALL)) {
            sendPacket(new PacketPlayOutGameStateChange(2, 0.0F));
        } else {
            sendPacket(new PacketPlayOutGameStateChange(1, 0.0F));
        }
    }

    @Override
    @Nullable
    public WeatherType getPlayerWeather() {
        return getBukkitPlayer().getPlayerWeather();
    }

    @Override
    public void resetPlayerWeather() {
        getBukkitPlayer().resetPlayerWeather();
    }

    @Override
    public void giveExp(int i) {
        getBukkitPlayer().giveExp(i);
    }

    @Override
    public void giveExpLevels(int i) {
        getBukkitPlayer().giveExpLevels(i);
    }

    @Override
    public float getExp() {
        return getBukkitPlayer().getExp();
    }

    @Override
    public void setExp(float v) {
        getBukkitPlayer().setExp(v);
    }

    @Override
    public int getLevel() {
        return getBukkitPlayer().getLevel();
    }

    @Override
    public void setLevel(int i) {
        getBukkitPlayer().setLevel(i);
    }

    @Override
    public int getTotalExperience() {
        return getBukkitPlayer().getTotalExperience();
    }

    @Override
    public void setTotalExperience(int i) {
        getBukkitPlayer().setTotalExperience(i);
    }

    @Override
    public void sendExperienceChange(float v) {
        getBukkitPlayer().sendExperienceChange(v);
    }

    @Override
    public void sendExperienceChange(float v, int i) {
        getBukkitPlayer().sendExperienceChange(v, i);
    }

    @Override
    public float getExhaustion() {
        return getBukkitPlayer().getExhaustion();
    }

    @Override
    public void setExhaustion(float v) {
        getBukkitPlayer().setExhaustion(v);
    }

    @Override
    public float getSaturation() {
        return getBukkitPlayer().getSaturation();
    }

    @Override
    public void setSaturation(float v) {
        getBukkitPlayer().setSaturation(v);
    }

    @Override
    public int getFoodLevel() {
        return getBukkitPlayer().getFoodLevel();
    }

    @Override
    public void setFoodLevel(int i) {
        getBukkitPlayer().setFoodLevel(i);
    }

    @Override
    public boolean getAllowFlight() {
        return getBukkitPlayer().getAllowFlight();
    }

    @Override
    public void setAllowFlight(boolean b) {
        getBukkitPlayer().setAllowFlight(b);
    }

    @Override
    public void setArrowCount(int arrows) {
        getEntityPlayer().setArrowCount(arrows);
    }

    @Override
    public void hidePlayer(@Nonnull TNLPlayer player) {
        sendPacket(new PacketPlayOutEntityDestroy(player.getEntityId()));
    }

    @Override
    public void showPlayer(@Nonnull TNLPlayer player) {
        sendPacket(new PacketPlayOutSpawnEntityLiving(player.getEntityPlayer()));
    }

    @Override
    public boolean isFlying() {
        return getBukkitPlayer().isFlying();
    }

    @Override
    public void setFlying(boolean b) {
        getBukkitPlayer().setFlying(b);
    }

    @Override
    public void setFlySpeed(float v) throws IllegalArgumentException {
        getBukkitPlayer().setFlySpeed(v);
    }

    @Override
    public void setWalkSpeed(float v) throws IllegalArgumentException {
        getBukkitPlayer().setWalkSpeed(v);
    }

    @Override
    public float getFlySpeed() {
        return getBukkitPlayer().getFlySpeed();
    }

    @Override
    public float getWalkSpeed() {
        return getBukkitPlayer().getWalkSpeed();
    }

    @Override
    public void setResourcePack(@Nonnull String s, byte[] bytes) {
        getBukkitPlayer().setResourcePack(s, bytes);
    }

    @Override
    public double getHealthScale() {
        return getBukkitPlayer().getHealthScale();
    }

    @Override
    @Nullable
    public Entity getSpectatorTarget() {
        return getCraftPlayer().getSpectatorTarget();
    }

    @Override
    public void spectate(@Nonnull Entity entity) {
        getCraftPlayer().setSpectatorTarget(entity);
    }

    @Override
    public void spectate() {
        getCraftPlayer().setSpectatorTarget(getCraftPlayer());
    }

    @Override
    public void resetTitle() {
        sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.RESET, null));
    }

    @Override
    public int getClientViewDistance() {
        return getBukkitPlayer().getClientViewDistance();
    }

    @Override
    @Nonnull
    public String getLocale() {
        return getBukkitPlayer().getLocale();
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return Language.fromLocale(getLocale());
    }

    @Override
    public void updateCommands() {
        getBukkitPlayer().updateCommands();
    }

    @Override
    public void openBook(@Nonnull ItemStack itemStack) {
        getBukkitPlayer().openBook(itemStack);
    }

    @Override
    @Nonnull
    public Location getLocation() {
        return getBukkitPlayer().getLocation();
    }

    @Override
    @Nonnull
    public Location getLocation(@Nonnull Location location) {
        return getBukkitPlayer().getLocation(location);
    }

    @Override
    @Nonnull
    public Location getTargetLocation(double i) {
        return getLocation().clone().add(getBukkitPlayer().getLocation().getDirection().multiply(i));
    }

    @Override
    public void setVelocity(@Nonnull Vector vector) {
        getBukkitPlayer().setVelocity(vector);
    }

    @Override
    @Nonnull
    public Vector getVelocity() {
        return getBukkitPlayer().getVelocity();
    }

    @Override
    public double getHeight() {
        return getBukkitPlayer().getHeight();
    }

    @Override
    public double getWidth() {
        return getBukkitPlayer().getWidth();
    }

    @Override
    public boolean isOnGround() {
        return getBukkitPlayer().isOnGround();
    }

    @Override
    @Nonnull
    public World getWorld() {
        return getBukkitPlayer().getWorld();
    }

    @Override
    @Nonnull
    public WorldServer getWorldServer() {
        return ((CraftWorld) getBukkitPlayer().getWorld()).getHandle();
    }

    @Override
    public void setRotation(float v, float v1) {
        getBukkitPlayer().setRotation(v, v1);
    }

    @Override
    public boolean teleport(@Nonnull Location location) {
        return getBukkitPlayer().teleport(location);
    }

    @Override
    public boolean teleport(@Nonnull Location location, @Nonnull PlayerTeleportEvent.TeleportCause teleportCause) {
        return getBukkitPlayer().teleport(location, teleportCause);
    }

    @Override
    public boolean teleport(@Nonnull Entity entity) {
        return getBukkitPlayer().teleport(entity);
    }

    @Override
    public boolean teleport(@Nonnull Entity entity, @Nonnull PlayerTeleportEvent.TeleportCause teleportCause) {
        return getBukkitPlayer().teleport(entity, teleportCause);
    }

    @Override
    @Nonnull
    public List<Entity> getNearbyEntities(double v, double v1, double v2) {
        return getBukkitPlayer().getNearbyEntities(v, v1, v2);
    }

    @Override
    public int getEntityId() {
        return getBukkitPlayer().getEntityId();
    }

    @Override
    public int getFireTicks() {
        return getBukkitPlayer().getFireTicks();
    }

    @Override
    public int getMaxFireTicks() {
        return getBukkitPlayer().getMaxFireTicks();
    }

    @Override
    public void setFireTicks(int i) {
        getBukkitPlayer().setFireTicks(i);
    }

    @Override
    public boolean isDead() {
        return getBukkitPlayer().isDead();
    }

    @Override
    @Nonnull
    public List<Entity> getPassengers() {
        return getBukkitPlayer().getPassengers();
    }

    @Override
    public boolean addPassenger(@Nonnull Entity entity) {
        return getBukkitPlayer().addPassenger(entity);
    }

    @Override
    public boolean removePassenger(@Nonnull Entity entity) {
        return getBukkitPlayer().removePassenger(entity);
    }

    @Override
    public boolean eject() {
        return getBukkitPlayer().eject();
    }

    @Override
    public float getFallDistance() {
        return getBukkitPlayer().getFallDistance();
    }

    @Override
    public void setFallDistance(float v) {
        getBukkitPlayer().setFallDistance(v);
    }

    @Override
    public void setLastDamageCause(@Nonnull EntityDamageEvent entityDamageEvent) {
        getBukkitPlayer().setLastDamageCause(entityDamageEvent);
    }

    @Override
    @Nullable
    public EntityDamageEvent getLastDamageCause() {
        return getBukkitPlayer().getLastDamageCause();
    }

    @Override
    @Nonnull
    public UUID getUniqueId() {
        return getBukkitPlayer().getUniqueId();
    }

    @Override
    public int getTicksLived() {
        return getBukkitPlayer().getTicksLived();
    }

    @Override
    public void setTicksLived(int i) {
        getBukkitPlayer().setTicksLived(i);
    }

    @Override
    public void playEffect(@Nonnull EntityEffect entityEffect) {
        getBukkitPlayer().playEffect(entityEffect);
    }

    @Override
    public boolean isInsideVehicle() {
        return getBukkitPlayer().isInsideVehicle();
    }

    @Override
    public boolean leaveVehicle() {
        return getBukkitPlayer().leaveVehicle();
    }

    @Override
    @Nullable
    public Entity getVehicle() {
        return getBukkitPlayer().getVehicle();
    }

    @Override
    public void setCustomNameVisible(boolean b) {
        getBukkitPlayer().setCustomNameVisible(b);
    }

    @Override
    public boolean isCustomNameVisible() {
        return getBukkitPlayer().isCustomNameVisible();
    }

    @Override
    public void setGlowing(boolean b) {
        getBukkitPlayer().setGlowing(b);
    }

    @Override
    public void setGlowing(boolean b, @Nonnull TNLPlayer... players) {
        boolean glowing = getEntityPlayer().glowing;
        getEntityPlayer().glowing = b;
        for (TNLPlayer all : players) {
            sendPacket(new PacketPlayOutEntityMetadata(getEntityId(), getEntityPlayer().getDataWatcher(), true));
        }
        getEntityPlayer().glowing = glowing;
    }

    @Override
    public void setGlowing(boolean b, @Nonnull Entity entity) {
        net.minecraft.server.v1_15_R1.Entity handle = ((CraftEntity) entity).getHandle();
        handle.glowing = b;
        sendPacket(new PacketPlayOutEntityMetadata(entity.getEntityId(), handle.getDataWatcher(), true));
        // handle.glowing = glowing;
    }

    @Override
    public boolean isGlowing() {
        return getBukkitPlayer().isGlowing();
    }

    @Override
    public void setInvulnerable(boolean b) {
        getBukkitPlayer().setInvulnerable(b);
    }

    @Override
    public boolean isInvulnerable() {
        return getBukkitPlayer().isInvulnerable();
    }

    @Override
    public boolean hasGravity() {
        return getBukkitPlayer().hasGravity();
    }

    @Override
    public void setGravity(boolean b) {
        getBukkitPlayer().setGravity(b);
    }

    @Override
    public int getPortalCooldown() {
        return getBukkitPlayer().getPortalCooldown();
    }

    @Override
    public void setPortalCooldown(int i) {
        getBukkitPlayer().setPortalCooldown(i);
    }

    @Override
    @Nonnull
    public Set<String> getScoreboardTags() {
        return getBukkitPlayer().getScoreboardTags();
    }

    @Override
    public boolean addScoreboardTag(@Nonnull String s) {
        return getBukkitPlayer().addScoreboardTag(s);
    }

    @Override
    public boolean removeScoreboardTag(@Nonnull String s) {
        return getBukkitPlayer().removeScoreboardTag(s);
    }

    @Override
    @Nonnull
    public PistonMoveReaction getPistonMoveReaction() {
        return getBukkitPlayer().getPistonMoveReaction();
    }

    @Override
    @Nonnull
    public BlockFace getFacing() {
        return getBukkitPlayer().getFacing();
    }

    @Override
    @Nonnull
    public BackFlip backflip() {
        return new BackFlip(this);
    }

    @Override
    public double getEyeHeight() {
        return getBukkitPlayer().getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean b) {
        return getBukkitPlayer().getEyeHeight(b);
    }

    @Override
    @Nonnull
    public Location getEyeLocation() {
        return getBukkitPlayer().getEyeLocation();
    }

    @Override
    @Nonnull
    public List<Block> getLineOfSight(@Nonnull Set<Material> set, int i) {
        return getBukkitPlayer().getLineOfSight(set, i);
    }

    @Override
    @Nonnull
    public Block getTargetBlock(@Nonnull Set<Material> set, int i) {
        return getBukkitPlayer().getTargetBlock(set, i);
    }

    @Override
    @Nonnull
    public List<Block> getLastTwoTargetBlocks(@Nonnull Set<Material> set, int i) {
        return getBukkitPlayer().getLastTwoTargetBlocks(set, i);
    }

    @Override
    @Nullable
    public Block getTargetBlockExact(int i) {
        return getBukkitPlayer().getTargetBlockExact(i);
    }

    @Override
    public int getRemainingAir() {
        return getBukkitPlayer().getRemainingAir();
    }

    @Override
    public void setRemainingAir(int i) {
        getBukkitPlayer().setRemainingAir(i);
    }

    @Override
    public int getMaximumAir() {
        return getBukkitPlayer().getMaximumAir();
    }

    @Override
    public void setMaximumAir(int i) {
        getBukkitPlayer().setMaximumNoDamageTicks(i);
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return getBukkitPlayer().getMaximumNoDamageTicks();
    }

    @Override
    public void setMaximumNoDamageTicks(int i) {
        getBukkitPlayer().setMaximumNoDamageTicks(i);
    }

    @Override
    public double getLastDamage() {
        return getBukkitPlayer().getLastDamage();
    }

    @Override
    public void setLastDamage(double v) {
        getBukkitPlayer().setLastDamage(v);
    }

    @Override
    public int getNoDamageTicks() {
        return getBukkitPlayer().getNoDamageTicks();
    }

    @Override
    public void setNoDamageTicks(int i) {
        getBukkitPlayer().setNoDamageTicks(i);
    }

    @Nullable
    @Override
    public TNLPlayer getKiller() {
        if (getBukkitPlayer().getKiller() != null) {
            return cast(getBukkitPlayer().getKiller());
        }
        return null;
    }

    @Override
    public boolean addPotionEffect(@Nonnull PotionEffect potionEffect) {
        return getBukkitPlayer().addPotionEffect(potionEffect);
    }

    @Override
    public boolean addPotionEffects(@Nonnull Collection<PotionEffect> collection) {
        return getBukkitPlayer().addPotionEffects(collection);
    }

    @Override
    public boolean hasPotionEffect(@Nonnull PotionEffectType potionEffectType) {
        return getBukkitPlayer().hasPotionEffect(potionEffectType);
    }

    @Override
    @Nullable
    public PotionEffect getPotionEffect(@Nonnull PotionEffectType potionEffectType) {
        return getBukkitPlayer().getPotionEffect(potionEffectType);
    }

    @Override
    public void removePotionEffect(@Nonnull PotionEffectType potionEffectType) {
        getBukkitPlayer().removePotionEffect(potionEffectType);
    }

    @Override
    @Nonnull
    public Collection<PotionEffect> getActivePotionEffects() {
        return getBukkitPlayer().getActivePotionEffects();
    }

    @Override
    public boolean hasLineOfSight(@Nonnull Entity entity) {
        return getBukkitPlayer().hasLineOfSight(entity);
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return getBukkitPlayer().getRemoveWhenFarAway();
    }

    @Override
    public void setRemoveWhenFarAway(boolean b) {
        getBukkitPlayer().setRemoveWhenFarAway(b);
    }

    @Override
    @Nullable
    public EntityEquipment getEquipment() {
        return getBukkitPlayer().getEquipment();
    }

    @Override
    public void setCanPickupItems(boolean b) {
        getBukkitPlayer().setCanPickupItems(b);
    }

    @Override
    public boolean getCanPickupItems() {
        return getBukkitPlayer().getCanPickupItems();
    }

    @Override
    public boolean isLeashed() {
        return getBukkitPlayer().isLeashed();
    }

    @Override
    @Nullable
    public Entity getLeashHolder() throws IllegalStateException {
        return getBukkitPlayer().getLeashHolder();
    }

    @Override
    public boolean setLeashHolder(@Nonnull Entity entity) {
        return getBukkitPlayer().setLeashHolder(entity);
    }

    @Override
    public boolean isGliding() {
        return getBukkitPlayer().isGliding();
    }

    @Override
    public void setGliding(boolean b) {
        getBukkitPlayer().setGliding(b);
    }

    @Override
    public boolean isSwimming() {
        return getBukkitPlayer().isSwimming();
    }

    @Override
    public void setSwimming(boolean b) {
        getBukkitPlayer().setSwimming(b);
    }

    @Override
    public boolean isRiptiding() {
        return getBukkitPlayer().isRiptiding();
    }

    @Override
    public boolean isSleeping() {
        return getBukkitPlayer().isSleeping();
    }

    @Override
    public void attack(@Nonnull Entity entity) {
        getBukkitPlayer().attack(entity);
    }

    @Override
    public void swingMainHand() {
        getBukkitPlayer().swingMainHand();
    }

    @Override
    public void swingOffHand() {
        getBukkitPlayer().swingOffHand();
    }

    @Override
    public void setCollidable(boolean b) {
        getBukkitPlayer().setCollidable(b);
    }

    @Override
    public boolean isCollidable() {
        return getBukkitPlayer().isCollidable();
    }

    @Override
    public void damage(double v) {
        getBukkitPlayer().damage(v);
    }

    @Override
    public void damage(double v, @Nullable Entity entity) {
        getBukkitPlayer().damage(v, entity);
    }

    @Override
    public double getHealth() {
        return getBukkitPlayer().getHealth();
    }

    @Override
    public void setHealth(double v) {
        getBukkitPlayer().setHealth(v);
    }

    @Override
    public double getAbsorptionAmount() {
        return getBukkitPlayer().getAbsorptionAmount();
    }

    @Override
    public void setAbsorptionAmount(double v) {
        getBukkitPlayer().setAbsorptionAmount(v);
    }

    @Override
    @Nullable
    public String getCustomName() {
        return getBukkitPlayer().getCustomName();
    }

    @Override
    public void setCustomName(@Nonnull String s) {
        getBukkitPlayer().setCustomName(s);
    }

    @Override
    public void setMetadata(@Nonnull String s, @Nonnull MetadataValue metadataValue) {
        getBukkitPlayer().setMetadata(s, metadataValue);
    }

    @Override
    @Nonnull
    public List<MetadataValue> getMetadata(@Nonnull String s) {
        return getBukkitPlayer().getMetadata(s);
    }

    @Override
    public boolean hasMetadata(@Nonnull String s) {
        return getBukkitPlayer().hasMetadata(s);
    }

    @Override
    public void removeMetadata(@Nonnull String s, @Nonnull Plugin plugin) {
        getBukkitPlayer().removeMetadata(s, plugin);
    }

    @Override
    public void sendPluginMessage(@Nonnull Plugin plugin, @Nonnull String s, byte[] bytes) {
        getBukkitPlayer().sendPluginMessage(plugin, s, bytes);
    }

    @Override
    @Nonnull
    public Set<String> getListeningPluginChannels() {
        return getBukkitPlayer().getListeningPluginChannels();
    }

    @Override
    public void inject() {
        try {
            if (isOnline()) {
                uninject();
                final NMSPlayer player = this;
                ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
                    @Override
                    public void channelRead(ChannelHandlerContext channelHandlerContext, Object packetObject) {
                        try {
                            PlayerPacketEvent<Packet<?>> event = new PlayerPacketEvent<>(player, ((Packet<?>) packetObject));
                            Bukkit.getPluginManager().callEvent(event);
                            if (!event.isCancelled()) {
                                super.channelRead(channelHandlerContext, event.getPacket());
                            }
                        } catch (Exception e) {
                            Logger.error.println(e);
                            uninject();
                        }
                    }

                    @Override
                    public void write(ChannelHandlerContext channelHandlerContext, Object packetObject, ChannelPromise channelPromise) {
                        try {
                            PlayerPacketEvent<Packet<?>> event = new PlayerPacketEvent<>(player, ((Packet<?>) packetObject));
                            Bukkit.getPluginManager().callEvent(event);
                            if (!event.isCancelled()) {
                                super.write(channelHandlerContext, event.getPacket(), channelPromise);
                            }
                        } catch (Exception e) {
                            Logger.error.println(e);
                            uninject();
                        }
                    }
                };
                ChannelPipeline pipeline = getNetworkManager().channel.pipeline();
                try {
                    pipeline.addBefore("packet_handler", getName() + "-TNLListener", channelDuplexHandler);
                } catch (Throwable ignored) {
                    uninject();
                }
            } else {
                Logger.error.println("§cFailed to inject §8'§4" + getName() + "§8'", "§cThe player can't be offline");
                disconnect("%prefix%\n" + "§cYou are online but your connection is offline?!");
            }
        } catch (Exception e) {
            uninject();
            Logger.error.println(e);
        }
    }

    @Override
    public void uninject() {
        try {
            Channel channel = getNetworkManager().channel;
            if (channel.pipeline().get(getName() + "-TNLListener") != null) {
                channel.eventLoop().submit(() -> {
                    channel.pipeline().remove(getName() + "-TNLListener");
                });
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public String toString() {
        return "TNLPlayer{" +
                "bukkitPlayer=" + bukkitPlayer +
                ", optionScoreboard=" + optionScoreboard +
                ", optionTeam=" + optionTeam +
                ", virtualStorage=" + virtualStorage +
                ", permissionManager=" + permissionManager +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NMSPlayer player = (NMSPlayer) o;
        return bukkitPlayer.equals(player.bukkitPlayer) && optionScoreboard.equals(player.optionScoreboard) && optionTeam.equals(player.optionTeam) && virtualStorage.equals(player.virtualStorage) && permissionManager.equals(player.permissionManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bukkitPlayer, optionScoreboard, optionTeam, virtualStorage, permissionManager);
    }
}
