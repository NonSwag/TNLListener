package net.nonswag.tnl.listener.api.plugin;

import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.utils.FileDownloader;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class PluginUpdate {

    @Nonnull private final String plugin;
    @Nonnull private final String currentVersion;
    @Nonnull private String latestVersion = "UNKNOWN";
    private boolean upToDate = false;

    public PluginUpdate(@Nonnull String plugin, @Nonnull String currentVersion) {
        this.plugin = plugin;
        this.currentVersion = currentVersion;
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://www.thenextlvl.net/themes/PluginAPI.html").openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line, latestVersion = null;
            while ((line = reader.readLine()) != null) sb.append(line.replace(" ", ""));
            reader.close();
            Object parse = new JSONParser().parse(sb.toString());
            if (parse instanceof JSONObject) {
                JSONObject object = ((JSONObject) parse);
                for (Object key : object.keySet()) {
                    if (key != null && key.toString().equalsIgnoreCase(plugin)) {
                        latestVersion = object.get(key).toString();
                        break;
                    }
                }
            } else return;
            if (latestVersion == null) {
                Logger.error.println("The Plugin '" + plugin + "' is not a (public) plugin by TheNextLvl.net");
            } else {
                setLatestVersion(latestVersion);
                setUpToDate(getCurrentVersion().equals(getLatestVersion()));
            }
            connection.disconnect();
        } catch (Exception e) {
            Logger.error.println(e);
        }
    }

    public PluginUpdate(@Nonnull Plugin plugin) {
        this(plugin.getName(), plugin.getDescription().getVersion());
    }

    public void downloadUpdate() {
        if (!isUpToDate()) {
            try {
                FileDownloader.downloadFile("https://www.thenextlvl.net/plugins/" + getPlugin() + ".jar", new File("plugins/").getAbsolutePath());
            } catch (Exception e) {
                Logger.error.println(e);
            }
        } else {
            Logger.debug.println("§aThe plugin §8'§6" + getPlugin() + "§8'§a is up to date");
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
