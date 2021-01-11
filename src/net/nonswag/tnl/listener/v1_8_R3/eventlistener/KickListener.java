package net.nonswag.tnl.listener.v1_8_R3.eventlistener;

import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class KickListener implements Listener {

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if (event.getReason().equals("disconnect.spam")) {
            if(NMSMain.isPunishSpamming()) {
                event.setReason(NMSMain.getKickMessageSpamming());
            } else {
                event.setCancelled(true);
            }
        }
        String reason = event.getReason();
        if (!reason.startsWith(NMSMain.getPrefix() + "")) {
            event.setReason(NMSMain.getPrefix() + "\n§c" + event.getReason());
        }
    }
}
