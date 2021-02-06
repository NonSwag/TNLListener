package net.nonswag.tnl.bridge.proxy;

import net.nonswag.tnl.api.event.EventManager;
import net.nonswag.tnl.bridge.events.BridgeShutdownEvent;
import net.nonswag.tnl.bridge.events.BridgeStartEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.ServerSocket;

public class ConnectionHandler {

    @Nullable private static ServerSocket serverSocket = null;

    public static void start() {
        try {
            setServerSocket(new ServerSocket(Bridge.getPort()));
            assert getServerSocket() != null;
            BridgeStartEvent event = new BridgeStartEvent(getServerSocket());
            EventManager.callEvent(event);
            if (!event.isCancelled()) {
                Bridge.print("Starting bridge...", "Listening on §8'§6localhost:" + Bridge.getPort() + "§8'");
                new PacketAdapter();
            }
        } catch (Throwable t) {
            Bridge.stacktrace(t, "Failed to start bridge on socket §8'§6localhost:" + Bridge.getPort() + "§8'");
        }
    }

    public static void stop() {
        assert getServerSocket() != null;
        BridgeShutdownEvent event = new BridgeShutdownEvent(getServerSocket());
        EventManager.callEvent(event);
        if (!event.isCancelled()) {
            Bridge.print("Stopping bridge...");
            try {
                getServerSocket().close();
                Bridge.print("Unregistered listener from §8'§6localhost:" + Bridge.getPort() + "§8'");
            } catch (Throwable t) {
                Bridge.stacktrace(t, "Failed to stop bridge on socket §8'§6localhost:" + Bridge.getPort() + "§8'");
            }
        }
    }

    public static void setServerSocket(@Nonnull ServerSocket serverSocket) {
        ConnectionHandler.serverSocket = serverSocket;
    }

    @Nullable
    public static ServerSocket getServerSocket() {
        return serverSocket;
    }
}
