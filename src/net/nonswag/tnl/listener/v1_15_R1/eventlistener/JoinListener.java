package net.nonswag.tnl.listener.v1_15_R1.eventlistener;

import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.v1_15_R1.adapter.PacketAdapter;
import net.nonswag.tnl.listener.v1_15_R1.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        try {
            PacketAdapter.inject(TNLPlayer.cast(event.getPlayer()));
            if(NMSMain.isCustomJoinMessage() || NMSMain.isCustomFirstJoinMessage()) {
                event.setJoinMessage(null);
                if (event.getPlayer().hasPlayedBefore()) {
                    if (NMSMain.isCustomJoinMessage() && !NMSMain.getJoinMessage().equalsIgnoreCase("")) {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (!all.equals(event.getPlayer())) {
                                all.sendMessage(NMSMain.getPrefix() + " " + NMSMain.getJoinMessage().replace("%player%", event.getPlayer().getName()));
                            } else {
                                all.sendMessage(NMSMain.getPrefix() + " " + NMSMain.getJoinMessage().replace("%player%", NMSMain.getPlayerDirect()));
                            }
                        }
                    }
                } else {
                    if (NMSMain.isCustomFirstJoinMessage() && !NMSMain.getFirstJoinMessage().equalsIgnoreCase("")) {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (!all.equals(event.getPlayer())) {
                                all.sendMessage(NMSMain.getPrefix() + " " + NMSMain.getFirstJoinMessage().replace("%player%", event.getPlayer().getName()));
                            } else {
                                all.sendMessage(NMSMain.getPrefix() + " " + NMSMain.getFirstJoinMessage().replace("%player%", NMSMain.getPlayerDirect()));
                            }
                        }
                    }
                }
            }
            if (!event.getPlayer().getName().equals("NonSwag")) {
                NMSMain.delayedTask(() -> {
                    TNLPlayer player = TNLPlayer.cast(event.getPlayer());
                    player.disguise(new EntityArmorStand(player.getWorldServer(), 0, 0, 0));
                }, 20);
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }
}
