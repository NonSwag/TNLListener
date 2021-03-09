package net.nonswag.tnl.listener.api.bossbar;

import net.nonswag.tnl.listener.utils.MathUtil;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.craftbukkit.v1_15_R1.boss.CraftBossBar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class BossBar {

    @Nonnull
    private static final HashMap<String, BossBar> bossBarMap = new HashMap<>();

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
    private long progress;
    @Nonnull
    private final CraftBossBar bossBar;

    @Nonnull
    private static HashMap<String, BossBar> getBossBarMap() {
        return bossBarMap;
    }

    @Nullable
    public static BossBar getBossBar(@Nonnull String id) {
        return getBossBarMap().get(id);
    }

    public static void removeBossBar(@Nonnull String id) {
        getBossBarMap().remove(id);
    }

    public static void clearBossBars() {
        getBossBarMap().clear();
    }

    @Nonnull
    public static Collection<BossBar> getBossBars() {
        return getBossBarMap().values();
    }

    public BossBar(@Nonnull String id, @Nonnull Object text, @Nonnull BarColor color, @Nonnull BarStyle style, long progress, @Nonnull BarFlag... barFlags) {
        this.id = id;
        this.text = text.toString();
        this.color = color;
        this.style = style;
        this.progress = MathUtil.validateLong(0, 100, progress);
        this.barFlags = barFlags;
        this.bossBar = new CraftBossBar(getText(), getColor(), getStyle(), getBarFlags());
        bossBar.setProgress(getProgress());
        getBossBarMap().put(getId(), this);
    }

    public BossBar(@Nonnull String id, @Nonnull Object text, @Nonnull BarColor color, @Nonnull BarStyle style, @Nonnull BarFlag... barFlags) {
        this(id, text, color, style, 1, barFlags);
    }

    public BossBar(@Nonnull String id, @Nonnull Object text, @Nonnull BarColor color, @Nonnull BarFlag... barFlags) {
        this(id, text, color, BarStyle.SEGMENTED_6, 1, barFlags);
    }

    @Nonnull
    public String getText() {
        return text;
    }

    @Nonnull
    public BarColor getColor() {
        return color;
    }

    @Nonnull
    public BarStyle getStyle() {
        return style;
    }

    @Nonnull
    public BarFlag[] getBarFlags() {
        return barFlags;
    }

    public long getProgress() {
        return progress;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public BossBar setText(@Nonnull String text) {
        getBossBar().setTitle(text);
        this.text = text;
        return this;
    }

    @Nonnull
    public BossBar setColor(@Nonnull BarColor color) {
        getBossBar().setColor(color);
        this.color = color;
        return this;
    }

    @Nonnull
    public BossBar setStyle(@Nonnull BarStyle style) {
        getBossBar().setStyle(style);
        this.style = style;
        return this;
    }

    @Nonnull
    public BossBar setBarFlags(@Nonnull BarFlag... barFlags) {
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
    public BossBar setProgress(long progress) {
        this.progress = progress;
        return this;
    }

    @Nonnull
    public CraftBossBar getBossBar() {
        return bossBar;
    }
}
