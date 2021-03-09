package net.nonswag.tnl.listener.api.reflection;

import com.google.gson.JsonObject;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.object.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Reflection {

    @Nullable
    public static Class<?> getClass(@Nonnull String link) {
        try {
            return Class.forName(link);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static void setField(@Nonnull Object clazz, @Nonnull String name, @Nullable Object value) {
        try {
            Field field = clazz.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(clazz, value);
            field.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Logger.error.println(e);
        }
    }

    public static void setField(@Nonnull Object clazz, @Nonnull Class<?> superclass, @Nonnull String name, @Nullable Object value) {
        try {
            Field field = superclass.getDeclaredField(name);
            field.setAccessible(true);
            field.set(clazz, value);
            field.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Logger.error.println(e);
        }
    }

    public static void setStaticFinalField(@Nonnull Class<?> clazz, @Nonnull String name, @Nullable Object value) {
        try {
            Field field = clazz.getDeclaredField(name);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            field.setAccessible(true);
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & 0xFFFFFFEF);
            field.set(null, value);
            modifiers.setAccessible(!modifiers.isAccessible());
            field.setAccessible(!field.isAccessible());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Logger.error.println(e);
        }
    }

    @Nullable
    public static Object getField(@Nonnull Object clazz, @Nonnull String field) {
        try {
            Field declaredField = clazz.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);
            return declaredField.get(clazz);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return null;
        }
    }

    @Nonnull
    public static JsonObject toJsonObject(@Nonnull Object clazz) {
        JsonObject object = new JsonObject();
        JsonObject fields = new JsonObject();
        for (String field : getFields(clazz.getClass())) {
            Object o = getField(clazz, field);
            if (o instanceof Set) {
                Set<?, ?> set = (Set<?, ?>) o;
                if ((set.getValue() instanceof String)) {
                    fields.addProperty(set.getKey().toString(), ((String) set.getValue()));
                } else if ((set.getValue() instanceof Number)) {
                    fields.addProperty(set.getKey().toString(), ((Number) set.getValue()));
                } else if ((set.getValue() instanceof Boolean)) {
                    fields.addProperty(set.getKey().toString(), ((Boolean) set.getValue()));
                } else if ((set.getValue() instanceof Character)) {
                    fields.addProperty(set.getKey().toString(), ((Character) set.getValue()));
                }
            } else {
                fields.addProperty(field, o == null ? "" : o.toString());
            }
        }
        object.add(clazz.getClass().getSimpleName(), fields);
        return object;
    }

    @Nonnull
    public static List<String> getFields(@Nonnull Class<?> clazz) {
        List<String> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            fields.add(field.getName());
        }
        return fields;
    }
}
