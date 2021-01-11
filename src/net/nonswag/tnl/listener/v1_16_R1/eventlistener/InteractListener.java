package net.nonswag.tnl.listener.v1_16_R1.eventlistener;

import net.nonswag.tnl.listener.v1_16_R1.eventhandler.PlayerBottleFillEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {

    @EventHandler
    public void onWaterBottleFill(CauldronLevelChangeEvent event) {
        if(event.getReason().equals(CauldronLevelChangeEvent.ChangeReason.BOTTLE_FILL)
                && event.getEntity() != null && event.getEntity() instanceof Player) {
            Player player = ((Player) event.getEntity());
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            ItemStack itemStack1 = player.getInventory().getItemInOffHand();
            PlayerBottleFillEvent e = null;
            if(itemStack != null && itemStack.getType().equals(Material.GLASS_BOTTLE)) {
                e = new PlayerBottleFillEvent(false, player, itemStack, event.getBlock());
            } else if(itemStack1 != null && itemStack1.getType().equals(Material.GLASS_BOTTLE)) {
                e = new PlayerBottleFillEvent(false, player, itemStack1, event.getBlock());
            }
            if(e != null) {
                Bukkit.getPluginManager().callEvent(e);
                event.setCancelled(e.isCancelled());
                if(itemStack != null && itemStack.getType().equals(Material.GLASS_BOTTLE)) {
                    player.getInventory().setItemInMainHand(e.getItemStack());
                } else if(itemStack1 != null && itemStack1.getType().equals(Material.GLASS_BOTTLE)) {
                    player.getInventory().setItemInOffHand(e.getItemStack());
                }
            }
        }
    }

}
