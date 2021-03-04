package net.nonswag.tnl.listener;

import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class TNLMain extends JavaPlugin {

    private static TNLMain instance = null;

    public TNLMain() {
        setInstance(this);
    }

    @Override
    public void onEnable() {
        TNLListener.getInstance().enable();
    }

    public static void setInstance(@Nonnull TNLMain instance) {
        TNLMain.instance = instance;
    }

    public static TNLMain getInstance() {
        return instance;
    }
}
