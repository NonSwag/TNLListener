package net.nonswag.tnl.listener.api.bossbar;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;

import javax.annotation.Nonnull;

public interface BossBar<CBB> {

    @Nonnull
    String getId();

    @Nonnull
    String getText();

    @Nonnull
    BarColor getColor();

    @Nonnull
    BarStyle getStyle();

    @Nonnull
    BarFlag[] getBarFlags();

    double getProgress();

    @Nonnull
    BossBar<CBB> setText(@Nonnull String text);

    @Nonnull
    BossBar<CBB> setColor(@Nonnull BarColor color);

    @Nonnull
    BossBar<CBB> setStyle(@Nonnull BarStyle style);

    @Nonnull
    BossBar<CBB> setBarFlags(@Nonnull BarFlag... barFlags);

    @Nonnull
    BossBar<CBB> setProgress(double progress);

    @Nonnull
    CBB getBossBar();

    @Nonnull
    static BossBar<?> create(@Nonnull String id, @Nonnull Object text, @Nonnull BarColor color, @Nonnull BarStyle style, double progress, @Nonnull BarFlag... barFlags) {
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5)) {
            return new net.nonswag.tnl.listener.api.bossbar.v1_16.R3.NMSBossBar(id, text, color, style, progress, barFlags);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            return new net.nonswag.tnl.listener.api.bossbar.v1_15.R1.NMSBossBar(id, text, color, style, progress, barFlags);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_10)) {
            throw new UnsupportedOperationException("method is not supported in this version");
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            throw new UnsupportedOperationException("method is not supported in this version");
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }
}
