package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.entity.TNLEntity;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;

public abstract class TNLEntityStatus {

    @Nonnull
    public static Object create(int id, @Nonnull Status status) {
        Object packet;
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus();
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutEntityStatus();
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_6)) {
            packet = new net.minecraft.server.v1_7_R4.PacketPlayOutEntityStatus();
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            packet = new net.minecraft.server.v1_7_R1.PacketPlayOutEntityStatus();
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getRecentVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        Reflection.setField(packet, "a", id);
        Reflection.setField(packet, "b", status.getId());
        return packet;
    }

    @Nonnull
    public static Object create(@Nonnull TNLEntity entity, @Nonnull Status status) {
        return create(entity.getId(), status);
    }

    @Nonnull
    public static Object create(@Nonnull Entity entity, @Nonnull Status status) {
        return create(entity.getEntityId(), status);
    }

    public enum Status {
        BURNING((byte) 0x01),
        CROUCHING((byte) 0x02),
        SPRINTING((byte) 0x08),
        SWIMMING((byte) 0x10),
        INVISIBLE((byte) 0x20),
        GLOWING((byte) 0x40),
        ELYTRA_FLY((byte) 0x80),
        PERMISSION_LEVEL_4((byte) 28),
        PERMISSION_LEVEL_3((byte) 27),
        PERMISSION_LEVEL_2((byte) 26),
        PERMISSION_LEVEL_1((byte) 25),
        PERMISSION_LEVEL_0((byte) 24);

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
}
