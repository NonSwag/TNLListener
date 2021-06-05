package net.nonswag.tnl.listener.listeners;

import net.nonswag.tnl.listener.Holograms;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.world.WorldInitEvent;

import javax.annotation.Nonnull;

public class WorldListener implements Listener {

    @EventHandler
    public void onWorldChange(@Nonnull PlayerChangedWorldEvent event) {
        Holograms.getInstance().reloadAll(TNLPlayer.cast(event.getPlayer()));
    }

    @EventHandler
    public void onWorldChange(@Nonnull WorldInitEvent event) {
        Holograms.getInstance().loadAll();
    }
}
