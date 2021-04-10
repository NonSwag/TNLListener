package net.nonswag.tnl.listener;

import net.nonswag.tnl.listener.api.config.minecraft.OperatorsJson;
import net.nonswag.tnl.listener.api.config.minecraft.ServerProperties;
import net.nonswag.tnl.listener.api.holograms.UpdateRunnable;
import net.nonswag.tnl.listener.api.message.Message;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.listener.events.MessagesInitializeEvent;
import net.nonswag.tnl.listener.events.SettingsInitializeEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class Loader extends JavaPlugin {

    private static Loader instance = null;

    public Loader() {
        setInstance(this);
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
        Holograms.getInstance().enable();
    }

    @Override
    public void onDisable() {
        ServerProperties.getInstance().save();
        OperatorsJson.getInstance().save();
        UpdateRunnable.stop();
    }

    private static void setInstance(@Nonnull Loader instance) {
        Loader.instance = instance;
    }

    public static Loader getInstance() {
        return instance;
    }
}
