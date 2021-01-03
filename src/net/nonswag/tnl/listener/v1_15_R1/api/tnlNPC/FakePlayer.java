package net.nonswag.tnl.listener.v1_15_R1.api.tnlNPC;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_15_R1.*;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.v1_15_R1.api.playerAPI.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class FakePlayer {

    private GameProfile profile;
    private final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
    private String name;
    private Location location;
    private WorldServer worldServer;
    private String[] skinValues = {"", ""};
    private boolean spawned = false;
    private EntityPlayer player = null;
    private List<TNLPlayer> receivers = new ArrayList<>();

    public FakePlayer(String name, Location location, TNLPlayer... receivers) {
        new FakePlayer(name, location, Arrays.asList(receivers));
    }

    public FakePlayer(String name, Location location, List<TNLPlayer> receivers) {
        setName(name);
        setLocation(location);
        setReceivers(receivers);
        setWorldServer(((CraftWorld) Objects.requireNonNull(getLocation().getWorld())).getHandle());
    }

    public List<TNLPlayer> getReceivers() {
        return receivers;
    }

    public FakePlayer setReceivers(List<TNLPlayer> receivers) {
        this.receivers = receivers;
        return this;
    }

    public boolean isSpawned() {
        return spawned;
    }

    private FakePlayer setSpawned(boolean spawned) {
        this.spawned = spawned;
        return this;
    }

    public WorldServer getWorldServer() {
        return worldServer;
    }

    public FakePlayer setWorldServer(WorldServer worldServer) {
        this.worldServer = worldServer;
        return this;
    }

    public FakePlayer setLocation(Location location) {
        this.location = location;
        return this;
    }

    public Location getLocation() {
        return location;
    }


    public String getName() {
        return name;
    }

    public FakePlayer setName(String name) {
        this.name = name;
        return this;
    }

    public MinecraftServer getServer() {
        return server;
    }

    public GameProfile getProfile() {
        return profile;
    }

    private FakePlayer setProfile(GameProfile profile) {
        if (!isSpawned()) {
            this.profile = profile;
        } else {
            NMSMain.stacktrace("Can't change the game profile an already existing fake player");
        }
        return this;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public FakePlayer setPlayer(EntityPlayer player) {
        this.player = player;
        return this;
    }

    public void spawn() {
        try {
            if (!isSpawned()) {
                setProfile(new GameProfile(UUID.randomUUID(), getName() == null ? "§7-§8/§7-" : getName()));
                getProfile().getProperties().put("textures", new Property("textures", getSkinValues()[0], getSkinValues()[1]));
                setPlayer(new EntityPlayer(getServer(), getWorldServer(), getProfile(), new PlayerInteractManager(getWorldServer())));
                getPlayer().setLocation(getLocation().getX(), getLocation().getY(), getLocation().getZ(), getLocation().getYaw(), getLocation().getPitch());
                for (TNLPlayer receiver : getReceivers()) {
                    receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, getPlayer()));
                    receiver.sendPacket(new PacketPlayOutNamedEntitySpawn(getPlayer()));
                    receiver.sendPacket(new PacketPlayOutEntityHeadRotation(getPlayer(), ((byte) (getPlayer().yaw * 256 / 360))));
                }
                setSpawned(true);
            } else {
                NMSMain.stacktrace("Can't spawn an already existing fake player");
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public void deSpawn() {
        if (isSpawned()) {
            for (TNLPlayer receiver : getReceivers()) {
                receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, getPlayer()));
                receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, getPlayer()));
                receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_GAME_MODE, getPlayer()));
                receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY, getPlayer()));
            }
            setSpawned(false);
        } else {
            NMSMain.stacktrace("Can't deSpawn an not existing fake player");
        }
    }

    private String[] getSkin(String playerName) {
        String texture = "";
        String signature = "";
        try {
            if (playerName == null) {
                playerName = getName();
            }
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
            URL url1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader1 = new InputStreamReader(url1.openStream());
            JsonObject property = new JsonParser().parse(reader1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            texture = property.get("value").getAsString();
            signature = property.get("signature").getAsString();
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        return new String[]{texture, signature};
    }

    private String[] getSkinValues() {
        return skinValues;
    }

    private FakePlayer setSkinValues(String[] skinValues) {
        if (!isSpawned()) {
            this.skinValues = skinValues;
        } else {
            NMSMain.stacktrace("Can't change the skin of an already existing fake player");
        }
        return this;
    }

    public FakePlayer setSkin(String name) {
        if (!isSpawned()) {
            setSkinValues(getSkin(name == null ? getName() : name));
        } else {
            NMSMain.stacktrace("Can't change the skin of an already existing fake player");
        }
        return this;
    }

    @Override
    public String toString() {
        return "FakePlayer{" +
                "profile=" + profile +
                ", server=" + server +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", worldServer=" + worldServer +
                ", skinValues=" + Arrays.toString(skinValues) +
                ", spawned=" + spawned +
                ", player=" + player +
                ", receivers=" + receivers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FakePlayer that = (FakePlayer) o;
        return spawned == that.spawned &&
                Objects.equals(profile, that.profile) &&
                Objects.equals(server, that.server) &&
                Objects.equals(name, that.name) &&
                Objects.equals(location, that.location) &&
                Objects.equals(worldServer, that.worldServer) &&
                Arrays.equals(skinValues, that.skinValues) &&
                Objects.equals(player, that.player) &&
                Objects.equals(receivers, that.receivers);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(profile, server, name, location, worldServer, spawned, player, receivers);
        result = 31 * result + Arrays.hashCode(skinValues);
        return result;
    }
}
