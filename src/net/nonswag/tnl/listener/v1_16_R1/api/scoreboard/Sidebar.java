package net.nonswag.tnl.listener.v1_16_R1.api.scoreboard;

import com.sun.istack.internal.NotNull;
import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sidebar {

    private static final HashMap<Player, Sidebar> sidebar = new HashMap<>();
    private final Player player;
    private final Team team;
    private final Scoreboard scoreboard;
    private final ScoreboardManager manager = Bukkit.getScoreboardManager();
    private final List<String> cachedLines = new ArrayList<>();
    private final List<String> lines = new ArrayList<>();
    private String title;

    public Sidebar(Player player) {
        this.player = player;
        this.scoreboard = getManager().getNewScoreboard();
        this.team = getScoreboard().registerNewTeam(getPlayer().getName());
    }

    public Player getPlayer() {
        return player;
    }

    public Sidebar setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<String> getLines() {
        return lines;
    }

    public Sidebar clearLines() {
        getLines().clear();
        return this;
    }

    public Sidebar addLine(String s) {
        getLines().add(s);
        return this;
    }

    public Sidebar setLines(List<String> lines) {
        clearLines();
        getLines().addAll(lines);
        return this;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public ScoreboardManager getManager() {
        return manager;
    }

    public Team getTeam() {
        return team;
    }

    public String getTitle() {
        return title;
    }

    public Sidebar setLine(int line, String text) {
        if (getLines().size() - 1 >= line) {
            getLines().set(line, text);
        }
        return this;
    }

    public boolean isAbs() {
        return getScoreboard().getObjective("TNLSidebar") == null;
    }

    public Sidebar createIfAbs() {
        if (isAbs()) {
            create();
        }
        return this;
    }

    public Sidebar create() {
        getTeam().addEntry(getPlayer().getName());
        Objective objective = getScoreboard().registerNewObjective("TNLSidebar", "dummy", getTitle());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        updateLines();
        getSidebar().put(getPlayer(), this);
        return this;
    }

    public List<String> getCachedLines() {
        return cachedLines;
    }

    public Sidebar updateLines() {
        if (getCachedLines().isEmpty()) {
            getCachedLines().addAll(getLines());
        }
        for(int i = 0; i < getLines().size(); i++) {
            try {
                if (!getLines().get(getLines().size() - i - 1).equals(getCachedLines().get(getCachedLines().size() - i - 1))) {
                    getScoreboard().resetScores(getCachedLines().get(getCachedLines().size() - i - 1));
                }
                getScoreboard().getObjective("TNLSidebar").getScore(getLines().get(getLines().size() - i - 1)).setScore(i);
            } catch (Throwable t) {
                NMSMain.stacktrace(t);
                break;
            }
        }
        if (!getCachedLines().equals(getLines())) {
            getCachedLines().clear();
            getCachedLines().addAll(getLines());
        }
        return this;
    }

    public Sidebar updateTitle() {
        getScoreboard().getObjective("TNLSidebar").setDisplayName(getTitle());
        return this;
    }

    public void send() {
        getPlayer().setScoreboard(getScoreboard());
    }

    public void delete() {
        getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        getScoreboard().clearSlot(DisplaySlot.BELOW_NAME);
        getScoreboard().clearSlot(DisplaySlot.PLAYER_LIST);
        getPlayer().setScoreboard(getManager().getNewScoreboard());
    }

    @NotNull
    public static HashMap<Player, Sidebar> getSidebar() {
        return sidebar;
    }

    @Nullable
    public static Sidebar getSidebar(@NotNull Player player) {
        Sidebar sidebar = getSidebar().get(player);
        return sidebar == null ? new Sidebar(player) : sidebar;
    }
}
