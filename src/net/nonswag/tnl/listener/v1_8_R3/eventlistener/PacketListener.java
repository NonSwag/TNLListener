package net.nonswag.tnl.listener.v1_8_R3.eventlistener;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.*;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.v1_8_R3.eventhandler.PlayerChatEvent;
import net.nonswag.tnl.listener.v1_8_R3.eventhandler.PlayerPacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PacketListener implements Listener {

    @EventHandler
    public void onPacket(PlayerPacketEvent event) {
        if (event.isIncoming()) {
            if (event.getPacket() instanceof PacketPlayInChat) {
                if (NMSMain.isBetterChat()) {
                    if (event.getPacketField("a").toString().replace(" ", "").equals("")) {
                        event.setCancelled(true);
                    } else {
                        if (event.getPacketField("a").toString().contains("§")) {
                            event.setPacketField("a", event.getPacketField("a").toString().replace("§", "?"));
                        }
                        if (event.getPacketField("a").toString().startsWith("/") && !NMSMain.isUseCommandLineAsChat()) {
                            return;
                        }
                        event.setCancelled(true);
                        PlayerChatEvent chatEvent = new PlayerChatEvent(event.getPlayer(), event.getPacketField("a").toString());
                        NMSMain.callEvent(chatEvent);
                        if (!chatEvent.isCancelled()) {
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                if (chatEvent.getFormat() != null) {
                                    all.sendMessage(chatEvent.getFormat());
                                } else {
                                    all.sendMessage(chatEvent.getPlayer().getDisplayName() + " §8» §r" + chatEvent.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        } else if (event.isOutgoing()) {
            if (event.getPacket() instanceof PacketPlayOutChat) {
                if (event.getPacketField("a") == null) {
                    return;
                }
                TextComponent textComponent = new TextComponent(event.getPacketField("a").toString());
                if (NMSMain.isBetterPermissions()) {
                    if (textComponent.getText().equalsIgnoreCase("TextComponent{text='', " +
                            "siblings=[TextComponent{text='I'm sorry, " +
                            "but you do not have permission to perform this command. " +
                            "Please contact the server administrators if you believe that this is in error.', " +
                            "siblings=[], style=Style{hasParent=true, color=§c, bold=null, italic=null, " +
                            "underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}], " +
                            "style=Style{hasParent=false, color=null, bold=null, italic=null, underlined=null, " +
                            "obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}")) {
                        event.setPacketField("a", new ChatMessage(NMSMain.getPermissionMessage()));
                    }
                }
            }
        }
    }
}
