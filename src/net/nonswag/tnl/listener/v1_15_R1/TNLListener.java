package net.nonswag.tnl.listener.v1_15_R1;

import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.v1_15_R1.adapter.PacketAdapter;
import net.nonswag.tnl.listener.v1_15_R1.api.playerAPI.TNLPlayer;
import net.nonswag.tnl.listener.v1_15_R1.eventListener.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class TNLListener {

    @Nonnull private static final HashMap<World, String> worldAliasHashMap = new HashMap<>();
    @Nonnull private static final HashMap<Player, TNLPlayer> playerHashMap = new HashMap<>();

    public static void onEnable() {
        try {
            Bukkit.getPluginManager().registerEvents(new PacketListener(), NMSMain.getPlugin());
            Bukkit.getPluginManager().registerEvents(new JoinListener(), NMSMain.getPlugin());
            Bukkit.getPluginManager().registerEvents(new InteractListener(), NMSMain.getPlugin());
            Bukkit.getPluginManager().registerEvents(new KickListener(), NMSMain.getPlugin());
            Bukkit.getPluginManager().registerEvents(new QuitListener(), NMSMain.getPlugin());
            NMSMain.print("Automatically Detected net.minecraft.server.v1_15_R1",
                    "Started the listener with org.bukkit.craftukkit.v1_15_R1",
                    "Using implemented api from net.nonswag.tnl.listener.v1_15_R1");
            for (TNLPlayer all : getOnlinePlayers()) {
                PacketAdapter.uninject(all);
                NMSMain.delayedTask(() -> PacketAdapter.inject(all), 1);
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    @Nonnull
    public static List<TNLPlayer> getOnlinePlayers() {
        return new ArrayList<>(getPlayerHashMap().values());
    }

    @Nonnull
    public static HashMap<World, String> getWorldAliasHashMap() {
        return worldAliasHashMap;
    }

    @Nonnull
    public static HashMap<Player, TNLPlayer> getPlayerHashMap() {
        return playerHashMap;
    }
}
