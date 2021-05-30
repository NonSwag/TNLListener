package net.nonswag.tnl.listener.api.player.event;

import net.nonswag.tnl.listener.api.player.TNLFakePlayer;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;

public class InteractEvent extends FakePlayerEvent {

    @Nonnull
    private final Type type;

    protected InteractEvent(@Nonnull TNLPlayer player, @Nonnull TNLFakePlayer fakePlayer, @Nonnull Type type) {
        super(fakePlayer, player);
        this.type = type;
    }

    @Nonnull
    public Type getType() {
        return type;
    }

    public enum Type {
        LEFT_CLICK,
        RIGHT_CLICK
    }
}
