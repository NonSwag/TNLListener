package net.nonswag.tnl.bridge;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.proxy.VelocityServer;
import net.nonswag.tnl.bridge.handler.ConnectedServer;
import net.nonswag.tnl.bridge.handler.ConnectionHandler;
import net.nonswag.tnl.cloud.api.fileAPI.Configuration;
import net.nonswag.tnl.cloud.api.systemAPI.Console;
import net.nonswag.tnl.cloud.utils.MathUtil;
import net.nonswag.tnl.listener.utils.StringUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*******************************************************
 * Copyright (C) 2019-2023 NonSwag kirschnerdavid2466@gmail.com
 *
 * This file is part of TNLListener and was created at the 10/31/20
 *
 * TNLListener can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/

@Plugin(authors = "NonSwag", id = "tnllistener", name = "TNLListener", version = "1.0", url = "http://www.thenextlvl.net")
public class Bridge {

    private static ProxyServer server;
    private static VelocityServer velocityServer;
    private static Logger logger;

    private static Configuration config;

    private static int port;
    private static String forwardingSecret = StringUtil.random(7);

    private static final List<ConnectedServer> connectedServer = new ArrayList<>();

    @Inject
    public Bridge(ProxyServer server, Logger logger) {
        setServer(server);
        setVelocityServer(((VelocityServer) server));
        setLogger(logger);
        File configFile = new File("/home/Minecraft/Velocity/plugins/TNLListener/");
        if (!configFile.exists()) {
            if (!configFile.mkdir()) {
                stacktrace("Failed to create the plugin specified plugin folder");
            }
        }
        if (configFile.exists()) {
            configFile = new File("/home/Minecraft/Velocity/plugins/TNLListener/config.tnl");
            if (!configFile.exists()) {
                try {
                    if (!configFile.createNewFile()) {
                        stacktrace("Failed to create config file");
                    }
                } catch (Throwable ignored) {
                    stacktrace("Failed to create the plugin config");
                }
            }
        }
        if (configFile.exists()) {
            setConfig(new Configuration(configFile));
            try {
                Integer port = getConfig().getInteger("port");
                if (port == null) {
                    port = MathUtil.randomInteger(20000, 30000);
                    getConfig().setValue("port", port);
                }
                setPort(port);
                String forwardingSecret = getConfig().getString("forwarding-secret");
                if (forwardingSecret == null) {
                    forwardingSecret = StringUtil.random(16);
                    getConfig().setValue("forwarding-secret", forwardingSecret);
                }
                setForwardingSecret(forwardingSecret);
                ConnectionHandler.start();
            } catch (Throwable t) {
                stacktrace(t);
            }
        } else {
            stacktrace("Failed to load the config (canceling plugin start)");
        }
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

    public static void setConfig(Configuration config) {
        Bridge.config = config;
    }

    public static Configuration getConfig() {
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
            Console.stacktrace("§8[§fTNLListener§8-§fBridge§8] §cAn error has occurred");
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

    public static List<ConnectedServer> getConnectedServer() {
        return connectedServer;
    }
}
