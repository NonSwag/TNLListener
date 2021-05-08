package net.nonswag.tnl.listener.api.bossbar.v1_16.R3;

import net.nonswag.tnl.listener.api.bossbar.TNLBossBar;
import net.nonswag.tnl.listener.utils.MathUtil;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.craftbukkit.v1_16_R3.boss.CraftBossBar;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class NMSBossBar implements TNLBossBar<CraftBossBar> {

    @Nonnull
    private final String id;
    @Nonnull
    private String text;
    @Nonnull
    private BarColor color;
    @Nonnull
    private BarStyle style;
    @Nonnull
    private BarFlag[] barFlags;
    private double progress;
    @Nonnull
    private final CraftBossBar bossBar;

    public NMSBossBar(@Nonnull String id, @Nonnull Object text, @Nonnull BarColor color, @Nonnull BarStyle style, double progress, @Nonnull BarFlag... barFlags) {
        this.id = id;
        this.text = text.toString();
        this.color = color;
        this.style = style;
        this.barFlags = barFlags;
        this.progress = MathUtil.validateDouble(0, 1, progress);
        this.bossBar = new CraftBossBar(getText(), getColor(), getStyle(), getBarFlags());
        setProgress(getProgress());
    }

    public NMSBossBar(@Nonnull String id, @Nonnull Object text, @Nonnull BarColor color, @Nonnull BarStyle style, @Nonnull BarFlag... barFlags) {
        this(id, text, color, style, 0, barFlags);
    }

    public NMSBossBar(@Nonnull String id, @Nonnull Object text, @Nonnull BarColor color, @Nonnull BarFlag... barFlags) {
        this(id, text, color, BarStyle.SOLID, 0, barFlags);
    }

    @Override
    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    @Override
    public String getText() {
        return text;
    }

    @Nonnull
    @Override
    public BarColor getColor() {
        return color;
    }

    @Nonnull
    @Override
    public BarStyle getStyle() {
        return style;
    }

    @Nonnull
    @Override
    public BarFlag[] getBarFlags() {
        return barFlags;
    }

    @Override
    public double getProgress() {
        return progress;
    }

    @Nonnull
    @Override
    public TNLBossBar<CraftBossBar> setText(@Nonnull String text) {
        getBossBar().setTitle(text);
        this.text = text;
        return this;
    }

    @Nonnull
    @Override
    public TNLBossBar<CraftBossBar> setColor(@Nonnull BarColor color) {
        getBossBar().setColor(color);
        this.color = color;
        return this;
    }

    @Nonnull
    @Override
    public TNLBossBar<CraftBossBar> setStyle(@Nonnull BarStyle style) {
        getBossBar().setStyle(style);
        this.style = style;
        return this;
    }

    @Nonnull
    @Override
    public TNLBossBar<CraftBossBar> setBarFlags(@Nonnull BarFlag... barFlags) {
        for (BarFlag flag : BarFlag.values()) {
            if (Arrays.asList(barFlags).contains(flag)) {
                getBossBar().addFlag(flag);
            } else {
                getBossBar().removeFlag(flag);
            }
        }
        this.barFlags = barFlags;
        return this;
    }

    @Nonnull
    @Override
    public TNLBossBar<CraftBossBar> setProgress(double progress) {
        this.progress = progress;
        getBossBar().setProgress(getProgress());
        return this;
    }

    @Nonnull
    @Override
    public CraftBossBar getBossBar() {
        return bossBar;
    }
}
