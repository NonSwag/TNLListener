package net.nonswag.tnl.listener.api.holograms;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.nonswag.tnl.listener.Holograms;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.holograms.event.InteractEvent;
import net.nonswag.tnl.listener.api.holograms.event.SendEvent;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.math.Range;
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
import java.util.function.Consumer;

/**
 * <blockquote><pre>
 * Placeholder: %{@link Placeholder placeholder}%
 *
 * "Placeholders": [
 *     "{@link TNLPlayer#getName() player}": "The name of the player who is reading this",
 *     "{@link TNLPlayer#getDisplayName() display_name}": "The display name of the player who is reading this",
 *     "{@link TNLPlayer#getLanguage() language}": "The client language of the player who is reading this",
 *     "{@link TNLListener#getServerName() server}": "The name of the current server",
 *     "{@link Server#getStatus()} status_$SERVER}": "The status of the server (Online/Offline/Starting)",
 *     "{@link Server#getPlayerCount() online_$SERVER}": "The player count of the server",
 *     "{@link Server#getMaxPlayerCount() max_online_$SERVER}": "The maximum player count of the server",
 *     "{@link Bukkit#getOnlinePlayers() online}": "The player count of this server",
 *     "{@link Bukkit#getMaxPlayers() max_online}": "The maximum player count of this server",
 *     "{@link TNLPlayer#getWorld() world}": "The name of this world",
 *     "{@link TNLPlayer#getWorldAlias() world_alias}": "The alias of this world",
 *     "{@link World#getPlayers() players_$WORLD}": "The player count of the world"
 * ]
 * </pre></blockquote>
 */

public class Hologram {

    @Nonnull
    private final String name;
    @Nonnull
    private final List<String> lines = new ArrayList<>();
    @Nullable
    private Location location;
    private double lineDistance = 0.25D;
    @Range(from = 1, to = 5)
    private int darkness = 1;
    private final boolean cache;
    @Nonnull
    private Consumer<SendEvent> onSend = event -> {};
    @Nonnull
    private Consumer<InteractEvent> onInteract = event -> {};

    public Hologram(@Nonnull String name, boolean cache, @Nonnull String... lines) {
        this.name = name;
        this.cache = cache;
        getLines().addAll(Arrays.asList(lines));
    }

    public Hologram(@Nonnull String name, @Nonnull String... lines) {
        this(name, true, lines);
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public List<String> getLines() {
        return lines;
    }

    @Nonnull
    public Hologram addLines(String... lines) {
        getLines().addAll(Arrays.asList(lines));
        return this;
    }

    @Nonnull
    public Hologram addLines(List<String> lines) {
        getLines().addAll(lines);
        return this;
    }

    @Nonnull
    public Hologram addLine(String line) {
        getLines().add(line);
        return this;
    }

    @Nonnull
    public Hologram setLines(String... lines) {
        getLines().clear();
        addLines(lines);
        return this;
    }

    public double getLineDistance() {
        return lineDistance;
    }

    @Range(from = 1, to = 5)
    public int getDarkness() {
        return darkness;
    }

    public boolean isCache() {
        return cache;
    }

    @Nonnull
    public Hologram setLineDistance(double lineDistance) {
        this.lineDistance = lineDistance;
        return this;
    }

    @Nonnull
    public Hologram setDarkness(@Range(from = 1, to = 5) int darkness) {
        if (darkness > 5) {
            darkness = 5;
            Logger.error.println("The hologram darkness can't be higher then 5");
        } else if (darkness < 1) {
            darkness = 1;
            Logger.error.println("The hologram darkness can't be lower then 1");
        }
        this.darkness = darkness;
        return this;
    }

    @Nonnull
    public Consumer<SendEvent> onSend() {
        return onSend;
    }

    @Nonnull
    public Consumer<InteractEvent> onInteract() {
        return onInteract;
    }

    @Nonnull
    public Hologram onSend(@Nonnull Consumer<SendEvent> onSend) {
        this.onSend = onSend;
        return this;
    }

    @Nonnull
    public Hologram onInteract(@Nonnull Consumer<InteractEvent> onInteract) {
        this.onInteract = onInteract;
        return this;
    }

    @Nonnull
    public Hologram setLocation(@Nonnull Location location) {
        this.location = location;
        return this;
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

    @Nonnull
    public Hologram save() {
        if (getLocation() != null) {
            JsonObject jsonObject = Holograms.getInstance().getSaves().getJsonElement().getAsJsonObject();
            if (!jsonObject.has(this.getName())) jsonObject.add(this.getName(), new JsonObject());
            JsonObject hologram = jsonObject.get(this.getName()).getAsJsonObject();
            hologram.addProperty("darkness", this.getDarkness());
            hologram.addProperty("line-distance", this.getLineDistance());
            hologram.addProperty("position", (getWorld() != null ? getWorld().getName() : "world") + ", " + getX() + ", " + getY() + ", " + getZ());
            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < this.getLines().size(); i++) {
                if (this.getLines().get(i) != null) jsonArray.add(this.getLines().get(i));
                else jsonArray.add("");
            }
            hologram.add("lines", jsonArray);
            Holograms.getInstance().save(this);
            Holograms.getInstance().getSaves().save();
        } else throw new NullPointerException("Location can't be null");
        return this;
    }

    @Nonnull
    public Hologram delete() {
        JsonObject jsonObject = Holograms.getInstance().getSaves().getJsonElement().getAsJsonObject();
        if (jsonObject.has(this.getName())) {
            jsonObject.remove(this.getName());
            Holograms.getInstance().delete(this);
            Holograms.getInstance().getSaves().save();
        }
        this.unloadAll();
        return this;
    }

    @Nonnull
    public Hologram updateAll() {
        Holograms.getInstance().updateAll(this);
        return this;
    }

    @Nonnull
    public Hologram update(TNLPlayer player) {
        Holograms.getInstance().update(this, player);
        return this;
    }

    @Nonnull
    public Hologram teleport(Location location, TNLPlayer player) {
        Holograms.getInstance().teleport(this, location, player);
        return this;
    }

    @Nonnull
    public Hologram teleportAll(Location location) {
        Holograms.getInstance().teleportAll(this, location);
        return this;
    }

    @Nonnull
    public Hologram teleportAll(double offsetX, double offsetY, double offsetZ) {
        Holograms.getInstance().teleportAll(this, offsetX, offsetY, offsetZ);
        return this;
    }

    @Nonnull
    public Hologram teleport(double offsetX, double offsetY, double offsetZ, TNLPlayer player) {
        Holograms.getInstance().teleport(this, offsetX, offsetY, offsetZ, player);
        return this;
    }

    @Nonnull
    public Hologram load(TNLPlayer player) {
        Holograms.getInstance().load(this, player);
        return this;
    }

    @Nonnull
    public Hologram loadAll() {
        Holograms.getInstance().loadAll(this);
        return this;
    }

    @Nonnull
    public Hologram unload(TNLPlayer player) {
        Holograms.getInstance().unload(this, player);
        return this;
    }

    @Nonnull
    public Hologram unloadAll() {
        Holograms.getInstance().unloadAll(this);
        return this;
    }

    @Nonnull
    public Hologram reloadAll() {
        Holograms.getInstance().reloadAll(this);
        return this;
    }
}
