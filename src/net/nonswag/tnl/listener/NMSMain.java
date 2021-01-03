package net.nonswag.tnl.listener;

import net.nonswag.tnl.listener.api.bridgeAPI.ProxyServer;
import net.nonswag.tnl.listener.api.serverAPI.Server;
import net.nonswag.tnl.listener.commands.BridgeCommand;
import net.nonswag.tnl.listener.enumerations.InternetProtocolAddress;
import net.nonswag.tnl.listener.listeners.CommandListener;
import net.nonswag.tnl.listener.tabCompleter.BridgeCommandTabCompleter;
import net.nonswag.tnl.listener.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NMSMain extends JavaPlugin {

    private static String permissionMessage = "§cYou have no Rights";
    private static String unknownCommandMessage = "§cCommand not found";
    private static String kickMessageSpamming = "§cSpamming is prohibited";

    private static Plugin plugin;
    private static JavaPlugin javaPlugin;

    private static boolean betterTNT = true;
    private static boolean betterFallingBlocks = true;

    private static boolean debug = true;

    private static boolean betterChat = true;
    private static boolean betterCommands = true;
    private static boolean betterPermissions = true;
    private static boolean useCommandLineAsChat = false;

    private static boolean on1_8 = false;
    private static boolean on1_14 = false;
    private static boolean on1_15 = false;
    private static boolean on1_16 = false;

    private static boolean autoUpdater = true;

    private static boolean punishSpamming = true;

    private static boolean customQuitMessage = true;
    private static boolean customKickMessage = true;
    private static boolean customJoinMessage = true;
    private static boolean customFirstJoinMessage = true;

    private static boolean customItemName = true;

    private static String prefix = "§8[§f§lTNL§8]§r";

    private static String standardItemName = "§8* §3%item_name%";
    private static String rareItemName = "§8* §5%item_name%";
    private static String epicItemName = "§8* §d%item_name%";

    private static String quitMessage = "§4%player% §cleft the game";
    private static String kickMessage = "§4%player% §cgot kicked due to §4%reason%";
    private static String joinMessage = "§6%player% §ajoined the game";
    private static String firstJoinMessage = "§6%player% §ajoined the game §8(§7the first time§8)";
    private static String playerDirect = "You";

    private static String serverName = "unknown";

    // CloudBridge

    private static ProxyServer proxyServer;
    private static InternetProtocolAddress proxyProtocolAddress = new InternetProtocolAddress("localhost", 25500);
    private static String forwardingSecret = StringUtil.random(16);

    @Override
    public void onEnable() {
        super.onEnable();
        setPlugin(this);
        setJavaPlugin(this);
        FileUtil.setServerFolder(getPlugin().getDataFolder().getAbsoluteFile().getParentFile().getParentFile());
        FileUtil.setLogFolder(FileUtil.getFile("/logs/"));
        FileUtil.setPluginFolder(FileUtil.getFile("/plugins/"));
        ConfigUtil.initConfig();
        new ConfigUtil.ConfigurationSection("debug", isDebug()).createIfAbsent();
        setServerName(FileUtil.getServerFolder().getName());
        setDebug(ConfigUtil.getConfig().getBoolean("debug", isDebug()));
        if (Bukkit.getVersion().contains("1.8")) {
            on1_8 = true;
            net.nonswag.tnl.listener.v1_8_R3.TNLListener.onEnable();
        } else if (Bukkit.getVersion().contains("1.14")) {
            on1_14 = true;
            net.nonswag.tnl.listener.v1_14_R1.TNLListener.onEnable();
        } else if (Bukkit.getVersion().contains("1.15")) {
            on1_15 = true;
            net.nonswag.tnl.listener.v1_15_R1.TNLListener.onEnable();
        } else if (Bukkit.getVersion().contains("1.16")) {
            on1_16 = true;
            net.nonswag.tnl.listener.v1_16_R1.TNLListener.onEnable();
        } else {
            NMSMain.stacktrace("Your NMS and CraftBukkit version is not supported",
                    "This plugin was developed for Spigot v1.8, v1.14, v1.15 and v1.16");
            Bukkit.getPluginManager().disablePlugin(getPlugin());
        }

        PluginCommand bridgeCommand = getCommand("bridge");
        if (bridgeCommand != null) {
            bridgeCommand.setExecutor(new BridgeCommand());
            bridgeCommand.setTabCompleter(new BridgeCommandTabCompleter());
            bridgeCommand.setPermission("tnl.admin");
            bridgeCommand.setPermissionMessage("§8[§f§lTNL§8] §cYou have no Rights §8(§4tnl.admin§8)");
        }

        Bukkit.getMessenger().registerOutgoingPluginChannel(getPlugin(), "BungeeCord");

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
        new ConfigUtil.ConfigurationSection("cloud.bridge.address", getProxyProtocolAddress().getAsString()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("cloud.bridge.forwarding-secret", getForwardingSecret()).createIfAbsent();
        new ConfigUtil.ConfigurationSection("servers", "example-server-1, example-server-2, example-server-3").createIfAbsent();

        List<String> servers = ConfigUtil.getConfig().getStringList("servers");
        for (String server : servers) {
            String value = ConfigUtil.getConfig().getString(server);
            if (value == null || value.isEmpty()) {
                new ConfigUtil.ConfigurationSection(server, "host:port").create();
            } else {
                if (value.equalsIgnoreCase("host:port")) {
                    NMSMain.stacktrace("You have to setup the server '" + server + "' correctly");
                } else {
                    try {
                        new Server(server, new InetSocketAddress(value.split(":")[0], Integer.parseInt(value.split(":")[1])));
                    } catch (Throwable t) {
                        NMSMain.stacktrace(t, "Failed to load server '" + server + "'",
                                "The ip-address format is 'host:port' (example localhost:25565)");
                    }
                }
            }
        }

        setBetterPermissions(ConfigUtil.getConfig().getBoolean("commands.use-better-permissions"));
        setBetterCommands(ConfigUtil.getConfig().getBoolean("commands.use-better-commands"));
        setPermissionMessage(ConfigUtil.getConfig().getString("commands.permission-message"));
        setUseCommandLineAsChat(ConfigUtil.getConfig().getBoolean("use-command-line-as-chat"));
        setUnknownCommandMessage(ConfigUtil.getConfig().getString("commands.unknown-command-message"));
        setBetterTNT(ConfigUtil.getConfig().getBoolean("performance.use-better-tnt"));
        setBetterFallingBlocks(ConfigUtil.getConfig().getBoolean("performance.use-better-falling-blocks"));
        setBetterChat(ConfigUtil.getConfig().getBoolean("chat.use-better-chat"));
        setAutoUpdater(ConfigUtil.getConfig().getBoolean("plugin.use-auto-updater"));
        setPunishSpamming(ConfigUtil.getConfig().getBoolean("punishments.spamming"));
        setPrefix(ConfigUtil.getConfig().getString("messages.prefix"));
        setKickMessageSpamming(ConfigUtil.getConfig().getString("messages.punish-spamming"));
        setCustomQuitMessage(ConfigUtil.getConfig().getBoolean("chat.use-custom-quit-message"));
        setCustomKickMessage(ConfigUtil.getConfig().getBoolean("chat.use-custom-kick-message"));
        setCustomJoinMessage(ConfigUtil.getConfig().getBoolean("chat.use-custom-join-message"));
        setCustomFirstJoinMessage(ConfigUtil.getConfig().getBoolean("chat.use-custom-first-join-message"));
        setQuitMessage(ConfigUtil.getConfig().getString("messages.custom-quit"));
        setKickMessage(ConfigUtil.getConfig().getString("messages.custom-kick"));
        setJoinMessage(ConfigUtil.getConfig().getString("messages.custom-join"));
        setFirstJoinMessage(ConfigUtil.getConfig().getString("messages.custom-first-join"));
        setPlayerDirect(ConfigUtil.getConfig().getString("messages.player-direct"));
        setStandardItemName(ConfigUtil.getConfig().getString("items.standard-custom-name"));
        setRareItemName(ConfigUtil.getConfig().getString("items.rare-custom-name"));
        setEpicItemName(ConfigUtil.getConfig().getString("items.epic-custom-name"));
        setProxyProtocolAddress(new InternetProtocolAddress(Objects.requireNonNull(ConfigUtil.getConfig().getString("cloud.bridge.address"))));
        setForwardingSecret(ConfigUtil.getConfig().getString("cloud.bridge.forwarding-secret"));

        registerEvents(new CommandListener(), getPlugin());
        try {
            new PluginUpdate(getPlugin()).downloadUpdate();
            setProxyServer(new ProxyServer(getProxyProtocolAddress()));
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public static JavaPlugin getJavaPlugin() {
        return javaPlugin;
    }

    public static void setJavaPlugin(JavaPlugin javaPlugin) {
        NMSMain.javaPlugin = javaPlugin;
    }

    public static void setPlayerDirect(String playerDirect) {
        NMSMain.playerDirect = playerDirect;
    }

    public static String getPlayerDirect() {
        return playerDirect == null ? "You" : playerDirect;
    }

    public static boolean isBetterChat() {
        return betterChat;
    }

    public static void setBetterChat(boolean betterChat) {
        NMSMain.betterChat = betterChat;
    }

    public static boolean isBetterCommands() {
        return betterCommands;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setBetterCommands(boolean betterCommands) {
        NMSMain.betterCommands = betterCommands;
    }

    public static boolean isBetterPermissions() {
        return betterPermissions;
    }

    public static void setDebug(boolean debug) {
        NMSMain.debug = debug;
    }

    public static void setCustomQuitMessage(boolean customQuitMessage) {
        NMSMain.customQuitMessage = customQuitMessage;
    }

    public static InternetProtocolAddress getProxyProtocolAddress() {
        return proxyProtocolAddress;
    }

    public static void setProxyProtocolAddress(InternetProtocolAddress proxyProtocolAddress) {
        NMSMain.proxyProtocolAddress = proxyProtocolAddress;
    }

    @Nullable
    public static PluginCommand getCommand(String name, JavaPlugin plugin) {
        try {
            return plugin.getCommand(name);
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        return null;
    }

    public static void registerEvents(Listener listener, Plugin plugin) {
        try {
            plugin = plugin == null ? getPlugin() : plugin;
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public static void setCustomKickMessage(boolean customKickMessage) {
        NMSMain.customKickMessage = customKickMessage;
    }

    public static void setCustomJoinMessage(boolean customJoinMessage) {
        NMSMain.customJoinMessage = customJoinMessage;
    }

    public static void setFirstJoinMessage(String firstJoinMessage) {
        NMSMain.firstJoinMessage = firstJoinMessage;
    }

    public static void setCustomFirstJoinMessage(boolean customFirstJoinMessage) {
        NMSMain.customFirstJoinMessage = customFirstJoinMessage;
    }

    public static String getFirstJoinMessage() {
        return firstJoinMessage == null ? "" : firstJoinMessage;
    }

    public static boolean isCustomItemName() {
        return customItemName;
    }

    public static String getEpicItemName() {
        return epicItemName;
    }

    public static void setStandardItemName(String standardItemName) {
        NMSMain.standardItemName = standardItemName;
    }

    public static void setRareItemName(String rareItemName) {
        NMSMain.rareItemName = rareItemName;
    }

    public static void setEpicItemName(String epicItemName) {
        NMSMain.epicItemName = epicItemName;
    }

    public static void setCustomItemName(boolean customItemName) {
        NMSMain.customItemName = customItemName;
    }

    public static String getStandardItemName() {
        return standardItemName;
    }

    public static String getRareItemName() {
        return rareItemName;
    }

    public static boolean isCustomFirstJoinMessage() {
        return customFirstJoinMessage;
    }

    public static boolean isCustomQuitMessage() {
        return customQuitMessage;
    }

    public static boolean isCustomKickMessage() {
        return customKickMessage;
    }

    public static boolean isCustomJoinMessage() {
        return customJoinMessage;
    }

    public static void setQuitMessage(String quitMessage) {
        NMSMain.quitMessage = quitMessage;
    }

    public static void setKickMessage(String kickMessage) {
        NMSMain.kickMessage = kickMessage;
    }

    public static void setJoinMessage(String joinMessage) {
        NMSMain.joinMessage = joinMessage;
    }

    public static String getQuitMessage() {
        return quitMessage == null ? "" : quitMessage;
    }

    public static String getKickMessage() {
        return kickMessage == null ? "" : kickMessage;
    }

    public static String getJoinMessage() {
        return joinMessage == null ? "" : joinMessage;
    }

    public static void setBetterPermissions(boolean betterPermissions) {
        NMSMain.betterPermissions = betterPermissions;
    }

    public static void setKickMessageSpamming(String kickMessageSpamming) {
        NMSMain.kickMessageSpamming = kickMessageSpamming;
    }

    public static String getServerName() {
        return serverName;
    }

    public static void setServerName(String serverName) {
        NMSMain.serverName = serverName;
    }

    public static String getKickMessageSpamming() {
        return kickMessageSpamming == null ? "" : kickMessageSpamming;
    }

    public static boolean isOn1_8() {
        return on1_8;
    }

    public static boolean isOn1_14() {
        return on1_14;
    }

    public static boolean isOn1_15() {
        return on1_15;
    }

    public static boolean isOn1_16() {
        return on1_16;
    }

    public static boolean isBetterTNT() {
        return betterTNT;
    }

    public static void setAutoUpdater(boolean autoUpdater) {
        NMSMain.autoUpdater = autoUpdater;
    }

    public static boolean isAutoUpdater() {
        return autoUpdater;
    }

    public static void setBetterTNT(boolean betterTNT) {
        if (isOn1_8()) {
            NMSMain.stacktrace("BetterTNT doesn't work on v1.8");
        }
        NMSMain.betterTNT = betterTNT;
    }

    public static void setPunishSpamming(boolean punishSpamming) {
        NMSMain.punishSpamming = punishSpamming;
    }

    public static boolean isPunishSpamming() {
        return punishSpamming;
    }

    public static boolean isBetterFallingBlocks() {
        if (isOn1_8()) {
            NMSMain.stacktrace("BetterFallingBlocks doesn't work on v1.8");
        }
        return betterFallingBlocks;
    }

    public static void setBetterFallingBlocks(boolean betterFallingBlocks) {
        NMSMain.betterFallingBlocks = betterFallingBlocks;
    }

    public static String getUnknownCommandMessage() {
        return unknownCommandMessage == null ? "" : unknownCommandMessage;
    }

    public static void setUnknownCommandMessage(String unknownCommandMessage) {
        NMSMain.unknownCommandMessage = unknownCommandMessage;
    }

    public static boolean isUseCommandLineAsChat() {
        return useCommandLineAsChat;
    }

    public static void setUseCommandLineAsChat(boolean useCommandLineAsChat) {
        NMSMain.useCommandLineAsChat = useCommandLineAsChat;
    }

    public static String getPermissionMessage() {
        return permissionMessage == null ? "" : permissionMessage;
    }

    public static void setPermissionMessage(String permissionMessage) {
        NMSMain.permissionMessage = permissionMessage;
    }

    public static Plugin getPlugin() {
        if (plugin == null || !plugin.isEnabled()) {
            setPlugin(JavaPlugin.getPlugin(NMSMain.class));
        }
        return plugin;
    }

    public static void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    @Override
    public void onDisable() {
        setOn1_8(false);
        setOn1_14(false);
        setOn1_15(false);
        setOn1_16(false);
        getProxyServer().disconnect();
    }

    public static void setPlugin(Plugin plugin) {
        NMSMain.plugin = plugin;
    }

    public static void setOn1_16(boolean on1_16) {
        NMSMain.on1_16 = on1_16;
    }

    public static void setOn1_15(boolean on1_15) {
        NMSMain.on1_15 = on1_15;
    }

    public static void setOn1_14(boolean on1_14) {
        NMSMain.on1_14 = on1_14;
    }

    public static void setOn1_8(boolean on1_8) {
        NMSMain.on1_8 = on1_8;
    }

    public static BukkitTask delayedTask(Runnable runnable, int delay) {
        return Bukkit.getScheduler().runTaskLater(getPlugin(), runnable, delay);
    }

    public static BukkitTask runTask(Runnable runnable) {
        return Bukkit.getScheduler().runTask(getPlugin(), runnable);
    }

    public static BukkitTask delayedTaskAsynchronously(Runnable runnable, int delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(getPlugin(), runnable, delay);
    }

    public static void deleteOldLogs() {
        throw new UnsupportedOperationException("This feature is currently not supported");
    }

    public static BukkitTask runTaskAsynchronously(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), runnable);
    }

    public static void runShellCommand(String command) {
        try {
            String string;
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((string = br.readLine()) != null) {
                NMSMain.print(string);
            }
            process.waitFor();
            NMSMain.print("Finished program with exit code: " + process.exitValue());
            process.destroy();
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    private static String getVersion() {
        if (isOn1_8()) {
            return " v1.8";
        } else if (isOn1_15()) {
            return " v1.15";
        } else {
            return "";
        }
    }

    public static void stacktrace(Throwable throwable, String... strings) {
        if (isDebug()) {
            Bukkit.getLogger().severe("[TNLListener" + getVersion() + "] An error has occurred");
            if (strings != null && strings.length > 0) {
                for (String string : strings) {
                    Bukkit.getLogger().severe("[TNLListener" + getVersion() + "] " + string);
                }
            }
            if (throwable != null) {
                if (throwable.getMessage() != null) {
                    Bukkit.getLogger().severe("[TNLListener" + getVersion() + "] " + throwable.getMessage());
                }
                if (throwable.getCause() != null) {
                    Bukkit.getLogger().severe("[TNLListener" + getVersion() + "] " + throwable.getCause().getMessage());
                }
                Bukkit.getLogger().severe("########## MORE INFORMATION ##########");
                throwable.printStackTrace();
                Bukkit.getLogger().severe("########## LESS INFORMATION ##########");
            }
        }
    }

    public static void stacktrace(String... strings) {
        if (isDebug() && strings != null && strings.length > 0) {
            Bukkit.getLogger().severe("[TNLListener" + getVersion() + "] An error has occurred");
            for (String string : strings) {
                Bukkit.getLogger().severe("[TNLListener" + getVersion() + "] " + string);
            }
        }
    }

    public static void warn(String... strings) {
        for (String string : strings) {
            Bukkit.getLogger().warning("[TNLListener" + getVersion() + "] " + string);
        }
    }

    public static void print(String... strings) {
        for (String string : strings) {
            Bukkit.getLogger().info("[TNLListener" + getVersion() + "] " + string);
        }
    }

    public static Location wrap(int x, int y, int z, String world) {
        return wrap(Bukkit.getWorld(world), x, y, z);
    }

    public static Location wrap(World world, int x, int y, int z) {
        if (world != null) {
            return new Location(world, MathUtil.toDouble(x), MathUtil.toDouble(y), MathUtil.toDouble(z));
        } else {
            return wrap(x, y, z);
        }
    }

    public static Location wrap(int x, int y, int z) {
        return new Location(null, x, y, z);
    }

    public static ProxyServer getProxyServer() {
        return proxyServer;
    }

    public static void setProxyServer(ProxyServer proxyServer) {
        NMSMain.proxyServer = proxyServer;
    }

    public static String getForwardingSecret() {
        return forwardingSecret;
    }

    public static void setForwardingSecret(String forwardingSecret) {
        NMSMain.forwardingSecret = forwardingSecret;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void setPrefix(String prefix) {
        NMSMain.prefix = prefix;
    }

    public static List<String> getWorlds() {
        List<String> worlds = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            worlds.add(world.getName());
        }
        return worlds;
    }
}
