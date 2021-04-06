package net.nonswag.tnl.listener.listeners.v1_15.R1;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_15_R1.*;
import net.nonswag.tnl.listener.Loader;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Color;
import net.nonswag.tnl.listener.api.message.MessageKey;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.object.Objects;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.listener.api.sign.SignMenu;
import net.nonswag.tnl.listener.events.*;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PacketListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPacket(PlayerPacketEvent<Packet<?>> event) {
        if (event.isIncoming()) {
            if (event.getPacket() instanceof PacketPlayInChat && Settings.BETTER_CHAT.getValue()) {
                String message = Color.Minecraft.unColorize(Objects.getOrDefault(((PacketPlayInChat) event.getPacket()).b(), ""), '§');
                if (message.length() <= 0 || Color.Minecraft.unColorize(message.replace(" ", ""), '&').isEmpty()) {
                    event.setCancelled(true);
                } else {
                    PlayerChatEvent chatEvent = new PlayerChatEvent(event.getPlayer(), message);
                    Bukkit.getPluginManager().callEvent(chatEvent);
                    event.setCancelled(chatEvent.isCancelled());
                    if (!chatEvent.isCancelled()) {
                        if (!chatEvent.isCommand()) {
                            event.setCancelled(true);
                            for (TNLPlayer all : TNLListener.getInstance().getOnlinePlayers()) {
                                if (chatEvent.getFormat() == null) {
                                    all.sendMessage(MessageKey.CHAT_FORMAT, new Placeholder("world", event.getPlayer().getWorldAlias()), new Placeholder("player", event.getPlayer().getName()), new Placeholder("message", message), new Placeholder("colored_message", Color.Minecraft.colorize(message, '&')), new Placeholder("text", Color.Minecraft.colorize(message, '&')));
                                } else {
                                    all.sendMessage(chatEvent.getFormat(), new Placeholder("world", event.getPlayer().getWorldAlias()), new Placeholder("player", event.getPlayer().getName()), new Placeholder("message", message), new Placeholder("colored_message", Color.Minecraft.colorize(message, '&')));
                                }
                            }
                        }
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayInUseEntity) {

                Entity entity = ((PacketPlayInUseEntity) event.getPacket()).a((World) event.getPlayer().getWorldServer());
                if (entity != null) {
                    if (((PacketPlayInUseEntity) event.getPacket()).b().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)) {
                        EntityDamageByPlayerEvent<Entity> damageEvent = new EntityDamageByPlayerEvent<>(event.getPlayer(), entity);
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
            } else if (event.getPacket() instanceof PacketPlayInBlockDig) {
                PlayerDamageBlockEvent.BlockDamageType damageType = PlayerDamageBlockEvent.BlockDamageType.fromString(((PacketPlayInBlockDig) event.getPacket()).d().name());
                BlockPosition position = ((PacketPlayInBlockDig) event.getPacket()).b();
                EnumDirection againstBlock = ((PacketPlayInBlockDig) event.getPacket()).c();
                Block block = new Location(event.getPlayer().getWorld(), position.getX(), position.getY(), position.getZ()).getBlock();
                Block relative = block.getRelative(againstBlock.getAdjacentX(), againstBlock.getAdjacentY(), againstBlock.getAdjacentZ());
                if (relative.getType().equals(Material.FIRE)) {
                    position = new BlockPosition(relative.getX(), relative.getY(), relative.getZ());
                    block = new Location(event.getPlayer().getWorld(), position.getX(), position.getY(), position.getZ()).getBlock();
                }
                PlayerDamageBlockEvent blockEvent = new PlayerDamageBlockEvent(event.getPlayer(), block, damageType);
                Bukkit.getPluginManager().callEvent(blockEvent);
                event.setCancelled(blockEvent.isCancelled());
                if (blockEvent.isCancelled() && blockEvent.isUpdate()) {
                    if (blockEvent.getBlockDamageType().equals(PlayerDamageBlockEvent.BlockDamageType.STOP_DESTROY_BLOCK)
                            || blockEvent.getBlockDamageType().equals(PlayerDamageBlockEvent.BlockDamageType.START_DESTROY_BLOCK)) {
                        for (BlockFace blockFace : BlockFace.values()) {
                            event.getPlayer().sendBlockChange(blockEvent.getBlock().getLocation(), blockFace);
                        }
                    } else if (blockEvent.getBlockDamageType().equals(PlayerDamageBlockEvent.BlockDamageType.DROP_ITEM) || blockEvent.getBlockDamageType().equals(PlayerDamageBlockEvent.BlockDamageType.DROP_ALL_ITEMS)) {
                        event.getPlayer().updateInventory();
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayInTabComplete) {
                if (((PacketPlayInTabComplete) event.getPacket()).c().startsWith("/")) {
                    if (!Settings.TAB_COMPLETER.getValue() && !event.getPlayer().getPermissionManager().hasPermission(Settings.TAB_COMPLETE_BYPASS_PERMISSION.getValue())) {
                        event.setCancelled(true);
                    }
                } else {
                    if (!Settings.CHAT_COMPLETER.getValue() && !event.getPlayer().getPermissionManager().hasPermission(Settings.TAB_COMPLETE_BYPASS_PERMISSION.getValue())) {
                        event.setCancelled(true);
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayInBlockPlace) {
                org.bukkit.inventory.ItemStack itemStack = null;
                if (((PacketPlayInBlockPlace) event.getPacket()).b().equals(EnumHand.MAIN_HAND)) {
                    itemStack = event.getPlayer().getInventory().getItemInMainHand();
                } else if (((PacketPlayInBlockPlace) event.getPacket()).b().equals(EnumHand.OFF_HAND)) {
                    itemStack = event.getPlayer().getInventory().getItemInOffHand();
                }
                if (itemStack != null && itemStack.getType().equals(Material.GLASS_BOTTLE)) {
                    Block target = event.getPlayer().getBukkitPlayer().getTargetBlockExact(5, FluidCollisionMode.ALWAYS);
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
            } else if (event.getPacket() instanceof PacketPlayInUpdateSign) {
                if (TNLListener.getInstance().getSignHashMap().containsKey(event.getPlayer().getUniqueId())) {
                    event.setCancelled(true);
                    SignMenu signMenu = TNLListener.getInstance().getSignHashMap().get(event.getPlayer().getUniqueId());
                    if (signMenu.getResponse().hasValue()) {
                        boolean success = signMenu.getResponse().nonnull().test(event.getPlayer(), ((PacketPlayInUpdateSign) event.getPacket()).c());
                        if (!success && signMenu.isReopenOnFail()) {
                            Bukkit.getScheduler().runTaskLater(Loader.getInstance(), () -> event.getPlayer().openVirtualSignEditor(signMenu), 2);
                        }
                    }
                    Bukkit.getScheduler().runTask(Loader.getInstance(), () -> TNLListener.getInstance().getSignHashMap().remove(event.getPlayer().getUniqueId()));
                    event.getPlayer().sendBlockChange(signMenu.getLocation(), signMenu.getLocation().getBlock().getBlockData());
                }
            } else if (event.getPacket() instanceof PacketPlayInUseItem) {
                BlockPosition position = ((PacketPlayInUseItem) event.getPacket()).c().getBlockPosition();
                Block block = new Location(event.getPlayer().getWorld(), position.getX(), position.getY(), position.getZ()).getBlock();
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
                    Bukkit.getScheduler().runTask(Loader.getInstance(), () -> {
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
                Objects<EntityTypes<?>> k = ((Objects<EntityTypes<?>>) event.getPacketField("k"));
                if (k.hasValue()) {
                    if (Settings.BETTER_TNT.getValue()) {
                        if (k.nonnull().equals(EntityTypes.TNT)) {
                            event.setCancelled(true);
                        }
                    }
                    if (Settings.BETTER_FALLING_BLOCKS.getValue()) {
                        if (k.nonnull().equals(EntityTypes.FALLING_BLOCK)) {
                            event.setCancelled(true);
                        }
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayOutEntityStatus) {
                int id = ((Objects<Integer>) event.getPacketField("a")).getOrDefault(-1);
                byte b = ((Objects<Byte>) event.getPacketField("b")).getOrDefault((byte) -1);
                if (event.getPlayer().getId() == id) {
                    if (b >= 24 && b < 28) {
                        event.setPacketField("b", (byte) 28);
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayOutChat) {
                Objects<ChatComponentText> a = (Objects<ChatComponentText>) event.getPacketField("a");
                if (a.hasValue()) {
                    TextComponent textComponent = new TextComponent(a.toString());
                    if (Settings.BETTER_PERMISSIONS.getValue()) {
                        if (textComponent.getText().equalsIgnoreCase("TextComponent{text='', " +
                                "siblings=[TextComponent{text='I'm sorry, " +
                                "but you do not have permission to perform this command. " +
                                "Please contact the server administrators if you believe that this is a mistake.', " +
                                "siblings=[], style=Style{hasParent=true, color=§c, bold=null, italic=null, " +
                                "underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}], " +
                                "style=Style{hasParent=false, color=null, bold=null, italic=null, underlined=null, " +
                                "obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}")) {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(MessageKey.NO_PERMISSION, new Placeholder("permission", "unknown"));
                        }
                    }
                }
            }
        }
    }
}
