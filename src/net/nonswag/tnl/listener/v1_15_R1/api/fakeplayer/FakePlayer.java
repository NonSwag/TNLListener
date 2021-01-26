package net.nonswag.tnl.listener.v1_15_R1.api.fakeplayer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_15_R1.*;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.v1_15_R1.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;

import javax.annotation.Nonnull;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class FakePlayer {

    @Nonnull private final String name;
    @Nonnull private final Location location;
    @Nonnull private final TNLPlayer[] receivers;
    @Nonnull private final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
    @Nonnull private final WorldServer worldServer;
    @Nonnull private final GameProfile profile;
    @Nonnull private final EntityPlayer player;
    @Nonnull private String[] skinValues = {"", ""};

    public FakePlayer(@Nonnull String name, @Nonnull Location location, @Nonnull TNLPlayer... receivers) {
        this.name = name;
        this.location = location;
        this.receivers = receivers;
        this.worldServer = ((CraftWorld) Objects.requireNonNull(getLocation().getWorld())).getHandle();
        this.profile = new GameProfile(UUID.randomUUID(), getName().isEmpty() ? "§7-§8/§7-" : getName());
        this.player = new EntityPlayer(getServer(), getWorldServer(), getProfile(), new PlayerInteractManager(getWorldServer()));
        this.player.setLocation(getLocation().getX(), getLocation().getY(), getLocation().getZ(), getLocation().getYaw(), getLocation().getPitch());
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public Location getLocation() {
        return location;
    }

    @Nonnull
    public TNLPlayer[] getReceivers() {
        return receivers;
    }

    @Nonnull
    public MinecraftServer getServer() {
        return server;
    }

    @Nonnull
    public WorldServer getWorldServer() {
        return worldServer;
    }

    @Nonnull
    public GameProfile getProfile() {
        return profile;
    }

    @Nonnull
    public EntityPlayer getPlayer() {
        return player;
    }

    @Nonnull
    public String[] getSkinValues() {
        return skinValues;
    }

    public void setSkinValues(@Nonnull String[] skinValues) {
        this.skinValues = skinValues;
        this.profile.getProperties().put("textures", new Property("textures", getSkinValues()[0], getSkinValues()[1]));
    }

    public void setSkin(@Nonnull String name) {
        setSkinValues(getSkin(name));
    }

    public void spawn() {
        for (TNLPlayer receiver : getReceivers()) {
            spawn(receiver);
        }
    }

    public void spawn(@Nonnull TNLPlayer receiver) {
        receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, getPlayer()));
        receiver.sendPacket(new PacketPlayOutNamedEntitySpawn(getPlayer()));
        receiver.sendPacket(new PacketPlayOutEntityHeadRotation(getPlayer(), ((byte) (getPlayer().yaw * 256 / 360))));
    }

    public void deSpawn() {
        for (TNLPlayer receiver : getReceivers()) {
            deSpawn(receiver);
        }
    }

    public void deSpawn(@Nonnull TNLPlayer receiver) {
        receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, getPlayer()));
        receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, getPlayer()));
        receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_GAME_MODE, getPlayer()));
        receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY, getPlayer()));
    }

    @Nonnull
    private String[] getSkin(@Nonnull String player) {
        String texture = "";
        String signature = "";
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + player);
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

    @Override
    public String toString() {
        return "FakePlayer{" +
                "name='" + name + '\'' +
                ", location=" + location +
                ", receivers=" + Arrays.toString(receivers) +
                ", server=" + server +
                ", worldServer=" + worldServer +
                ", profile=" + profile +
                ", player=" + player +
                ", skinValues=" + Arrays.toString(skinValues) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FakePlayer that = (FakePlayer) o;
        return name.equals(that.name) && location.equals(that.location) && Arrays.equals(receivers, that.receivers) && server.equals(that.server) && worldServer.equals(that.worldServer) && profile.equals(that.profile) && player.equals(that.player) && Arrays.equals(skinValues, that.skinValues);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, location, server, worldServer, profile, player);
        result = 31 * result + Arrays.hashCode(receivers);
        result = 31 * result + Arrays.hashCode(skinValues);
        return result;
    }
}
