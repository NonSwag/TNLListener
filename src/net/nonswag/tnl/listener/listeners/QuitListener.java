package net.nonswag.tnl.listener.listeners;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.message.MessageKey;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.settings.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        try {
            event.setQuitMessage(null);
            if(Settings.QUIT_MESSAGE.getValue()) {
                for (TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> all : TNLListener.getInstance().getOnlinePlayers()) {
                    all.sendMessage(MessageKey.QUIT_MESSAGE, new Placeholder("player", event.getPlayer().getName()));
                }
            }
        } catch (Exception e) {
            Logger.error.println(e);
        }
        TNLListener.getInstance().getPlayerHashMap().remove(event.getPlayer());
    }
}
