package net.nonswag.tnl.listener;

import net.nonswag.tnl.listener.api.command.CommandManager;
import net.nonswag.tnl.listener.api.config.minecraft.ServerProperties;
import net.nonswag.tnl.listener.api.event.EventManager;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.plugin.PluginUpdate;
import net.nonswag.tnl.listener.api.server.Server;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.listener.api.sign.SignMenu;
import net.nonswag.tnl.listener.api.version.ServerVersion;
import net.nonswag.tnl.listener.listeners.JoinListener;
import net.nonswag.tnl.listener.listeners.KickListener;
import net.nonswag.tnl.listener.listeners.QuitListener;
import net.nonswag.tnl.listener.listeners.WorldListener;
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
    private final HashMap<Player, TNLPlayer> playerHashMap = new HashMap<>();
    @Nonnull
    private final HashMap<UUID, SignMenu> signHashMap = new HashMap<>();
    @Nonnull
    private final ServerVersion version;
    @Nonnull
    private final String serverName = ServerProperties.getInstance().getOrDefault("server-name", new File("").getAbsoluteFile().getName());

    protected TNLListener() {
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
        if (getVersion().equals(ServerVersion.UNKNOWN)) {
            System.exit(1);
        }
    }

    protected void load() {
        if (Settings.DELETE_OLD_LOGS.getValue()) {
            deleteOldLogs();
        }
    }

    protected void enable() {
        CommandManager commandManager = new CommandManager(Bootstrap.getInstance());
        EventManager eventManager = new EventManager(Bootstrap.getInstance());
        new Thread(() -> {
            for (String server : Settings.SERVERS.getValue()) {
                if (Settings.getConfig().has("server-" + server)) {
                    String value = Settings.getConfig().getString("server-" + server);
                    if (value != null && !value.isEmpty()) {
                        if (value.equalsIgnoreCase("host:port")) {
                            Logger.error.println("§cYou have to setup the server §8'§4" + server + "§8'§c correctly");
                        } else {
                            try {
                                Server s = new Server(server, new InetSocketAddress(value.split(":")[0], Integer.parseInt(value.split(":")[1])));
                                Logger.info.println("§aInitialized new server §8'§6" + s.toString() + "§8'");
                            } catch (Exception e) {
                                Logger.error.println("§cFailed to load server §8'§4" + server + "§8'", "§cThe ip-address format is §8'§4host:port§8' (§4example localhost:25565§8)", e);
                            }
                        }
                    } else {
                        Settings.getConfig().setValue("server-" + server, "host:port");
                        Logger.info.println("§aFound new server §8'§6" + server + "§8'");
                    }
                } else {
                    Settings.getConfig().setValue("server-" + server, "host:port");
                    Logger.info.println("§aFound new server §8'§6" + server + "§8'");
                }
            }
            Settings.getConfig().save();
        }, "server-loader").start();
        Bukkit.getMessenger().registerOutgoingPluginChannel(Bootstrap.getInstance(), "BungeeCord");
        if (Settings.AUTO_UPDATER.getValue()) {
            new PluginUpdate(Bootstrap.getInstance()).downloadUpdate();
        }
        try {
            if (getVersion().equals(ServerVersion.v1_16_5)) {
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.v1_16.R3.PacketListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.modern.CommandListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.modern.InteractListener());
            } else if (getVersion().equals(ServerVersion.v1_15_2)) {
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.v1_15.R1.PacketListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.modern.CommandListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.modern.InteractListener());
            } else if (getVersion().equals(ServerVersion.v1_7_10)) {
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.v1_7.R4.PacketListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.legacy.CommandListener());
            } else if (getVersion().equals(ServerVersion.v1_7_2)) {
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.v1_7.R1.PacketListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.legacy.CommandListener());
            }
            Logger.info.println("§aLoading §6TNLListener §8(§7" + getVersion().name().replace("_", ".") + "§8)");
            eventManager.registerListener(new WorldListener());
            eventManager.registerListener(new JoinListener());
            eventManager.registerListener(new KickListener());
            eventManager.registerListener(new QuitListener());
        } catch (Throwable t) {
            Logger.error.println("§cFailed to register listener", t);
        }
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
    public TNLPlayer getPlayer(@Nonnull Player player) {
        return getPlayerHashMap().get(player);
    }

    @Nonnull
    public String getServerName() {
        return serverName;
    }

    @Nullable
    public TNLPlayer getPlayer(@Nonnull CommandSender player) {
        if (player instanceof Player) {
            return getPlayerHashMap().get(player);
        }
        return null;
    }

    @Nullable
    public TNLPlayer getPlayer(@Nullable Entity player) {
        if (player instanceof Player) {
            return getPlayerHashMap().get(player);
        } else {
            return null;
        }
    }

    @Nullable
    public TNLPlayer getPlayer(@Nonnull String player) {
        return getPlayerHashMap().get(Bukkit.getPlayer(player));
    }

    @Nullable
    public TNLPlayer getPlayer(@Nonnull UUID player) {
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
