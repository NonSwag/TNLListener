package net.nonswag.tnl.listener.api.holograms;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.nonswag.tnl.listener.Holograms;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.server.Server;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <blockquote><pre>
 * Placeholder: %{@link Placeholder placeholder}%
 *
 * "Placeholders": [
 *     "{@link TNLPlayer#getName() player}": "The name of the player who is reading this",
 *     "{@link TNLPlayer#getDisplayName() display_name}": "The display name of the player who is reading this",
 *     "{@link TNLPlayer#getLanguage() language}": "The client language of the player who is reading this",
 *     "{@link TNLListener#getServerName() server}": "The name of the current server",
 *     "{@link Server#isOnline() status_$SERVER}": "The status of the server (Online/Offline)",
 *     "{@link Server#getPlayerCount() online_$SERVER}": "The player count of the server",
 *     "{@link Server#getMaxPlayerCount() max_online_$SERVER}": "The maximum player count of the server",
 *     "{@link Bukkit#getOnlinePlayers() online}": "The player count of this server",
 *     "{@link Bukkit#getMaxPlayers() max_online}": "The maximum player count of this server",
 *     "{@link TNLPlayer#getWorld() world}": "The name of this world",
 *     "{@link TNLPlayer#getWorldAlias() world_alias}": "The alias of this world",
 *     "{@link World#getPlayerCount() players_$WORLD}": "The player count of the world"
 * ]
 * </pre></blockquote>
 */

public class Hologram {

    @Nonnull private final String name;
    @Nonnull private final List<String> lines = new ArrayList<>();
    @Nullable private Location location;
    private double lineDistance = 0.25D;
    private int darkness = 1;

    public Hologram(@Nonnull String name, boolean cache, String... lines) {
        this.name = name.toLowerCase();
        getLines().addAll(Arrays.asList(lines));
        if (cache) {
            Holograms.getInstance().save(this);
        }
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public List<String> getLines() {
        return lines;
    }

    public void addLines(String... lines) {
        getLines().addAll(Arrays.asList(lines));
    }

    public void addLines(List<String> lines) {
        getLines().addAll(lines);
    }

    public void addLine(String line) {
        getLines().add(line);
    }

    public double getLineDistance() {
        return lineDistance;
    }

    public int getDarkness() {
        return darkness;
    }

    public void setLineDistance(double lineDistance) {
        this.lineDistance = lineDistance;
    }

    public void setDarkness(int darkness) {
        if (darkness > 5) {
            darkness = 5;
            Logger.error.println(new IllegalArgumentException("The hologram darkness can't be higher then 5"));
        } else if (darkness < 1) {
            darkness = 1;
            Logger.error.println(new IllegalArgumentException("The hologram darkness can't be lower then 1"));
        }
        this.darkness = darkness;
    }

    public void setLocation(@Nonnull Location location) {
        this.location = location;
    }

    @Nullable
    public Location getLocation() {
        return location;
    }

    public double getX() {
        return getLocation() == null ? 0 : getLocation().getX();
    }

    public double getY() {
        return getLocation() == null ? 0 : getLocation().getY();
    }

    public double getZ() {
        return getLocation() == null ? 0 : getLocation().getZ();
    }

    @Nullable
    public World getWorld() {
        return getLocation() == null ? null : getLocation().getWorld();
    }

    public void save() {
        if (getLocation() != null) {
            JsonObject jsonObject = Holograms.getInstance().getSaves().getJsonElement().getAsJsonObject();
            if (!jsonObject.has(this.getName())) {
                jsonObject.add(this.getName(), new JsonObject());
            }
            JsonObject hologram = jsonObject.get(this.getName()).getAsJsonObject();
            hologram.addProperty("darkness", this.getDarkness());
            hologram.addProperty("line-distance", this.getLineDistance());
            hologram.addProperty("position", getLocation().getWorld().getName() + ", " + getLocation().getX() + ", " + getLocation().getY() + ", " + getLocation().getZ());
            if (!hologram.has("lines")) {
                hologram.add("lines", new JsonArray());
            }
            JsonArray jsonArray = hologram.get("lines").getAsJsonArray();
            for (int i = 0; i < this.getLines().size(); i++) {
                if (this.getLines().get(i) != null && !this.getLines().get(i).isEmpty()) {
                    jsonArray.add(this.getLines().get(i));
                }
            }
            Holograms.getInstance().save(this);
            Holograms.getInstance().getSaves().save();
        } else {
            throw new NullPointerException("Location can't be null");
        }
    }

    public void delete() {
        JsonObject jsonObject = Holograms.getInstance().getSaves().getJsonElement().getAsJsonObject();
        if (jsonObject.has(this.getName())) {
            jsonObject.remove(this.getName());
            Holograms.getInstance().delete(this);
            Holograms.getInstance().getSaves().save();
        }
        this.unloadAll();
    }

    public void updateAll() {
        Holograms.getInstance().updateAll(this);
    }

    public void update(TNLPlayer player) {
        Holograms.getInstance().update(this, player);
    }

    public void teleport(Location location, TNLPlayer player) {
        Holograms.getInstance().teleport(this, location, player);
    }

    public void teleportAll(Location location) {
        Holograms.getInstance().teleportAll(this, location);
    }

    public void teleportAll(double offsetX, double offsetY, double offsetZ) {
        Holograms.getInstance().teleportAll(this, offsetX, offsetY, offsetZ);
    }

    public void teleport(double offsetX, double offsetY, double offsetZ, TNLPlayer player) {
        Holograms.getInstance().teleport(this, offsetX, offsetY, offsetZ, player);
    }

    public void load(TNLPlayer player) {
        Holograms.getInstance().load(this, player);
    }

    public void loadAll() {
        Holograms.getInstance().loadAll(this);
    }

    public void unload(TNLPlayer player) {
        Holograms.getInstance().unload(this, player);
    }

    public void unloadAll() {
        Holograms.getInstance().unloadAll(this);
    }

    public void reloadAll() {
        Holograms.getInstance().reloadAll(this);
    }

    @Override
    public String toString() {
        return "Hologram{" +
                "name='" + name + '\'' +
                ", lines=" + lines +
                ", location=" + location +
                ", lineDistance=" + lineDistance +
                ", darkness=" + darkness +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hologram hologram = (Hologram) o;
        return Double.compare(hologram.lineDistance, lineDistance) == 0 && darkness == hologram.darkness && name.equals(hologram.name) && lines.equals(hologram.lines) && Objects.equals(location, hologram.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lines, location, lineDistance, darkness);
    }
}
