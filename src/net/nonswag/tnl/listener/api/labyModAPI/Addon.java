package net.nonswag.tnl.listener.api.labyModAPI;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Addon {

    private UUID uuid;
    private String name;

    public static List<Addon> getAddons(JsonObject jsonObject) {
        if (!jsonObject.has("addons") || !jsonObject.get("addons").isJsonArray()) {
            return new ArrayList<>();
        }
        List<Addon> addons = new ArrayList<>();
        for (JsonElement arrayElement : jsonObject.get("addons").getAsJsonArray()) {
            if (!arrayElement.isJsonObject()) {
                continue;
            }
            JsonObject arrayObject = arrayElement.getAsJsonObject();
            if (!arrayObject.has("uuid") || !arrayObject.get("uuid").isJsonPrimitive() || !arrayObject.get("uuid").getAsJsonPrimitive().isString()
                    || !arrayObject.has("name") || !arrayObject.get("name").isJsonPrimitive() || !arrayObject.get("name").getAsJsonPrimitive().isString()) {
                continue;
            }
            UUID uuid;
            try {
                uuid = UUID.fromString(arrayObject.get("uuid").getAsString());
            } catch (IllegalArgumentException ex) {
                continue;
            }
            addons.add(new Addon(uuid, arrayObject.get("name").getAsString()));
        }
        return addons;
    }

    public Addon(UUID uuid, String name) {
        setUuid(uuid);
        setName(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }
}
