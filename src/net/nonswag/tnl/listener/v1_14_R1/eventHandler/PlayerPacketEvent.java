package net.nonswag.tnl.listener.v1_14_R1.eventHandler;

import com.sun.istack.internal.NotNull;
import net.minecraft.server.v1_14_R1.Packet;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.v1_14_R1.api.playerAPI.TNLPlayer;
import net.nonswag.tnl.listener.v1_14_R1.utils.PacketUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.messaging.PluginChannelDirection;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerPacketEvent extends Event implements Cancellable {

    private static HandlerList handlers = new HandlerList();
    private Player player;
    private Packet packet;

    public PlayerPacketEvent(Player player, Object packet) {
        super(true);
        try {
            this.player = player;
            this.packet = (Packet) packet;
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public static void setHandlers(HandlerList handlers) { PlayerPacketEvent.handlers = handlers; }

    public Player getPlayer() { return this.player; }

    public Packet getPacket() { return this.packet; }

    @Override
    public String getEventName() { return super.getEventName(); }

    public void setPlayer(Player player) { this.player = player; }

    @Override
    protected void finalize() throws Throwable { super.finalize(); }

    @Override
    protected Object clone() throws CloneNotSupportedException { return super.clone(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerPacketEvent that = (PlayerPacketEvent) o;
        return cancelled == that.cancelled &&
                Objects.equals(player, that.player) &&
                Objects.equals(packet, that.packet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, packet, cancelled);
    }

    public void setPacket(Packet packet) { this.packet = packet; }

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
    public Packet searchFor(@NotNull String string, boolean ignoreCase) {
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

    @Override
    public String toString() {
        return "PlayerPacketEvent{" +
                "player=" + player +
                ", packet=" + packet +
                ", cancelled=" + cancelled +
                '}';
    }

    public void setPacketField(String packetField, Object value) {
        try {
            if (getPacketField(packetField) != null) {
                Field field = packet.getClass().getDeclaredField(packetField);
                field.setAccessible(true);
                field.set(packet, value);
                setPacket(packet);
            }
        } catch (Throwable t) {
            if (t instanceof IllegalArgumentException) {
                NMSMain.stacktrace("Cannot set packet field '" + packetField + ", " +
                        getPacketField(packetField).getClass().getName() + "' to '" + value + ", " +
                        value.getClass().getName() + "'");
            } else {
                NMSMain.stacktrace(t);
            }
        }
    }

    public void failedToWrite() {
        if (getPacketFields() != null && getPacketFields().size() > 0) {
            List<String> strings = new ArrayList<>();
            for (String field : getPacketFields()) {
                strings.add("§8(§7field§8: §6" + field + " §8-> '§6" + getPacketField(field) + "§8')");
            }
            new TNLPlayer(getPlayer()).disconnect(NMSMain.getPrefix() + "\n§cFailed to write Packet" +
                    "\n§4" + getPacketName() + "\n\n" + String.join("\n", strings).replace("null", "-/-"));
        } else {
            new TNLPlayer(getPlayer()).disconnect(NMSMain.getPrefix() + "\n§cFailed to write Packet" +
                    "\n§4" + getPacketName());
        }
    }

    public PluginChannelDirection getPluginChannelDirection() {
        return isOutgoing() ? PluginChannelDirection.OUTGOING : PluginChannelDirection.INCOMING;
    }

    public Object getPacketField(String packetField) {
        return PacketUtil.getPacketField(this.packet, packetField);
    }

    public List<String> getPacketFields() { return PacketUtil.getPacketFields(this.packet); }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

    private boolean cancelled = false;

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancel) { cancelled = cancel; }

}
