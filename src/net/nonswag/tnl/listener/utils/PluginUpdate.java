package net.nonswag.tnl.listener.utils;

import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class PluginUpdate {

    private final Plugin plugin;
    private final String currentVersion;
    private String latestVersion = "UNKNOWN";
    private boolean upToDate = false;
    private final boolean debug;

    public PluginUpdate(Plugin plugin) {
        this(plugin, true);
    }

    public PluginUpdate(Plugin plugin, boolean debug) {
        this.plugin = plugin;
        this.currentVersion = getPlugin().getDescription().getVersion();
        this.debug = debug;
        try {
            URL url = new URL("http://www.thenextlvl.net/themes/PluginAPI");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();
            String line, version = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line.replace(" ", ""));
            }
            String[] strings = sb.toString().split(",");
            for (String string : strings) {
                String[] strings1 = string.split("\"");
                for (int i1 = 0; i1 < strings1.length; i1++) {
                    if (strings1[i1].equals(plugin.getName())) {
                        version = strings1[i1 + 2];
                        break;
                    }
                }
            }
            if (version == null) {
                if (isDebug()) {
                    NMSMain.stacktrace("The Plugin '" + plugin.getName() + "' is not a (public) plugin by TheNextLvl.net");
                }
            } else {
                this.latestVersion = version;
                this.upToDate = (getCurrentVersion().equals(getLatestVersion()));
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public boolean isUpToDate() {
        return upToDate;
    }

    private boolean isDebug() {
        return debug;
    }

    private void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    private void setUpToDate(boolean upToDate) {
        this.upToDate = upToDate;
    }

    public void downloadUpdate() throws Throwable {
        if (!isUpToDate()) {
            FileDownloader.downloadFile("http://www.thenextlvl.net/plugins/" + getPlugin().getName() + ".jar",
                    NMSMain.getPlugin().getDataFolder().getParentFile().getAbsolutePath());
        } else {
            if (isDebug()) {
                NMSMain.print("The plugin '" + getPlugin().getName() + "' is up to date");
            }
        }
    }
}
