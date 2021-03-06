package net.nonswag.tnl.listener.api.player;

import net.nonswag.tnl.listener.Bootstrap;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.entity.TNLEntityPlayer;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.packet.*;
import net.nonswag.tnl.listener.api.player.event.InteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class FakePlayer {

    @Nonnull
    private final TNLEntityPlayer player;
    @Nonnull
    private Location location;
    @Nonnull
    private Consumer<InteractEvent> onInteract = event -> {};

    public FakePlayer(@Nonnull String name, @Nonnull Location location) {
        this.location = location;
        this.player = TNLEntityPlayer.create(getLocation(), new GameProfile(name));
        getPlayer().setPing(32);
    }

    @Nonnull
    public TNLEntityPlayer getPlayer() {
        return player;
    }

    @Nonnull
    public Location getLocation() {
        return location;
    }

    @Nonnull
    public Consumer<InteractEvent> onInteract() {
        return onInteract;
    }

    @Nonnull
    public FakePlayer onInteract(@Nonnull Consumer<InteractEvent> onInteract) {
        this.onInteract = onInteract;
        return this;
    }

    @Nonnull
    public FakePlayer setLocation(@Nonnull Location location) {
        this.location = location;
        return this;
    }

    @Nonnull
    public FakePlayer setSkin(@Nonnull Skin skin) {
        getPlayer().getGameProfile().setSkin(skin);
        return this;
    }

    @Nonnull
    public FakePlayer setSkin(@Nonnull String player) {
        setSkin(Skin.getSkin(player));
        Logger.warn.println("Please use the properties <'" + getPlayer().getGameProfile().getSkin() + "'> instead of the name '" + player + "'>");
        return this;
    }

    @Nonnull
    public FakePlayer spawn(@Nonnull TNLPlayer receiver) {
        receiver.sendPacket(TNLPlayerInfo.create(getPlayer(), TNLPlayerInfo.Action.ADD_PLAYER));
        receiver.sendPacket(TNLNamedEntitySpawn.create(getPlayer()));
        receiver.sendPacket(TNLEntityMetadata.create(getPlayer()));
        receiver.sendPacket(TNLEntityEquipment.create(getPlayer()));
        receiver.sendPacket(TNLEntityHeadRotation.create(getPlayer()));
        Bukkit.getScheduler().runTaskLater(Bootstrap.getInstance(), () -> hideTabListName(receiver), 20);
        receiver.registerFakePlayer(this);
        return this;
    }

    @Nonnull
    public FakePlayer spawn() {
        for (TNLPlayer receiver : TNLListener.getInstance().getOnlinePlayers()) spawn(receiver);
        return this;
    }

    @Nonnull
    public FakePlayer sendStatus(@Nonnull TNLPlayer receiver, @Nonnull TNLEntityStatus.Status status) {
        receiver.sendPacket(TNLEntityStatus.create(getPlayer().getId(), status));
        return this;
    }

    @Nonnull
    public FakePlayer playAnimation(@Nonnull TNLPlayer receiver, @Nonnull TNLEntityAnimation.Animation animation) {
        receiver.sendPacket(TNLEntityAnimation.create(getPlayer(), animation));
        return this;
    }

    @Nonnull
    public FakePlayer setVelocity(@Nonnull TNLPlayer receiver, @Nonnull Vector vector) {
        receiver.sendPacket(TNLEntityVelocity.create(getPlayer().getId(), new Vector(vector.getX(), vector.getY(), vector.getZ())));
        return this;
    }

    @Nonnull
    public FakePlayer hideTabListName(@Nonnull TNLPlayer receiver) {
        receiver.sendPacket(TNLPlayerInfo.create(getPlayer(), TNLPlayerInfo.Action.REMOVE_PLAYER));
        return this;
    }

    @Nonnull
    public FakePlayer showTabListName(@Nonnull TNLPlayer receiver) {
        receiver.sendPacket(TNLPlayerInfo.create(getPlayer(), TNLPlayerInfo.Action.ADD_PLAYER));
        return this;
    }

    @Nonnull
    public FakePlayer deSpawn(@Nonnull TNLPlayer receiver) {
        receiver.sendPacket(TNLPlayerInfo.create(getPlayer(), TNLPlayerInfo.Action.REMOVE_PLAYER));
        receiver.sendPacket(TNLEntityDestroy.create(getPlayer()));
        receiver.unregisterFakePlayer(this);
        return this;
    }

    @Nonnull
    public FakePlayer deSpawn() {
        for (TNLPlayer receiver : TNLListener.getInstance().getOnlinePlayers()) deSpawn(receiver);
        return this;
    }

    enum Pose {
        STANDING(0),
        FALL_FLYING(1),
        SLEEPING(2),
        SWIMMING(3),
        SPIN_ATTACK(4),
        SNEAKING(5),
        DYING(6);

        private final int id;

        Pose(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
