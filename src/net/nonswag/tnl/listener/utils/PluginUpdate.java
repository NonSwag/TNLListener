package net.nonswag.tnl.listener.utils;

import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class PluginUpdate {

    public static void main(String[] args) {
        new PluginUpdate("TNLListener", "1.0");
    }

    @Nonnull private final String plugin;
    @Nonnull private final String currentVersion;
    @Nonnull private String latestVersion = "UNKNOWN";
    private boolean upToDate = false;

    public PluginUpdate(@Nonnull String plugin, @Nonnull String currentVersion) {
        this.plugin = plugin;
        this.currentVersion = currentVersion;
        try {
            URL url = new URL("https://www.thenextlvl.net/themes/PluginAPI.html");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line, latestVersion = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line.replace(" ", ""));
            }
            reader.close();
            String[] strings = sb.toString().split(",");
            for (String string : strings) {
                String[] strings1 = string.split("\"");
                for (int i1 = 0; i1 < strings1.length; i1++) {
                    if (strings1[i1].equals(plugin)) {
                        latestVersion = strings1[i1 + 2];
                        break;
                    }
                }
            }
            if (latestVersion == null) {
                NMSMain.stacktrace("The Plugin '" + plugin + "' is not a (public) plugin by TheNextLvl.net");
            } else {
                this.latestVersion = latestVersion;
                this.upToDate = (getCurrentVersion().equals(getLatestVersion()));
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public PluginUpdate(@Nonnull Plugin plugin) {
        this(plugin.getName(), plugin.getDescription().getVersion());
    }

    public void downloadUpdate() {
        if (!isUpToDate()) {
            try {
                FileDownloader.downloadFile("http://thenextlvl.net/plugins/" + getPlugin() + ".jar",
                        new File("").getAbsolutePath());
            } catch (Throwable t) {
                NMSMain.stacktrace(t);
            }
        } else {
            NMSMain.print("The plugin '" + getPlugin() + "' is up to date");
        }
    }

    public void setLatestVersion(@Nonnull String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public void setUpToDate(boolean upToDate) {
        this.upToDate = upToDate;
    }

    @Nonnull
    public String getPlugin() {
        return plugin;
    }

    @Nonnull
    public String getCurrentVersion() {
        return currentVersion;
    }

    @Nonnull
    public String getLatestVersion() {
        return latestVersion;
    }

    public boolean isUpToDate() {
        return upToDate;
    }

    @Override
    public String toString() {
        return "PluginUpdate{" +
                "plugin='" + plugin + '\'' +
                ", currentVersion='" + currentVersion + '\'' +
                ", latestVersion='" + latestVersion + '\'' +
                ", upToDate=" + upToDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginUpdate that = (PluginUpdate) o;
        return upToDate == that.upToDate && plugin.equals(that.plugin) && currentVersion.equals(that.currentVersion) && latestVersion.equals(that.latestVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plugin, currentVersion, latestVersion, upToDate);
    }
}
