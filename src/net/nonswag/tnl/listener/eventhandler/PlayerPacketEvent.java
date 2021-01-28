package net.nonswag.tnl.listener.eventhandler;

import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.utils.PacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.messaging.PluginChannelDirection;

import java.util.ArrayList;
import java.util.List;

public class PlayerPacketEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final TNLPlayer player;
    private final Packet<?> packet;
    private boolean cancelled = false;

    public PlayerPacketEvent(TNLPlayer player, Object packet) {
        this(player, (Packet<?>) packet);
    }

    public PlayerPacketEvent(TNLPlayer player, Packet<?> packet) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.packet = packet;
    }

    public TNLPlayer getPlayer() {
        return player;
    }

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

    public Packet<?> searchFor(String string, boolean ignoreCase) {
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

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancel) { cancelled = cancel; }
}
