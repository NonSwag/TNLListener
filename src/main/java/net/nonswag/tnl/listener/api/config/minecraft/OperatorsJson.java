package net.nonswag.tnl.listener.api.config.minecraft;

import net.nonswag.tnl.listener.api.config.JsonConfig;

import javax.annotation.Nonnull;

public final class OperatorsJson extends JsonConfig {

    @Nonnull
    private static final OperatorsJson instance = new OperatorsJson();

    protected OperatorsJson() {
        super("ops.json");
    }

    @Nonnull
    public static OperatorsJson getInstance() {
        return instance;
    }
}
