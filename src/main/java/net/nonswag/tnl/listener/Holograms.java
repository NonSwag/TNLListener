package net.nonswag.tnl.listener;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.nonswag.tnl.listener.api.config.JsonConfig;
import net.nonswag.tnl.listener.api.entity.TNLArmorStand;
import net.nonswag.tnl.listener.api.holograms.Hologram;
import net.nonswag.tnl.listener.api.holograms.UpdateRunnable;
import net.nonswag.tnl.listener.api.holograms.event.SendEvent;
import net.nonswag.tnl.listener.api.holograms.event.UpdateEvent;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.object.Objects;
import net.nonswag.tnl.listener.api.packet.*;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.server.Server;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Holograms {

    @Nonnull
    private static final Holograms instance = new Holograms();

    @Nonnull
    private final JsonConfig saves = new JsonConfig("plugins/Holograms/", "saves.json");
    @Nonnull
    private final HashMap<String, Hologram> hologramHashMap = new HashMap<>();
    private long updateTime = 5000L;

    protected Holograms() {
    }

    protected void load() {
        if (!getSaves().getJsonElement().getAsJsonObject().has("update-time")) {
            getSaves().getJsonElement().getAsJsonObject().addProperty("update-time", updateTime);
        } else {
            setUpdateTime(getSaves().getJsonElement().getAsJsonObject().get("update-time").getAsLong());
        }
    }

    protected void enable() {
        unloadAll();
        UpdateRunnable.start();
        loadAll();
    }

    protected void disable() {
        UpdateRunnable.stop();
    }

    @Nonnull
    public static Holograms getInstance() {
        return instance;
    }

    @Nonnull
    public JsonConfig getSaves() {
        return saves;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void loadAll(@Nonnull Hologram hologram) {
        for (TNLPlayer all : TNLListener.getInstance().getOnlinePlayers()) load(hologram, all);
    }

    public void loadAll(@Nonnull TNLPlayer player) {
        for (Hologram hologram : cachedValues()) load(hologram, player);
    }

    public void loadAll() {
        getHologramHashMap().clear();
        List<String> holograms = list();
        for (String s : holograms) {
            try {
                if (getSaves().getJsonElement().getAsJsonObject().has(s) && getSaves().getJsonElement().getAsJsonObject().get(s).isJsonObject()) {
                    Hologram hologram = getOrDefault(s, create(s));
                    JsonObject jsonObject = getSaves().getJsonElement().getAsJsonObject().getAsJsonObject(hologram.getName());
                    hologram.setDarkness(Objects.getOrDefault(jsonObject.get("darkness").getAsInt(), 1));
                    hologram.setLineDistance(Objects.getOrDefault(jsonObject.get("line-distance").getAsDouble(), 0.25D));
                    String position = jsonObject.get("position").getAsString();
                    String[] split = position.split(", ");
                    if (split.length == 4) {
                        Location location = new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
                        hologram.setLocation(location);
                        JsonArray lines = jsonObject.get("lines").getAsJsonArray();
                        for (JsonElement line : lines) {
                            String string = line.getAsString();
                            if (string != null && !string.replace(" ", "").isEmpty()) hologram.getLines().add(string);
                            else hologram.getLines().add("");
                        }
                        loadAll(hologram);
                    }
                }
            } catch (Exception e) {
                Logger.error.println(e);
            }
        }
    }

    public void load(@Nonnull Hologram hologram, @Nonnull TNLPlayer player) {
        if (hologram.getLocation() != null && player.getWorld().equals(hologram.getWorld())) {
            final List<Object> packets = new ArrayList<>();
            final List<TNLArmorStand> armorStands = new ArrayList<>();
            for (int line = 0; line < hologram.getLines().size(); line++) {
                if (hologram.getLines().get((hologram.getLines().size() - 1) - line) == null || hologram.getLines().get((hologram.getLines().size() - 1) - line).isEmpty()) {
                    continue;
                }
                for (int darkness = 0; darkness < hologram.getDarkness(); darkness++) {
                    TNLArmorStand armorStand = TNLArmorStand.create(player.getWorld(), hologram.getX(), hologram.getY() + (line * hologram.getLineDistance()), hologram.getZ(), hologram.getLocation().getYaw(), hologram.getLocation().getPitch());
                    String s = hologram.getLines().get((hologram.getLines().size() - 1) - line).
                            replace("&", "§").
                            replace(">>", "»").
                            replace("<<", "«").
                            replace("%player%", player.getName()).
                            replace("%display_name%", player.getDisplayName()).
                            replace("%language%", player.getLocale()).
                            replace("%server%", TNLListener.getInstance().getServerName()).
                            replace("%online%", Bukkit.getOnlinePlayers().size() + "").
                            replace("%max_online%", Bukkit.getMaxPlayers() + "").
                            replace("%world%", player.getWorld().getName() + "").
                            replace("%world_alias%", player.getWorldAlias() + "");
                    if (s.contains("%status_") || s.contains("%online_") || s.contains("%max_online_") || s.contains("%players_")) {
                        for (Server server : TNLListener.getInstance().getServers()) {
                            if (s.contains("%status_" + server.getName() + "%")) {
                                s = s.replace("%status_" + server.getName() + "%", server.getStatus().isOnline() ? "Online" : (server.getStatus().isOffline() ? "Offline" : "Starting"));
                            }
                            if (s.contains("%online_" + server.getName() + "%")) {
                                s = s.replace("%online_" + server.getName() + "%", server.getPlayerCount() + "");
                            }
                            if (s.contains("%max_online_" + server.getName() + "%")) {
                                s = s.replace("%max_online_" + server.getName() + "%", server.getMaxPlayerCount() + "");
                            }
                        }
                        for (World world : Bukkit.getWorlds()) {
                            if (s.contains("%players_" + world.getName() + "%")) {
                                s = s.replace("%players_" + world.getName() + "%", world.getPlayers().size() + "");
                            }
                        }
                    }
                    armorStand.setInvisible(true);
                    armorStand.setSmall(true);
                    armorStand.setCustomNameVisible(true);
                    armorStand.setBasePlate(true);
                    armorStand.setMarker(true);
                    armorStand.setCustomName(s);
                    player.getVirtualStorage().put("hologram=" + hologram.getName() + ",line=" + line + ",darkness=" + darkness, armorStand.getId());
                    player.getVirtualStorage().put("hologram-by-id=" + armorStand.getId(), armorStand);
                    armorStands.add(armorStand);
                }
            }
            if (!armorStands.isEmpty()) {
                SendEvent event = new SendEvent(player, hologram, armorStands);
                hologram.onSend().accept(event);
                for (TNLArmorStand armorStand : event.getArmorStands()) {
                    packets.add(TNLEntitySpawn.create(armorStand));
                    packets.add(TNLEntityMetadata.create(armorStand));
                    packets.add(TNLEntityEquipment.create(armorStand));
                }
                player.sendPackets(packets);
            }
        }
    }

    public void update(@Nonnull Hologram hologram, @Nonnull TNLPlayer player) {
        final List<TNLArmorStand> armorStands = new ArrayList<>();
        for (int line = 0; line < hologram.getLines().size(); line++) {
            for (int darkness = 0; darkness < hologram.getDarkness(); darkness++) {
                Objects<Number> id = player.getVirtualStorage().getNumber("hologram=" + hologram.getName() + ",line=" + line + ",darkness=" + darkness);
                if (id.hasValue()) {
                    Objects<TNLArmorStand> hologramById = player.getVirtualStorage().get("hologram-by-id=" + id.nonnull().intValue(), TNLArmorStand.class);
                    if (hologramById.hasValue()) {
                        TNLArmorStand armorStand = hologramById.nonnull();
                        String s = hologram.getLines().get((hologram.getLines().size() - 1) - line).
                                replace("&", "§").
                                replace(">>", "»").
                                replace("<<", "«").
                                replace("%player%", player.getName()).
                                replace("%display_name%", player.getDisplayName()).
                                replace("%language%", player.getLocale()).
                                replace("%server%", TNLListener.getInstance().getServerName()).
                                replace("%online%", Bukkit.getOnlinePlayers().size() + "").
                                replace("%max_online%", Bukkit.getMaxPlayers() + "").
                                replace("%world%", player.getWorld().getName() + "").
                                replace("%world_alias%", player.getWorldAlias() + "");
                        if (s.contains("%status_") || s.contains("%online_") || s.contains("%max_online_") || s.contains("%players_")) {
                            for (Server server : TNLListener.getInstance().getServers()) {
                                if (s.contains("%status_" + server.getName() + "%")) {
                                    s = s.replace("%status_" + server.getName() + "%", server.getStatus().isOnline() ? "Online" : (server.getStatus().isOffline() ? "Offline" : "Starting"));
                                }
                                if (s.contains("%online_" + server.getName() + "%")) {
                                    s = s.replace("%online_" + server.getName() + "%", server.getPlayerCount() + "");
                                }
                                if (s.contains("%max_online_" + server.getName() + "%")) {
                                    s = s.replace("%max_online_" + server.getName() + "%", server.getMaxPlayerCount() + "");
                                }
                            }
                            for (World world : Bukkit.getWorlds()) {
                                if (s.contains("%players_" + world.getName() + "%")) {
                                    s = s.replace("%players_" + world.getName() + "%", world.getPlayers().size() + "");
                                }
                            }
                        }
                        if (armorStand.getBukkitEntity().getName().equals(s)) continue;
                        armorStand.setCustomName(s);
                        armorStand.setCustomNameVisible(true);
                        armorStands.add(armorStand);
                    }
                }
            }
        }
        if (!armorStands.isEmpty()) {
            UpdateEvent event = new UpdateEvent(player, hologram, armorStands);
            hologram.onUpdate().accept(event);
            for (TNLArmorStand armorStand : armorStands) {
                player.sendPacket(TNLEntityMetadata.create(armorStand));
            }
        }
    }

    public void updateAll(@Nonnull Hologram hologram) {
        for (TNLPlayer all : TNLListener.getInstance().getOnlinePlayers()) update(hologram, all);
    }

    public void teleport(@Nonnull Hologram hologram, @Nonnull Location location, @Nonnull TNLPlayer player) {
        for (int line = 0; line < hologram.getLines().size(); line++) {
            for (int darkness = 0; darkness < hologram.getDarkness(); darkness++) {
                Objects<Number> id = player.getVirtualStorage().getNumber("hologram=" + hologram.getName() + ",line=" + line + ",darkness=" + darkness);
                if (id.hasValue()) {
                    player.sendPacket(TNLEntityTeleport.create(id.nonnull().intValue(), location.getX(), location.getY() + (line * hologram.getLineDistance()), location.getZ(), location.getYaw(), location.getPitch()));
                }
            }
        }
    }

    public void teleportAll(@Nonnull Hologram hologram, @Nonnull Location location) {
        for (TNLPlayer all : TNLListener.getInstance().getOnlinePlayers()) teleport(hologram, location, all);
    }

    public void teleportAll(@Nonnull Hologram hologram, double offsetX, double offsetY, double offsetZ) {
        if (hologram.getLocation() != null) {
            hologram.teleportAll(hologram.getLocation().clone().add(offsetX, offsetY, offsetZ));
        }
    }

    public void teleport(@Nonnull Hologram hologram, double offsetX, double offsetY, double offsetZ, @Nonnull TNLPlayer player) {
        if (hologram.getLocation() != null) {
            hologram.teleport(hologram.getLocation().clone().add(offsetX, offsetY, offsetZ), player);
        }
    }

    public void reloadAll() {
        unloadAll();
        loadAll();
    }

    public void reloadAll(@Nonnull TNLPlayer player) {
        unloadAll(player);
        loadAll(player);
    }

    public void reloadAll(@Nonnull Hologram hologram) {
        unloadAll(hologram);
        loadAll(hologram);
    }

    public void reload(@Nonnull Hologram hologram, @Nonnull TNLPlayer player) {
        unload(hologram, player);
        load(hologram, player);
    }

    public void unloadAll(@Nonnull Hologram hologram) {
        for (TNLPlayer all : TNLListener.getInstance().getOnlinePlayers()) unload(hologram, all);
    }

    public void unloadAll(@Nonnull TNLPlayer player) {
        for (Hologram hologram : cachedValues()) unload(hologram, player);
    }

    public void unloadAll() {
        for (Hologram hologram : cachedValues()) unloadAll(hologram);
    }

    public void unload(@Nonnull Hologram hologram, @Nonnull TNLPlayer player) {
        for (int line = 0; line < hologram.getLines().size(); line++) {
            if (hologram.getLines().get(line) == null || hologram.getLines().get(line).isEmpty()) {
                continue;
            }
            for (int darkness = 0; darkness < hologram.getDarkness(); darkness++) {
                String key = "hologram=" + hologram.getName() + ",line=" + line + ",darkness=" + darkness;
                if (player.getVirtualStorage().getNumber(key).hasValue()) {
                    player.sendPacket(TNLEntityDestroy.create(player.getVirtualStorage().getNumber(key).nonnull().intValue()));
                }
                player.getVirtualStorage().remove(key);
            }
        }
    }

    @Nonnull
    private HashMap<String, Hologram> getHologramHashMap() {
        return hologramHashMap;
    }

    @Nonnull
    public Hologram getOrDefault(@Nonnull String name, @Nonnull Hologram hologram) {
        if (!getHologramHashMap().containsKey(name)) getHologramHashMap().put(name, hologram);
        return getHologramHashMap().get(name);
    }

    @Nullable
    public Hologram get(@Nonnull String name) {
        return getHologramHashMap().get(name);
    }

    @Nonnull
    public Hologram create(@Nonnull String name, boolean cache) {
        Hologram hologram = new Hologram(name, cache);
        if (cache) save(hologram);
        return hologram;
    }

    @Nonnull
    public Hologram create(@Nonnull String name) {
        return create(name, true);
    }

    public void save(@Nonnull Hologram hologram) {
        getHologramHashMap().put(hologram.getName(), hologram);
    }

    public void delete(@Nonnull Hologram hologram) {
        getHologramHashMap().remove(hologram.getName());
    }

    @Nonnull
    public List<Hologram> cachedValues() {
        return new ArrayList<>(getHologramHashMap().values());
    }

    @Nonnull
    public List<String> cachedNameValues() {
        return new ArrayList<>(getHologramHashMap().keySet());
    }

    @Nonnull
    public List<String> list() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : getSaves().getJsonElement().getAsJsonObject().entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    @Nonnull
    public List<TNLArmorStand> getArmorStands(@Nonnull Hologram hologram, @Nonnull TNLPlayer player) {
        List<TNLArmorStand> armorStands = new ArrayList<>();
        for (int i = 0; i < hologram.getLines().size(); i++) {
            for (int darkness = 0; darkness < hologram.getDarkness(); darkness++) {
                Objects<Number> number = player.getVirtualStorage().getNumber("hologram=" + hologram.getName() + ",line=" + i + ",darkness=" + darkness);
                if (number.hasValue()) {
                    Objects<TNLArmorStand> armorStand = player.getVirtualStorage().get("hologram-by-id=" + number.nonnull(), TNLArmorStand.class);
                    if (armorStand.hasValue()) armorStands.add(armorStand.nonnull());
                }
            }
        }
        return armorStands;
    }

    @Nonnull
    public List<Integer> getIds(@Nonnull Hologram hologram, @Nonnull TNLPlayer player) {
        List<Integer> armorStands = new ArrayList<>();
        for (int i = 0; i < hologram.getLines().size(); i++) {
            for (int darkness = 0; darkness < hologram.getDarkness(); darkness++) {
                Objects<Number> number = player.getVirtualStorage().getNumber("hologram=" + hologram.getName() + ",line=" + i + ",darkness=" + darkness);
                if (number.hasValue()) armorStands.add(number.nonnull().intValue());
            }
        }
        return armorStands;
    }
}
