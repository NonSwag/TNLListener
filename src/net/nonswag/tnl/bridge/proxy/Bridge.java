package net.nonswag.tnl.bridge.proxy;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.proxy.VelocityServer;
import net.nonswag.tnl.api.event.EventManager;
import net.nonswag.tnl.bridge.proxy.listeners.JoinListener;
import net.nonswag.tnl.bridge.proxy.listeners.MessageDecodeListener;
import net.nonswag.tnl.bridge.proxy.listeners.PacketListener;
import net.nonswag.tnl.listener.api.file.JsonConfig;
import net.nonswag.tnl.cloud.api.system.Console;
import net.nonswag.tnl.cloud.utils.MathUtil;
import net.nonswag.tnl.listener.utils.StringUtil;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Plugin(authors = "NonSwag", id = "tnllistener", name = "TNLListener", version = "2.2", url = "https://www.thenextlvl.net/")
public class Bridge {

    private static ProxyServer server;
    private static VelocityServer velocityServer;
    private static Logger logger;

    private static JsonConfig config;

    private static int port;
    private static String forwardingSecret = StringUtil.random(16);

    private static String prefix = "§8[§f§lTNL§8]§r";

    @Nonnull private static final List<ConnectedServer> connectedServer = new ArrayList<>();

    @Inject
    public Bridge(ProxyServer server, Logger logger) {
        try {
            setServer(server);
            setVelocityServer(((VelocityServer) server));
            setLogger(logger);

            EventManager.registerEvent(new MessageDecodeListener());
            EventManager.registerEvent(new PacketListener());

            setConfig(new JsonConfig(new File("/home/Minecraft/Velocity/plugins/TNLListener/config.json")));
            try {
                Integer port = getConfig().getInteger("port");
                if (port == null) {
                    port = MathUtil.randomInteger(20000, 30000);
                    getConfig().set("port", port);
                }
                setPort(port);
            } catch (Throwable t) {
                stacktrace(t);
            }
            try {
                String forwardingSecret = getConfig().getString("forwarding-secret");
                if (forwardingSecret == null) {
                    getConfig().set("forwarding-secret", Bridge.forwardingSecret);
                } else {
                    setForwardingSecret(forwardingSecret);
                }
                print("§aForwarding secret is §8'§6" + getForwardingSecret() + "§8'");
                ConnectionHandler.start();
            } catch (Throwable t) {
                stacktrace(t);
            }
        } catch (Throwable t) {
            stacktrace(t);
        }
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        getServer().getEventManager().register(this, new JoinListener());
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        ConnectionHandler.stop();
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        Bridge.port = port;
    }

    public static String getForwardingSecret() {
        return forwardingSecret;
    }

    public static void setForwardingSecret(String forwardingSecret) {
        Bridge.forwardingSecret = forwardingSecret;
    }

    public static void setConfig(JsonConfig config) {
        Bridge.config = config;
    }

    public static JsonConfig getConfig() {
        return config;
    }

    public static void setServer(ProxyServer server) {
        Bridge.server = server;
    }

    public static void setVelocityServer(VelocityServer velocityServer) {
        Bridge.velocityServer = velocityServer;
    }

    public static void setLogger(Logger logger) {
        Bridge.logger = logger;
    }

    public static ProxyServer getServer() {
        return server;
    }

    public static VelocityServer getVelocityServer() {
        return velocityServer;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void setPrefix(String prefix) {
        Bridge.prefix = prefix;
    }

    public static void stacktrace(Throwable throwable, String... strings) {
        Console.stacktrace("§8[§fTNLListener§8-§fBridge§8] §cAn error has occurred");
        if (strings != null && strings.length > 0) {
            for (String string : strings) {
                Console.stacktrace("§8[§fTNLListener§8-§fBridge§8] §c" + string);
            }
        }
        if (throwable != null) {
            if (throwable.getMessage() != null) {
                Console.stacktrace("§8[§fTNLListener§8-§fBridge§8] §c" + throwable.getMessage());
            }
            if (throwable.getCause() != null) {
                Console.stacktrace("§8[§fTNLListener§8-§fBridge§8] §c" + throwable.getCause().getMessage());
            }
            Console.stacktrace("§8########## §7MORE INFORMATION §8##########");
            throwable.printStackTrace();
            Console.stacktrace("§8########## §7LESS INFORMATION §8##########");
        }
    }

    public static void stacktrace(String... strings) {
        if (strings != null && strings.length > 0) {
            for (String string : strings) {
                Console.stacktrace("§8[§fTNLListener§8-§fBridge§8] §c" + string);
            }
        }
    }

    public static void warn(String... strings) {
        for (String string : strings) {
            getLogger().warn("§8[§fTNLListener§8-§fBridge§8] §e" + string);
        }
    }

    public static void print(String... strings) {
        for (String string : strings) {
            Console.print("§8[§fTNLListener§8-§fBridge§8] §a" + string);
        }
    }

    @Nonnull
    public static List<ConnectedServer> getConnectedServer() {
        return connectedServer;
    }

    @Nullable
    public static ConnectedServer getConnectedServer(@Nonnull RegisteredServer server) {
        for (ConnectedServer connectedServer : getConnectedServer()) {
            if (server.equals(connectedServer.getServer())) {
                return connectedServer;
            }
        }
        return null;
    }

    @Nullable
    public static ConnectedServer getConnectedServer(@Nonnull Socket server) {
        for (ConnectedServer connectedServer : getConnectedServer()) {
            if (server.getInetAddress().equals(connectedServer.getServer().getServerInfo().getAddress().getAddress())) {
                return connectedServer;
            }
        }
        return null;
    }
}
