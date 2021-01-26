package net.nonswag.tnl.listener.v1_15_R1.api.fakeplayer;

import com.google.common.annotations.Beta;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_15_R1.*;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.api.mojang.Mojang;
import net.nonswag.tnl.listener.api.mojang.PlayerProfile;
import net.nonswag.tnl.listener.v1_15_R1.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

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
        this.profile.getProperties().put("textures", new Property("textures", skinValues[0], skinValues[1]));
    }

    public void setSkin(@Nonnull String name) {
        setSkinValues(getSkin(name));
        NMSMain.warn("Please use the properties '" + Arrays.toString(getSkinValues()) + "' instead of the name '" + name + "'");
    }

    public void setSkin(@Nonnull String value, @Nonnull String signature) {
        setSkinValues(new String[]{value, signature});
    }

    @Beta
    public void setSkin(@Nonnull String skin,
                        @Nonnull String cape,
                        @Nonnull Mojang.SkinType skinType,
                        @Nonnull String signature) {
        setSkin(PlayerProfile.TexturesProperty.createBase64Profile(skin, cape, skinType), signature);
    }

    @Beta
    public void setSkin(@Nonnull PlayerProfile.TexturesProperty texturesProperty) {
        setSkin(texturesProperty.toBase64(), texturesProperty.getSignature());
        if (texturesProperty.getSkin().isPresent()) {
            NMSMain.warn("Please use the static link '" + texturesProperty.getSkin().get().toString() + "' for the skin");
        }
        if (texturesProperty.getCape().isPresent()) {
            NMSMain.warn("Please use the static link '" + texturesProperty.getCape().get().toString() + "' for the cape");
        }
    }

    public void spawn() {
        for (TNLPlayer receiver : getReceivers()) {
            spawn(receiver);
        }
    }

    public void spawn(@Nonnull TNLPlayer receiver) {
        getPlayer().getDataWatcher().set(DataWatcherRegistry.a.a(16), (byte) 127);
        receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, getPlayer()));
        receiver.sendPacket(new PacketPlayOutNamedEntitySpawn(getPlayer()));
        receiver.sendPacket(new PacketPlayOutEntityMetadata(getPlayer().getId(), getPlayer().getDataWatcher(), true));
        receiver.sendPacket(new PacketPlayOutEntityHeadRotation(getPlayer(), (byte) (getPlayer().yaw * 256 / 360)));
        for (EnumItemSlot slot : EnumItemSlot.values()) {
            receiver.sendPacket(new PacketPlayOutEntityEquipment(getPlayer().getId(), slot, getPlayer().getEquipment(slot)));
        }
    }

    public void playAnimate(@Nonnull TNLPlayer receiver, @Nonnull Animation animation) {
        receiver.sendPacket(new PacketPlayOutAnimation(getPlayer(), animation.getId()));
    }

    public void playStatus(@Nonnull TNLPlayer receiver, @Nonnull Status status) {
        receiver.sendPacket(new PacketPlayOutEntityStatus(getPlayer(), status.getId()));
    }

    public void addEffect(@Nonnull TNLPlayer receiver, @Nonnull PotionEffect effect) {
        receiver.sendPacket(new PacketPlayOutEntityEffect(getPlayer().getId(),
                new MobEffect(MobEffectList.fromId(effect.getType().getId()),
                        effect.getDuration(),
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.hasParticles(),
                        effect.hasIcon())));
    }

    public void removeEffect(@Nonnull TNLPlayer receiver, @Nonnull PotionEffectType effect) {
        receiver.sendPacket(new PacketPlayOutRemoveEntityEffect(getPlayer().getId(), MobEffectList.fromId(effect.getId())));
    }

    public void setVelocity(@Nonnull TNLPlayer receiver, @Nonnull Vector vector) {
        receiver.sendPacket(new PacketPlayOutEntityVelocity(getPlayer().getId(), new Vec3D(vector.getX(), vector.getY(), vector.getZ())));
    }

    public void deSpawn() {
        for (TNLPlayer receiver : getReceivers()) {
            deSpawn(receiver);
        }
    }

    public void deSpawn(@Nonnull TNLPlayer receiver) {
        receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, getPlayer()));
        receiver.sendPacket(new PacketPlayOutEntityDestroy(getPlayer().getId()));
    }

    @Nonnull
    private String[] getSkin(@Nonnull String player) {
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
        BURNING(1),
        CROUCHING(2),
        HIDDEN(32),
        ;

        private final byte id;

        Status(int id) {
            this.id = ((byte) id);
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
}
