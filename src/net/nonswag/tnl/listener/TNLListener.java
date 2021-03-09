package net.nonswag.tnl.listener;

import com.google.gson.JsonElement;
import net.nonswag.tnl.listener.api.command.CommandManager;
import net.nonswag.tnl.listener.api.event.EventManager;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.server.Server;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.listener.listeners.*;
import net.nonswag.tnl.listener.utils.PluginUpdate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TNLListener {

    @Nonnull
    private static final TNLListener instance = new TNLListener();

    @Nonnull
    private final HashMap<World, String> worldAliasHashMap = new HashMap<>();
    @Nonnull
    private final HashMap<Player, TNLPlayer> playerHashMap = new HashMap<>();

    protected TNLListener() {
    }

    protected void enable() {
        CommandManager commandManager = new CommandManager(TNLMain.getInstance());
        EventManager eventManager = new EventManager(TNLMain.getInstance());
        new Thread(() -> {
            for (JsonElement server : Settings.SERVERS.getValue()) {
                if (Settings.getConfig().getJsonElement().getAsJsonObject().has("server-" + server.getAsString())) {
                    String value = Settings.getConfig().getJsonElement().getAsJsonObject().get("server-" + server.getAsString()).getAsString();
                    if (value != null && !value.isEmpty()) {
                        if (value.equalsIgnoreCase("host:port")) {
                            Logger.error.println("§cYou have to setup the server §8'§4" + server.getAsString() + "§8'§c correctly");
                        } else {
                            try {
                                Server s = new Server(server.getAsString(), new InetSocketAddress(value.split(":")[0], Integer.parseInt(value.split(":")[1])));
                                Logger.info.println("§aInitialized new server §8'§6" + s.toString() + "§8'");
                            } catch (Exception e) {
                                Logger.error.println("§cFailed to load server §8'§4" + server.getAsString() + "§8'", "§cThe ip-address format is §8'§4host:port§8' (§4example localhost:25565§8)", e);
                            }
                        }
                    } else {
                        Settings.getConfig().getJsonElement().getAsJsonObject().addProperty("server-" + server.getAsString(), "host:port");
                        Logger.info.println("§aFound new server §8'§6" + server.getAsString() + "§8'");
                    }
                } else {
                    Settings.getConfig().getJsonElement().getAsJsonObject().addProperty("server-" + server.getAsString(), "host:port");
                    Logger.info.println("§aFound new server §8'§6" + server.getAsString() + "§8'");
                }
            }
            Settings.getConfig().save();
        }, "server-loader").start();
        Bukkit.getMessenger().registerOutgoingPluginChannel(TNLMain.getInstance(), "BungeeCord");
        new PluginUpdate(TNLMain.getInstance()).downloadUpdate();
        try {
            eventManager.registerListener(new PacketListener());
            eventManager.registerListener(new JoinListener());
            eventManager.registerListener(new InteractListener());
            eventManager.registerListener(new KickListener());
            eventManager.registerListener(new QuitListener());
            eventManager.registerListener(new CommandListener());
        } catch (Throwable t) {
            Logger.error.println("§cFailed to register listener", t);
        }
    }

    public void updatePlayers() {
        getPlayerHashMap().keySet().removeIf(next -> !next.isOnline());
    }

    @Nonnull
    public List<TNLPlayer> getOnlinePlayers() {
        return new ArrayList<>(getPlayerHashMap().values());
    }

    @Nonnull
    public HashMap<World, String> getWorldAliasHashMap() {
        return worldAliasHashMap;
    }

    @Nonnull
    public HashMap<Player, TNLPlayer> getPlayerHashMap() {
        return playerHashMap;
    }

    @Nonnull
    public static TNLListener getInstance() {
        return instance;
    }

    public void deleteOldLogs() {
        throw new UnsupportedOperationException("This feature is currently not supported");
    }

    @Nonnull
    public List<String> getWorlds() {
        List<String> worlds = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            worlds.add(world.getName());
        }
        return worlds;
    }
}
