package net.nonswag.tnl.listener.api.fakeplayer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_15_R1.*;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.util.Vector;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class FakePlayer {

    private final String name;
    private final Location location;
    private final TNLPlayer[] receivers;
    private final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
    private final WorldServer worldServer;
    private final GameProfile profile;
    private final EntityPlayer player;
    private String[] skinValues = {"", ""};

    public FakePlayer(String name, Location location, TNLPlayer... receivers) {
        this.name = name;
        this.location = location;
        this.receivers = receivers;
        this.worldServer = ((CraftWorld) Objects.requireNonNull(getLocation().getWorld())).getHandle();
        this.profile = new GameProfile(UUID.randomUUID(), getName());
        this.player = new EntityPlayer(getServer(), getWorldServer(), getProfile(), new PlayerInteractManager(getWorldServer()));
        this.player.setLocation(getLocation().getX(), getLocation().getY(), getLocation().getZ(), getLocation().getYaw(), getLocation().getPitch());
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public TNLPlayer[] getReceivers() {
        return receivers;
    }

    public MinecraftServer getServer() {
        return server;
    }

    public WorldServer getWorldServer() {
        return worldServer;
    }

    public GameProfile getProfile() {
        return profile;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public String[] getSkinValues() {
        return skinValues;
    }

    public void setSkinValues(String[] skinValues) {
        this.skinValues = skinValues;
        this.profile.getProperties().put("textures", new Property("textures", skinValues[0], skinValues[1]));
    }

    public void setSkin(String name) {
        setSkinValues(getSkin(name));
        NMSMain.warn("Please use the properties '" + Arrays.toString(getSkinValues()) + "' instead of the name '" + name + "'");
    }

    public void setSkin(String value, String signature) {
        setSkinValues(new String[]{value, signature});
    }

    public void spawn() {
        for (TNLPlayer receiver : getReceivers()) {
            spawn(receiver);
        }
    }

    public void spawn(TNLPlayer receiver) {
        getPlayer().getDataWatcher().set(DataWatcherRegistry.a.a(16), (byte) 127);
        getPlayer().ping = 32;
        receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, getPlayer()));
        receiver.sendPacket(new PacketPlayOutNamedEntitySpawn(getPlayer()));
        receiver.sendPacket(new PacketPlayOutEntityMetadata(getPlayer().getId(), getPlayer().getDataWatcher(), true));
        receiver.sendPacket(new PacketPlayOutEntityHeadRotation(getPlayer(), (byte) (getPlayer().yaw * 256 / 360)));
        for (EnumItemSlot slot : EnumItemSlot.values()) {
            receiver.sendPacket(new PacketPlayOutEntityEquipment(getPlayer().getId(), slot, getPlayer().getEquipment(slot)));
        }
        NMSMain.delayedTask(() -> hideTablistName(receiver), 10);
    }

    public void playAnimate(TNLPlayer receiver, Animation animation) {
        receiver.sendPacket(new PacketPlayOutAnimation(getPlayer(), animation.getId()));
    }

    public void playStatus(TNLPlayer receiver, Status status) {
        receiver.sendPacket(new PacketPlayOutEntityStatus(getPlayer(), status.getId()));
    }

    public void setVelocity(TNLPlayer receiver, Vector vector) {
        receiver.sendPacket(new PacketPlayOutEntityVelocity(getPlayer().getId(), new Vec3D(vector.getX(), vector.getY(), vector.getZ())));
    }

    public void hideTablistName(TNLPlayer receiver) {
        receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, getPlayer()));
    }

    public void showTablistName(TNLPlayer receiver) {
        receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, getPlayer()));
    }

    public void deSpawn() {
        for (TNLPlayer receiver : getReceivers()) {
            deSpawn(receiver);
        }
    }

    public void deSpawn(TNLPlayer receiver) {
        receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, getPlayer()));
        receiver.sendPacket(new PacketPlayOutEntityDestroy(getPlayer().getId()));
    }

    private String[] getSkin(String player) {
        String value = "";
        String signature = "";
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + player);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
            URL url1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader1 = new InputStreamReader(url1.openStream());
            JsonObject property = new JsonParser().parse(reader1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            value = property.get("value").getAsString();
            signature = property.get("signature").getAsString();
        } catch (Throwable ignored) {
        }
        return new String[]{value, signature};
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

    public enum Animation {
        SWING_HAND(0),
        SWING_OFFHAND(3),
        NORMAL_DAMAGE(1),
        CRITICAL_DAMAGE(4),
        MAGICAL_DAMAGE(5),
        ;

        private final int id;

        Animation(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Animation{" +
                    "id=" + id +
                    '}';
        }
    }

    public enum Status {
        BURNING((byte) 0x01),
        CROUCHING((byte) 0x02),
        SPRINTING((byte) 0x08),
        SWIMMING((byte) 0x10),
        INVISIBLE((byte) 0x20),
        GLOWING((byte) 0x40),
        ELYTRA_FLY((byte) 0x80),
        ;

        private final byte id;

        Status(byte id) {
            this.id = id;
        }

        public byte getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Status{" +
                    "id=" + id +
                    '}';
        }
    }

    public enum Pose {
        STANDING(0),
        FALL_FLYING(1),
        SLEEPING(2),
        SWIMMING(3),
        SPIN_ATTACK(4),
        SNEAKING(5),
        DYING(6),
        ;

        private final int id;

        Pose(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Pose{" +
                    "id=" + id +
                    '}';
        }
    }
}
