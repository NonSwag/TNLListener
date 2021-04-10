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
            Logger.debug.println("§aRegistered listener §8'§6" + listener.getClass().getSimpleName() + "§8'");
        } catch (Throwable t) {
            Logger.error.println("§cFailed to register listener §8'§4" + listener.getClass().getSimpleName() + "§8'", t);
        }
    }
}
