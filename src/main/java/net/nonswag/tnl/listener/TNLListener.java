package net.nonswag.tnl.listener;

import net.nonswag.tnl.listener.api.config.minecraft.ServerProperties;
import net.nonswag.tnl.listener.api.event.EventManager;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.plugin.PluginUpdate;
import net.nonswag.tnl.listener.api.server.Server;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.listener.api.version.Version;
import net.nonswag.tnl.listener.listeners.JoinListener;
import net.nonswag.tnl.listener.listeners.KickListener;
import net.nonswag.tnl.listener.listeners.QuitListener;
import net.nonswag.tnl.listener.listeners.WorldListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class TNLListener {

    @Nonnull
    private static final TNLListener instance = new TNLListener();

    @Nonnull
    private final HashMap<Player, TNLPlayer> playerHashMap = new HashMap<>();
    @Nonnull
    private final HashMap<String, Server> serverHashMap = new HashMap<>();
    @Nonnull
    private final Version version;
    @Nonnull
    private final String serverName = ServerProperties.getInstance().getOrDefault("server-name", new File("").getAbsoluteFile().getName());

    protected TNLListener() {
        Version version = Version.UNKNOWN;
        for (Version serverVersion : Version.values()) if (!serverVersion.equals(Version.UNKNOWN)) {
            for (String v : serverVersion.getVersions()) {
                if (Bukkit.getVersion().toLowerCase().contains(v)) {
                    version = serverVersion;
                    break;
                }
            }
        }
        this.version = version;
        if (getVersion().equals(Version.UNKNOWN)) {
            Logger.error.println("Your server version is not supported (" + Bukkit.getVersion() + ")");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }
            throw new RuntimeException("Your server version could not be detected: " + Bukkit.getVersion());
        }
    }

    protected void enable() {
        EventManager eventManager = new EventManager(Bootstrap.getInstance());
        for (String server : Settings.SERVERS.getValue()) {
            if (Settings.getConfig().has("server-" + server)) {
                String value = Settings.getConfig().getString("server-" + server);
                if (value != null && !value.isEmpty()) {
                    if (value.equalsIgnoreCase("host:port")) {
                        Logger.error.println("You have to setup the server <'" + server + "'> correctly");
                    } else {
                        try {
                            Server s = new Server(server, new InetSocketAddress(value.split(":")[0], Integer.parseInt(value.split(":")[1])));
                            getServerHashMap().put(s.getName(), s);
                            Logger.debug.println("Initialized new server <'" + s.getName() + "'> (" + s.getInetSocketAddress().getHostString() + ":" + s.getInetSocketAddress().getPort() + ")");
                        } catch (Exception e) {
                            Logger.error.println("Failed to load server <'" + server + "'>", "The ip-address format is 'host:port' (example localhost:25565)", e.getMessage());
                        }
                    }
                } else {
                    Settings.getConfig().setValue("server-" + server, "host:port");
                    Logger.debug.println("Found new server <'" + server + "'>");
                }
            } else {
                Settings.getConfig().setValue("server-" + server, "host:port");
                Logger.debug.println("Found new server <'" + server + "'>");
            }
        }
        if (Settings.DELETE_OLD_LOGS.getValue()) deleteOldLogs();
        Settings.getConfig().save();
        Bukkit.getMessenger().registerOutgoingPluginChannel(Bootstrap.getInstance(), "BungeeCord");
        if (Settings.AUTO_UPDATER.getValue()) new PluginUpdate(Bootstrap.getInstance()).downloadUpdate();
        try {
            if (getVersion().equals(Version.v1_17)) {
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.v1_17.R1.PacketListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.modern.CommandListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.modern.InteractListener());
            } else if (getVersion().equals(Version.v1_16_4)) {
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.v1_16.R3.PacketListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.modern.CommandListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.modern.InteractListener());
            } else if (getVersion().equals(Version.v1_15_2)) {
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.v1_15.R1.PacketListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.modern.CommandListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.modern.InteractListener());
            } else if (getVersion().equals(Version.v1_7_6)) {
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.v1_7.R4.PacketListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.legacy.CommandListener());
            } else if (getVersion().equals(Version.v1_7_2)) {
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.v1_7.R1.PacketListener());
                eventManager.registerListener(new net.nonswag.tnl.listener.listeners.legacy.CommandListener());
            }
            Logger.debug.println("Loading TNLListener (" + getVersion() + ")");
            eventManager.registerListener(new WorldListener());
            eventManager.registerListener(new JoinListener());
            eventManager.registerListener(new KickListener());
            eventManager.registerListener(new QuitListener());
        } catch (Throwable t) {
            Logger.error.println("Failed to register listener", t);
        }
        for (Player all : Bukkit.getOnlinePlayers()) {
            TNLPlayer player = TNLPlayer.cast(all);
            player.inject();
        }
        updateTeams();
    }

    protected void disable() {
        for (TNLPlayer all : getOnlinePlayers()) {
            all.closeSignMenu();
            all.closeGUI();
            all.stopConversation();
            Holograms.getInstance().unloadAll(all);
            all.uninject();
        }
    }

    @Nonnull
    public List<TNLPlayer> getOnlinePlayers() {
        return new ArrayList<>(getPlayerHashMap().values());
    }

    @Nonnull
    public HashMap<Player, TNLPlayer> getPlayerHashMap() {
        return playerHashMap;
    }

    @Nonnull
    public HashMap<String, Server> getServerHashMap() {
        return serverHashMap;
    }

    @Nonnull
    public Collection<Server> getServers() {
        return getServerHashMap().values();
    }

    @Nonnull
    public String getServerName() {
        return serverName;
    }

    @Nonnull
    public static TNLListener getInstance() {
        return instance;
    }

    @Nonnull
    public Version getVersion() {
        return version;
    }

    public void deleteOldLogs() {
        File file = new File("logs/");
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File all : files) {
                    if (all.isFile() && all.getName().endsWith(".log.gz")) {
                        if (!all.delete()) {
                            Logger.error.println("Failed to delete file <'" + all.getAbsolutePath() + "§8'>");
                        } else {
                            Logger.debug.println("Deleted old log file <'" + all.getAbsolutePath() + "'>");
                        }
                    }
                }
            }
        }
    }

    @Nonnull
    public List<String> getWorlds() {
        List<String> worlds = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) worlds.add(world.getName());
        return worlds;
    }

    public void updateTeams() {
        for (TNLPlayer all : getOnlinePlayers()) all.updateTeam();
    }

    public void setCollisions(@Nonnull org.bukkit.scoreboard.Team.OptionStatus collision) {
        for (TNLPlayer all : TNLListener.getInstance().getOnlinePlayers()) {
            for (Team team : all.getScoreboard().getTeams()) team.setOption(Team.Option.COLLISION_RULE, collision);
        }
    }

    public void setNameTagVisibility(@Nonnull org.bukkit.scoreboard.Team.OptionStatus nameTagVisibility) {
        for (TNLPlayer all : TNLListener.getInstance().getOnlinePlayers()) {
            for (Team team : all.getScoreboard().getTeams()) {
                team.setOption(Team.Option.NAME_TAG_VISIBILITY, nameTagVisibility);
            }
        }
    }
}
