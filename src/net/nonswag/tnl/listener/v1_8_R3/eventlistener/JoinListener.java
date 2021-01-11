package net.nonswag.tnl.listener.v1_8_R3.eventlistener;

import net.nonswag.tnl.listener.v1_8_R3.adapter.PacketAdapter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PacketAdapter.inject(event.getPlayer().getPlayer());
    }
}
