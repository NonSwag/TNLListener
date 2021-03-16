package net.nonswag.tnl.listener.listeners;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.message.MessageKey;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        try {
            TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> player = null;
            if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
                player = net.nonswag.tnl.listener.api.player.v1_7_R1.NMSPlayer.cast(event.getPlayer());
            } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
                player = net.nonswag.tnl.listener.api.player.v1_15_R1.NMSPlayer.cast(event.getPlayer());
            }
            if (player != null) {
                player.getPermissionManager().updatePermissions();
                player.inject();
                if (!Settings.JOIN_MESSAGE.getValue() || Settings.FIRST_JOIN_MESSAGE.getValue()) {
                    event.setJoinMessage(null);
                    if (player.hasPlayedBefore()) {
                        if (Settings.JOIN_MESSAGE.getValue()) {
                            for (TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> all : TNLListener.getInstance().getOnlinePlayers()) {
                                all.sendMessage(MessageKey.JOIN_MESSAGE, new Placeholder("player", event.getPlayer().getName()));
                            }
                        }
                    } else {
                        if (Settings.FIRST_JOIN_MESSAGE.getValue()) {
                            for (TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> all : TNLListener.getInstance().getOnlinePlayers()) {
                                all.sendMessage(MessageKey.FIRST_JOIN_MESSAGE, new Placeholder("player", event.getPlayer().getName()));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.error.println(e);
        }
    }
}
