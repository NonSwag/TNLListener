package net.nonswag.tnl.listener;

import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class TNLListener {

    @Nonnull
    private static final HashMap<World, String> worldAliasHashMap = new HashMap<>();
    @Nonnull
    private static final HashMap<Player, TNLPlayer> playerHashMap = new HashMap<>();

    public static void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PacketListener(), NMSMain.getPlugin());
        Bukkit.getPluginManager().registerEvents(new JoinListener(), NMSMain.getPlugin());
        Bukkit.getPluginManager().registerEvents(new InteractListener(), NMSMain.getPlugin());
        Bukkit.getPluginManager().registerEvents(new KickListener(), NMSMain.getPlugin());
        Bukkit.getPluginManager().registerEvents(new QuitListener(), NMSMain.getPlugin());
    }

    public static void updatePlayers() {
        for (Player player : new ArrayList<>(getPlayerHashMap().keySet())) {
            if (!player.isOnline()) {
                getPlayerHashMap().remove(player);
            }
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
