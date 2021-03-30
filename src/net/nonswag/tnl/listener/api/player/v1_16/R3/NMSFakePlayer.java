package net.nonswag.tnl.listener.api.player.v1_16.R3;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.v1_16_R3.*;
import net.nonswag.tnl.listener.Loader;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.Skin;
import net.nonswag.tnl.listener.api.player.TNLFakePlayer;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NMSFakePlayer implements TNLFakePlayer<MinecraftServer, WorldServer, EntityPlayer> {

    @Nonnull
    private String name;
    @Nonnull
    private Location location;
    @Nonnull
    private TNLPlayer[] receivers;
    @Nonnull
    private MinecraftServer server;
    @Nonnull
    private WorldServer worldServer;
    @Nonnull
    private GameProfile profile;
    @Nonnull
    private EntityPlayer player;
    @Nonnull
    private Skin skin;

    public NMSFakePlayer(@Nonnull String name, @Nonnull Location location, @Nonnull NMSPlayer... receivers) {
        this.name = name;
        this.location = location;
        this.receivers = receivers;
        this.worldServer = ((CraftWorld) Objects.requireNonNull(getLocation().getWorld())).getHandle();
        this.profile = new GameProfile(UUID.randomUUID(), getName());
        this.server = ((CraftServer) Bukkit.getServer()).getServer();
        this.player = new EntityPlayer(getServer(), getWorldServer(), getProfile(), new PlayerInteractManager(getWorldServer()));
        this.player.setLocation(getLocation().getX(), getLocation().getY(), getLocation().getZ(), getLocation().getYaw(), getLocation().getPitch());
        this.skin = new Skin("", "");
    }

    @Override
    public void setSkin(@Nonnull Skin skin) {
        this.skin = skin;
        this.profile.getProperties().put("textures", new Property("textures", getSkin().getValue(), getSkin().getSignature()));
    }

    @Override
    public void setSkin(@Nonnull String player) {
        setSkin(Skin.getSkin(player));
        Logger.warn.println("Please use the properties '" + getSkin().toString() + "' instead of the name '" + player + "'");
    }

    @Override
    public void spawn() {
        for (TNLPlayer receiver : getReceivers()) {
            spawn(receiver);
        }
    }

    @Override
    public void spawn(@Nonnull TNLPlayer receiver) {
        getPlayer().getDataWatcher().set(DataWatcherRegistry.a.a(16), (byte) 127);
        getPlayer().ping = 32;
        receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, getPlayer()));
        receiver.sendPacket(new PacketPlayOutNamedEntitySpawn(getPlayer()));
        receiver.sendPacket(new PacketPlayOutEntityMetadata(getPlayer().getId(), getPlayer().getDataWatcher(), true));
        receiver.sendPacket(new PacketPlayOutEntityHeadRotation(getPlayer(), (byte) (getPlayer().yaw * 256 / 360)));
        List<Pair<EnumItemSlot, ItemStack>> equipment = new ArrayList<>();
        for (EnumItemSlot slot : EnumItemSlot.values()) {
            equipment.add(new Pair<>(slot, getPlayer().getEquipment(slot)));
        }
        receiver.sendPacket(new PacketPlayOutEntityEquipment(getPlayer().getId(), equipment));
        Bukkit.getScheduler().runTaskLater(Loader.getInstance(), () -> hideTablistName(receiver), 10);
    }

    @Override
    public void playAnimation(@Nonnull TNLPlayer receiver, @Nonnull Animation animation) {
        receiver.sendPacket(new PacketPlayOutAnimation(getPlayer(), animation.getId()));
    }

    @Override
    public void sendStatus(@Nonnull TNLPlayer receiver, @Nonnull Status status) {
        receiver.sendPacket(new PacketPlayOutEntityStatus(getPlayer(), status.getId()));
    }

    @Override
    public void setVelocity(@Nonnull TNLPlayer receiver, @Nonnull Vector vector) {
        receiver.sendPacket(new PacketPlayOutEntityVelocity(getPlayer().getId(), new Vec3D(vector.getX(), vector.getY(), vector.getZ())));
    }

    @Override
    public void hideTablistName(@Nonnull TNLPlayer receiver) {
        receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, getPlayer()));
    }

    @Override
    public void showTablistName(@Nonnull TNLPlayer receiver) {
        receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, getPlayer()));
    }

    @Override
    public void deSpawn() {
        for (TNLPlayer receiver : getReceivers()) {
            deSpawn(receiver);
        }
    }

    @Override
    public void deSpawn(@Nonnull TNLPlayer receiver) {
        receiver.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, getPlayer()));
        receiver.sendPacket(new PacketPlayOutEntityDestroy(getPlayer().getId()));
    }

    @Nonnull
    @Override
    public MinecraftServer getServer() {
        return server;
    }

    @Nonnull
    @Override
    public WorldServer getWorldServer() {
        return worldServer;
    }

    @Nonnull
    @Override
    public EntityPlayer getPlayer() {
        return player;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public Location getLocation() {
        return location;
    }

    @Nonnull
    @Override
    public TNLPlayer[] getReceivers() {
        return receivers;
    }

    @Nonnull
    @Override
    public GameProfile getProfile() {
        return profile;
    }

    @Nonnull
    @Override
    public Skin getSkin() {
        return skin;
    }

    @Override
    public void setServer(@Nonnull MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void setWorldServer(@Nonnull WorldServer worldServer) {
        this.worldServer = worldServer;
    }

    @Override
    public void setPlayer(@Nonnull EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void setName(@Nonnull String name) {
        this.name = name;
    }

    @Override
    public void setLocation(@Nonnull Location location) {
        this.location = location;
    }

    @Override
    public void setReceivers(@Nonnull TNLPlayer... receivers) {
        this.receivers = receivers;
    }

    @Override
    public void setProfile(@Nonnull GameProfile profile) {
        this.profile = profile;
    }
}
