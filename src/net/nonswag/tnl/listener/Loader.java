package net.nonswag.tnl.listener;

import net.nonswag.tnl.listener.api.message.Message;
import net.nonswag.tnl.listener.api.settings.Settings;
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
            Message.init();
            Settings.init();
        } catch (Throwable t) {
            t.printStackTrace();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(1);
            return;
        }
        TNLListener.getInstance().enable();
    }

    public static void setInstance(@Nonnull Loader instance) {
        Loader.instance = instance;
    }

    public static Loader getInstance() {
        return instance;
    }
}
