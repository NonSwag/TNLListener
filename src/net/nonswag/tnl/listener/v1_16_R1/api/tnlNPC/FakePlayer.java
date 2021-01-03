package net.nonswag.tnl.listener.v1_16_R1.api.tnlNPC;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R1.*;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.v1_16_R1.utils.PacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class FakePlayer {

    private GameProfile profile;
    private boolean showUpInTab = true;
    private final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
    private String name;
    private Location location;
    private WorldServer worldServer;
    private String[] skinValues = {"", ""};
    private boolean spawned = false;
    private EntityPlayer player = null;
    private List<Player> receivers = new ArrayList<>();

    public FakePlayer(String name, Location location, Player... receivers) {
        new FakePlayer(name, location, Arrays.asList(receivers));
    }

    public FakePlayer(String name, Location location, List<Player> receivers) {
        setName(name);
        setLocation(location);
        setReceivers(receivers);
        setWorldServer(((CraftWorld) getLocation().getWorld()).getHandle());
    }

    public List<Player> getReceivers() {
        return receivers;
    }

    public FakePlayer setReceivers(List<Player> receivers) {
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

    public boolean isShowUpInTab() {
        return showUpInTab;
    }

    public String getName() {
        return name;
    }

    public FakePlayer setName(String name) {
        this.name = name;
        return this;
    }

    public FakePlayer setShowUpInTab(boolean showUpInTab) {
        this.showUpInTab = showUpInTab;
        if (isSpawned() && !isShowUpInTab()) {
            for (Player receiver : getReceivers()) {
                PacketUtil.sendPacket(receiver, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, getPlayer()));
            }
        }
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
                for (Player receiver : getReceivers()) {
                    PacketUtil.sendPacket(receiver, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, getPlayer()));
                    PacketUtil.sendPacket(receiver, new PacketPlayOutNamedEntitySpawn(getPlayer()));
                    PacketUtil.sendPacket(receiver, new PacketPlayOutEntityHeadRotation(getPlayer(), ((byte) (getPlayer().yaw * 256 / 360))));
                    if (!isShowUpInTab()) {
                        NMSMain.delayedTask(() -> PacketUtil.sendPacket(receiver, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, getPlayer())), 10);
                    }
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
            for (Player receiver : getReceivers()) {
                PacketUtil.sendPacket(receiver, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, getPlayer()));
                PacketUtil.sendPacket(receiver, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, getPlayer()));
                PacketUtil.sendPacket(receiver, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_GAME_MODE, getPlayer()));
                PacketUtil.sendPacket(receiver, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY, getPlayer()));
            }
            setSpawned(false);
        } else {
            NMSMain.stacktrace("Can't deSpawn an not existing fake player");
        }
    }

    public void spawn(Player receiver) {
        try {
            if (getSkinValues()[0].equalsIgnoreCase("")) {
                try {
                    setSkin(getName());
                } catch (Throwable ignored) {
                }
            }
            PacketUtil.sendPacket(receiver, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, player));
            PacketUtil.sendPacket(receiver, new PacketPlayOutNamedEntitySpawn(getPlayer()));
            PacketUtil.sendPacket(receiver, new PacketPlayOutEntityHeadRotation(getPlayer(), ((byte) (getPlayer().yaw * 256 / 360))));
            if (!isShowUpInTab()) {
                NMSMain.delayedTask(() -> PacketUtil.sendPacket(receiver, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, getPlayer())), 10);
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FakePlayer that = (FakePlayer) o;
        return showUpInTab == that.showUpInTab &&
                spawned == that.spawned &&
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
        int result = Objects.hash(profile, showUpInTab, server, name, location, worldServer, spawned, player, receivers);
        result = 31 * result + Arrays.hashCode(skinValues);
        return result;
    }

    @Override
    public String toString() {
        return "FakePlayer{" +
                "profile=" + profile +
                ", showUpInTab=" + showUpInTab +
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
}
