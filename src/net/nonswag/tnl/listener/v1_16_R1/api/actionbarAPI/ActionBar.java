package net.nonswag.tnl.listener.v1_16_R1.api.actionbarAPI;

import net.minecraft.server.v1_16_R1.ChatMessageType;
import net.minecraft.server.v1_16_R1.IChatBaseComponent;
import net.minecraft.server.v1_16_R1.PacketPlayOutChat;
import net.nonswag.tnl.listener.v1_16_R1.utils.PacketUtil;
import org.bukkit.entity.Player;

public class ActionBar {

    public static void send(Player player, String message) {
        try {
            if (message != null && player != null && player.isOnline()) {
                PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), ChatMessageType.a((byte) 2), player.getUniqueId());
                PacketUtil.sendPacket(player, packet);
            }
        } catch (Throwable ignored) {
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
