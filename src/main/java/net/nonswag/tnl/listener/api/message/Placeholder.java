package net.nonswag.tnl.listener.api.message;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Placeholder {

    public static class Registry {

        @Nonnull
        private static final HashMap<String, String> PLACEHOLDERS = new HashMap<>();
        @Nonnull
        private static final List<Formulary> FORMULARY = new ArrayList<>();

        @Nonnull
        static List<String> values() {
            return new ArrayList<>(PLACEHOLDERS.keySet());
        }

        @Nonnull
        static List<Formulary> formularies() {
            return new ArrayList<>(FORMULARY);
        }

        public static boolean isRegistered(@Nonnull String placeholder) {
            return PLACEHOLDERS.containsKey(placeholder);
        }

        public static void register(@Nonnull Placeholder placeholder) {
            if (!PLACEHOLDERS.containsKey(placeholder.getPlaceholder())) {
                PLACEHOLDERS.put(placeholder.getPlaceholder(), placeholder.getObject().toString());
            } else {
                Logger.error.println("A static placeholder named <'" + placeholder.getPlaceholder() + "'> is already registered");
            }
        }

        public static void unregister(@Nonnull String placeholder) {
            if (PLACEHOLDERS.containsKey(placeholder)) {
                PLACEHOLDERS.remove(placeholder);
            } else Logger.error.println("A static placeholder named <'" + placeholder + "'> is not registered");
        }

        public static void updateValue(@Nonnull Placeholder placeholder) {
            if (PLACEHOLDERS.containsKey(placeholder.getPlaceholder())) {
                PLACEHOLDERS.put(placeholder.getPlaceholder(), placeholder.getObject().toString());
            } else Logger.error.println("A static placeholder named <'" + placeholder + "'> is not registered");
        }

        @Nullable
        public static Placeholder valueOf(@Nonnull String placeholder) {
            if (isRegistered(placeholder)) return new Placeholder(placeholder, PLACEHOLDERS.get(placeholder));
            else return null;
        }

        static {
            register(new Placeholder("prefix", Message.PREFIX.getText()));
            register(new Placeholder("nl", "\n"));
            register(new Placeholder("server", TNLListener.getInstance().getServerName()));
            register(new Placeholder("max_online", Bukkit.getMaxPlayers()));

            register(player -> new Placeholder("player", player.getName()));
            register(player -> new Placeholder("display_name", player.getDisplayName()));
            register(player -> new Placeholder("custom_name", player.getCustomName()));
            register(player -> new Placeholder("player_list_name", player.getPlayerListName()));
            register(player -> new Placeholder("language", player.getLanguage().getName()));
            register(player -> new Placeholder("world_alias", player.getWorldAlias()));
            register(player -> new Placeholder("world", player.getWorld().getName()));
            register(player -> new Placeholder("health", player.getHealth()));
            register(player -> new Placeholder("max_health", player.getMaxHealth()));
            register(player -> new Placeholder("food_level", player.getFoodLevel()));
            register(player -> new Placeholder("version", player.getVersion().getRecentVersion()));
            register(player -> new Placeholder("gamemode", player.getGameMode().name().toLowerCase()));
        }

        public static void register(@Nonnull Formulary formulary) {
            if (!FORMULARY.contains(formulary)) FORMULARY.add(formulary);
            else Logger.error.println("This dynamic placeholder is already registered");
        }

        public static void unregister(@Nonnull Formulary formulary) {
            if (FORMULARY.contains(formulary)) FORMULARY.remove(formulary);
            else Logger.error.println("This dynamic placeholder is not registered");
        }
    }

    public interface Formulary {
        @Nonnull
        Placeholder check(@Nonnull TNLPlayer player);
    }

    @Nonnull
    private final String placeholder;
    @Nonnull
    private final Object object;

    public Placeholder(@Nonnull String placeholder, @Nullable Object object) {
        this.placeholder = placeholder;
        this.object = (object == null ? "§8-§7/§8-§r" : object);
    }

    @Nonnull
    public String getPlaceholder() {
        return placeholder;
    }

    @Nonnull
    public Object getObject() {
        return object;
    }

    @Nonnull
    public String replace(@Nonnull Object value) {
        return value.toString().replace("%" + getPlaceholder() + "%", getObject().toString());
    }

    @Nonnull
    public static String replace(@Nonnull Object value, @Nonnull Placeholder... placeholders) {
        String string = value.toString();
        for (Placeholder placeholder : placeholders) {
            string = placeholder.replace(string);
        }
        return string;
    }
}
