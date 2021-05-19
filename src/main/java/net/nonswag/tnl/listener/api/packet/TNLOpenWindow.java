package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.math.Range;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.version.ServerVersion;

import javax.annotation.Nonnull;

public abstract class TNLOpenWindow {

    @Nonnull
    public static Object create(@Range(from = 0, to = 6) int size, @Nonnull String title) {
        Object packet;
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4) || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5)) {
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutOpenWindow();
            Reflection.setField(packet, "c", new net.minecraft.server.v1_16_R3.ChatMessage(title));
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutOpenWindow();
            Reflection.setField(packet, "c", new net.minecraft.server.v1_15_R1.ChatMessage(title));
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        Reflection.setField(packet, "a", 1);
        Reflection.setField(packet, "b", size);
        return packet;
    }
}
