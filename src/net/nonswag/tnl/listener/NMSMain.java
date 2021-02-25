package net.nonswag.tnl.listener;

import net.nonswag.tnl.listener.api.annotations.Soon;
import net.nonswag.tnl.listener.api.annotations.UndefinedNullability;
import net.nonswag.tnl.bridge.receiver.ProxyServer;
import net.nonswag.tnl.listener.api.command.CommandManager;
import net.nonswag.tnl.listener.api.server.Server;
import net.nonswag.tnl.listener.commands.BridgeCommand;
import net.nonswag.tnl.listener.enumerations.InternetProtocolAddress;
import net.nonswag.tnl.listener.listeners.CommandListener;
import net.nonswag.tnl.listener.tabcompleter.BridgeCommandTabCompleter;
import net.nonswag.tnl.listener.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class NMSMain extends JavaPlugin {

    @Nonnull private static String permissionMessage = "§cYou have no Rights §8(§4%permission%§8)";
    @Nonnull private static String unknownCommandMessage = "§cThe Command §8(§4%command%§8)§c doesn't exist";
    @Nonnull private static String kickMessageSpamming = "§cSpamming is prohibited";

    @Nonnull private static String prefix = "§8[§f§lTNL§8]§r";

    @Nonnull private static String standardItemName = "§8* §3%item_name%";
    @Nonnull private static String rareItemName = "§8* §5%item_name%";
    @Nonnull private static String epicItemName = "§8* §d%item_name%";

    @Nonnull private static String quitMessage = "§4%player% §cleft the game";
    @Nonnull private static String kickMessage = "§4%player% §cgot kicked due to §4%reason%";
    @Nonnull private static String joinMessage = "§6%player% §ajoined the game";
    @Nonnull private static String firstJoinMessage = "§6%player% §ajoined the game §8(§7the first time§8)";
    @Nonnull private static String playerDirect = "§8(§7You§8) §6%player%";

    @Nonnull private static String serverName = "unknown";

    @UndefinedNullability private static Plugin plugin;
    @UndefinedNullability private static JavaPlugin javaPlugin;
    @UndefinedNullability private static CommandManager commandManager;

    private static boolean betterTNT = true;
    private static boolean betterFallingBlocks = true;

    private static boolean debug = true;

    private static boolean betterChat = true;
    private static boolean betterCommands = true;
    private static boolean betterPermissions = true;
    private static boolean useCommandLineAsChat = false;

    private static boolean autoUpdater = true;

    private static boolean punishSpamming = true;

    private static boolean customQuitMessage = true;
    private static boolean customKickMessage = true;
    private static boolean customJoinMessage = true;
    private static boolean customFirstJoinMessage = true;

    private static boolean customItemName = true;

    // CloudBridge

    @UndefinedNullability private static ProxyServer proxyServer;
    @UndefinedNullability private static InternetProtocolAddress proxyProtocolAddress = new InternetProtocolAddress("localhost", 25500);
    @UndefinedNullability private static String forwardingSecret = StringUtil.random(16);

    @Override
    public void onEnable() {
        setPlugin(this);
        setJavaPlugin(this);
        setCommandManager(new CommandManager(getJavaPlugin()));
        NMSMain.print(Bukkit.getVersion(), Bukkit.getBukkitVersion());
        /*
        if (!Bukkit.getVersion().contains("1.15.2")) {
            Bukkit.getPluginManager().disablePlugin(getPlugin());
            return;
        }
         */
        FileUtil.setServerFolder(getPlugin().getDataFolder().getAbsoluteFile().getParentFile().getParentFile());
        FileUtil.setLogFolder(FileUtil.getFile("/logs/"));
        FileUtil.setPluginFolder(FileUtil.getFile("/plugins/"));
        ConfigUtil.initConfig();
        new ConfigUtil.ConfigurationSection("debug", isDebug()).createIfAbsent();
        setServerName(FileUtil.getServerFolder().getName());
        setDebug(ConfigUtil.getConfig().getBoolean("debug", isDebug()));
        getCommandManager().registerCommand("bridge", "tnl.admin", new BridgeCommand(), new BridgeCommandTabCompleter());
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
        new ConfigUtil.ConfigurationSection("servers", new ArrayList<>(Arrays.asList("example-server-1", "example-server-2", "example-server-3"))).createIfAbsent();

        new Thread(() -> {
            List<String> servers = ConfigUtil.getConfig().getStringList("servers");
            for (String server : servers) {
                String value = ConfigUtil.getConfig().getString("server." + server);
                if (value == null || value.isEmpty()) {
                    new ConfigUtil.ConfigurationSection("server." + server, "host:port").create();
                } else {
                    if (value.equalsIgnoreCase("host:port")) {
                        NMSMain.stacktrace("You have to setup the server '" + server + "' correctly");
                    } else {
                        try {
                            Server s = new Server(server, new InetSocketAddress(value.split(":")[0], Integer.parseInt(value.split(":")[1])));
                            NMSMain.print("Initialized new server '" + s.toString() + "'");
                        } catch (Throwable t) {
                            NMSMain.stacktrace(t, "Failed to load server '" + server + "'",
                                    "The ip-address format is 'host:port' (example localhost:25565)");
                        }
                    }
                }
            }
        }).start();

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
        setProxyProtocolAddress(new InternetProtocolAddress(Objects.requireNonNull(ConfigUtil.getConfig().getString("cloud.bridge.address"))));
        setForwardingSecret(ConfigUtil.getConfig().getString("cloud.bridge.forwarding-secret"));

        print("§aForwarding secret is §8'§6" + getForwardingSecret() + "§8'");

        Bukkit.getPluginManager().registerEvents(new CommandListener(), getPlugin());

        new PluginUpdate(getPlugin()).downloadUpdate();
        setProxyServer(new ProxyServer(getProxyProtocolAddress()));

        TNLListener.onEnable();
    }

    @Override
    public void onDisable() {
        getProxyServer().disconnect();
    }

    public static void callEvent(@Nonnull Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    public static BukkitTask delayedTask(@Nonnull Runnable runnable, int delay) {
        return Bukkit.getScheduler().runTaskLater(getPlugin(), runnable, delay);
    }

    public static BukkitTask runTask(@Nonnull Runnable runnable) {
        return Bukkit.getScheduler().runTask(getPlugin(), runnable);
    }

    public static BukkitTask delayedTaskAsynchronously(@Nonnull Runnable runnable, int delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(getPlugin(), runnable, delay);
    }

    @Soon
    public static void deleteOldLogs() {
        throw new UnsupportedOperationException("This feature is currently not supported");
    }

    public static BukkitTask runTaskAsynchronously(@Nonnull Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), runnable);
    }

    public static void runShellCommand(@Nonnull String command) {
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

    public static void stacktrace(Throwable throwable, boolean full, String... strings) {
        if (isDebug()) {
            try {
                Bukkit.getLogger().severe("[TNLListener] An error has occurred");
                if (strings != null && strings.length > 0) {
                    for (String string : strings) {
                        Bukkit.getLogger().severe("[TNLListener] " + string);
                    }
                }
                if (throwable != null) {
                    if (throwable.getMessage() != null) {
                        Bukkit.getLogger().severe("[TNLListener] " + throwable.getMessage());
                    }
                    if (throwable.getCause() != null) {
                        Bukkit.getLogger().severe("[TNLListener] " + throwable.getCause().getMessage());
                    }
                    if (full) {
                        Bukkit.getLogger().severe("########## MORE INFORMATION ##########");
                        throwable.printStackTrace();
                        Bukkit.getLogger().severe("########## LESS INFORMATION ##########");
                    }
                }
            } catch (Throwable t) {
                System.err.println("[TNLListener] An error has occurred");
                if (strings != null && strings.length > 0) {
                    for (String string : strings) {
                        System.err.println("[TNLListener] " + string);
                    }
                }
                if (throwable != null) {
                    if (throwable.getMessage() != null) {
                        System.err.println("[TNLListener] " + throwable.getMessage());
                    }
                    if (throwable.getCause() != null) {
                        System.err.println("[TNLListener] " + throwable.getCause().getMessage());
                    }
                    if (full) {
                        System.err.println("########## MORE INFORMATION ##########");
                        throwable.printStackTrace();
                        System.err.println("########## LESS INFORMATION ##########");
                    }
                }
            }
        }
    }

    public static void stacktrace(Throwable throwable, String... strings) {
        stacktrace(throwable, true, strings);
    }

    public static void stacktrace(String... strings) {
        if (isDebug() && strings != null && strings.length > 0) {
            try {
                Bukkit.getLogger().severe("[TNLListener] An error has occurred");
                for (String string : strings) {
                    Bukkit.getLogger().severe("[TNLListener] " + string);
                }
            } catch (Throwable t) {
                System.err.println("[TNLListener] An error has occurred");
                for (String string : strings) {
                    System.err.println("[TNLListener] " + string);
                }
            }
        }
    }

    public static void warn(String... strings) {
        try {
            for (String string : strings) {
                Bukkit.getLogger().warning("[TNLListener] " + string);
            }
        } catch (Throwable t) {
            for (String string : strings) {
                System.out.println("[TNLListener] " + string);
            }
        }
    }

    public static void print(String... strings) {
        try {
            for (String string : strings) {
                Bukkit.getLogger().info("[TNLListener] " + string);
            }
        } catch (Throwable t) {
            for (String string : strings) {
                System.out.println("[TNLListener] " + string);
            }
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

    public static List<String> getWorlds() {
        List<String> worlds = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            worlds.add(world.getName());
        }
        return worlds;
    }

    public static void setPrefix(@Nonnull String prefix) {
        NMSMain.prefix = prefix;
    }

    public static void setKickMessageSpamming(@Nonnull String kickMessageSpamming) {
        NMSMain.kickMessageSpamming = kickMessageSpamming;
    }

    public static void setPermissionMessage(@Nonnull String permissionMessage) {
        NMSMain.permissionMessage = permissionMessage;
    }

    public static void setEpicItemName(@Nonnull String epicItemName) {
        NMSMain.epicItemName = epicItemName;
    }

    public static void setRareItemName(@Nonnull String rareItemName) {
        NMSMain.rareItemName = rareItemName;
    }

    public static void setQuitMessage(@Nonnull String quitMessage) {
        NMSMain.quitMessage = quitMessage;
    }

    public static void setStandardItemName(@Nonnull String standardItemName) {
        NMSMain.standardItemName = standardItemName;
    }

    public static void setKickMessage(@Nonnull String kickMessage) {
        NMSMain.kickMessage = kickMessage;
    }

    public static void setUnknownCommandMessage(@Nonnull String unknownCommandMessage) {
        NMSMain.unknownCommandMessage = unknownCommandMessage;
    }

    public static void setServerName(@Nonnull String serverName) {
        NMSMain.serverName = serverName;
    }

    public static void setJavaPlugin(JavaPlugin javaPlugin) {
        NMSMain.javaPlugin = javaPlugin;
    }

    public static void setPlugin(Plugin plugin) {
        NMSMain.plugin = plugin;
    }

    public static void setCommandManager(CommandManager commandManager) {
        NMSMain.commandManager = commandManager;
    }

    public static void setAutoUpdater(boolean autoUpdater) {
        NMSMain.autoUpdater = autoUpdater;
    }

    public static void setBetterChat(boolean betterChat) {
        NMSMain.betterChat = betterChat;
    }

    public static void setBetterCommands(boolean betterCommands) {
        NMSMain.betterCommands = betterCommands;
    }

    public static void setBetterFallingBlocks(boolean betterFallingBlocks) {
        NMSMain.betterFallingBlocks = betterFallingBlocks;
    }

    public static void setBetterPermissions(boolean betterPermissions) {
        NMSMain.betterPermissions = betterPermissions;
    }

    public static void setBetterTNT(boolean betterTNT) {
        NMSMain.betterTNT = betterTNT;
    }

    public static void setCustomFirstJoinMessage(boolean customFirstJoinMessage) {
        NMSMain.customFirstJoinMessage = customFirstJoinMessage;
    }

    public static void setCustomItemName(boolean customItemName) {
        NMSMain.customItemName = customItemName;
    }

    public static void setCustomJoinMessage(boolean customJoinMessage) {
        NMSMain.customJoinMessage = customJoinMessage;
    }

    public static void setCustomKickMessage(boolean customKickMessage) {
        NMSMain.customKickMessage = customKickMessage;
    }

    public static void setCustomQuitMessage(boolean customQuitMessage) {
        NMSMain.customQuitMessage = customQuitMessage;
    }

    public static void setDebug(boolean debug) {
        NMSMain.debug = debug;
    }

    public static void setFirstJoinMessage(@Nonnull String firstJoinMessage) {
        NMSMain.firstJoinMessage = firstJoinMessage;
    }

    public static void setForwardingSecret(String forwardingSecret) {
        NMSMain.forwardingSecret = forwardingSecret;
    }

    public static void setJoinMessage(@Nonnull String joinMessage) {
        NMSMain.joinMessage = joinMessage;
    }

    public static void setPlayerDirect(@Nonnull String playerDirect) {
        NMSMain.playerDirect = playerDirect;
    }

    public static void setProxyProtocolAddress(InternetProtocolAddress proxyProtocolAddress) {
        NMSMain.proxyProtocolAddress = proxyProtocolAddress;
    }

    public static void setProxyServer(ProxyServer proxyServer) {
        NMSMain.proxyServer = proxyServer;
    }

    public static void setPunishSpamming(boolean punishSpamming) {
        NMSMain.punishSpamming = punishSpamming;
    }

    public static void setUseCommandLineAsChat(boolean useCommandLineAsChat) {
        NMSMain.useCommandLineAsChat = useCommandLineAsChat;
    }

    @Nonnull
    public static String getUnknownCommandMessage() {
        return unknownCommandMessage;
    }

    @Nonnull
    public static String getKickMessageSpamming() {
        return kickMessageSpamming;
    }

    @Nonnull
    public static String getQuitMessage() {
        return quitMessage;
    }

    @Nonnull
    public static String getPrefix() {
        return prefix;
    }

    @Nonnull
    public static String getEpicItemName() {
        return epicItemName;
    }

    @Nonnull
    public static String getKickMessage() {
        return kickMessage;
    }

    @Nonnull
    public static String getPermissionMessage() {
        return permissionMessage;
    }

    @Nonnull
    public static String getJoinMessage() {
        return joinMessage;
    }

    @Nonnull
    public static String getRareItemName() {
        return rareItemName;
    }

    @Nonnull
    public static String getStandardItemName() {
        return standardItemName;
    }

    @Nonnull
    public static String getServerName() {
        return serverName;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static JavaPlugin getJavaPlugin() {
        return javaPlugin;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static InternetProtocolAddress getProxyProtocolAddress() {
        return proxyProtocolAddress;
    }

    public static ProxyServer getProxyServer() {
        return proxyServer;
    }

    @Nonnull
    public static String getFirstJoinMessage() {
        return firstJoinMessage;
    }

    public static String getForwardingSecret() {
        return forwardingSecret;
    }

    @Nonnull
    public static String getPlayerDirect() {
        return playerDirect;
    }

    public static boolean isAutoUpdater() {
        return autoUpdater;
    }

    public static boolean isBetterChat() {
        return betterChat;
    }

    public static boolean isBetterCommands() {
        return betterCommands;
    }

    public static boolean isBetterFallingBlocks() {
        return betterFallingBlocks;
    }

    public static boolean isBetterPermissions() {
        return betterPermissions;
    }

    public static boolean isBetterTNT() {
        return betterTNT;
    }

    public static boolean isCustomFirstJoinMessage() {
        return customFirstJoinMessage;
    }

    public static boolean isCustomItemName() {
        return customItemName;
    }

    public static boolean isCustomJoinMessage() {
        return customJoinMessage;
    }

    public static boolean isCustomKickMessage() {
        return customKickMessage;
    }

    public static boolean isCustomQuitMessage() {
        return customQuitMessage;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static boolean isPunishSpamming() {
        return punishSpamming;
    }

    public static boolean isUseCommandLineAsChat() {
        return useCommandLineAsChat;
    }
}
