package net.nonswag.tnl.listener.api.scoreboard;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Sidebar {

    @Nonnull
    protected final TNLPlayer player;
    @Nonnull
    protected final Objective objective;

    public Sidebar(@Nonnull TNLPlayer player) {
        this.player = player;
        Objective display = getPlayer().getScoreboard().getObjective("TNLSidebar");
        if (display != null) {
            display.unregister();
        }
        this.objective = getPlayer().getScoreboard().registerNewObjective("TNLSidebar", "dummy", "");
        getObjective().setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    @Nonnull
    protected TNLPlayer getPlayer() {
        return player;
    }

    @Nonnull
    protected Objective getObjective() {
        return objective;
    }

    @Nonnull
    public Sidebar setTitle(@Nonnull String title) {
        getObjective().setDisplayName(title);
        return this;
    }

    @Nonnull
    public String getTitle() {
        return getObjective().getDisplayName();
    }

    @Nonnull
    public Sidebar setScore(int score, @Nonnull String content) {
        Team team = getTeamByScore(score);
        if (team != null) {
            if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)
                    || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)
                    || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5)) {
                if (content.length() > 64) {
                    team.setPrefix(content.substring(0, 63));
                } else {
                    team.setPrefix(content);
                }
            } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)
                    || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_10)) {
                if (content.length() > 32) {
                    team.setPrefix(content.substring(0, 31));
                } else {
                    team.setPrefix(content);
                }
            } else {
                Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getVersion() + "§8'§c is not registered please report this error to an contributor");
                throw new IllegalStateException();
            }
            showScore(score);
        }
        return this;
    }

    @Nullable
    public String getScore(int score) {
        Team team = getTeamByScore(score);
        if (team != null) return team.getPrefix();
        return null;
    }

    @Nonnull
    public List<String> getScoreEntries() {
        List<String> scores = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            String score = getScore(i);
            if (score != null) scores.add(score);
        }
        return scores;
    }

    @Nonnull
    public List<Integer> getScores() {
        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < 15; i++) if (getScore(i) != null) scores.add(i);
        return scores;
    }

    @Nullable
    protected Team getTeamByScore(int score) {
        Entry name = Entry.getById(score);
        if (name != null) {
            Team team = getPlayer().getScoreboard().getEntryTeam(name.getName());
            if (team == null) {
                team = getPlayer().getScoreboard().registerNewTeam(name.name());
            }
            if (!team.hasEntry(name.getName())) team.addEntry(name.getName());
            return team;
        }
        return null;
    }

    @Nonnull
    public Sidebar showScore(int score) {
        Entry name = Entry.getById(score);
        if (name != null && !objective.getScore(name.getName()).isScoreSet()) objective.getScore(name.getName()).setScore(score);
        return this;
    }

    @Nonnull
    public Sidebar hideScore(int score) {
        Entry name = Entry.getById(score);
        if (name != null && objective.getScore(name.getName()).isScoreSet()) getPlayer().getScoreboard().resetScores(name.getName());
        return this;
    }

    public enum Entry {
        ENTRY_0(ChatColor.BLACK, 0),
        ENTRY_1(ChatColor.DARK_BLUE, 1),
        ENTRY_2(ChatColor.DARK_GREEN, 2),
        ENTRY_3(ChatColor.DARK_AQUA, 3),
        ENTRY_4(ChatColor.DARK_RED, 4),
        ENTRY_5(ChatColor.DARK_PURPLE, 5),
        ENTRY_6(ChatColor.GOLD, 6),
        ENTRY_7(ChatColor.GRAY, 7),
        ENTRY_8(ChatColor.DARK_GRAY, 8),
        ENTRY_9(ChatColor.BLUE, 9),
        ENTRY_10(ChatColor.GREEN, 10),
        ENTRY_11(ChatColor.AQUA, 11),
        ENTRY_12(ChatColor.RED, 12),
        ENTRY_13(ChatColor.LIGHT_PURPLE, 13),
        ENTRY_14(ChatColor.YELLOW, 14);

        @Nonnull
        private final String name;
        private final int entry;

        Entry(@Nonnull ChatColor color, int entry) {
            this.name = color.toString();
            this.entry = entry;
        }

        @Nonnull
        public String getName() {
            return name;
        }

        public int getEntry() {
            return entry;
        }

        @Nullable
        public static Entry getById(int entry) {
            for (Entry value : values()) if (value.getEntry() == entry) return value;
            return null;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "name='" + name + '\'' +
                    ", entry=" + entry +
                    '}';
        }
    }
}
