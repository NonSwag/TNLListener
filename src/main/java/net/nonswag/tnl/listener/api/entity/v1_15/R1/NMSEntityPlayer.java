package net.nonswag.tnl.listener.api.entity.v1_15.R1;

import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.nonswag.tnl.listener.api.entity.TNLEntityPlayer;
import net.nonswag.tnl.listener.api.player.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;

import javax.annotation.Nonnull;
import java.util.Objects;

public class NMSEntityPlayer extends EntityPlayer implements TNLEntityPlayer {

    @Nonnull
    private final GameProfile gameProfile;

    public NMSEntityPlayer(@Nonnull World world, double x, double y, double z, float yaw, float pitch, @Nonnull GameProfile profile) {
        super(((CraftServer) Bukkit.getServer()).getServer(), ((CraftWorld) world).getHandle(),
                new com.mojang.authlib.GameProfile(profile.getUniqueId(), profile.getName()),
                new PlayerInteractManager(((CraftWorld) world).getHandle()));
        super.getProfile().getProperties().put("textures", new Property("textures", profile.getSkin().getValue(), profile.getSkin().getSignature()));
        super.getDataWatcher().set(DataWatcherRegistry.a.a(16), (byte) 127);
        this.gameProfile = profile;
    }

    public NMSEntityPlayer(@Nonnull World world, double x, double y, double z, @Nonnull GameProfile profile) {
        this(world, x, y, z, 0, 0, profile);
    }

    public NMSEntityPlayer(@Nonnull Location location, @Nonnull GameProfile profile) {
        this(Objects.requireNonNull(location.getWorld()), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), profile);
    }

    @Nonnull
    @Override
    public CraftPlayer getBukkitEntity() {
        return super.getBukkitEntity();
    }

    @Override
    public void setPing(int ping) {
        super.ping = ping;
    }

    @Override
    public int getPing() {
        return super.ping;
    }

    @Override
    @Nonnull
    public GameProfile getGameProfile() {
        return gameProfile;
    }
}
