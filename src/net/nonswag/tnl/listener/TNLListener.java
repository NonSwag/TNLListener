package net.nonswag.tnl.listener;

import net.nonswag.tnl.listener.api.annotations.Soon;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.server.Server;
import net.nonswag.tnl.listener.listeners.*;
import net.nonswag.tnl.listener.utils.ConfigUtil;
import net.nonswag.tnl.listener.utils.FileUtil;
import net.nonswag.tnl.listener.utils.MathUtil;
import net.nonswag.tnl.listener.utils.PluginUpdate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TNLListener {

    @Nonnull
    private static final TNLListener instance = new TNLListener();

    @Nonnull
    private final HashMap<World, String> worldAliasHashMap = new HashMap<>();
    @Nonnull
    private final HashMap<Player, TNLPlayer> playerHashMap = new HashMap<>();
    @Nonnull
    private final NMSMain main;
    @Nonnull
    private String permissionMessage = "§cYou have no Rights §8(§4%permission%§8)";
    @Nonnull
    private String unknownCommandMessage = "§cThe Command §8(§4%command%§8)§c doesn't exist";
    @Nonnull
    private String kickMessageSpamming = "§cSpamming is prohibited";
    @Nonnull
    private String prefix = "§8[§f§lTNL§8]§r";
    @Nonnull
    private String standardItemName = "§8* §3%item_name%";
    @Nonnull
    private String rareItemName = "§8* §5%item_name%";
    @Nonnull
    private String epicItemName = "§8* §d%item_name%";
    @Nonnull
    private String quitMessage = "§4%player% §cleft the game";
    @Nonnull
    private String kickMessage = "§4%player% §cgot kicked due to §4%reason%";
    @Nonnull
    private String joinMessage = "§6%player% §ajoined the game";
    @Nonnull
    private String firstJoinMessage = "§6%player% §ajoined the game §8(§7the first time§8)";
    @Nonnull
    private String playerDirect = "§8(§7You§8) §6%player%";
    @Nonnull
    private String serverName = "unknown";
    private boolean betterTNT = true;
    private boolean betterFallingBlocks = true;
    private boolean debug = true;
    private boolean betterChat = true;
    private boolean betterCommands = true;
    private boolean betterPermissions = true;
    private boolean useCommandLineAsChat = false;
    private boolean autoUpdater = true;
    private boolean punishSpamming = true;
    private boolean customQuitMessage = true;
    private boolean customKickMessage = true;
    private boolean customJoinMessage = true;
    private boolean customFirstJoinMessage = true;
    private boolean customItemName = true;

    protected TNLListener(@Nonnull NMSMain main) {
        this.main = main;
    }

    protected TNLListener() {
        this.main = new NMSMain();
    }

    protected void enable() {
        Logger.info.println(Bukkit.getVersion(), Bukkit.getBukkitVersion());
        /*
        if (!Bukkit.getVersion().contains("1.15.2")) {
            Bukkit.getPluginManager().disablePlugin(getPlugin());
            return;
        }
         */
        Bukkit.getPluginManager().registerEvents(new PacketListener(), getMain());
        Bukkit.getPluginManager().registerEvents(new JoinListener(), getMain());
        Bukkit.getPluginManager().registerEvents(new InteractListener(), getMain());
        Bukkit.getPluginManager().registerEvents(new KickListener(), getMain());
        Bukkit.getPluginManager().registerEvents(new QuitListener(), getMain());
        Bukkit.getPluginManager().registerEvents(new CommandListener(), getMain());
        FileUtil.setServerFolder(getMain().getDataFolder().getAbsoluteFile().getParentFile().getParentFile());
        FileUtil.setLogFolder(FileUtil.getFile("/logs/"));
        FileUtil.setPluginFolder(FileUtil.getFile("/plugins/"));
        ConfigUtil.initConfig();
        new ConfigUtil.ConfigurationSection("debug", isDebug()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("commands.use-better-permissions", isBetterPermissions()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("commands.use-better-commands", isBetterCommands()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("commands.permission-message", getPermissionMessage()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("commands.unknown-command-message", getUnknownCommandMessage()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("commands.use-command-line-as-chat", isUseCommandLineAsChat()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("performance.use-better-tnt", isBetterTNT()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("performance.use-better-falling-blocks", isBetterFallingBlocks()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("chat.use-better-chat", isBetterChat()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("plugin.use-auto-updater", isAutoUpdater()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("punishments.spamming", isPunishSpamming()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("messages.punish-spamming", getKickMessageSpamming()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("chat.use-custom-quit-message", isCustomQuitMessage()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("chat.use-custom-kick-message", isCustomKickMessage()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("chat.use-custom-join-message", isCustomJoinMessage()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("chat.use-custom-first-join-message", isCustomFirstJoinMessage()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("messages.prefix", getPrefix()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("messages.custom-quit", getQuitMessage()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("messages.custom-kick", getKickMessage()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("messages.custom-join", getJoinMessage()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("messages.custom-first-join", getFirstJoinMessage()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("messages.player-direct", getPlayerDirect()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("items.use-custom-name", isCustomItemName()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("items.standard-custom-name", getStandardItemName()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("items.rare-custom-name", getRareItemName()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("items.epic-custom-name", getEpicItemName()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("servers", new ArrayList<>(Arrays.asList("example-server-1", "example-server-2", "example-server-3"))).createIfAbsent();

        new Thread(() -> {
            List<String> servers = ConfigUtil.getConfig().getStringList("servers");
            for (String server : servers) {
                String value = ConfigUtil.getConfig().getString("server." + server);
                if (value == null || value.isEmpty()) {
                    new ConfigUtil.ConfigurationSection("server." + server, "host:port").create();
                } else {
                    if (value.equalsIgnoreCase("host:port")) {
                        Logger.error.println("You have to setup the server '" + server + "' correctly");
                    } else {
                        try {
                            Server s = new Server(server, new InetSocketAddress(value.split(":")[0], Integer.parseInt(value.split(":")[1])));
                            Logger.info.println("Initialized new server '" + s.toString() + "'");
                        } catch (Exception e) {
                            Logger.error.println(e, "Failed to load server '" + server + "'",
                                    "The ip-address format is 'host:port' (example localhost:25565)");
                        }
                    }
                }
            }
        }).start();

        setDebug(ConfigUtil.getConfig().getBoolean("debug", isDebug()));
        setBetterPermissions(ConfigUtil.getConfig().getBoolean("commands.use-better-permissions"));
        setBetterCommands(ConfigUtil.getConfig().getBoolean("commands.use-better-commands"));
        setPermissionMessage(ConfigUtil.getString("commands.permission-message"));
        setUseCommandLineAsChat(ConfigUtil.getConfig().getBoolean("use-command-line-as-chat"));
        setUnknownCommandMessage(ConfigUtil.getString("commands.unknown-command-message"));
        setBetterTNT(ConfigUtil.getConfig().getBoolean("performance.use-better-tnt"));
        setBetterFallingBlocks(ConfigUtil.getConfig().getBoolean("performance.use-better-falling-blocks"));
        setBetterChat(ConfigUtil.getConfig().getBoolean("chat.use-better-chat"));
        setAutoUpdater(ConfigUtil.getConfig().getBoolean("plugin.use-auto-updater"));
        setPunishSpamming(ConfigUtil.getConfig().getBoolean("punishments.spamming"));
        setPrefix(ConfigUtil.getString("messages.prefix"));
        setKickMessageSpamming(ConfigUtil.getString("messages.punish-spamming"));
        setCustomQuitMessage(ConfigUtil.getConfig().getBoolean("chat.use-custom-quit-message"));
        setCustomKickMessage(ConfigUtil.getConfig().getBoolean("chat.use-custom-kick-message"));
        setCustomJoinMessage(ConfigUtil.getConfig().getBoolean("chat.use-custom-join-message"));
        setCustomFirstJoinMessage(ConfigUtil.getConfig().getBoolean("chat.use-custom-first-join-message"));
        setQuitMessage(ConfigUtil.getString("messages.custom-quit"));
        setKickMessage(ConfigUtil.getString("messages.custom-kick"));
        setJoinMessage(ConfigUtil.getString("messages.custom-join"));
        setFirstJoinMessage(ConfigUtil.getString("messages.custom-first-join"));
        setPlayerDirect(ConfigUtil.getString("messages.player-direct"));
        setStandardItemName(ConfigUtil.getString("items.standard-custom-name"));
        setRareItemName(ConfigUtil.getString("items.rare-custom-name"));
        setEpicItemName(ConfigUtil.getString("items.epic-custom-name"));
        setServerName(FileUtil.getServerFolder().getName());

        Bukkit.getMessenger().registerOutgoingPluginChannel(getMain(), "BungeeCord");
        new PluginUpdate(getMain()).downloadUpdate();
    }

    public void updatePlayers() {
        for (Player player : new ArrayList<>(getPlayerHashMap().keySet())) {
            if (!player.isOnline()) {
                getPlayerHashMap().remove(player);
            }
        }
    }

    @Nonnull
    public NMSMain getMain() {
        return main;
    }

    @Nonnull
    public String getPermissionMessage() {
        return permissionMessage;
    }

    public void setPermissionMessage(@Nonnull String permissionMessage) {
        this.permissionMessage = permissionMessage;
    }

    @Nonnull
    public String getUnknownCommandMessage() {
        return unknownCommandMessage;
    }

    public void setUnknownCommandMessage(@Nonnull String unknownCommandMessage) {
        this.unknownCommandMessage = unknownCommandMessage;
    }

    @Nonnull
    public String getKickMessageSpamming() {
        return kickMessageSpamming;
    }

    public void setKickMessageSpamming(@Nonnull String kickMessageSpamming) {
        this.kickMessageSpamming = kickMessageSpamming;
    }

    @Nonnull
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(@Nonnull String prefix) {
        this.prefix = prefix;
    }

    @Nonnull
    public String getStandardItemName() {
        return standardItemName;
    }

    public void setStandardItemName(@Nonnull String standardItemName) {
        this.standardItemName = standardItemName;
    }

    @Nonnull
    public String getRareItemName() {
        return rareItemName;
    }

    public void setRareItemName(@Nonnull String rareItemName) {
        this.rareItemName = rareItemName;
    }

    @Nonnull
    public String getEpicItemName() {
        return epicItemName;
    }

    public void setEpicItemName(@Nonnull String epicItemName) {
        this.epicItemName = epicItemName;
    }

    @Nonnull
    public String getQuitMessage() {
        return quitMessage;
    }

    public void setQuitMessage(@Nonnull String quitMessage) {
        this.quitMessage = quitMessage;
    }

    @Nonnull
    public String getKickMessage() {
        return kickMessage;
    }

    public void setKickMessage(@Nonnull String kickMessage) {
        this.kickMessage = kickMessage;
    }

    @Nonnull
    public String getJoinMessage() {
        return joinMessage;
    }

    public void setJoinMessage(@Nonnull String joinMessage) {
        this.joinMessage = joinMessage;
    }

    @Nonnull
    public String getFirstJoinMessage() {
        return firstJoinMessage;
    }

    public void setFirstJoinMessage(@Nonnull String firstJoinMessage) {
        this.firstJoinMessage = firstJoinMessage;
    }

    @Nonnull
    public String getPlayerDirect() {
        return playerDirect;
    }

    public void setPlayerDirect(@Nonnull String playerDirect) {
        this.playerDirect = playerDirect;
    }

    @Nonnull
    public String getServerName() {
        return serverName;
    }

    public void setServerName(@Nonnull String serverName) {
        this.serverName = serverName;
    }

    public boolean isBetterTNT() {
        return betterTNT;
    }

    public void setBetterTNT(boolean betterTNT) {
        this.betterTNT = betterTNT;
    }

    public boolean isBetterFallingBlocks() {
        return betterFallingBlocks;
    }

    public void setBetterFallingBlocks(boolean betterFallingBlocks) {
        this.betterFallingBlocks = betterFallingBlocks;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isBetterChat() {
        return betterChat;
    }

    public void setBetterChat(boolean betterChat) {
        this.betterChat = betterChat;
    }

    public boolean isBetterCommands() {
        return betterCommands;
    }

    public void setBetterCommands(boolean betterCommands) {
        this.betterCommands = betterCommands;
    }

    public boolean isBetterPermissions() {
        return betterPermissions;
    }

    public void setBetterPermissions(boolean betterPermissions) {
        this.betterPermissions = betterPermissions;
    }

    public boolean isUseCommandLineAsChat() {
        return useCommandLineAsChat;
    }

    public void setUseCommandLineAsChat(boolean useCommandLineAsChat) {
        this.useCommandLineAsChat = useCommandLineAsChat;
    }

    public boolean isAutoUpdater() {
        return autoUpdater;
    }

    public void setAutoUpdater(boolean autoUpdater) {
        this.autoUpdater = autoUpdater;
    }

    public boolean isPunishSpamming() {
        return punishSpamming;
    }

    public void setPunishSpamming(boolean punishSpamming) {
        this.punishSpamming = punishSpamming;
    }

    public boolean isCustomQuitMessage() {
        return customQuitMessage;
    }

    public void setCustomQuitMessage(boolean customQuitMessage) {
        this.customQuitMessage = customQuitMessage;
    }

    public boolean isCustomKickMessage() {
        return customKickMessage;
    }

    public void setCustomKickMessage(boolean customKickMessage) {
        this.customKickMessage = customKickMessage;
    }

    public boolean isCustomJoinMessage() {
        return customJoinMessage;
    }

    public void setCustomJoinMessage(boolean customJoinMessage) {
        this.customJoinMessage = customJoinMessage;
    }

    public boolean isCustomFirstJoinMessage() {
        return customFirstJoinMessage;
    }

    public void setCustomFirstJoinMessage(boolean customFirstJoinMessage) {
        this.customFirstJoinMessage = customFirstJoinMessage;
    }

    public boolean isCustomItemName() {
        return customItemName;
    }

    public void setCustomItemName(boolean customItemName) {
        this.customItemName = customItemName;
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

    @Soon
    public void deleteOldLogs() {
        throw new UnsupportedOperationException("This feature is currently not supported");
    }

    public void runShellCommand(@Nonnull String command) {
        try {
            String string;
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((string = br.readLine()) != null) {
                Logger.debug.println(string);
            }
            process.waitFor();
            Logger.debug.println("Finished program with exit code: " + process.exitValue());
            process.destroy();
        } catch (Exception e) {
            Logger.error.println(e);
        }
    }

    public Location wrap(int x, int y, int z, String world) {
        return wrap(Bukkit.getWorld(world), x, y, z);
    }

    public Location wrap(World world, int x, int y, int z) {
        if (world != null) {
            return new Location(world, MathUtil.toDouble(x), MathUtil.toDouble(y), MathUtil.toDouble(z));
        } else {
            return wrap(x, y, z);
        }
    }

    public Location wrap(int x, int y, int z) {
        return new Location(null, x, y, z);
    }

    public List<String> getWorlds() {
        List<String> worlds = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            worlds.add(world.getName());
        }
        return worlds;
    }
}
