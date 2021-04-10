package net.nonswag.tnl.listener.api.config.minecraft;

import net.nonswag.tnl.listener.api.config.PropertyConfig;

import javax.annotation.Nonnull;

public final class ServerProperties extends PropertyConfig {

    @Nonnull
    private static final ServerProperties instance = new ServerProperties();

    protected ServerProperties() {
        super("server.properties");
    }

    @Nonnull
    public static ServerProperties getInstance() {
        return instance;
    }
}
