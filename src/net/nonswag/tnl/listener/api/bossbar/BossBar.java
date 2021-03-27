package net.nonswag.tnl.listener.api.bossbar;

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
}
