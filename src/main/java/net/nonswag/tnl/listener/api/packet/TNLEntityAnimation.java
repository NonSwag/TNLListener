package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.entity.TNLEntityPlayer;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.ServerVersion;

import javax.annotation.Nonnull;

public abstract class TNLEntityAnimation {

    @Nonnull
    public static Object create(@Nonnull TNLEntityPlayer player, @Nonnull Animation animation) {
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            return new net.minecraft.server.v1_16_R3.PacketPlayOutAnimation((net.minecraft.server.v1_16_R3.EntityPlayer) player, animation.getId());
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            return new net.minecraft.server.v1_15_R1.PacketPlayOutAnimation((net.minecraft.server.v1_15_R1.EntityPlayer) player, animation.getId());
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_6)) {
            return new net.minecraft.server.v1_7_R4.PacketPlayOutAnimation((net.minecraft.server.v1_7_R4.EntityPlayer) player, animation.getId());
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            return new net.minecraft.server.v1_7_R1.PacketPlayOutAnimation((net.minecraft.server.v1_7_R1.EntityPlayer) player, animation.getId());
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getRecentVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }

    public enum Animation {
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
}
