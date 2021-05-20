package net.nonswag.tnl.listener.listeners.v1_7.R1;

import net.minecraft.server.v1_7_R1.*;
import net.nonswag.tnl.listener.Holograms;
import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.holograms.Hologram;
import net.nonswag.tnl.listener.api.holograms.event.InteractEvent;
import net.nonswag.tnl.listener.api.message.MessageKey;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.object.Objects;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.listener.events.EntityDamageByPlayerEvent;
import net.nonswag.tnl.listener.events.PlayerChatEvent;
import net.nonswag.tnl.listener.events.PlayerInteractAtEntityEvent;
import net.nonswag.tnl.listener.events.PlayerPacketEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PacketListener implements Listener {

    @EventHandler
    public void onPacket(PlayerPacketEvent<Packet> event) {
        if (event.isIncoming()) {
            if (event.getPacket() instanceof PacketPlayInChat && Settings.BETTER_CHAT.getValue()) {
                PlayerChatEvent chatEvent = new PlayerChatEvent(event.getPlayer(), ((PacketPlayInChat) event.getPacket()).c());
                event.getPlayer().chat(chatEvent);
                event.setCancelled(!chatEvent.isCommand());
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
                PacketPlayInUseEntity packet = (PacketPlayInUseEntity) event.getPacket();
                Entity entity = packet.a((World) event.getPlayer().getWorldServer());
                if (entity != null) {
                    TNLEvent entityEvent;
                    if (packet.c().equals(EnumEntityUseAction.ATTACK)) {
                        entityEvent = new EntityDamageByPlayerEvent(event.getPlayer(), entity.getBukkitEntity());
                    } else {
                        entityEvent = new PlayerInteractAtEntityEvent(event.getPlayer(), entity.getBukkitEntity());
                    }
                    if (!entityEvent.call()) event.setCancelled(true);
                } else {
                    Objects<Integer> id = event.getPacketField("a", Integer.class);
                    if (id.hasValue()) {
                        InteractEvent.Type type = packet.c().equals(EnumEntityUseAction.ATTACK) ?
                                InteractEvent.Type.LEFT_CLICK : InteractEvent.Type.RIGHT_CLICK;
                        for (Hologram hologram : Holograms.getInstance().cachedValues()) {
                            for (Integer integer : Holograms.getInstance().getIds(hologram, event.getPlayer())) {
                                if (integer.equals(id.nonnull())) {
                                    InteractEvent interactEvent = new InteractEvent(event.getPlayer(), hologram, type);
                                    hologram.onInteract().accept(interactEvent);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        } else if (event.isOutgoing()) {
           if (event.getPacket() instanceof PacketPlayOutEntityStatus) {
               int id = ((Objects<Integer>) event.getPacketField("a")).getOrDefault(-1);
               if (event.getPlayer().getId() == id) {
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
