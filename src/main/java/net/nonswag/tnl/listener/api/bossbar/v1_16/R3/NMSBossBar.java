package net.nonswag.tnl.listener.api.bossbar.v1_16.R3;

import net.nonswag.tnl.listener.api.bossbar.BossBar;
import net.nonswag.tnl.listener.utils.MathUtil;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.craftbukkit.v1_16_R3.boss.CraftBossBar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class NMSBossBar implements BossBar<CraftBossBar> {

    @Nonnull
    private static final HashMap<String, NMSBossBar> bossBarMap = new HashMap<>();

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

    @Nonnull
    private static HashMap<String, NMSBossBar> getBossBarMap() {
        return bossBarMap;
    }

    @Nullable
    public static NMSBossBar getBossBar(@Nonnull String id) {
        return getBossBarMap().get(id);
    }

    @Nonnull
    public static NMSBossBar getOrCreateBossBar(@Nonnull String id, @Nonnull NMSBossBar bossBar) {
        if (!getBossBarMap().containsKey(id)) {
            getBossBarMap().put(id, bossBar);
        }
        return getBossBarMap().get(id);
    }

    public static void createBossBar(@Nonnull String id, @Nonnull NMSBossBar bossBar) {
        getBossBarMap().put(id, bossBar);
    }

    public static void deleteBossBar(@Nonnull String id, @Nonnull NMSBossBar bossBar) {
        getBossBarMap().put(id, bossBar);
    }

    public static void removeBossBar(@Nonnull String id) {
        getBossBarMap().remove(id);
    }

    public static void clearBossBars() {
        getBossBarMap().clear();
    }

    @Nonnull
    public static Collection<NMSBossBar> getBossBars() {
        return getBossBarMap().values();
    }

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
    public BossBar<CraftBossBar> setText(@Nonnull String text) {
        getBossBar().setTitle(text);
        this.text = text;
        return this;
    }

    @Nonnull
    @Override
    public BossBar<CraftBossBar> setColor(@Nonnull BarColor color) {
        getBossBar().setColor(color);
        this.color = color;
        return this;
    }

    @Nonnull
    @Override
    public BossBar<CraftBossBar> setStyle(@Nonnull BarStyle style) {
        getBossBar().setStyle(style);
        this.style = style;
        return this;
    }

    @Nonnull
    @Override
    public BossBar<CraftBossBar> setBarFlags(@Nonnull BarFlag... barFlags) {
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
    public BossBar<CraftBossBar> setProgress(double progress) {
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
