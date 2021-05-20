package net.nonswag.tnl.listener.api.player.v1_15.R1;

import io.netty.channel.*;
import net.minecraft.server.v1_15_R1.*;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.bossbar.TNLBossBar;
import net.nonswag.tnl.listener.api.bossbar.v1_15.R1.NMSBossBar;
import net.nonswag.tnl.listener.api.gui.GUI;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.object.Generic;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.sign.SignMenu;
import net.nonswag.tnl.listener.api.storage.VirtualStorage;
import net.nonswag.tnl.listener.events.PlayerPacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NMSPlayer implements TNLPlayer {

    @Nonnull
    private static final HashMap<UUID, List<String>> bossBars = new HashMap<>();
    @Nonnull
    private static final HashMap<UUID, NMSBossBar> bossHashMap = new HashMap<>();
    @Nonnull
    private final Player bukkitPlayer;
    @Nonnull
    private final VirtualStorage virtualStorage = new VirtualStorage();

    protected NMSPlayer(@Nonnull Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
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

    @Nonnull
    @Override
    public WorldServer getWorldServer() {
        return ((CraftWorld) getWorld()).getHandle();
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
    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    @Override
    public void sendPacket(@Nonnull Object packet) {
        getPlayerConnection().sendPacket((Packet<?>) packet);
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
        Material material = Material.getMaterial(signMenu.getType().name());
        if (material != null) {
            sendBlockChange(signMenu.getLocation(), material.createBlockData());
        } else {
            sendBlockChange(signMenu.getLocation(), Material.SPRUCE_WALL_SIGN.createBlockData());
        }
        PacketPlayOutTileEntityData updatePacket = tileEntitySign.getUpdatePacket();
        assert updatePacket != null;
        sendPacket(updatePacket);
        sendPacket(editor);
        getVirtualStorage().put("current-sign", signMenu);
    }

    @Override
    public void exitCombat() {
        getEntityPlayer().exitCombat();
    }

    @Override
    public void enterCombat() {
        getEntityPlayer().enterCombat();
    }

    @Override
    public void openGUI(@Nonnull GUI gui) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void hideTabListName(@Nonnull TNLPlayer[] players) {
        for (TNLPlayer all : players) {
            if (!all.getUniqueId().equals(getUniqueId())) {
                sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((NMSPlayer) all).getEntityPlayer()));
            }
        }
    }

    @Override
    public void disguise(@Nonnull Generic<?> entity, @Nonnull TNLPlayer receiver) {
        if (!receiver.getUniqueId().equals(getUniqueId()) && entity.getParameter() instanceof EntityLiving) {
            receiver.sendPacket(new PacketPlayOutEntityDestroy(this.getId()));
            ((EntityLiving) entity.getParameter()).setLocation(getLocation().getX(), getLocation().getY(), getLocation().getZ(), getLocation().getYaw(), getLocation().getPitch());
            ((EntityLiving) entity.getParameter()).world = this.getWorldServer();
            Reflection.setField(entity, net.minecraft.server.v1_15_R1.Entity.class, "id", this.getId());
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

    @Override
    public void sendBossBar(@Nonnull TNLBossBar<?> bossBar, long millis) {
        TNLBossBar.BOSS_BARS.put(bossBar.getId(), bossBar);
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
            if (getBossHashMap().get(getBukkitPlayer().getUniqueId()) != null && getBossHashMap().get(getBukkitPlayer().getUniqueId()).equals(bossBar)) {
                hideBossBar(getBossHashMap().get(getBukkitPlayer().getUniqueId()));
            }
        }).start();
    }

    @Override
    public void sendBossBar(@Nonnull TNLBossBar<?> bossBar) {
        TNLBossBar.BOSS_BARS.put(bossBar.getId(), bossBar);
        if (!getBossBars(getUniqueId()).contains(bossBar.getId())) {
            sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.ADD, ((NMSBossBar) bossBar).getBossBar().getHandle()));
            List<String> bars = getBossBars(getUniqueId());
            bars.add(bossBar.getId());
            getBossBars().put(getUniqueId(), bars);
        }
        updateBossBar(bossBar);
    }

    @Override
    public void updateBossBar(@Nonnull TNLBossBar<?> bossBar) {
        TNLBossBar.BOSS_BARS.put(bossBar.getId(), bossBar);
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

    @Override
    public void hideBossBar(@Nonnull TNLBossBar<?> bossBar) {
        TNLBossBar<?> bar = TNLBossBar.get(bossBar.getId());
        if (bar != null) bossBar = bar;
        sendPacket(new PacketPlayOutBoss(PacketPlayOutBoss.Action.REMOVE, ((NMSBossBar) bossBar).getBossBar().getHandle()));
        List<String> bars = getBossBars(getUniqueId());
        bars.remove(bossBar.getId());
        getBossBars().put(getUniqueId(), bars);
        TNLBossBar.BOSS_BARS.remove(bossBar.getId());
    }

    @Override
    public void sendActionbar(@Nonnull String actionbar) {
        sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + actionbar + "\"}"), ChatMessageType.a((byte) 2)));
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
    @Nonnull
    public VirtualStorage getVirtualStorage() {
        return virtualStorage;
    }

    @Override
    public void setPlayerTime(long time, boolean b) {
        getBukkitPlayer().setPlayerTime(time, b);
        sendPacket(new PacketPlayOutUpdateTime(time, time, true));
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
    public void setArrowCount(int arrows) {
        getEntityPlayer().setArrowCount(arrows);
    }

    @Override
    public void setGlowing(boolean glowing, @Nonnull TNLPlayer player) {
        boolean current = ((NMSPlayer) player).getEntityPlayer().glowing;
        ((NMSPlayer) player).getEntityPlayer().glowing = glowing;
        sendPacket(new PacketPlayOutEntityMetadata(player.getId(), ((NMSPlayer) player).getEntityPlayer().getDataWatcher(), true));
        ((NMSPlayer) player).getEntityPlayer().glowing = current;
    }

    @Override
    public void setGlowing(@Nonnull Entity entity, boolean glowing) {
        DataWatcher dataWatcher = ((CraftEntity) entity).getHandle().getDataWatcher();
        dataWatcher.set(DataWatcherRegistry.a.a(0), (byte) 0x40);
        sendPacket(new PacketPlayOutEntityMetadata(entity.getEntityId(), dataWatcher, true));
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
                            if (event.call()) super.channelRead(channelHandlerContext, event.getPacket());
                        } catch (Exception e) {
                            Logger.error.println(e);
                            uninject();
                        }
                    }

                    @Override
                    public void write(ChannelHandlerContext channelHandlerContext, Object packetObject, ChannelPromise channelPromise) {
                        try {
                            PlayerPacketEvent<Packet<?>> event = new PlayerPacketEvent<>(player, ((Packet<?>) packetObject));
                            if (event.call()) super.write(channelHandlerContext, event.getPacket(), channelPromise);
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
        return getName();
    }
}
