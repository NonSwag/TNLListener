package net.nonswag.tnl.listener.api.player.v1_7.R4;

import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.io.netty.channel.*;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.bossbar.TNLBossBar;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.object.Generic;
import net.nonswag.tnl.listener.api.player.Skin;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.sign.SignMenu;
import net.nonswag.tnl.listener.api.storage.VirtualStorage;
import net.nonswag.tnl.listener.api.title.Title;
import net.nonswag.tnl.listener.events.PlayerPacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NMSPlayer implements TNLPlayer {

    @Nonnull
    private final Player bukkitPlayer;
    @Nonnull
    private final VirtualStorage virtualStorage = new VirtualStorage();

    public NMSPlayer(@Nonnull Player player) {
        this.bukkitPlayer = player;
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
    public void setPing(int ping) {
        getEntityPlayer().ping = ping;
    }

    @Override
    public void sendPacket(@Nonnull Object packet) {
        getPlayerConnection().sendPacket((Packet) packet);
    }

    @Override
    public void openVirtualSignEditor(@Nonnull SignMenu signMenu) {
        Location location = new Location(getWorld(), getLocation().getBlockX(), getLocation().getBlockY() - 5, getLocation().getBlockZ());
        signMenu.setLocation(location);
        TileEntitySign tileEntitySign = new TileEntitySign();
        tileEntitySign.a(getWorldServer());
        tileEntitySign.x = location.getBlockX();
        tileEntitySign.y = location.getBlockY();
        tileEntitySign.z = location.getBlockZ();
        for (int line = 0; line < signMenu.getLines().length; line++) {
            tileEntitySign.lines[line] = signMenu.getLines()[line];
        }
        sendPacket(tileEntitySign.getUpdatePacket());
        sendPacket(new PacketPlayOutOpenSignEditor(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    @Override
    public void setSkin(@Nonnull Skin skin, @Nonnull TNLPlayer player) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setCooldown(@Nonnull Material material, int i) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void exitCombat() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void enterCombat() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void hideTabListName(@Nonnull TNLPlayer[] players) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

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

    @Override
    public void sendBossBar(@Nonnull TNLBossBar<?> TNLBossBar) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void sendBossBar(@Nonnull TNLBossBar<?> TNLBossBar, long millis) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void updateBossBar(@Nonnull TNLBossBar<?> TNLBossBar) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void hideBossBar(@Nonnull TNLBossBar<?> TNLBossBar) {
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
    @Nonnull
    public VirtualStorage getVirtualStorage() {
        return virtualStorage;
    }

    @Nullable
    @Override
    public Entity getShoulderEntityLeft() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setShoulderEntityLeft(@Nullable Entity entity) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Nullable
    @Override
    public Entity getShoulderEntityRight() {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setShoulderEntityRight(@Nullable Entity entity) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Nonnull
    @Override
    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    @Override
    public void setArrowCount(int arrows) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    @Nonnull
    public WorldServer getWorldServer() {
        return ((CraftWorld) getBukkitPlayer().getWorld()).getHandle();
    }

    @Override
    public void setGlowing(boolean b, @Nonnull TNLPlayer player) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void setGlowing(@Nonnull Entity entity, boolean b) {
        throw new UnsupportedOperationException("method is not supported in this version");
    }

    @Override
    public void uninject() {
        try {
            net.nonswag.tnl.listener.api.object.Objects<Channel> field = (net.nonswag.tnl.listener.api.object.Objects<Channel>) Reflection.getField(getNetworkManager(), "k");
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
                ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
                    @Override
                    public void channelRead(ChannelHandlerContext channelHandlerContext, Object packetObject) {
                        try {
                            PlayerPacketEvent<Packet> event = new PlayerPacketEvent<>(NMSPlayer.this, ((Packet) packetObject));
                            if (event.call()) super.channelRead(channelHandlerContext, event.getPacket());
                        } catch (Exception e) {
                            Logger.error.println(e.getMessage());
                            uninject();
                        }
                    }

                    @Override
                    public void write(ChannelHandlerContext channelHandlerContext, Object packetObject, ChannelPromise channelPromise) {
                        try {
                            PlayerPacketEvent<Packet> event = new PlayerPacketEvent<>(NMSPlayer.this, ((Packet) packetObject));
                            if (event.call()) super.write(channelHandlerContext, event.getPacket(), channelPromise);
                        } catch (Exception e) {
                            Logger.error.println(e.getMessage());
                            uninject();
                        }

                    }
                };
                net.nonswag.tnl.listener.api.object.Objects<Channel> field = (net.nonswag.tnl.listener.api.object.Objects<Channel>) Reflection.getField(getNetworkManager(), "k");
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
                Logger.error.println("Failed to inject '" + getName() + "'>", "The player can't be offline");
                disconnect("%prefix%\n" + "§cYou are online but your connection is offline?!");
            }
        } catch (Exception e) {
            uninject();
            Logger.error.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
