package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.Version;

import javax.annotation.Nonnull;

public abstract class TNLUpdateTime {

    @Nonnull
    public static Object create(long timestamp) {
        if (TNLListener.getInstance().getVersion().equals(Version.v1_16_4)) {
            return new net.minecraft.server.v1_16_R3.PacketPlayOutUpdateTime(timestamp, timestamp, true);
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_15_2)) {
            return new net.minecraft.server.v1_15_R1.PacketPlayOutUpdateTime(timestamp, timestamp, true);
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_7_6)) {
            return new net.minecraft.server.v1_7_R4.PacketPlayOutUpdateTime(timestamp, timestamp, true);
        } else if (TNLListener.getInstance().getVersion().equals(Version.v1_7_2)) {
            return new net.minecraft.server.v1_7_R1.PacketPlayOutUpdateTime(timestamp, timestamp, true);
        } else {
            Logger.error.println("Version <'" + TNLListener.getInstance().getVersion().getRecentVersion() + "'> is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }
}
