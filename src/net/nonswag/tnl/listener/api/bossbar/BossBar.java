package net.nonswag.tnl.listener.api.bossbar;

import net.nonswag.tnl.listener.utils.MathUtil;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.craftbukkit.v1_15_R1.boss.CraftBossBar;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class BossBar {

    private String text;
    private BarColor color;
    private BarStyle style;
    private BarFlag[] barFlags;
    private String id;
    private long progress;
    private final CraftBossBar bossBar;
    private static final HashMap<String, BossBar> bossBarMap = new HashMap<>();

    private static HashMap<String, BossBar> getBossBarMap() {
        return bossBarMap;
    }

    public static BossBar getBossBar(String id) {
        return getBossBarMap().get(id);
    }

    public static void removeBossBar(String id) {
        getBossBarMap().remove(id);
    }

    public static void clearBossBars() {
        getBossBarMap().clear();
    }

    public static Collection<BossBar> getBossBars() {
        return getBossBarMap().values();
    }

    public BossBar(String id, Object text, BarColor color, BarStyle style, long progress, BarFlag... barFlags) {
        this();
        setText(text.toString()).setColor(color).setStyle(style).setProgress(MathUtil.validateLong(0, 100, progress)).setBarFlags(barFlags).setId(id);
    }

    public BossBar(String id, Object text, BarColor color, BarStyle style, BarFlag... barFlags) {
        this(id, text, color, style, 1, barFlags);
    }

    public BossBar(String id, Object text, BarColor color, BarFlag... barFlags) {
        this(id, text, color, BarStyle.SOLID, 1, barFlags);
    }

    public BossBar() {
        this.bossBar = new CraftBossBar(getText() == null || getText().isEmpty() ? "§7-§8/§7-" : getText(),
                getColor() == null ? BarColor.WHITE : getColor(),
                getStyle() == null ? BarStyle.SOLID : getStyle(),
                getBarFlags() == null || getBarFlags().length == 0 ? BarFlag.values() : getBarFlags());
        getBossBarMap().put(getId(), this);
    }

    public String getText() {
        return text;
    }

    public BarColor getColor() {
        return color;
    }

    public BarStyle getStyle() {
        return style;
    }

    public BarFlag[] getBarFlags() {
        return barFlags;
    }

    public long getProgress() {
        return progress;
    }

    public String getId() {
        return id;
    }

    public BossBar setText(String text) {
        getBossBar().setTitle(text);
        this.text = text;
        return this;
    }

    public BossBar setColor(BarColor color) {
        getBossBar().setColor(color);
        this.color = color;
        return this;
    }

    public BossBar setStyle(BarStyle style) {
        getBossBar().setStyle(style);
        this.style = style;
        return this;
    }

    public BossBar setBarFlags(BarFlag... barFlags) {
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

    public BossBar setProgress(long progress) {
        this.progress = progress;
        return this;
    }

    public BossBar setId(String id) {
        this.id = id;
        return this;
    }

    public CraftBossBar getBossBar() {
        return bossBar;
    }
}
