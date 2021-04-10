package net.nonswag.tnl.listener.api.sign;

import net.nonswag.tnl.listener.api.object.Objects;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.function.BiPredicate;

public class SignMenu {

    @Nonnull
    private final Location location;
    @Nonnull
    private final TNLPlayer viewer;
    @Nonnull
    private final String[] lines;
    @Nonnull
    private final Objects<BiPredicate<TNLPlayer, String[]>> response = new Objects<>();
    @Nonnull
    private Type type = Type.DARK_OAK_WALL_SIGN;
    private boolean reopenOnFail = false;

    public SignMenu(@Nonnull Location location, @Nonnull TNLPlayer viewer, @Nonnull String... lines) {
        this.location = location;
        this.viewer = viewer;
        this.lines = lines;
    }

    @Nonnull
    public Location getLocation() {
        return location;
    }

    @Nonnull
    public TNLPlayer getViewer() {
        return viewer;
    }

    @Nonnull
    public String[] getLines() {
        return lines;
    }

    @Nonnull
    public Objects<BiPredicate<TNLPlayer, String[]>> getResponse() {
        return response;
    }

    @Nonnull
    public Type getType() {
        return type;
    }

    public void setType(@Nonnull Type type) {
        this.type = type;
    }

    @Nonnull
    public SignMenu response(@Nonnull BiPredicate<TNLPlayer, String[]> response) {
        getResponse().setValue(response);
        return this;
    }

    public void setReopenOnFail(boolean reopenOnFail) {
        this.reopenOnFail = reopenOnFail;
    }

    public boolean isReopenOnFail() {
        return reopenOnFail;
    }

    public enum Type {
        SPRUCE_WALL_SIGN,
        DARK_OAK_WALL_SIGN,
        BIRCH_WALL_SIGN,
        ACACIA_WALL_SIGN,
        JUNGLE_WALL_SIGN,
        OAK_WALL_SIGN,
        WARPED_WALL_SIGN,
        CRIMSON_WALL_SIGN,
        SPRUCE_SIGN,
        DARK_OAK_SIGN,
        BIRCH_SIGN,
        ACACIA_SIGN,
        JUNGLE_SIGN,
        OAK_SIGN,
        WARPED_SIGN,
        CRIMSON_SIGN
    }
}
