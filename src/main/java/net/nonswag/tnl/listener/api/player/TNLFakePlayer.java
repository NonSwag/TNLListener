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

public interface TNLFakePlayer {

    @Nonnull
    TNLEntityPlayer getPlayer();

    @Nonnull
    Location getLocation();

    @Nonnull
    Consumer<InteractEvent> onInteract();

    @Nonnull
    TNLFakePlayer onInteract(@Nonnull Consumer<InteractEvent> onInteract);

    @Nonnull
    TNLFakePlayer setLocation(@Nonnull Location location);

    @Nonnull
    default TNLFakePlayer setSkin(@Nonnull Skin skin) {
        getPlayer().getGameProfile().setSkin(skin);
        return this;
    }

    @Nonnull
    default TNLFakePlayer setSkin(@Nonnull String player) {
        setSkin(Skin.getSkin(player));
        Logger.warn.println("Please use the properties §8'§6" + getPlayer().getGameProfile().getSkin() + "§8'§a instead of the name §8'§6" + player + "§8'");
        return this;
    }

    @Nonnull
    default TNLFakePlayer spawn(@Nonnull TNLPlayer receiver) {
        receiver.sendPacket(TNLPlayerInfo.create(getPlayer(), TNLPlayerInfo.Action.ADD_PLAYER));
        receiver.sendPacket(TNLNamedEntitySpawn.create(getPlayer()));
        receiver.sendPacket(TNLEntityMetadata.create(getPlayer()));
        receiver.sendPacket(TNLEntityEquipment.create(getPlayer()));
        receiver.sendPacket(TNLEntityHeadRotation.create(getPlayer()));
        Bukkit.getScheduler().runTaskLater(Bootstrap.getInstance(), () -> hideTabListName(receiver), 20);
        return this;
    }

    @Nonnull
    default TNLFakePlayer spawn() {
        for (TNLPlayer receiver : TNLListener.getInstance().getOnlinePlayers()) spawn(receiver);
        return this;
    }

    @Nonnull
    default TNLFakePlayer sendStatus(@Nonnull TNLPlayer receiver, @Nonnull TNLEntityStatus.Status status) {
        receiver.sendPacket(TNLEntityStatus.create(getPlayer().getId(), status));
        return this;
    }

    @Nonnull
    default TNLFakePlayer playAnimation(@Nonnull TNLPlayer receiver, @Nonnull TNLEntityAnimation.Animation animation) {
        receiver.sendPacket(TNLEntityAnimation.create(getPlayer(), animation));
        return this;
    }

    @Nonnull
    TNLFakePlayer setVelocity(@Nonnull TNLPlayer receiver, @Nonnull Vector vector);

    @Nonnull
    default TNLFakePlayer hideTabListName(@Nonnull TNLPlayer receiver) {
        receiver.sendPacket(TNLPlayerInfo.create(getPlayer(), TNLPlayerInfo.Action.REMOVE_PLAYER));
        return this;
    }

    @Nonnull
    default TNLFakePlayer showTabListName(@Nonnull TNLPlayer receiver) {
        receiver.sendPacket(TNLPlayerInfo.create(getPlayer(), TNLPlayerInfo.Action.ADD_PLAYER));
        return this;
    }

    @Nonnull
    default TNLFakePlayer deSpawn(@Nonnull TNLPlayer receiver) {
        receiver.sendPacket(TNLPlayerInfo.create(getPlayer(), TNLPlayerInfo.Action.REMOVE_PLAYER));
        receiver.sendPacket(TNLEntityDestroy.create(getPlayer()));
        return this;
    }

    @Nonnull
    default TNLFakePlayer deSpawn() {
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

        @Override
        public String toString() {
            return "Pose{" +
                    "id=" + id +
                    '}';
        }
    }
}
