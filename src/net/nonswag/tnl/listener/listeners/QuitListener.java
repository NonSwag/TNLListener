package net.nonswag.tnl.listener.listeners;

import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        try {
            event.setQuitMessage(null);
            if(TNLListener.getInstance().isCustomQuitMessage() && !TNLListener.getInstance().getQuitMessage().equalsIgnoreCase("")) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (!all.equals(event.getPlayer())) {
                        all.sendMessage(TNLListener.getInstance().getPrefix() + " " + TNLListener.getInstance().getQuitMessage().replace("%player%", event.getPlayer().getName()));
                    } else {
                        all.sendMessage(TNLListener.getInstance().getPrefix() + " " + TNLListener.getInstance().getQuitMessage().replace("%player%", TNLListener.getInstance().getPlayerDirect().replace("%player%", event.getPlayer().getDisplayName())));
                    }
                }
            }
        } catch (Exception e) {
            Logger.error.println(e);
        }
        Bukkit.getScheduler().runTaskAsynchronously(NMSMain.getInstance(), TNLListener.getInstance()::updatePlayers);
    }
}
