package net.nonswag.tnl.bridge.handler;

import net.nonswag.tnl.bridge.Bridge;

import java.net.ServerSocket;

/*******************************************************
 * Copyright (C) 2019-2023 NonSwag kirschnerdavid2466@gmail.com
 *
 * This file is part of TNLListener and was created at the 10/31/20
 *
 * TNLListener can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/

public class ConnectionHandler {

    private static ServerSocket serverSocket = null;

    public static void start() {
        Bridge.print("Starting bridge...");
        try {
            setServerSocket(new ServerSocket(Bridge.getPort()));
            Bridge.print("Listening on §8'§6localhost:" + Bridge.getPort() + "§8'");
            new PacketAdapter();
        } catch (Throwable t) {
            Bridge.stacktrace(t, "Failed to start bridge on socket §8'§6localhost:" + Bridge.getPort() + "§8'");
        }
    }

    public static void stop() {
        if (getServerSocket() != null) {
            Bridge.print("Stopping bridge...");
            try {
                getServerSocket().close();
                Bridge.print("Unregistered listener from §8'§6localhost:" + Bridge.getPort() + "§8'");
            } catch (Throwable t) {
                Bridge.stacktrace(t, "Failed to stop bridge on socket §8'§6localhost:" + Bridge.getPort() + "§8'");
            }
        }
    }

    public static void setServerSocket(ServerSocket serverSocket) {
        ConnectionHandler.serverSocket = serverSocket;
    }

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }
}
