package net.nonswag.tnl.listener.api.reflection;

import com.google.gson.JsonObject;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.object.Objects;
import net.nonswag.tnl.listener.api.object.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
            modifiers.setAccessible(false);
            field.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Logger.error.println(e);
        }
    }

    @Nonnull
    public static Objects<Method> getMethod(@Nonnull Object clazz, @Nonnull String method, @Nullable Class<?>... parameters) {
        try {
            Method declaredMethod = clazz.getClass().getDeclaredMethod(method, parameters);
            declaredMethod.setAccessible(true);
            return new Objects<>(declaredMethod);
        } catch (NoSuchMethodException e) {
            Logger.error.println(e);
            return (Objects<Method>) Objects.EMPTY;
        }
    }

    @Nonnull
    public static Objects<?> getField(@Nonnull Object clazz, @Nonnull String field) {
        try {
            Field declaredField = clazz.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);
            return new Objects<>(declaredField.get(clazz));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return Objects.EMPTY;
        }
    }

    @Nonnull
    public static JsonObject toJsonObject(@Nonnull Object clazz) {
        JsonObject object = new JsonObject();
        JsonObject fields = new JsonObject();
        for (String field : getFields(clazz.getClass())) {
            Objects<?> o = getField(clazz, field);
            if (o.getValue() instanceof Pair) {
                Pair<?, ?> pair = (Pair<?, ?>) o.getValue();
                if ((pair.getValue() instanceof String)) {
                    fields.addProperty(pair.getKey().toString(), ((String) pair.getValue()));
                } else if ((pair.getValue() instanceof Number)) {
                    fields.addProperty(pair.getKey().toString(), ((Number) pair.getValue()));
                } else if ((pair.getValue() instanceof Boolean)) {
                    fields.addProperty(pair.getKey().toString(), ((Boolean) pair.getValue()));
                } else if ((pair.getValue() instanceof Character)) {
                    fields.addProperty(pair.getKey().toString(), ((Character) pair.getValue()));
                }
            } else {
                fields.addProperty(field, o.hasValue() ? o.nonnull().toString() : "");
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
