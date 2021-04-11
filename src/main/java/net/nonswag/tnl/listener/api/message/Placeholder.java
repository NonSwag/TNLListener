package net.nonswag.tnl.listener.api.message;

import net.nonswag.tnl.listener.api.logger.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Placeholder {

    public static class Registry {

        @Nonnull
        private static final HashMap<String, String> PLACEHOLDERS = new HashMap<>();

        @Nonnull
        public static List<String> values() {
            return new ArrayList<>(PLACEHOLDERS.keySet());
        }

        public static boolean isRegistered(@Nonnull String placeholder) {
            return PLACEHOLDERS.containsKey(placeholder);
        }

        public static void register(@Nonnull Placeholder placeholder) {
            if (!PLACEHOLDERS.containsKey(placeholder.getPlaceholder())) {
                PLACEHOLDERS.put(placeholder.getPlaceholder(), placeholder.getObject().toString());
                Logger.debug.println("§aRegistered static placeholder §8'§6" + placeholder.getPlaceholder() + "§8'§a with value §8'§f" + placeholder.getObject().toString() + "§8'");
            } else {
                Logger.debug.println("§cA static placeholder named §8'§4" + placeholder.getPlaceholder() + "§8'§c is already registered");
            }
        }

        public static void unregister(@Nonnull String placeholder) {
            if (PLACEHOLDERS.containsKey(placeholder)) {
                PLACEHOLDERS.remove(placeholder);
                Logger.debug.println("§aUnregistered static placeholder §8'§6" + placeholder + "§8'");
            } else {
                Logger.error.println("§cA static placeholder named §8'§4" + placeholder + "§8'§c is not registered");
            }
        }

        public static void updateValue(@Nonnull Placeholder placeholder) {
            if (PLACEHOLDERS.containsKey(placeholder.getPlaceholder())) {
                PLACEHOLDERS.put(placeholder.getPlaceholder(), placeholder.getObject().toString());
                Logger.debug.println("§aUpdated static placeholder §8'§6" + placeholder.getPlaceholder() + "§8'", "§aChanged value to §8'§6" + placeholder.getObject().toString() + "§8'");
            } else {
                Logger.error.println("§cA static placeholder named §8'§4" + placeholder + "§8'§c is not registered");
            }
        }

        @Nullable
        public static Placeholder valueOf(@Nonnull String placeholder) {
            if (isRegistered(placeholder)) {
                return new Placeholder(placeholder, PLACEHOLDERS.get(placeholder));
            } else {
                return null;
            }
        }

        static {
            register(new Placeholder("prefix", Message.PREFIX.getText()));
            register(new Placeholder("nl", "\n"));
        }
    }

    @Nonnull private final String placeholder;
    @Nonnull private final Object object;

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

    @Override
    public String toString() {
        return "Placeholder{" +
                "placeholder='" + placeholder + '\'' +
                ", object=" + object +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Placeholder that = (Placeholder) o;
        return placeholder.equals(that.placeholder) && object.equals(that.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeholder, object);
    }
}