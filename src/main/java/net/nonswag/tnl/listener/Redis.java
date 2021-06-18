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

    @Nonnull
    @Override
    public String set(@Nonnull String key, @Nullable String value) {
        if (value == null) return super.sentinelRemove(key);
        else return super.set(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8));
    }

    @Nonnull
    public String set(@Nonnull String key, @Nullable Object value) {
        return set(key, value == null ? null : value.toString());
    }

    public int getInt(@Nonnull String key) {
        return Integer.parseInt(get(key));
    }

    public double getDouble(@Nonnull String key) {
        return Double.parseDouble(get(key));
    }

    public long getLong(@Nonnull String key) {
        return Long.parseLong(get(key));
    }

    public short getShort(@Nonnull String key) {
        return Short.parseShort(get(key));
    }

    public boolean getBoolean(@Nonnull String key) {
        return Boolean.parseBoolean(get(key));
    }

    @Override
    public boolean isConnected() {
        return super.isConnected() && !super.isBroken();
    }

    public void init() {
        try {
            connect();
        } catch (Exception e) {
            Logger.error.println("Failed to connect to Redis <'" + getConfig().getString("host") + ":" + getConfig().getString("port") + "'>");
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
