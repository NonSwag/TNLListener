package net.nonswag.tnl.listener.api.holograms;

import net.nonswag.tnl.listener.TNLListener;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Option {
    LINE_DISTANCE(Double.class),
    X_POSITION(Double.class),
    Y_POSITION(Double.class),
    Z_POSITION(Double.class),
    DARKNESS(Integer.class, "1", "2", "3", "4", "5"),
    LINES(String.class),
    WORLD(World.class, TNLListener.getInstance().getWorlds()),
    ;

    @Nonnull private final String name;
    @Nonnull private final Class<?> clazz;
    @Nonnull private final String type;
    @Nonnull private final List<String> values;

    Option(@Nonnull Class<?> clazz, @Nonnull List<String> values) {
        this.name = name().toLowerCase();
        this.clazz = clazz;
        this.type = clazz.getSimpleName();
        this.values = values;
    }

    Option(@Nonnull Class<?> clazz, @Nullable String... values) {
        this.name = name().toLowerCase();
        this.clazz = clazz;
        this.type = clazz.getSimpleName();
        this.values = Arrays.asList(values);
    }

    Option(@Nonnull Class<?> clazz) {
        this.name = name().toLowerCase();
        this.clazz = clazz;
        this.type = clazz.getSimpleName();
        this.values = new ArrayList<>();
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public Class<?> getClazz() {
        return clazz;
    }

    @Nonnull
    public String getType() {
        return type;
    }

    @Nonnull
    public List<String> getValues() {
        return values;
    }
}
