package net.nonswag.tnl.listener.listeners.v1_7_R1;

import net.minecraft.server.v1_7_R1.*;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Color;
import net.nonswag.tnl.listener.api.message.MessageKey;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.object.Objects;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.listener.events.EntityDamageByPlayerEvent;
import net.nonswag.tnl.listener.events.PlayerChatEvent;
import net.nonswag.tnl.listener.events.PlayerInteractAtEntityEvent;
import net.nonswag.tnl.listener.events.PlayerPacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PacketListener implements Listener {

    @EventHandler
    public void onPacket(PlayerPacketEvent<Packet> event) {
        if (event.isIncoming()) {
            if (event.getPacket() instanceof PacketPlayInChat && Settings.BETTER_CHAT.getValue()) {
                String message = Color.Minecraft.unColorize(Objects.getOrDefault(((PacketPlayInChat) event.getPacket()).c(), ""), '§');
                if (message.length() <= 0 || Color.Minecraft.unColorize(message.replace(" ", ""), '&').isEmpty()) {
                    event.setCancelled(true);
                } else {
                    PlayerChatEvent chatEvent = new PlayerChatEvent(event.getPlayer(), message);
                    Bukkit.getPluginManager().callEvent(chatEvent);
                    event.setCancelled(chatEvent.isCancelled());
                    if (!chatEvent.isCancelled() && !event.isCancelled()) {
                        if (!chatEvent.isCommand()) {
                            event.setCancelled(true);
                            String[] strings = message.split(" ");
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                if (message.toLowerCase().contains(all.getDisplayName().toLowerCase())) {
                                    for (String string : strings) {
                                        if (string.equalsIgnoreCase("@" + all.getDisplayName())) {
                                            message = message.replace(string + " ", "§8(§3" + all.getDisplayName() + "§8) §f");
                                            message = message.replace(string, "§8(§3" + all.getDisplayName() + "§8) §f");
                                            all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 5);
                                        }
                                    }
                                }
                                all.playSound(all.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 15);
                            }
                            for (TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> all : TNLListener.getInstance().getOnlinePlayers()) {
                                if (chatEvent.getFormat() == null) {
                                    all.sendMessage(MessageKey.CHAT_FORMAT, new Placeholder("world", event.getPlayer().getWorldAlias()), new Placeholder("player", event.getPlayer().getName()), new Placeholder("message", message), new Placeholder("colored_message", Color.Minecraft.colorize(message, '&')), new Placeholder("text", Color.Minecraft.colorize(message, '&')));
                                } else {
                                    all.sendMessage(chatEvent.getFormat(), new Placeholder("world", event.getPlayer().getWorldAlias()), new Placeholder("player", event.getPlayer().getName()), new Placeholder("message", message), new Placeholder("colored_message", Color.Minecraft.colorize(message, '&')));
                                }
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
            } else if (event.getPacket() instanceof PacketPlayInUseEntity) {
                if (!event.isCancelled()) {
                    Entity entity = ((PacketPlayInUseEntity) event.getPacket()).a((World) event.getPlayer().getWorldServer());
                    if (entity != null) {
                        if (((PacketPlayInUseEntity) event.getPacket()).c().equals(EnumEntityUseAction.ATTACK)) {
                            EntityDamageByPlayerEvent<Entity> damageEvent = new EntityDamageByPlayerEvent<>(event.getPlayer(), entity);
                            Bukkit.getPluginManager().callEvent(damageEvent);
                            if (damageEvent.isCancelled()) {
                                event.setCancelled(true);
                            }
                        }
                    } else {
                        PlayerInteractAtEntityEvent interactEvent = new PlayerInteractAtEntityEvent(event.getPlayer(), ((Objects<Integer>) event.getPacketField("a")).getOrDefault(-1));
                        Bukkit.getPluginManager().callEvent(interactEvent);
                        if (interactEvent.isCancelled()) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        } else if (event.isOutgoing()) {
           if (event.getPacket() instanceof PacketPlayOutEntityStatus) {
               int id = ((Objects<Integer>) event.getPacketField("a")).getOrDefault(-1);
               if (event.getPlayer().getEntityId() == id) {
                   event.setPacketField("b", (byte) 28);
               }
           } else if (event.getPacket() instanceof PacketPlayOutChat) {
                Objects<IChatBaseComponent> a = ((Objects<IChatBaseComponent>) event.getPacketField("a"));
                if (a.hasValue()) {
                    String text = a.nonnull().toString();
                    if (Settings.BETTER_PERMISSIONS.getValue()) {
                        if (text.equalsIgnoreCase("TextComponent{text='', siblings=[TextComponent{text='I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.', siblings=[], style=Style{hasParent=true, color=§c, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null}}], style=Style{hasParent=false, color=null, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null}}")) {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(MessageKey.NO_PERMISSION, new Placeholder("permission", "tnl.admin"));
                        }
                    }
                }
            }
        }
    }
}
