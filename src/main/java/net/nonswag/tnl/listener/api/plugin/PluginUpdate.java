package net.nonswag.tnl.listener.api.plugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.utils.FileDownloader;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class PluginUpdate {

    @Nonnull
    private final String plugin;
    @Nonnull
    private final String currentVersion;
    @Nonnull
    private String latestVersion = "1.0";
    private boolean upToDate = false;

    public PluginUpdate(@Nonnull String plugin, @Nonnull String currentVersion) {
        this.plugin = plugin;
        this.currentVersion = currentVersion;
    }

    public PluginUpdate(@Nonnull Plugin plugin) {
        this(plugin.getName(), plugin.getDescription().getVersion());
    }

    public void update() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://www.thenextlvl.net/themes/PluginAPI.html").openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line.replace(" ", ""));
            reader.close();
            JsonElement jsonElement = new JsonParser().parse(sb.toString());
            if (jsonElement.isJsonObject()) {
                JsonObject plugin = jsonElement.getAsJsonObject().getAsJsonObject(getPlugin());
                if (plugin != null) {
                    if (plugin.has("latest-version")) setLatestVersion(plugin.get("latest-version").getAsString());
                }
                setUpToDate(getCurrentVersion().equals(getLatestVersion()));
            } else {
                Logger.error.println("The Plugin '" + getPlugin() + "'> is not a (public) plugin by TheNextLvl.net");
            }
            connection.disconnect();
        } catch (Exception e) {
            Logger.error.println("Failed to Update '" + getPlugin() + "'>", e.getMessage());
        }
    }

    public void downloadUpdate() {
        if (!isUpToDate()) {
            try {
                FileDownloader.downloadFile("https://www.thenextlvl.net/plugins/" + getPlugin() + ".jar", new File("plugins/updates").getAbsolutePath());
            } catch (Exception e) {
                Logger.error.println("Failed to Download '" + getPlugin() + "'>", e.getMessage());
            }
        } else {
            Logger.debug.println("§aThe plugin '" + getPlugin() + "'> is up to date");
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
}
