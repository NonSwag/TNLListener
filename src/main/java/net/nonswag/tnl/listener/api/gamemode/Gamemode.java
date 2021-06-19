package net.nonswag.tnl.listener.api.gamemode;

import org.bukkit.GameMode;

import javax.annotation.Nonnull;

public enum Gamemode {
    UNKNOWN("Unknown", -1),
    SURVIVAL("Survival", 0),
    CREATIVE("Creative", 1),
    ADVENTURE("Adventure", 2),
    SPECTATOR("Spectator", 3);

    @Nonnull
    private final String name;
    private final int id;

    Gamemode(@Nonnull String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isUnknown() {
        return equals(UNKNOWN);
    }

    public boolean isSurvival() {
        return equals(SURVIVAL);
    }

    public boolean isCreative() {
        return equals(CREATIVE);
    }

    public boolean isAdventure() {
        return equals(ADVENTURE);
    }

    public boolean isSpectator() {
        return equals(SPECTATOR);
    }

    @Nonnull
    public GameMode bukkit() {
        if (equals(Gamemode.CREATIVE)) return GameMode.CREATIVE;
        else if (equals(Gamemode.ADVENTURE)) return GameMode.ADVENTURE;
        else if (equals(Gamemode.SPECTATOR)) return GameMode.SPECTATOR;
        return GameMode.SURVIVAL;
    }

    @Override
    public String toString() {
        return "Gamemode{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    @Nonnull
    public static Gamemode cast(@Nonnull GameMode bukkit) {
        if (bukkit.equals(GameMode.CREATIVE)) return CREATIVE;
        else if (bukkit.equals(GameMode.ADVENTURE)) return ADVENTURE;
        else if (bukkit.equals(GameMode.SPECTATOR)) return SPECTATOR;
        return SURVIVAL;
    }

    @Nonnull
    public static Gamemode cast(@Nonnull String string) {
        for (Gamemode mode : values()) {
            if (mode.name().toLowerCase().startsWith(string.toLowerCase()) ||
                    String.valueOf(mode.getId()).equals(string)) return mode;
        }
        return UNKNOWN;
    }

    @Nonnull
    public static Gamemode cast(int i) {
        for (Gamemode mode : values()) if (mode.getId() == i) return mode;
        return UNKNOWN;
    }
}
