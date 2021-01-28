package net.nonswag.tnl.listener.utils;

import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ConfigUtil {

    private static FileConfiguration config;
    private static final Plugin plugin = NMSMain.getPlugin();

    public static void initConfig() {
        plugin.saveConfig();
        if(config == null) {
            config = plugin.getConfig();
        }
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public static FileConfiguration getConfig() { return config; }

    public static boolean isSet(String path) { return config.isSet(path); }

    @Nonnull
    public static String getString(String path) {
        return new net.nonswag.tnl.listener.api.object.Object<>(getConfig().getString(path)).getOrDefault("");
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

    public static Plugin getPlugin() {
        return plugin;
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
                NMSMain.print("Successfully set '" + path + "' to '" + value.toString() + "'");
            } catch (Throwable t) {
                NMSMain.stacktrace(t);
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
            return Objects.equals(path, that.path) &&
                    Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path, value);
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
