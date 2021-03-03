package net.nonswag.tnl.listener.utils;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.object.Objects;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;

public class ConfigUtil {

    private static FileConfiguration config;

    public static void initConfig() {
        TNLListener.getInstance().getMain().saveConfig();
        if(config == null) {
            config = TNLListener.getInstance().getMain().getConfig();
        }
        TNLListener.getInstance().getMain().reloadConfig();
        config = TNLListener.getInstance().getMain().getConfig();
    }

    public static FileConfiguration getConfig() { return config; }

    public static boolean isSet(String path) { return config.isSet(path); }

    @Nonnull
    public static String getString(String path) {
        return new Objects<>(getConfig().getString(path)).getOrDefault("");
    }

    public static Object get(String path) {
        return config.get(path);
    }

    public static void set(String path, Object value) {
        config.set(path, value);
        initConfig();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static void setConfig(FileConfiguration config) {
        ConfigUtil.config = config;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    public static class ConfigurationSection {

        private final String path;
        private final Object value;

        public ConfigurationSection(String path, Object value) {
            this.path = path;
            this.value = value;
        }

        public boolean isAbsent() {
            return !ConfigUtil.isSet(path);
        }

        public void createIfAbsent() {
            if(isAbsent()) {
                create();
            }
        }

        public void create() {
            try {
                ConfigUtil.set(path, value);
                Logger.info.println("Successfully set '" + path + "' to '" + value.toString() + "'");
            } catch (Exception e) {
                Logger.error.println(e);
            }
        }

        public String getPath() {
            return path;
        }

        public Object getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConfigurationSection that = (ConfigurationSection) o;
            return java.util.Objects.equals(path, that.path) &&
                    java.util.Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(path, value);
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public String toString() {
            return "ConfigurationSection{" +
                    "path='" + path + '\'' +
                    ", value=" + value +
                    '}';
        }
    }
}
