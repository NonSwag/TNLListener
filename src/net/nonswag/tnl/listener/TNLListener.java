package net.nonswag.tnl.listener;

import com.google.gson.JsonElement;
import net.nonswag.tnl.listener.api.command.CommandManager;
import net.nonswag.tnl.listener.api.event.EventManager;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.plugin.PluginUpdate;
import net.nonswag.tnl.listener.api.server.Server;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.listener.api.sign.SignMenu;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import net.nonswag.tnl.listener.listeners.InteractListener;
import net.nonswag.tnl.listener.listeners.JoinListener;
import net.nonswag.tnl.listener.listeners.KickListener;
import net.nonswag.tnl.listener.listeners.QuitListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TNLListener {

    private static final TNLListener instance = new TNLListener();

    @Nonnull
    private final HashMap<World, String> worldAliasHashMap = new HashMap<>();
    @Nonnull
    private final HashMap<Player, TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> playerHashMap = new HashMap<>();
    @Nonnull
    private final HashMap<UUID, SignMenu> signHashMap = new HashMap<>();
    @Nonnull
    private final ServerVersion version;

    TNLListener() {
        ServerVersion version = ServerVersion.UNKNOWN;
        for (ServerVersion serverVersion : ServerVersion.values()) {
            if (!serverVersion.equals(ServerVersion.UNKNOWN)) {
                if (Bukkit.getVersion().toLowerCase().contains(serverVersion.getVersion().toLowerCase())) {
                    version = serverVersion;
                    break;
                }
            }
        }
        this.version = version;
    }

    protected void enable() {
        CommandManager commandManager = new CommandManager(Loader.getInstance());
        EventManager eventManager = new EventManager(Loader.getInstance());
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
        Bukkit.getMessenger().registerOutgoingPluginChannel(Loader.getInstance(), "BungeeCord");
        if (Settings.DELETE_OLD_LOGS.getValue()) {
            deleteOldLogs();
        }
        if (Settings.AUTO_UPDATER.getValue()) {
            new PluginUpdate(Loader.getInstance()).downloadUpdate();
        }
        try {
            if (getVersion().equals(ServerVersion.v1_15_2)) {
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.v1_15_R1.PacketListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.v1_15_R1.CommandListener());
                eventManager.registerListener(new InteractListener());
            } else if (getVersion().equals(ServerVersion.v1_7_2)) {
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.v1_7_R1.PacketListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.v1_7_R1.CommandListener());
            }
            eventManager.registerListener(new JoinListener());
            eventManager.registerListener(new KickListener());
            eventManager.registerListener(new QuitListener());
        } catch (Throwable t) {
            Logger.error.println("§cFailed to register listener", t);
        }
    }

    @Nonnull
    public List<TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> getOnlinePlayers() {
        return new ArrayList<>(getPlayerHashMap().values());
    }

    @Nonnull
    public HashMap<World, String> getWorldAliasHashMap() {
        return worldAliasHashMap;
    }

    @Nonnull
    public HashMap<Player, TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> getPlayerHashMap() {
        return playerHashMap;
    }

    @Nonnull
    public TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> getPlayer(@Nonnull Player player) {
        return getPlayerHashMap().get(player);
    }

    @Nullable
    public TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> getPlayer(@Nonnull CommandSender player) {
        if (player instanceof Player) {
            return getPlayerHashMap().get(player);
        }
        return null;
    }

    @Nullable
    public TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> getPlayer(@Nullable Entity player) {
        if (player instanceof Player) {
            return getPlayerHashMap().get(player);
        } else {
            return null;
        }
    }

    @Nullable
    public TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> getPlayer(@Nonnull String player) {
        return getPlayerHashMap().get(Bukkit.getPlayer(player));
    }

    @Nullable
    public TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> getPlayer(@Nonnull UUID player) {
        return getPlayerHashMap().get(Bukkit.getPlayer(player));
    }

    public static TNLListener getInstance() {
        return instance;
    }

    @Nonnull
    public ServerVersion getVersion() {
        return version;
    }

    public void deleteOldLogs() {
        File file = new File("logs/");
        if (file.exists() && file.isDirectory()) {
            for (File all : file.listFiles()) {
                if (all.isFile() && all.getName().endsWith(".log.gz")) {
                    if (!all.delete()) {
                        Logger.error.println("§cFailed to delete file §8'§4" + all.getAbsolutePath() + "§8'");
                    } else {
                        Logger.debug.println("§aDeleted old log file §8'§6" + all.getAbsolutePath() + "§8'");
                    }
                }
            }
        }
    }

    @Nonnull
    public List<String> getWorlds() {
        List<String> worlds = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            worlds.add(world.getName());
        }
        return worlds;
    }

    @Nonnull
    public HashMap<UUID, SignMenu> getSignHashMap() {
        return signHashMap;
    }
}
