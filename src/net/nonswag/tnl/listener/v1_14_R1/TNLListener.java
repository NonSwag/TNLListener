package net.nonswag.tnl.listener.v1_14_R1;

import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.v1_14_R1.adapter.PacketAdapter;
import net.nonswag.tnl.listener.v1_14_R1.eventListener.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TNLListener {

    public static void onEnable() {
        try {
            Bukkit.getPluginManager().registerEvents(new PacketListener(), NMSMain.getPlugin());
            Bukkit.getPluginManager().registerEvents(new JoinListener(), NMSMain.getPlugin());
            Bukkit.getPluginManager().registerEvents(new InteractListener(), NMSMain.getPlugin());
            Bukkit.getPluginManager().registerEvents(new KickListener(), NMSMain.getPlugin());
            Bukkit.getPluginManager().registerEvents(new QuitListener(), NMSMain.getPlugin());
            NMSMain.print("Automatically Detected net.minecraft.server.v1_14_R1",
                    "Started the listener with org.bukkit.craftukkit.v1_14_R1",
                    "Using implemented api from net.nonswag.tnl.listener.v1_14_R1");
            for (Player all : Bukkit.getOnlinePlayers()) {
                PacketAdapter.uninject(all);
                NMSMain.delayedTask(() -> PacketAdapter.inject(all), 1);
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }
}
