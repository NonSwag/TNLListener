package net.nonswag.tnl.listener.eventhandler;

import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.utils.PacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.messaging.PluginChannelDirection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PlayerPacketEvent extends Event implements Cancellable {

    @Nonnull private static final HandlerList handlers = new HandlerList();
    @Nonnull private final TNLPlayer player;
    @Nonnull private final Packet<?> packet;
    private boolean cancelled = false;

    public <P> PlayerPacketEvent(@Nonnull TNLPlayer player, @Nonnull P packet) {
        this(player, (Packet<?>) packet);
    }

    public PlayerPacketEvent(@Nonnull TNLPlayer player, @Nonnull Packet<?> packet) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.packet = packet;
    }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    @Nonnull
    public Packet<?> getPacket() {
        return packet;
    }

    @Nonnull
    public String getPacketName() { return PacketUtil.getPacketName(this.packet); }

    public boolean isIncoming() { return getPacketName().contains("PlayIn"); }

    public boolean isOutgoing() { return getPacketName().contains("PlayOut"); }

    public void debug(boolean console, boolean player) {
        if(console) {
            Logger.info.println("----- " + getPacketName() + " -----");
        }
        if(player && !(getPacket() instanceof PacketPlayOutChat)) {
            getPlayer().sendMessage("----- " + getPacketName() + " -----");
        }
        for (String field : getPacketFields()) {
            if(console) {
                Logger.info.println(field + " = '" + getPacketField(field) + "'");
            }
            if(player && !(getPacket() instanceof PacketPlayOutChat)) {
                getPlayer().sendMessage(field + " = '" + getPacketField(field) + "'");
            }
        }
    }

    @Nullable
    public Packet<?> searchFor(@Nonnull String string, boolean ignoreCase) {
        for (String field : getPacketFields()) {
            Object packetField = getPacketField(field);
            if(packetField != null) {
                if (ignoreCase) {
                    if (packetField.toString().toLowerCase().contains(string.toLowerCase())) {
                        return getPacket();
                    }
                } else {
                    if (packetField.toString().contains(string)) {
                        return getPacket();
                    }
                }
            }
        }
        return null;
    }

    public void setPacketField(@Nonnull String packetField, @Nullable Object value) {
        PacketUtil.setPacketField(getPacket(), packetField, value);
    }

    public void failedToWrite() {
        if (!getPacketFields().isEmpty()) {
            List<String> strings = new ArrayList<>();
            for (String field : getPacketFields()) {
                strings.add("§8(§7field§8: §6" + field + " §8-> '§6" + getPacketField(field) + "§8')");
            }
            getPlayer().disconnect(TNLListener.getInstance().getPrefix() + "\n§cFailed to write Packet" +
                    "\n§4" + getPacketName() + "\n\n" + String.join("\n", strings).replace("null", "-/-"));
        } else {
            getPlayer().disconnect(TNLListener.getInstance().getPrefix() + "\n§cFailed to write Packet" +
                    "\n§4" + getPacketName());
        }
    }

    @Nonnull
    public PluginChannelDirection getPluginChannelDirection() {
        return isOutgoing() ? PluginChannelDirection.OUTGOING : PluginChannelDirection.INCOMING;
    }

    @Nullable
    public Object getPacketField(@Nonnull String field) {
        return PacketUtil.getPacketField(field, this.packet);
    }

    @Nonnull
    public List<String> getPacketFields() { return PacketUtil.getPacketFields(this.packet); }

    @Override
    @Nonnull
    public HandlerList getHandlers() { return handlers; }

    @Nonnull
    public static HandlerList getHandlerList() { return handlers; }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancel) { cancelled = cancel; }
}
