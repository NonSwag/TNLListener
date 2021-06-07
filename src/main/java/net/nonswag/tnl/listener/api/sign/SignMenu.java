package net.nonswag.tnl.listener.api.sign;

import net.nonswag.tnl.listener.api.object.Objects;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiPredicate;

public class SignMenu {

    @Nonnull
    private final String[] lines;
    @Nonnull
    private final Objects<BiPredicate<TNLPlayer, String[]>> response = new Objects<>();
    @Nonnull
    private Type type = Type.DARK_OAK_WALL_SIGN;
    @Nullable
    private Location location = null;
    private boolean reopenOnFail = false;

    public SignMenu(@Nonnull String... lines) {
        for (int i = 0; i < lines.length; i++) if (lines[i] == null) lines[i] = "";
        this.lines = lines;
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

    @Nullable
    public Location getLocation() {
        return location;
    }

    public void setLocation(@Nonnull Location location) {
        this.location = location;
    }

    @Nonnull
    public SignMenu response(@Nonnull BiPredicate<TNLPlayer, String[]> response) {
        getResponse().setValue(response);
        return this;
    }

    @Nonnull
    public SignMenu setReopenOnFail(boolean reopenOnFail) {
        this.reopenOnFail = reopenOnFail;
        return this;
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
