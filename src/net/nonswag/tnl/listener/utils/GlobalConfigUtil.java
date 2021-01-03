package net.nonswag.tnl.listener.utils;

import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class GlobalConfigUtil {

    private FileConfiguration config;
    private final Plugin plugin;

    public GlobalConfigUtil(Plugin plugin) {
        this.plugin = plugin;
        setConfig(plugin.getConfig());
    }

    public void initConfig() {
        getPlugin().saveConfig();
        if (getConfig() == null) {
            setConfig(getPlugin().getConfig());
        }
        getPlugin().reloadConfig();
        setConfig(getPlugin().getConfig());
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public boolean isSet(String path) {
        return getConfig().isSet(path);
    }

    public Object get(String path) {
        return getConfig().get(path);
    }

    public void set(String path, Object value) {
        getConfig().set(path, value);
        initConfig();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public Plugin getPlugin() {
        return plugin;
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
            if (isAbsent()) {
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
