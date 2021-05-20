package net.nonswag.tnl.listener.events;

import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PlayerItemPickEvent extends TNLEvent {

    @Nonnull
    private final TNLPlayer player;
    private final int slot;

    public PlayerItemPickEvent(@Nonnull TNLPlayer player, int slot) {
        this.player = player;
        this.slot = slot;
    }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    public int getSlot() {
        return slot;
    }

    @Override
    public String toString() {
        return "PlayerItemPickEvent{" +
                "player=" + player +
                ", slot=" + slot +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PlayerItemPickEvent that = (PlayerItemPickEvent) o;
        return slot == that.slot && player.equals(that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), player, slot);
    }
}
