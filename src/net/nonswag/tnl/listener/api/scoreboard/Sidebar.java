package net.nonswag.tnl.listener.api.scoreboard;

import net.minecraft.server.v1_15_R1.*;
import net.nonswag.tnl.listener.api.player.v1_15_R1.NMSPlayer;

import javax.annotation.Nonnull;

public class Sidebar {

    @Nonnull
    private static final Scoreboard scoreboard = new Scoreboard();
    @Nonnull
    private static final ScoreboardObjective scoreboardObjective = scoreboard.registerObjective("TNLSidebar", IScoreboardCriteria.DUMMY, new ChatMessage(""), IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);

    @Nonnull
    public static Scoreboard getScoreboard() {
        return scoreboard;
    }

    @Nonnull
    public static ScoreboardObjective getScoreboardObjective() {
        return scoreboardObjective;
    }

    public void add(@Nonnull NMSPlayer player) {
        player.sendPacket(new PacketPlayOutScoreboardObjective(scoreboardObjective, 1));
        player.sendPacket(new PacketPlayOutScoreboardObjective(scoreboardObjective, 0));
        player.sendPacket(new PacketPlayOutScoreboardDisplayObjective(1, scoreboardObjective));
    }

    public void remove(@Nonnull NMSPlayer player) {
        PacketPlayOutScoreboardObjective removePacket = new PacketPlayOutScoreboardObjective(scoreboardObjective, 1);
        player.sendPacket(removePacket);
    }

    public void setScore(int score, @Nonnull String text, @Nonnull NMSPlayer player) {
        PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, "TNLSidebar", text, score);
        ScoreboardScore scoreboardScore = new ScoreboardScore(scoreboard, scoreboardObjective, text);
        scoreboardScore.setScore(score);
        player.sendPacket(packetPlayOutScoreboardScore);
    }

    public void setTitle(@Nonnull String scoreboardDisplayName) {
        scoreboardObjective.setDisplayName(new ChatMessage(scoreboardDisplayName));
    }
}
