package net.nonswag.tnl.listener.api.player.event;

import net.nonswag.tnl.listener.api.player.FakePlayer;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;

public abstract class FakePlayerEvent extends PlayerEvent {

    @Nonnull
    private final FakePlayer fakePlayer;

    public FakePlayerEvent(@Nonnull FakePlayer fakePlayer, @Nonnull TNLPlayer player) {
        super(player);
        this.fakePlayer = fakePlayer;
    }

    @Nonnull
    public FakePlayer getFakePlayer() {
        return fakePlayer;
    }
}
