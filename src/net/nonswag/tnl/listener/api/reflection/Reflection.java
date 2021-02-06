package net.nonswag.tnl.listener.api.reflection;

import org.json.simple.JSONObject;

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
        } catch (Throwable t) {
            return null;
        }
    }

    public static void setField(@Nonnull Object clazz,
                                @Nonnull String name,
                                @Nullable Object value) {
        try {
            Field field = clazz.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(clazz, value);
            field.setAccessible(false);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void setField(@Nonnull Object clazz,
                                @Nonnull Class<?> superclass,
                                @Nonnull String name,
                                @Nullable Object value) {
        try {
            Field field = superclass.getDeclaredField(name);
            field.setAccessible(true);
            field.set(clazz, value);
            field.setAccessible(false);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void setStaticFinalField(@Nonnull Class<?> clazz,
                                           @Nonnull String name,
                                           @Nullable Object value) {
        try {
            Field field = clazz.getDeclaredField(name);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            field.setAccessible(true);
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & 0xFFFFFFEF);
            field.set(null, value);
            modifiers.setAccessible(!modifiers.isAccessible());
            field.setAccessible(!field.isAccessible());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Nullable
    public static Object getField(@Nonnull Object clazz,
                                  @Nonnull String field) {
        try {
            Field declaredField = clazz.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);
            return declaredField.get(clazz);
        } catch (Throwable t) {
            return null;
        }
    }

    @Nonnull
    public static JSONObject toJsonObject(@Nonnull Object clazz) {
        JSONObject object = new JSONObject();
        JSONObject fields = new JSONObject();
        for (String field : getFields(clazz.getClass())) {
            fields.put(field, getField(clazz, field));
        }
        object.put(clazz.getClass().getSimpleName(), fields);
        return object;
    }

    @Nonnull
    public static List<String> getFields(@Nonnull Class<?> clazz) {
        List<String> fields = new ArrayList<>();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                fields.add(field.getName());
            }
        } catch (Throwable ignored) {
        }
        return fields;
    }
}
