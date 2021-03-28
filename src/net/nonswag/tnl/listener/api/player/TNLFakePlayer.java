package net.nonswag.tnl.listener.api.player;

import com.mojang.authlib.GameProfile;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

public interface TNLFakePlayer<S, W, P> {

    @Nonnull
    S getServer();

    @Nonnull
    W getWorldServer();

    @Nonnull
    P getPlayer();

    @Nonnull
    String getName();

    @Nonnull
    Location getLocation();

    @Nonnull
    TNLPlayer[] getReceivers();

    @Nonnull
    GameProfile getProfile();

    @Nonnull
    Skin getSkin();

    void setServer(@Nonnull S server);

    void setWorldServer(@Nonnull W worldServer);

    void setPlayer(@Nonnull P player);

    void setName(@Nonnull String name);

    void setLocation(@Nonnull Location location);

    void setReceivers(@Nonnull TNLPlayer... receivers);

    void setProfile(@Nonnull GameProfile profile);

    void setSkin(@Nonnull Skin skin);

    void setSkin(@Nonnull String player);

    void spawn(@Nonnull TNLPlayer receiver);

    void spawn();

    void playAnimation(@Nonnull TNLPlayer receiver, @Nonnull Animation animation);

    void sendStatus(@Nonnull TNLPlayer receiver, @Nonnull Status status);

    void setVelocity(@Nonnull TNLPlayer receiver, @Nonnull Vector vector);

    void hideTablistName(@Nonnull TNLPlayer receiver);

    void showTablistName(@Nonnull TNLPlayer receiver);

    void deSpawn(@Nonnull TNLPlayer receiver);

    void deSpawn();

    enum Animation {
        SWING_HAND(0),
        SWING_OFFHAND(3),
        NORMAL_DAMAGE(1),
        CRITICAL_DAMAGE(4),
        MAGICAL_DAMAGE(5);

        private final int id;

        Animation(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Animation{" +
                    "id=" + id +
                    '}';
        }
    }

    enum Status {
        BURNING((byte) 0x01),
        CROUCHING((byte) 0x02),
        SPRINTING((byte) 0x08),
        SWIMMING((byte) 0x10),
        INVISIBLE((byte) 0x20),
        GLOWING((byte) 0x40),
        ELYTRA_FLY((byte) 0x80);

        private final byte id;

        Status(byte id) {
            this.id = id;
        }

        public byte getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Status{" +
                    "id=" + id +
                    '}';
        }
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
