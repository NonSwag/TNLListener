package net.nonswag.tnl.listener;

import net.nonswag.tnl.listener.api.message.Message;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.listener.events.MessagesInitializeEvent;
import net.nonswag.tnl.listener.events.RedisInitializeEvent;
import net.nonswag.tnl.listener.events.SettingsInitializeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Bootstrap extends JavaPlugin {

    @Nullable
    private static Bootstrap instance = null;
    @Nonnull
    private final List<ShutdownHook> shutdownHooks = new ArrayList<>();

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
            if (new SettingsInitializeEvent().call()) Settings.init();
            if (new MessagesInitializeEvent().call()) Message.init();
            if (new RedisInitializeEvent().call()) Redis.getInstance().init();
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
        for (ShutdownHook shutdownHook : getShutdownHooks()) shutdownHook.onShutdown();
        Holograms.getInstance().disable();
        TNLListener.getInstance().disable();
    }

    @Nonnull
    private List<ShutdownHook> getShutdownHooks() {
        return shutdownHooks;
    }

    public void registerShutdownHook(@Nonnull ShutdownHook shutdownHook) {
        getShutdownHooks().add(shutdownHook);
    }

    public void unregisterShutdownHook(@Nonnull ShutdownHook shutdownHook) {
        getShutdownHooks().remove(shutdownHook);
    }

    private static void setInstance(@Nonnull Bootstrap instance) {
        Bootstrap.instance = instance;
    }

    @Nonnull
    public static Bootstrap getInstance() {
        assert instance != null;
        return instance;
    }

    public interface ShutdownHook {
        void onShutdown();
    }
}
