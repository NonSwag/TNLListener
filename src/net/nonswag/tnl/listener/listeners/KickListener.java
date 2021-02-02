package net.nonswag.tnl.listener.listeners;

import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.TNLListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class KickListener implements Listener {

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        try {
            if (event.getReason().equals("disconnect.spam")) {
                if (NMSMain.isPunishSpamming()) {
                    event.setReason(NMSMain.getKickMessageSpamming());
                } else {
                    event.setCancelled(true);
                }
            }
            event.setLeaveMessage("");
            if (!event.isCancelled() && NMSMain.isCustomKickMessage() && !NMSMain.getJoinMessage().equalsIgnoreCase("")) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (!all.equals(event.getPlayer())) {
                        all.sendMessage(NMSMain.getPrefix() + " " + NMSMain.getKickMessage().replace("%player%", event.getPlayer().getName()).replace("%reason%", event.getReason()));
                    } else {
                        all.sendMessage(NMSMain.getPrefix() + " " + NMSMain.getQuitMessage().replace("%player%", NMSMain.getPlayerDirect().replace("%player%", event.getPlayer().getDisplayName())).replace("%reason%", event.getReason()));
                    }
                }
            }
            String reason = event.getReason();
            if (!reason.startsWith(NMSMain.getPrefix() + "")) {
                event.setReason(NMSMain.getPrefix() + "\n§c" + event.getReason());
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        NMSMain.runTaskAsynchronously(TNLListener::updatePlayers);
    }
}
