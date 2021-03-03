package net.nonswag.tnl.listener.listeners;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_15_R1.*;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.eventhandler.*;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PacketListener implements Listener {

    @EventHandler
    public void onPacket(PlayerPacketEvent event) {
        if (event.isIncoming()) {
            if (event.getPacket() instanceof PacketPlayInChat) {
                if (TNLListener.getInstance().isBetterChat()) {
                    String message = ((PacketPlayInChat) event.getPacket()).b();
                    if (message == null || message.length() <= 0 || message.replace(" ", "").equals("")) {
                        event.setCancelled(true);
                    } else {
                        if (message.contains("§")) {
                            message = message.replace("§", "?");
                        }
                        PlayerChatEvent chatEvent = new PlayerChatEvent(event.getPlayer(), message);
                        Bukkit.getPluginManager().callEvent(chatEvent);
                        event.setCancelled(chatEvent.isCancelled());
                        if (!chatEvent.isCancelled() && !event.isCancelled()) {
                            if (!chatEvent.isCommand() || TNLListener.getInstance().isUseCommandLineAsChat()) {
                                event.setCancelled(true);
                                for (Player all : Bukkit.getOnlinePlayers()) {
                                    if (chatEvent.getFormat() == null) {
                                        all.sendMessage(chatEvent.getPlayer().getName() + " §8» §r" + chatEvent.getMessage());
                                    } else {
                                        all.sendMessage(chatEvent.getFormat());
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayInUseEntity) {
                if (!event.isCancelled()) {
                    Entity entity = ((PacketPlayInUseEntity) event.getPacket()).a(event.getPlayer().getWorldServer());
                    if (entity != null) {
                        if (((PacketPlayInUseEntity) event.getPacket()).b().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)) {
                            EntityDamageByPlayerEvent damageEvent = new EntityDamageByPlayerEvent(event.getPlayer(), entity);
                            Bukkit.getPluginManager().callEvent(damageEvent);
                            if (damageEvent.isCancelled()) {
                                event.setCancelled(true);
                            }
                        }
                    } else {
                        PlayerInteractAtEntityEvent interactEvent = new PlayerInteractAtEntityEvent(event.getPlayer(), ((PacketPlayInUseEntity) event.getPacket()).getEntityId());
                        Bukkit.getPluginManager().callEvent(interactEvent);
                        if (interactEvent.isCancelled()) {
                            event.setCancelled(true);
                        }
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayInBlockDig) {
                PlayerDamageBlockEvent.BlockDamageType damageType = PlayerDamageBlockEvent.BlockDamageType.fromPacket(((PacketPlayInBlockDig) event.getPacket()));
                if (damageType.equals(PlayerDamageBlockEvent.BlockDamageType.STOP_DESTROY_BLOCK)
                        || damageType.equals(PlayerDamageBlockEvent.BlockDamageType.START_DESTROY_BLOCK)
                        || damageType.equals(PlayerDamageBlockEvent.BlockDamageType.ABORT_DESTROY_BLOCK)) {
                    BlockPosition position = ((PacketPlayInBlockDig) event.getPacket()).b();
                    Location location = TNLListener.getInstance().wrap(event.getPlayer().getWorld(), position.getX(), position.getY(), position.getZ());
                    PlayerDamageBlockEvent blockEvent = new PlayerDamageBlockEvent(event.getPlayer(), location.getBlock(), damageType);
                    Bukkit.getPluginManager().callEvent(blockEvent);
                    event.setCancelled(blockEvent.isCancelled());
                }
            } else if (event.getPacket() instanceof PacketPlayInBlockPlace) {
                org.bukkit.inventory.ItemStack itemStack = null;
                if (((PacketPlayInBlockPlace) event.getPacket()).b().equals(EnumHand.MAIN_HAND)) {
                    itemStack = event.getPlayer().getInventory().getItemInMainHand();
                } else if (((PacketPlayInBlockPlace) event.getPacket()).b().equals(EnumHand.OFF_HAND)) {
                    itemStack = event.getPlayer().getInventory().getItemInOffHand();
                }
                if (itemStack != null && itemStack.getType().equals(Material.GLASS_BOTTLE)) {
                    Block target = event.getPlayer().getTargetBlockExact(5, FluidCollisionMode.ALWAYS);
                    if (!(target != null && (target.getType().equals(Material.WATER)
                            || (target.getBlockData() instanceof Waterlogged
                            && ((Waterlogged) target.getBlockData()).isWaterlogged())
                            || target.getType().equals(Material.KELP)
                            || target.getType().equals(Material.KELP_PLANT)))) {
                        for (int i = 0; i < 6; i++) {
                            target = event.getPlayer().getTargetBlockExact(i);
                            if (target != null && (target.getType().equals(Material.WATER)
                                    || (target.getBlockData() instanceof Waterlogged
                                    && ((Waterlogged) target.getBlockData()).isWaterlogged())
                                    || target.getType().equals(Material.KELP)
                                    || target.getType().equals(Material.KELP_PLANT))) {
                                break;
                            }
                        }
                    }
                    if (target != null && (target.getType().equals(Material.WATER)
                            || (target.getBlockData() instanceof Waterlogged
                            && ((Waterlogged) target.getBlockData()).isWaterlogged())
                            || target.getType().equals(Material.KELP)
                            || target.getType().equals(Material.KELP_PLANT))) {
                        PlayerBottleFillEvent e = new PlayerBottleFillEvent(event.getPlayer(), itemStack, target);
                        Bukkit.getPluginManager().callEvent(e);
                        if (e.isCancelled()) {
                            event.setCancelled(true);
                            event.getPlayer().updateInventory();
                        }
                        if (((PacketPlayInBlockPlace) event.getPacket()).b().equals(EnumHand.MAIN_HAND)) {
                            event.getPlayer().getInventory().setItemInMainHand(e.getItemStack());
                        } else if (((PacketPlayInBlockPlace) event.getPacket()).b().equals(EnumHand.OFF_HAND)) {
                            event.getPlayer().getInventory().setItemInOffHand(e.getItemStack());
                        }
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayInUseItem) {
                BlockPosition position = ((PacketPlayInUseItem) event.getPacket()).c().getBlockPosition();
                org.bukkit.block.Block block = TNLListener.getInstance().wrap(event.getPlayer().getWorld(), position.getX(), position.getY(), position.getZ()).getBlock();
                final EnumDirection direction = ((PacketPlayInUseItem) event.getPacket()).c().getDirection();
                try {
                    block = block.getRelative(BlockFace.valueOf(direction.name()));
                } catch (Exception ignored) {
                }
                org.bukkit.inventory.ItemStack itemStack;
                if (((PacketPlayInUseItem) event.getPacket()).b().equals(EnumHand.MAIN_HAND)) {
                    itemStack = event.getPlayer().getInventory().getItemInMainHand();
                } else {
                    itemStack = event.getPlayer().getInventory().getItemInOffHand();
                }
                PlayerInteractEvent interactEvent = new PlayerInteractEvent(event.getPlayer(), block, itemStack);
                Bukkit.getPluginManager().callEvent(interactEvent);
                event.setCancelled(interactEvent.isCancelled());
                if (interactEvent.isCancelled()) {
                    Bukkit.getScheduler().runTask(NMSMain.getInstance(), () -> {
                        interactEvent.getBlock().getState().update();
                        for (BlockFace blockFace : BlockFace.values()) {
                            Block b = interactEvent.getBlock().getRelative(blockFace).getLocation().getBlock();
                            event.getPlayer().sendBlockChange(b.getLocation(), b.getBlockData());
                        }
                        interactEvent.getPlayer().updateInventory();
                    });
                }
            }
        } else if (event.isOutgoing()) {
            if (event.getPacket() instanceof PacketPlayOutSpawnEntity) {
                Object k = event.getPacketField("k");
                if (k != null) {
                    if (TNLListener.getInstance().isBetterTNT()) {
                        if (k.equals(EntityTypes.TNT)) {
                            event.setCancelled(true);
                        }
                    }
                    if (TNLListener.getInstance().isBetterFallingBlocks()) {
                        if (k.equals(EntityTypes.FALLING_BLOCK)) {
                            event.setCancelled(true);
                        }
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayOutChat) {
                Object a = event.getPacketField("a");
                if (a != null) {
                    TextComponent textComponent = new TextComponent(a.toString());
                    if (TNLListener.getInstance().isBetterPermissions()) {
                        if (textComponent.getText().equalsIgnoreCase("TextComponent{text='', " +
                                "siblings=[TextComponent{text='I'm sorry, " +
                                "but you do not have permission to perform this command. " +
                                "Please contact the server administrators if you believe that this is a mistake.', " +
                                "siblings=[], style=Style{hasParent=true, color=§c, bold=null, italic=null, " +
                                "underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}], " +
                                "style=Style{hasParent=false, color=null, bold=null, italic=null, underlined=null, " +
                                "obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}")) {
                            event.setPacketField("a", new ChatMessage(TNLListener.getInstance().getPermissionMessage()));
                        }
                    }
                }
            }
        }
    }
}
