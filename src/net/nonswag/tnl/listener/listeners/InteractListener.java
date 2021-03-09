package net.nonswag.tnl.listener.listeners;

import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.eventhandler.PlayerBottleFillEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {

    @EventHandler
    public void onWaterBottleFill(CauldronLevelChangeEvent event) {
        TNLPlayer player = TNLPlayer.cast(event.getEntity());
        if (event.getReason().equals(CauldronLevelChangeEvent.ChangeReason.BOTTLE_FILL) && player != null) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            ItemStack itemStack1 = player.getInventory().getItemInOffHand();
            PlayerBottleFillEvent e = null;
            if (itemStack.getType().equals(Material.GLASS_BOTTLE)) {
                e = new PlayerBottleFillEvent(player, itemStack, event.getBlock());
            } else if (itemStack1.getType().equals(Material.GLASS_BOTTLE)) {
                e = new PlayerBottleFillEvent(player, itemStack1, event.getBlock());
            }
            if (e != null) {
                Bukkit.getPluginManager().callEvent(e);
                event.setCancelled(e.isCancelled());
                if (itemStack.getType().equals(Material.GLASS_BOTTLE)) {
                    player.getInventory().setItemInMainHand(e.getItemStack());
                } else if (itemStack1.getType().equals(Material.GLASS_BOTTLE)) {
                    player.getInventory().setItemInOffHand(e.getItemStack());
                }
            }
        }
    }
}
