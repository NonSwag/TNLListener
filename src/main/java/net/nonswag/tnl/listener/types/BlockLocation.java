package net.nonswag.tnl.listener.types;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockLocation {

    @Nullable
    private final World world;
    private final int x, y, z;
    @Nonnull
    private final Location location;

    public BlockLocation(String world, int x, int y, int z) {
        this.world = Bukkit.getWorld(world);
        this.x = x;
        this.y = y;
        this.z = z;
        this.location = new Location(getWorld(), getX(), getY(), getZ());
    }

    @Nullable
    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Nonnull
    public Location getLocation() {
        return location;
    }
}
