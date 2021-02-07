package net.nonswag.tnl.bridge.packets;

import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListener;
import net.nonswag.tnl.listener.api.object.Set;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

public class PlayerInfoPacket implements Packet<PacketListener> {

    @Nonnull private final UUID uniqueId;
    @Nonnull private final Set<?, ?> set;

    public PlayerInfoPacket(@Nonnull UUID uniqueId, @Nonnull Set<?, ?> set) {
        this.uniqueId = uniqueId;
        this.set = set;
    }

    @Nonnull
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Nonnull
    public Set<?, ?> getSet() {
        return set;
    }

    @Override
    public String toString() {
        return "PlayerInfoPacket{" +
                "uniqueId=" + uniqueId +
                ", set=" + set +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerInfoPacket that = (PlayerInfoPacket) o;
        return uniqueId.equals(that.uniqueId) && set.equals(that.set);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId, set);
    }
}
