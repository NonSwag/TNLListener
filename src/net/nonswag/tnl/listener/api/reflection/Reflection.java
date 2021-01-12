package net.nonswag.tnl.listener.api.reflection;

import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Reflection {

    public static Class<?> getClass(String link) {
        try {
            return Class.forName(link);
        } catch (Throwable t) {
            return null;
        }
    }

    public static void setField(Object clazz, String name, Object value) {
        try {
            Field field = clazz.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(clazz, value);
            field.setAccessible(false);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void setField(Object clazz, Class<?> superclass, String name, Object value) {
        try {
            Field field = superclass.getDeclaredField(name);
            field.setAccessible(true);
            field.set(clazz, value);
            field.setAccessible(false);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void setStaticFinalField(Class<?> clazz, String name, Object value) {
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

    public static Object getField(Object clazz, String field) {
        try {
            Field declaredField = clazz.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);
            return declaredField.get(clazz);
        } catch (Throwable t) {
            return null;
        }
    }

    public static JSONObject toJsonObject(Object clazz) {
        JSONObject jsonObject = new JSONObject();
        for (String field : getFields(clazz.getClass())) {
            jsonObject.put(field, getField(clazz, field));
        }
        return jsonObject;
    }

    public static List<String> getFields(Class<?> clazz) {
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
