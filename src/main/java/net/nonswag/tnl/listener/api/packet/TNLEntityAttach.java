package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.entity.TNLEntity;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.version.ServerVersion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TNLEntityAttach {

    @Nonnull
    public static Object create(@Nonnull TNLEntity holder, @Nullable TNLEntity leashed) {
        Object packet;
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutAttachEntity();
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutAttachEntity();
        } else {
            Logger.error.println("Version <'" + TNLListener.getInstance().getVersion().getRecentVersion() + "'> is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        Reflection.setField(packet, "a", holder.getId());
        Reflection.setField(packet, "b", leashed != null ? leashed.getId() : 0);
        return packet;
    }
}
