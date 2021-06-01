package net.nonswag.tnl.listener.listeners.v1_15.R1;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_15_R1.*;
import net.minecraft.server.v1_15_R1.World;
import net.nonswag.tnl.listener.Bootstrap;
import net.nonswag.tnl.listener.Holograms;
import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.gui.GUI;
import net.nonswag.tnl.listener.api.gui.GUIItem;
import net.nonswag.tnl.listener.api.gui.Interaction;
import net.nonswag.tnl.listener.api.holograms.Hologram;
import net.nonswag.tnl.listener.api.holograms.event.InteractEvent;
import net.nonswag.tnl.listener.api.message.MessageKey;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.object.Objects;
import net.nonswag.tnl.listener.api.packet.TNLSetSlot;
import net.nonswag.tnl.listener.api.player.FakePlayer;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.listener.api.sign.SignMenu;
import net.nonswag.tnl.listener.events.*;
import org.bukkit.*;
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
                PlayerChatEvent chatEvent = new PlayerChatEvent(event.getPlayer(), ((PacketPlayInChat) event.getPacket()).b());
                event.getPlayer().chat(chatEvent);
                event.setCancelled(!chatEvent.isCommand());
            } else if (event.getPacket() instanceof PacketPlayInUseEntity) {
                PacketPlayInUseEntity packet = (PacketPlayInUseEntity) event.getPacket();
                Entity entity = packet.a((World) event.getPlayer().getWorldServer());
                if (entity != null) {
                    TNLEvent entityEvent;
                    if (packet.b().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)) {
                        entityEvent = new EntityDamageByPlayerEvent(event.getPlayer(), entity.getBukkitEntity());
                    } else {
                        entityEvent = new PlayerInteractAtEntityEvent(event.getPlayer(), entity.getBukkitEntity());
                    }
                    if (!entityEvent.call()) event.setCancelled(true);
                } else {
                    Objects<Integer> id = event.getPacketField("a", Integer.class);
                    if (id.hasValue()) {
                        FakePlayer fakePlayer = event.getPlayer().getFakePlayer(id.nonnull());
                        if (fakePlayer != null) {
                            net.nonswag.tnl.listener.api.player.event.InteractEvent.Type type = packet.b().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) ?
                                    net.nonswag.tnl.listener.api.player.event.InteractEvent.Type.LEFT_CLICK : net.nonswag.tnl.listener.api.player.event.InteractEvent.Type.RIGHT_CLICK;
                            fakePlayer.onInteract().accept(new net.nonswag.tnl.listener.api.player.event.InteractEvent(event.getPlayer(), fakePlayer, type));
                        } else {
                            InteractEvent.Type type = packet.b().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) ?
                                    InteractEvent.Type.LEFT_CLICK : InteractEvent.Type.RIGHT_CLICK;
                            for (Hologram hologram : Holograms.getInstance().cachedValues()) {
                                for (Integer integer : Holograms.getInstance().getIds(hologram, event.getPlayer())) {
                                    if (integer.equals(id.nonnull())) {
                                        InteractEvent interactEvent = new InteractEvent(hologram, event.getPlayer(), type);
                                        hologram.onInteract().accept(interactEvent);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayInBlockDig) {
                PlayerDamageBlockEvent.BlockDamageType damageType = PlayerDamageBlockEvent.BlockDamageType.fromString(((PacketPlayInBlockDig) event.getPacket()).d().name());
                if (!damageType.equals(PlayerDamageBlockEvent.BlockDamageType.UNKNOWN)) {
                    BlockPosition position = ((PacketPlayInBlockDig) event.getPacket()).b();
                    EnumDirection againstBlock = ((PacketPlayInBlockDig) event.getPacket()).c();
                    Block block = new Location(event.getPlayer().getWorld(), position.getX(), position.getY(), position.getZ()).getBlock();
                    Block relative = block.getRelative(againstBlock.getAdjacentX(), againstBlock.getAdjacentY(), againstBlock.getAdjacentZ());
                    if (relative.getType().equals(Material.FIRE)) {
                        position = new BlockPosition(relative.getX(), relative.getY(), relative.getZ());
                        block = new Location(event.getPlayer().getWorld(), position.getX(), position.getY(), position.getZ()).getBlock();
                    }
                    PlayerDamageBlockEvent blockEvent = new PlayerDamageBlockEvent(event.getPlayer(), block, damageType);
                    event.setCancelled(!blockEvent.call());
                    if (blockEvent.isCancelled() && blockEvent.isUpdate()) {
                        if (blockEvent.getBlockDamageType().equals(PlayerDamageBlockEvent.BlockDamageType.STOP_DESTROY_BLOCK)
                                || blockEvent.getBlockDamageType().equals(PlayerDamageBlockEvent.BlockDamageType.START_DESTROY_BLOCK)) {
                            for (BlockFace blockFace : BlockFace.values()) {
                                event.getPlayer().sendBlockChange(blockEvent.getBlock().getLocation(), blockFace);
                            }
                        }
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
            } else if (event.getPacket() instanceof PacketPlayInPickItem) {
                PlayerItemPickEvent pickEvent = new PlayerItemPickEvent(event.getPlayer(), ((PacketPlayInPickItem) event.getPacket()).b());
                if (!pickEvent.call()) event.setCancelled(true);
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
                        if (!e.call()) {
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
                SignMenu signMenu = event.getPlayer().getSignMenu();
                if (signMenu != null) {
                    event.setCancelled(true);
                    if (signMenu.getResponse().hasValue()) {
                        Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> {
                            boolean success = signMenu.getResponse().nonnull().test(event.getPlayer(), ((PacketPlayInUpdateSign) event.getPacket()).c());
                            if (!success && signMenu.isReopenOnFail()) {
                                Bukkit.getScheduler().runTaskLater(Bootstrap.getInstance(), () -> event.getPlayer().openVirtualSignEditor(signMenu), 2);
                            }
                        });
                    }
                    event.getPlayer().getVirtualStorage().remove("current-sign");
                    event.getPlayer().sendBlockChange(signMenu.getLocation(), signMenu.getLocation().getBlock().getBlockData());
                }
            } else if (event.getPacket() instanceof PacketPlayInWindowClick) {
                GUI gui = event.getPlayer().getGUI();
                if (gui != null) {
                    PacketPlayInWindowClick packet = (PacketPlayInWindowClick) event.getPacket();
                    int slot = packet.c();
                    Interaction.Type type = Interaction.Type.fromNMS(packet.d(), packet.g().name());
                    boolean cancel = false;
                    if (slot <= gui.getSize()) {
                        cancel = gui.getClickListener().onClick(event.getPlayer(), slot, type);
                        GUIItem item = gui.getItem(slot);
                        if (item != null) {
                            Interaction interaction = item.getInteraction(type);
                            if (interaction != null) interaction.getAction().accept(event.getPlayer());
                            cancel = true;
                        }
                    }
                    if (cancel) {
                        event.setCancelled(true);
                        event.getPlayer().sendPacket(TNLSetSlot.create(TNLSetSlot.Inventory.COURSER, -1, Material.AIR));
                        event.getPlayer().updateInventory();
                        event.getPlayer().updateGUI();
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayInCloseWindow) {
                GUI gui = event.getPlayer().getGUI();
                if (gui != null) {
                    gui.getViewers().remove(event.getPlayer());
                    event.getPlayer().getVirtualStorage().remove("current-gui");
                    if(gui.isPlaySounds()) {
                        event.getPlayer().playSound(Sound.BLOCK_CHEST_CLOSE);
                    }
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
                } else itemStack = event.getPlayer().getInventory().getItemInOffHand();
                PlayerInteractEvent interactEvent = new PlayerInteractEvent(event.getPlayer(), block, itemStack);
                if (!interactEvent.call()) {
                    event.setCancelled(true);
                    Bukkit.getScheduler().runTask(Bootstrap.getInstance(), () -> {
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
            } else if (event.getPacket() instanceof PacketPlayOutCloseWindow) {
                if (!event.isCancelled()) {
                    GUI gui = event.getPlayer().getGUI();
                    if (gui != null) {
                        gui.getViewers().remove(event.getPlayer());
                        event.getPlayer().getVirtualStorage().remove("current-gui");
                        if(gui.isPlaySounds()) {
                            event.getPlayer().playSound(Sound.BLOCK_CHEST_CLOSE);
                        }
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
