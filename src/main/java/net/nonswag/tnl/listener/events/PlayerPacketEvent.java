package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.object.Objects;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.messaging.PluginChannelDirection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PlayerPacketEvent<Packet> extends Event implements Cancellable {

    @Nonnull
    private static final HandlerList handlers = new HandlerList();
    @Nonnull
    private final TNLPlayer player;
    @Nonnull
    private final Packet packet;
    private boolean cancelled = false;

    public PlayerPacketEvent(@Nonnull TNLPlayer player, @Nonnull Packet packet) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.packet = packet;
    }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    @Nonnull
    public Packet getPacket() {
        return packet;
    }

    @Nonnull
    public String getPacketName() {
        return packet.getClass().getSimpleName();
    }

    public boolean isIncoming() {
        return getPacketName().contains("PlayIn");
    }

    public boolean isOutgoing() {
        return getPacketName().contains("PlayOut");
    }

    @Nullable
    public Packet searchFor(@Nonnull String string, boolean ignoreCase) {
        for (String field : getPacketFields()) {
            Objects<?> packetField = getPacketField(field);
            if (packetField.hasValue()) {
                if (ignoreCase) {
                    if (packetField.nonnull().toString().toLowerCase().contains(string.toLowerCase())) {
                        return getPacket();
                    }
                } else {
                    if (packetField.nonnull().toString().contains(string)) {
                        return getPacket();
                    }
                }
            }
        }
        return null;
    }

    public void setPacketField(@Nonnull String packetField, @Nullable Object value) {
        Reflection.setField(getPacket(), packetField, value);
    }

    public void failedToWrite() {
        setCancelled(true);
        if (!getPacketFields().isEmpty()) {
            List<String> strings = new ArrayList<>();
            for (String field : getPacketFields()) {
                strings.add("§8(§7field§8: §6" + field + " §8-> '§6" + getPacketField(field, Object.class).getOrDefault("-/-") + "§8')");
            }
            getPlayer().disconnect("§cFailed to write Packet\n§4" + getPacketName() + "\n\n" + String.join("\n", strings).replace("null", "-/-"));
        } else {
            getPlayer().disconnect("§cFailed to write Packet\n§4" + getPacketName());
        }
    }

    @Nonnull
    public PluginChannelDirection getPluginChannelDirection() {
        return isOutgoing() ? PluginChannelDirection.OUTGOING : PluginChannelDirection.INCOMING;
    }

    @Nonnull
    public Objects<?> getPacketField(@Nonnull String field) {
        return Reflection.getField(packet, field);
    }

    @Nonnull
    public <T> Objects<T> getPacketField(@Nonnull String field, @Nonnull Class<? extends T> clazz) {
        return (Objects<T>) Reflection.getField(packet, field);
    }

    @Nonnull
    public List<String> getPacketFields() {
        return Reflection.getFields(packet.getClass());
    }

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return handlers;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
