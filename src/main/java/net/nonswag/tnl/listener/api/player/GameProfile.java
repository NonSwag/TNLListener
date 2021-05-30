package net.nonswag.tnl.listener.api.player;

import javax.annotation.Nonnull;
import java.util.UUID;

public class GameProfile {

    @Nonnull
    private final UUID uniqueId;
    @Nonnull
    private final String name;
    @Nonnull
    private Skin skin;

    public GameProfile(@Nonnull UUID uniqueId, @Nonnull String name, @Nonnull Skin skin) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.skin = skin;
    }

    public GameProfile(@Nonnull UUID uniqueId, @Nonnull String name) {
        this(uniqueId, name, new Skin("", ""));
    }

    public GameProfile(@Nonnull String name) {
        this(UUID.randomUUID(), name);
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Nonnull
    public Skin getSkin() {
        return skin;
    }

    public void setSkin(@Nonnull Skin skin) {
        this.skin = skin;
    }
}
