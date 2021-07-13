package net.nonswag.tnl.listener.api.event;

import net.nonswag.tnl.listener.api.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class EventManager {

    @Nonnull
    private final JavaPlugin plugin;

    public EventManager(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Nonnull
    public JavaPlugin getPlugin() {
        return plugin;
    }

    public void registerListener(@Nonnull Listener listener) {
        try {
            Bukkit.getPluginManager().registerEvents(listener, getPlugin());
            Logger.debug.println("Registered listener <'" + listener.getClass().getSimpleName() + "'>");
        } catch (Throwable t) {
            Logger.error.println("Failed to register listener <'" + listener.getClass().getSimpleName() + "'>", t.getMessage());
        }
    }
}
