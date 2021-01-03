package net.nonswag.tnl.listener.v1_15_R1.api.scoreboardAPI;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Sidebar {

    @Nonnull private static final HashMap<Player, Sidebar> sidebar = new HashMap<>();
    @Nonnull private final Player player;
    @Nonnull private List<String> lines = new ArrayList<>();
    @Nullable private String title;

    public Sidebar(@Nonnull Player player) {
        this.player = player;
    }

    @Nonnull
    public Player getPlayer() {
        return player;
    }

    @Nonnull
    public List<String> getLines() {
        return lines;
    }

    public Sidebar setLines(@Nonnull List<String> lines) {
        this.lines = lines;
        return this;
    }

    public Sidebar clearLines() {
        getLines().clear();
        return this;
    }

    public Sidebar addLine(@Nonnull String line) {
        getLines().add(line);
        return this;
    }

    public Sidebar addLines(@Nonnull String... lines) {
        getLines().addAll(Arrays.asList(lines));
        return this;
    }

    public Sidebar addLines(@Nonnull List<String> lines) {
        getLines().addAll(lines);
        return this;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public Sidebar setTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    public void sendSidebar() {
        CraftScoreboard scoreboard = ((CraftScoreboard) Bukkit.getScoreboardManager().getNewScoreboard());
        Objective objective = scoreboard.registerNewObjective("TNLSidebar", "dummy", getTitle());
        for (int i = 0; i < getLines().size() && i < 15; i++) {
            objective.getScore(getLines().get(getLines().size() - i - 1)).setScore(i);
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(scoreboard);
    }

    @Nonnull
    public static HashMap<Player, Sidebar> getSidebar() {
        return sidebar;
    }

    @Nonnull
    public static Sidebar getSidebar(@Nonnull Player player) {
        final Sidebar sidebar = getSidebar().get(player);
        return sidebar == null ? new Sidebar(player) : sidebar;
    }
}
