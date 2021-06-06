package net.nonswag.tnl.listener;

import net.nonswag.tnl.listener.api.config.PropertyConfig;
import net.nonswag.tnl.listener.api.logger.Logger;
import redis.clients.jedis.Jedis;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;

public class Redis extends Jedis {

    @Nonnull
    private static final PropertyConfig config = new PropertyConfig("plugins/Listener/Redis", "config.properties");

    static {
        getConfig().setValueIfAbsent("host", "localhost");
        getConfig().setValueIfAbsent("port", 6379);
        getConfig().save();
    }

    @Nonnull
    private static final Redis instance = new Redis();

    protected Redis() {
        super(getConfig().getString("host"), getConfig().getInteger("port"));
    }

    @Override
    public String set(@Nonnull String key, @Nullable String value) {
        if (value == null) return super.sentinelRemove(key);
        else return super.set(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean isConnected() {
        return super.isConnected() && !super.isBroken();
    }

    public void init() {
        try {
            connect();
        } catch (Exception e) {
            Logger.error.println("Failed to connect to Redis §8'§4" + getConfig().getString("host") + ":" + getConfig().getString("port") + "§8'");
        }
    }

    @Nonnull
    private static PropertyConfig getConfig() {
        return config;
    }

    @Nonnull
    public static Redis getInstance() {
        return instance;
    }
}
