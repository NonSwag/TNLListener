package net.nonswag.tnl.listener.api.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Sidebar {

    private static final HashMap<Player, Sidebar> sidebar = new HashMap<>();
    private final Player player;
    private List<String> lines = new ArrayList<>();
    private String title;

    public Sidebar(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public List<String> getLines() {
        return lines;
    }

    public Sidebar setLines(List<String> lines) {
        this.lines = lines;
        return this;
    }

    public Sidebar clearLines() {
        getLines().clear();
        return this;
    }

    public Sidebar addLine(String line) {
        getLines().add(line);
        return this;
    }

    public Sidebar addLines(String... lines) {
        getLines().addAll(Arrays.asList(lines));
        return this;
    }

    public Sidebar addLines(List<String> lines) {
        getLines().addAll(lines);
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Sidebar setTitle(String title) {
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

    public static HashMap<Player, Sidebar> getSidebar() {
        return sidebar;
    }

    public static Sidebar getSidebar(Player player) {
        final Sidebar sidebar = getSidebar().get(player);
        return sidebar == null ? new Sidebar(player) : sidebar;
    }
}
