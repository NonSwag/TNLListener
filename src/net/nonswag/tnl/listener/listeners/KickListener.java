package net.nonswag.tnl.listener.listeners;

import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
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
                if (TNLListener.getInstance().isPunishSpamming()) {
                    event.setReason(TNLListener.getInstance().getKickMessageSpamming());
                } else {
                    event.setCancelled(true);
                }
            }
            event.setLeaveMessage("");
            if (!event.isCancelled() && TNLListener.getInstance().isCustomKickMessage() && !TNLListener.getInstance().getJoinMessage().equalsIgnoreCase("")) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (!all.equals(event.getPlayer())) {
                        all.sendMessage(TNLListener.getInstance().getPrefix() + " " + TNLListener.getInstance().getKickMessage().replace("%player%", event.getPlayer().getName()).replace("%reason%", event.getReason()));
                    } else {
                        all.sendMessage(TNLListener.getInstance().getPrefix() + " " + TNLListener.getInstance().getQuitMessage().replace("%player%", TNLListener.getInstance().getPlayerDirect().replace("%player%", event.getPlayer().getDisplayName())).replace("%reason%", event.getReason()));
                    }
                }
            }
            String reason = event.getReason();
            if (!reason.startsWith(TNLListener.getInstance().getPrefix() + "")) {
                event.setReason(TNLListener.getInstance().getPrefix() + "\n§c" + event.getReason());
            }
        } catch (Exception e) {
            Logger.error.println(e);
        }
        Bukkit.getScheduler().runTaskAsynchronously(NMSMain.getInstance(), () -> TNLListener.getInstance().updatePlayers());
    }
}
