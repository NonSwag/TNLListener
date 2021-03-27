package net.nonswag.tnl.listener.types;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "BlockLocation{" +
                "world=" + world +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", location=" + location +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockLocation that = (BlockLocation) o;
        return x == that.x && y == that.y && z == that.z && Objects.equals(world, that.world) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z, location);
    }
}
