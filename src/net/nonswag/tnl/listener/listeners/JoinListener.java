package net.nonswag.tnl.listener.listeners;

import net.nonswag.tnl.listener.TNLMain;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.adapter.PacketAdapter;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        try {
            TNLPlayer player = TNLPlayer.cast(event.getPlayer());
            PacketAdapter.inject(player);
            if (TNLListener.getInstance().isCustomJoinMessage() || TNLListener.getInstance().isCustomFirstJoinMessage()) {
                event.setJoinMessage(null);
                if (player.hasPlayedBefore()) {
                    if (TNLListener.getInstance().isCustomJoinMessage() && !TNLListener.getInstance().getJoinMessage().equalsIgnoreCase("")) {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (!all.equals(event.getPlayer())) {
                                all.sendMessage(TNLListener.getInstance().getPrefix() + " " + TNLListener.getInstance().getJoinMessage().replace("%player%", event.getPlayer().getName()));
                            } else {
                                all.sendMessage(TNLListener.getInstance().getPrefix() + " " + TNLListener.getInstance().getJoinMessage().replace("%player%", TNLListener.getInstance().getPlayerDirect().replace("%player%", event.getPlayer().getDisplayName())));
                            }
                        }
                    }
                } else {
                    if (TNLListener.getInstance().isCustomFirstJoinMessage() && !TNLListener.getInstance().getFirstJoinMessage().equalsIgnoreCase("")) {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (!all.equals(event.getPlayer())) {
                                all.sendMessage(TNLListener.getInstance().getPrefix() + " " + TNLListener.getInstance().getFirstJoinMessage().replace("%player%", event.getPlayer().getName()));
                            } else {
                                all.sendMessage(TNLListener.getInstance().getPrefix() + " " + TNLListener.getInstance().getFirstJoinMessage().replace("%player%", TNLListener.getInstance().getPlayerDirect().replace("%player%", event.getPlayer().getDisplayName())));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.error.println(e);
        }
        Bukkit.getScheduler().runTaskAsynchronously(TNLMain.getInstance(), TNLListener.getInstance()::updatePlayers);
    }
}
