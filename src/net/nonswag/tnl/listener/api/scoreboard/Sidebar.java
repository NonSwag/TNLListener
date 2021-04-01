package net.nonswag.tnl.listener.api.scoreboard;

import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;
import java.util.HashMap;

public interface Sidebar<S, O> {

    @Nonnull
    S getScoreboard();

    @Nonnull
    O getScoreboardObjective();

    @Nonnull
    TNLPlayer getPlayer();

    void setPlayer(@Nonnull TNLPlayer player);

    void setScoreboard(@Nonnull S scoreboard);

    void setScoreboardObjective(@Nonnull O scoreboardObjective);

    void add();

    void remove();

    void setScore(int score, @Nonnull String text);

    void setTitle(@Nonnull String scoreboardDisplayName);

    abstract class Storage {

        @Nonnull
        private static final HashMap<TNLPlayer, Sidebar<?, ?>> saves = new HashMap<>();

        @Nonnull
        public static HashMap<TNLPlayer, Sidebar<?, ?>> getSaves() {
            return saves;
        }
    }
}
