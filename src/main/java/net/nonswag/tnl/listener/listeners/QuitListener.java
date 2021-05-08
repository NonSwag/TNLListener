package net.nonswag.tnl.listener.listeners;

import net.nonswag.tnl.listener.Holograms;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.message.MessageKey;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Holograms.getInstance().unloadAll(TNLPlayer.cast(event.getPlayer()));
        event.setQuitMessage(null);
        if (Settings.QUIT_MESSAGE.getValue()) {
            for (TNLPlayer all : TNLListener.getInstance().getOnlinePlayers()) {
                all.sendMessage(MessageKey.QUIT_MESSAGE, new Placeholder("player", event.getPlayer().getName()));
            }
        }
        TNLListener.getInstance().getPlayerHashMap().entrySet().removeIf(entry -> !Bukkit.getOnlinePlayers().contains(entry.getKey()) || !entry.getKey().isOnline());
    }
}
