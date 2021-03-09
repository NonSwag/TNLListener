package net.nonswag.tnl.listener.listeners;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.TNLMain;
import net.nonswag.tnl.listener.api.message.ChatComponent;
import net.nonswag.tnl.listener.api.message.Message;
import net.nonswag.tnl.listener.api.object.Objects;
import net.nonswag.tnl.listener.api.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class KickListener implements Listener {

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if (event.getReason().equals("disconnect.spam")) {
            if (Objects.getOrDefault(Settings.PUNISH_SPAMMING.getValue(), true)) {
                event.setReason(Message.KICKED_SPAMMING.getText());
            } else {
                event.setCancelled(true);
            }
        }
        event.setLeaveMessage("");
        String reason = event.getReason();
        if (!reason.startsWith(Message.PREFIX.getText()) || !reason.toLowerCase().startsWith("%prefix%")) {
            event.setReason(ChatComponent.getText("%prefix%\n§c" + event.getReason()));
        }
        Bukkit.getScheduler().runTaskAsynchronously(TNLMain.getInstance(), () -> TNLListener.getInstance().updatePlayers());
    }
}
