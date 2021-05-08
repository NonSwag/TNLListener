package net.nonswag.tnl.listener.api.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;

import javax.annotation.Nonnull;
import java.util.Objects;

public class Team {

    @Nonnull
    private static final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    @Nonnull
    public static final Team NONE = new Team(9999999).setColor(ChatColor.GRAY);

    protected final int id;
    @Nonnull
    protected final org.bukkit.scoreboard.Team team;
    @Nonnull
    private String prefix = "";
    @Nonnull
    private String suffix = "";
    @Nonnull
    private ChatColor color = ChatColor.WHITE;

    public Team(int id) {
        this.id = id;
        org.bukkit.scoreboard.Team team = getScoreboard().getTeam("team-" + getId());
        if (team != null) {
            team.unregister();
        }
        this.team = getScoreboard().registerNewTeam("team-" + getId());
    }

    public int getId() {
        return id;
    }

    @Nonnull
    public org.bukkit.scoreboard.Team getTeam() {
        return team;
    }

    @Nonnull
    public String getPrefix() {
        return prefix;
    }

    @Nonnull
    public String getSuffix() {
        return suffix;
    }

    @Nonnull
    public ChatColor getColor() {
        return color;
    }

    @Nonnull
    public Team setPrefix(@Nonnull String prefix) {
        this.prefix = prefix;
        getTeam().setPrefix(prefix);
        return this;
    }

    @Nonnull
    public Team setSuffix(@Nonnull String suffix) {
        this.suffix = suffix;
        getTeam().setSuffix(suffix);
        return this;
    }

    @Nonnull
    public Team setColor(@Nonnull ChatColor color) {
        this.color = color;
        getTeam().setColor(color);
        return this;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                ", color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id == team.id && prefix.equals(team.prefix) && suffix.equals(team.suffix) && color == team.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prefix, suffix, color);
    }

    @Nonnull
    public static Scoreboard getScoreboard() {
        return scoreboard;
    }
}
