package net.nonswag.tnl.listener.api.player.v1_16.R3;

import net.minecraft.server.v1_16_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_16_R3.Vec3D;
import net.nonswag.tnl.listener.api.entity.TNLEntityPlayer;
import net.nonswag.tnl.listener.api.player.GameProfile;
import net.nonswag.tnl.listener.api.player.TNLFakePlayer;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.player.event.InteractEvent;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class NMSFakePlayer implements TNLFakePlayer {

    @Nonnull
    private Location location;
    @Nonnull
    private final TNLEntityPlayer player;
    @Nonnull
    private Consumer<InteractEvent> onInteract = event -> {};

    public NMSFakePlayer(@Nonnull String name, @Nonnull Location location) {
        this.location = location;
        this.player = TNLEntityPlayer.create(getLocation(), new GameProfile(name));
        getPlayer().setPing(32);
    }

    @Nonnull
    @Override
    public NMSFakePlayer setVelocity(@Nonnull TNLPlayer receiver, @Nonnull Vector vector) {
        receiver.sendPacket(new PacketPlayOutEntityVelocity(getPlayer().getId(), new Vec3D(vector.getX(), vector.getY(), vector.getZ())));
        return this;
    }

    @Nonnull
    @Override
    public Consumer<InteractEvent> onInteract() {
        return onInteract;
    }

    @Nonnull
    @Override
    public NMSFakePlayer onInteract(@Nonnull Consumer<InteractEvent> onInteract) {
        this.onInteract = onInteract;
        return this;
    }

    @Nonnull
    @Override
    public NMSFakePlayer setLocation(@Nonnull Location location) {
        this.location = location;
        return this;
    }

    @Override
    @Nonnull
    public TNLEntityPlayer getPlayer() {
        return player;
    }

    @Override
    @Nonnull
    public Location getLocation() {
        return location;
    }
}
