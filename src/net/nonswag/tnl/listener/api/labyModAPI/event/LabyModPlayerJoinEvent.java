package net.nonswag.tnl.listener.api.labyModAPI.event;

import net.nonswag.tnl.listener.api.labyModAPI.Addon;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import java.util.List;

public class LabyModPlayerJoinEvent extends Event {

    private final static HandlerList handlerList = new HandlerList();

    private final Player player;
    private final String modVersion;
    private final boolean chunkCachingEnabled;
    private final int chunkCachingVersion;
    private final List<Addon> addons;

    public LabyModPlayerJoinEvent(Player player, String modVersion, boolean chunkCachingEnabled, int chunkCachingVersion, List<Addon> addons) {
        this.player = player;
        this.modVersion = modVersion;
        this.chunkCachingEnabled = chunkCachingEnabled;
        this.chunkCachingVersion = chunkCachingVersion;
        this.addons = addons;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public boolean isChunkCachingEnabled() {
        return chunkCachingEnabled;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public List<Addon> getAddons() {
        return addons;
    }

    public String getModVersion() {
        return modVersion;
    }

    public Player getPlayer() {
        return player;
    }

    public int getChunkCachingVersion() {
        return chunkCachingVersion;
    }
}
