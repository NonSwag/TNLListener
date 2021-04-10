package net.nonswag.tnl.listener.api.scoreboard.v1_15.R1;

import net.minecraft.server.v1_15_R1.*;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.scoreboard.Sidebar;

import javax.annotation.Nonnull;

public class NMSSidebar implements Sidebar<Scoreboard, ScoreboardObjective> {

    @Nonnull
    private Scoreboard scoreboard = new Scoreboard();
    @Nonnull
    private ScoreboardObjective scoreboardObjective;
    @Nonnull
    private TNLPlayer player;

    public NMSSidebar(@Nonnull TNLPlayer player) {
        this.scoreboardObjective = getScoreboard().registerObjective("TNLSidebar", IScoreboardCriteria.DUMMY, new ChatMessage(""), IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
        this.player = player;
    }

    @Override
    @Nonnull
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    @Nonnull
    public ScoreboardObjective getScoreboardObjective() {
        return scoreboardObjective;
    }

    @Override
    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    @Override
    public void setScoreboard(@Nonnull Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    @Override
    public void setScoreboardObjective(@Nonnull ScoreboardObjective scoreboardObjective) {
        this.scoreboardObjective = scoreboardObjective;
    }

    @Override
    public void setPlayer(@Nonnull TNLPlayer player) {
        this.player = player;
    }

    @Override
    public void add() {
        getPlayer().sendPacket(new PacketPlayOutScoreboardObjective(getScoreboardObjective(), 1));
        getPlayer().sendPacket(new PacketPlayOutScoreboardObjective(getScoreboardObjective(), 0));
        getPlayer().sendPacket(new PacketPlayOutScoreboardDisplayObjective(1, getScoreboardObjective()));
    }

    @Override
    public void remove() {
        getPlayer().sendPacket(new PacketPlayOutScoreboardObjective(getScoreboardObjective(), 1));
    }

    @Override
    public void setScore(int score, @Nonnull String text) {
        PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, "TNLSidebar", text, score);
        ScoreboardScore scoreboardScore = new ScoreboardScore(getScoreboard(), getScoreboardObjective(), text);
        scoreboardScore.setScore(score);
        getPlayer().sendPacket(packetPlayOutScoreboardScore);
    }

    @Override
    public void setTitle(@Nonnull String scoreboardDisplayName) {
        getScoreboardObjective().setDisplayName(new ChatMessage(scoreboardDisplayName));
    }
}
