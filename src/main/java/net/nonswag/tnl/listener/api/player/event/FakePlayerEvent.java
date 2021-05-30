package net.nonswag.tnl.listener.api.player.event;

import net.nonswag.tnl.listener.api.player.TNLFakePlayer;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;

public abstract class FakePlayerEvent extends PlayerEvent {

    @Nonnull
    private final TNLFakePlayer fakePlayer;

    protected FakePlayerEvent(@Nonnull TNLFakePlayer fakePlayer, @Nonnull TNLPlayer player) {
        super(player);
        this.fakePlayer = fakePlayer;
    }

    @Nonnull
    public TNLFakePlayer getFakePlayer() {
        return fakePlayer;
    }
}
