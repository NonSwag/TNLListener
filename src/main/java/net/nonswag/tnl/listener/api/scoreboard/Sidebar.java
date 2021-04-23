package net.nonswag.tnl.listener.api.scoreboard;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.version.ServerVersion;

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

    @Nonnull
    static Sidebar<?, ?> create(@Nonnull TNLPlayer player) {
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4) || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5)) {
            return new net.nonswag.tnl.listener.api.scoreboard.v1_16.R3.NMSSidebar(player);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            return new net.nonswag.tnl.listener.api.scoreboard.v1_15.R1.NMSSidebar(player);
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_10)) {
            throw new UnsupportedOperationException("method is not supported in this version");
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            throw new UnsupportedOperationException("method is not supported in this version");
        } else {
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }
}
