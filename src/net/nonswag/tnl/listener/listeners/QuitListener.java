package net.nonswag.tnl.listener.listeners;

import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.TNLListener;
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
            if(NMSMain.isCustomQuitMessage() && !NMSMain.getQuitMessage().equalsIgnoreCase("")) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (!all.equals(event.getPlayer())) {
                        all.sendMessage(NMSMain.getPrefix() + " " + NMSMain.getQuitMessage().replace("%player%", event.getPlayer().getName()));
                    } else {
                        all.sendMessage(NMSMain.getPrefix() + " " + NMSMain.getQuitMessage().replace("%player%", NMSMain.getPlayerDirect().replace("%player%", event.getPlayer().getDisplayName())));
                    }
                }
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        NMSMain.runTaskAsynchronously(TNLListener::updatePlayers);
    }
}