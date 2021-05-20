package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.object.Objects;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import org.bukkit.plugin.messaging.PluginChannelDirection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PlayerPacketEvent<Packet> extends TNLEvent {

    @Nonnull
    private final TNLPlayer player;
    @Nonnull
    private final Packet packet;

    public PlayerPacketEvent(@Nonnull TNLPlayer player, @Nonnull Packet packet) {
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
    public String toString() {
        return "PlayerPacketEvent{" +
                "player=" + player +
                ", packet=" + packet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PlayerPacketEvent<?> that = (PlayerPacketEvent<?>) o;
        return player.equals(that.player) && packet.equals(that.packet);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), player, packet);
    }
}
