package net.nonswag.tnl.listener.v1_15_R1.eventHandler;

import com.sun.istack.internal.NotNull;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.v1_15_R1.api.playerAPI.TNLPlayer;
import net.nonswag.tnl.listener.v1_15_R1.utils.PacketUtil;
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

    private static HandlerList handlers = new HandlerList();
    @Nonnull private final TNLPlayer player;
    @Nonnull private final Packet<?> packet;

    public PlayerPacketEvent(@Nonnull TNLPlayer player, @Nonnull Object packet) {
        this(player, (Packet<?>) packet);
    }

    public PlayerPacketEvent(@Nonnull TNLPlayer player, @Nonnull Packet<?> packet) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.packet = packet;
    }

    public static void setHandlers(HandlerList handlers) { PlayerPacketEvent.handlers = handlers; }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    @Nonnull
    public Packet<?> getPacket() {
        return packet;
    }

    public String getPacketName() { return PacketUtil.getPacketName(this.packet); }

    public boolean isIncoming() { return getPacketName().contains("PlayIn"); }

    public boolean isOutgoing() { return getPacketName().contains("PlayOut"); }

    public void debug(boolean console, boolean player) {
        if(console) {
            NMSMain.print("----- " + getPacketName() + " -----");
        }
        if(player && !(getPacket() instanceof PacketPlayOutChat)) {
            getPlayer().sendMessage("----- " + getPacketName() + " -----");
        }
        for (String field : getPacketFields()) {
            if(console) {
                NMSMain.print(field + " = '" + getPacketField(field) + "'");
            }
            if(player && !(getPacket() instanceof PacketPlayOutChat)) {
                getPlayer().sendMessage(field + " = '" + getPacketField(field) + "'");
            }
        }
    }

    @Nullable
    public Packet<?> searchFor(@NotNull String string, boolean ignoreCase) {
        for (String field : getPacketFields()) {
            if(getPacketField(field) != null) {
                if (ignoreCase) {
                    if (getPacketField(field).toString().toLowerCase().contains(string.toLowerCase())) {
                        return getPacket();
                    }
                } else {
                    if (getPacketField(field).toString().contains(string)) {
                        return getPacket();
                    }
                }
            }
        }
        return null;
    }

    public void setPacketField(String packetField, Object value) {
        PacketUtil.setPacketField(getPacket(), packetField, value);
    }

    public void failedToWrite() {
        if (getPacketFields() != null && getPacketFields().size() > 0) {
            List<String> strings = new ArrayList<>();
            for (String field : getPacketFields()) {
                strings.add("§8(§7field§8: §6" + field + " §8-> '§6" + getPacketField(field) + "§8')");
            }
            getPlayer().disconnect(NMSMain.getPrefix() + "\n§cFailed to write Packet" +
                    "\n§4" + getPacketName() + "\n\n" + String.join("\n", strings).replace("null", "-/-"));
        } else {
            getPlayer().disconnect(NMSMain.getPrefix() + "\n§cFailed to write Packet" +
                    "\n§4" + getPacketName());
        }
    }

    public PluginChannelDirection getPluginChannelDirection() {
        return isOutgoing() ? PluginChannelDirection.OUTGOING : PluginChannelDirection.INCOMING;
    }

    public Object getPacketField(String packetField) {
        return PacketUtil.getPacketField(packetField, this.packet);
    }

    public List<String> getPacketFields() { return PacketUtil.getPacketFields(this.packet); }

    @Nonnull
    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

    private boolean cancelled = false;

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancel) { cancelled = cancel; }

}
