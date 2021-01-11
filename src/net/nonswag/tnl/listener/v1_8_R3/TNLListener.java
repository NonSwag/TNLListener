package net.nonswag.tnl.listener.v1_8_R3;

import io.netty.channel.Channel;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.v1_8_R3.adapter.PacketAdapter;
import net.nonswag.tnl.listener.v1_8_R3.eventlistener.JoinListener;
import net.nonswag.tnl.listener.v1_8_R3.eventlistener.KickListener;
import net.nonswag.tnl.listener.v1_8_R3.eventlistener.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TNLListener {

    public static void onEnable() {
        try {
            Bukkit.getPluginManager().registerEvents(new PacketListener(), NMSMain.getPlugin());
            Bukkit.getPluginManager().registerEvents(new JoinListener(), NMSMain.getPlugin());
            Bukkit.getPluginManager().registerEvents(new KickListener(), NMSMain.getPlugin());
            NMSMain.print("Automatically Detected net.minecraft.server.v1_8_R3",
                    "Started the listener with org.bukkit.craftukkit.v1_8_R3",
                    "Using implemented api from net.nonswag.tnl.listener.v1_8_R3");
            for (Player all : Bukkit.getOnlinePlayers()) {
                Channel channel = ((CraftPlayer) all).getHandle().playerConnection.networkManager.channel;
                channel.eventLoop().submit(() -> {
                    channel.pipeline().remove(all.getName() + "-TNLListener");
                });
                NMSMain.delayedTask(() -> PacketAdapter.inject(all), 1);
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }
}
