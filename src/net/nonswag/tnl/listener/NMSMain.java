package net.nonswag.tnl.listener;

import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class NMSMain extends JavaPlugin {

    private static NMSMain instance = null;

    public NMSMain() {
        setInstance(this);
    }

    @Override
    public void onEnable() {
        TNLListener listener = new TNLListener(this);
        listener.enable();
    }

    public static void setInstance(@Nonnull NMSMain instance) {
        NMSMain.instance = instance;
    }

    public static NMSMain getInstance() {
        return instance;
    }
}
