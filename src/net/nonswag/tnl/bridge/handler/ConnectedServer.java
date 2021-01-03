package net.nonswag.tnl.bridge.handler;

import net.nonswag.tnl.listener.api.serverAPI.Server;

import javax.annotation.Nonnull;
import java.net.Socket;

/*******************************************************
 * Copyright (C) 2019-2023 NonSwag kirschnerdavid2466@gmail.com
 *
 * This file is part of TNLListener and was created at the 10/31/20
 *
 * TNLListener can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/

public class ConnectedServer {

    @Nonnull private final Socket socket;
    @Nonnull private final Server server;

    public ConnectedServer(@Nonnull Socket socket, @Nonnull Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Nonnull
    public Socket getSocket() {
        return socket;
    }

    @Nonnull
    public Server getServer() {
        return server;
    }
}
