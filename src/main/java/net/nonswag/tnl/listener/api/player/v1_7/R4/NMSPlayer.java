package net.nonswag.tnl.listener.api.player.v1_7.R4;

import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.io.netty.channel.*;
import net.nonswag.tnl.listener.Bootstrap;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.bossbar.BossBar;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.message.*;
import net.nonswag.tnl.listener.api.object.Generic;
import net.nonswag.tnl.listener.api.permission.PermissionManager;
import net.nonswag.tnl.listener.api.permission.Permissions;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.scoreboard.Sidebar;
import net.nonswag.tnl.listener.api.sign.SignMenu;
import net.nonswag.tnl.listener.api.title.Title;
import net.nonswag.tnl.listener.events.PlayerPacketEvent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.util.CraftMagicNumbers;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.*;

public class NMSPlayer implements TNLPlayer {

    @Nonnull private final Player bukkitPlayer;
    @Nonnull private final Scoreboard optionScoreboard = new Scoreboard();
    @Nonnull private final ScoreboardTeam optionTeam = new ScoreboardTeam(getOptionScoreboard(), "TNLOptionPacket");
    @Nonnull private final HashMap<String, Object> virtualStorage = new HashMap<>();
    @Nonnull private final Permissions permissionManager;

    protected NMSPlayer(@Nonnull Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        this.permissionManager = new PermissionManager(this);
    }

    @Nonnull
    public static NMSPlayer cast(@Nonnull Player player) {
        if (!TNLListener.getInstance().getPlayerHashMap().containsKey(player)) {
            TNLListener.getInstance().getPlayerHashMap().put(player, new NMSPlayer(player));
        }
        return (NMSPlayer) TNLListener.getInstance().getPlayerHashMap().get(player);
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
    public Permissions getPermissionManager() {
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

    @Nonnull
    @Override
    public Entity getBukkitEntity() {
        return getBukkitPlayer();
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
    public void sendPacket(@Nonnull Object packet) {
        if (Bukkit.isPrimaryThread()) {
            getPlayerConnection().sendPacket(((Packet) packet));
        } else {
            Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> getPlayerConnection().sendPacket(((Packet) packet)));
        }
    }

    @Override
    public void sendPackets(@Nonnull Object... packets) {
        for (Object packet : packets) {
            sendPacket(packet);
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
            language = getLanguage();
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
        disconnect(MessageKey.KICKED, "\n§cDisconnected");
    }

    @Override
    public void disconnect(@Nonnull MessageKey messageKey, @Nonnull String append, @Nonnull Placeholder... placeholders) {
        Language language = Language.ROOT;
        if (!messageKey.isSystemMessage()) {
            language = getLanguage();
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
            getPlayerConnection().disconnect(kickMessage);
        } else {
            Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> getPlayerConnection().disconnect(kickMessage));
        }
    }

    @Override
    public <EnumTeamPush> void setCollision(@Nonnull EnumTeamPush collision) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public String getWorldAlias() {
        return getWorldAlias(getWorld());
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
    public void openInventory(@Nonnull InventoryView inventoryView) {
        getBukkitPlayer().openInventory(inventoryView);
    }

    @Override
    public void openSignEditor(@Nonnull Location location) {
        sendPacket(new PacketPlayOutOpenSignEditor(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    @Override
    public void openVirtualSignEditor(@Nonnull SignMenu signMenu) {
        TileEntitySign tileEntitySign = new TileEntitySign();
        tileEntitySign.a(getWorldServer());
        tileEntitySign.x = signMenu.getLocation().getBlockX();
        tileEntitySign.y = signMenu.getLocation().getBlockY();
        tileEntitySign.z = signMenu.getLocation().getBlockZ();
        for (int line = 0; line < 4; line++) {
            if (signMenu.getLines().length >= (line + 1)) {
                tileEntitySign.lines[line] = signMenu.getLines()[line];
            }
        }
        sendPacket(tileEntitySign.getUpdatePacket());
        sendPacket(new PacketPlayOutOpenSignEditor(signMenu.getLocation().getBlockX(), signMenu.getLocation().getBlockY(), signMenu.getLocation().getBlockZ()));
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
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setCooldown(@Nonnull Material material, int i) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setCooldown(@Nonnull ItemStack itemStack, int i) {
        throw new UnsupportedOperationException("method is not supported in this version");
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
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void wakeup(boolean b) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    @Nonnull
    public Location getBedLocation() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    @Nonnull
    public GameMode getGameMode() {
        return getCraftPlayer().getGameMode();
    }

    @Override
    public void setGameMode(@Nonnull GameMode gameMode) {
        getCraftPlayer().setGameMode(gameMode);
    }

    @Override
    public boolean isBlocking() {
        return getBukkitPlayer().isBlocking();
    }

    @Override
    public boolean isHandRaised() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public int getExpToLevel() {
        return getBukkitPlayer().getExpToLevel();
    }

    @Override
    public float getAttackCooldown() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void hideTabListName(@Nonnull TNLPlayer[] players) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Nonnull
    @Override
    public void disguise(@Nonnull Generic<?> entity) {
        disguise(entity, TNLListener.getInstance().getOnlinePlayers());
    }

    @Nonnull
    @Override
    public void disguise(@Nonnull Generic<?> entity, @Nonnull List<TNLPlayer> receivers) {
        for (TNLPlayer receiver : receivers) {
            disguise(entity, receiver);
        }
    }

    @Nonnull
    @Override
    public void disguise(@Nonnull Generic<?> entity, @Nonnull TNLPlayer receiver) {
        if (!this.equals(receiver) && entity.getParameter() instanceof EntityLiving) {
            receiver.sendPacket(new PacketPlayOutEntityDestroy(this.getId()));
            ((EntityLiving) entity.getParameter()).setLocation(getLocation().getX(), getLocation().getY(), getLocation().getZ(), getLocation().getYaw(), getLocation().getPitch());
            ((EntityLiving) entity.getParameter()).world = getWorldServer();
            Reflection.setField(entity, Entity.class, "id", this.getId());
            receiver.sendPacket(new PacketPlayOutSpawnEntityLiving(((EntityLiving) entity.getParameter())));
        }
    }

    @Nonnull
    @Override
    public void updateBossBar(@Nonnull BossBar<?> bossBar) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Nonnull
    @Override
    public void hideBossBar(@Nonnull BossBar<?> bossBar) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Nonnull
    @Override
    public void sendBossBar(@Nonnull BossBar<?> bossBar) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Nonnull
    @Override
    public void sendBossBar(@Nonnull BossBar<?> bossBar, long millis) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void sendTitle(@Nonnull Title title) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void sendTitle(@Nonnull Title.Animation animation) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void sendActionbar(@Nonnull String actionbar) {
        sendPacket(new PacketPlayOutChat(new ChatMessage(actionbar)));
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
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    @Nullable
    public String getPlayerListFooter() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setPlayerListHeader(@Nonnull String s) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setPlayerListFooter(@Nonnull String s) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setPlayerListHeaderFooter(@Nonnull String s, @Nonnull String s1) {
        throw new UnsupportedOperationException("method is not supported in this version");
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
    public void sendBlockChange(@Nonnull Location location, @Nonnull BlockData block) {
        PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(location.getBlockX(), location.getBlockY(), location.getBlockZ(), ((CraftWorld) location.getWorld()).getHandle());
        packet.block = CraftMagicNumbers.getBlock(block.getMaterial());
        sendPacket(packet);
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
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setPlayerTime(long l, boolean b) {
        getBukkitPlayer().setPlayerTime(l, b);
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
    }

    @Override
    public void setPlayerWeather(@Nonnull WeatherType weatherType) {
        getBukkitPlayer().setPlayerWeather(weatherType);
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
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void sendExperienceChange(float v, int i) {
        throw new UnsupportedOperationException("method is not supported in this version");
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
        throw new UnsupportedOperationException("method is not supported in this version");
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
        net.nonswag.tnl.listener.api.object.Objects<Method> resourcePack = Reflection.getMethod(getBukkitPlayer(), "setResourcePack", String.class);
        if (resourcePack.hasValue()) {
            try {
                resourcePack.nonnull().invoke(s);
            } catch (IllegalAccessException | InvocationTargetException e) {
                Logger.error.println(e);
            }
        }
    }

    @Override
    public double getHealthScale() {
        return getBukkitPlayer().getHealthScale();
    }

    @Override
    @Nullable
    public Entity getSpectatorTarget() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void spectate(@Nonnull Entity entity) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void spectate() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void resetTitle() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public int getClientViewDistance() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    @Nonnull
    public String getLocale() {
        return getLanguage().getShorthand();
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        Language language = Language.ENGLISH;
        try {
            net.nonswag.tnl.listener.api.object.Objects<Method> locale = Reflection.getMethod(getBukkitPlayer().spigot(), "getLocale");
            if (locale.hasValue()) {
                language = Language.fromLocale((String) locale.nonnull().invoke(getBukkitPlayer().spigot()));
            }
        } catch (Exception ignored) {
        }
        return language;
    }

    @Override
    public void updateCommands() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void openBook(@Nonnull ItemStack itemStack) {
        throw new UnsupportedOperationException("method is not supported in this version");
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
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public double getWidth() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public boolean isOnGround() {
        return getEntityPlayer().onGround;
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
        throw new UnsupportedOperationException("method is not supported in this version");
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
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public boolean addPassenger(@Nonnull Entity entity) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public boolean removePassenger(@Nonnull Entity entity) {
        throw new UnsupportedOperationException("method is not supported in this version");
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
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setGlowing(boolean b, @Nonnull TNLPlayer... players) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setGlowing(boolean b, @Nonnull Entity entity) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public boolean isGlowing() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setInvulnerable(boolean b) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public boolean isInvulnerable() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public boolean hasGravity() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setGravity(boolean b) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public int getPortalCooldown() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setPortalCooldown(int i) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    @Nonnull
    public Set<String> getScoreboardTags() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public boolean addScoreboardTag(@Nonnull String s) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public boolean removeScoreboardTag(@Nonnull String s) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    @Nonnull
    public PistonMoveReaction getPistonMoveReaction() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    @Nonnull
    public BlockFace getFacing() {
        throw new UnsupportedOperationException("method is not supported in this version");
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
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    @Nonnull
    public Block getTargetBlock(@Nonnull Set<Material> set, int i) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    @Nonnull
    public List<Block> getLastTwoTargetBlocks(@Nonnull Set<Material> set, int i) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    @Nullable
    public Block getTargetBlockExact(int i) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Nonnull
    @Override
    public Sidebar<?, ?> getSidebar() {
        throw new UnsupportedOperationException("method is not supported in this version");
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
        throw new UnsupportedOperationException("method is not supported in this version");
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

    @Override
    @Nullable
    public TNLPlayer getKiller() {
        if (getBukkitPlayer().getKiller() != null) {
            return NMSPlayer.cast(getBukkitPlayer().getKiller());
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
        throw new UnsupportedOperationException("method is not supported in this version");
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
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setGliding(boolean b) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public boolean isSwimming() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setSwimming(boolean b) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public boolean isRiptiding() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public boolean isSleeping() {
        return getBukkitPlayer().isSleeping();
    }

    @Override
    public void attack(@Nonnull Entity entity) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void swingMainHand() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void swingOffHand() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setCollidable(boolean b) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public boolean isCollidable() {
        throw new UnsupportedOperationException("method is not supported in this version");
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
        return getEntityPlayer().getHealth();
    }

    @Override
    public void setHealth(double v) {
        getBukkitPlayer().setHealth(v);
    }

    @Override
    public double getAbsorptionAmount() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setAbsorptionAmount(double v) {
        throw new UnsupportedOperationException("method is not supported in this version");
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
    public void uninject() {
        try {
            net.nonswag.tnl.listener.api.object.Objects<Channel> field = (net.nonswag.tnl.listener.api.object.Objects<Channel>) Reflection.getField(getNetworkManager(), "m");
            if (field.hasValue()) {
                Channel channel = field.nonnull();
                if (channel.pipeline().get(getName() + "-TNLListener") != null) {
                    channel.eventLoop().submit(() -> channel.pipeline().remove(getName() + "-TNLListener"));
                }
            } else {
                disconnect();
            }
        } catch (Exception ignored) {
        }
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
                            PlayerPacketEvent<Packet> event = new PlayerPacketEvent<>(player, ((Packet) packetObject));
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
                            PlayerPacketEvent<Packet> event = new PlayerPacketEvent<>(player, ((Packet) packetObject));
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
                net.nonswag.tnl.listener.api.object.Objects<Channel> field = (net.nonswag.tnl.listener.api.object.Objects<Channel>) Reflection.getField(getNetworkManager(), "m");
                if (field.hasValue()) {
                    Channel channel = field.nonnull();
                    ChannelPipeline pipeline = channel.pipeline();
                    try {
                        pipeline.addBefore("packet_handler", getName() + "-TNLListener", channelDuplexHandler);
                    } catch (Exception ignored) {
                        uninject();
                    }
                } else {
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
    @Nonnull
    public Set<String> getListeningPluginChannels() {
        return getBukkitPlayer().getListeningPluginChannels();
    }

    @Override
    public String toString() {
        return "NMSPlayer{" +
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
        NMSPlayer nmsPlayer = (NMSPlayer) o;
        return bukkitPlayer.equals(nmsPlayer.bukkitPlayer) && optionScoreboard.equals(nmsPlayer.optionScoreboard) && optionTeam.equals(nmsPlayer.optionTeam) && virtualStorage.equals(nmsPlayer.virtualStorage) && permissionManager.equals(nmsPlayer.permissionManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bukkitPlayer, optionScoreboard, optionTeam, virtualStorage, permissionManager);
    }

    @Override
    public int getId() {
        return getEntityPlayer().getId();
    }
}