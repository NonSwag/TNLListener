package net.nonswag.tnl.listener.listeners;

import net.nonswag.tnl.listener.Holograms;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldListener implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Holograms.getInstance().reloadAll(TNLPlayer.cast(event.getPlayer()));
    }
}