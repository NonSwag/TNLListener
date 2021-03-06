package net.nonswag.tnl.listener.listeners;

import net.nonswag.tnl.listener.Holograms;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.message.MessageKey;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.settings.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(@Nonnull PlayerJoinEvent event) {
        TNLPlayer player = TNLPlayer.cast(event.getPlayer());
        player.inject();
        player.getPermissionManager().updatePermissions();
        Holograms.getInstance().loadAll(player);
        player.updateTeam();
        if (!Settings.JOIN_MESSAGE.getValue() || Settings.FIRST_JOIN_MESSAGE.getValue()) {
            event.setJoinMessage(null);
            if (player.hasPlayedBefore()) {
                if (Settings.JOIN_MESSAGE.getValue()) {
                    for (TNLPlayer all : TNLListener.getInstance().getOnlinePlayers()) {
                        all.sendMessage(MessageKey.JOIN_MESSAGE);
                    }
                }
            } else {
                if (Settings.FIRST_JOIN_MESSAGE.getValue()) {
                    for (TNLPlayer all : TNLListener.getInstance().getOnlinePlayers()) {
                        all.sendMessage(MessageKey.FIRST_JOIN_MESSAGE);
                    }
                }
            }
        }
    }
}
