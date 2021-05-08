package net.nonswag.tnl.listener;

import net.nonswag.tnl.listener.api.message.Message;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.listener.events.MessagesInitializeEvent;
import net.nonswag.tnl.listener.events.SettingsInitializeEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class Bootstrap extends JavaPlugin {

    protected static Bootstrap instance = null;

    public Bootstrap() {
        setInstance(this);
    }

    @Override
    public void onLoad() {
        try {
            Holograms.getInstance().load();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void onEnable() {
        try {
            Bukkit.getPluginManager().callEvent(new SettingsInitializeEvent());
            Settings.init();
            Bukkit.getPluginManager().callEvent(new MessagesInitializeEvent());
            Message.init();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
            return;
        }
        TNLListener.getInstance().enable();
        Bukkit.getScheduler().runTaskLater(this, () -> Holograms.getInstance().enable(), 20);
    }

    @Override
    public void onDisable() {
        Holograms.getInstance().disable();
        TNLListener.getInstance().disable();
    }

    private static void setInstance(@Nonnull Bootstrap instance) {
        Bootstrap.instance = instance;
    }

    public static Bootstrap getInstance() {
        return instance;
    }
}
