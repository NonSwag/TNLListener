package net.nonswag.tnl.listener.api.mojangAPI;

import com.google.common.annotations.Beta;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Mojang {

    public static ServiceStatus getStatus(ServiceType serviceType) {
        try {
            return ServiceStatus.valueOf(((String) ((JSONObject) getJSONArray("https://status.mojang.com/check").get(serviceType.ordinal())).get(serviceType.getName())).toUpperCase());
        } catch (Throwable t) {
            return ServiceStatus.RED;
        }
    }

    public String getUuidOfName(String name) {
        return (String) getJSONObject("https://api.mojang.com/users/profiles/minecraft/" + name).get("id");
    }

    public String getUuidOfName(String name, String timestamp) {
        return (String) getJSONObject("https://api.mojang.com/users/profiles/minecraft/" + name + "?at=" + timestamp).get("id");
    }

    public Map<String, Long> getNameHistoryOfPlayer(UUID uuid) {
        JSONArray arr = getJSONArray("https://api.mojang.com/user/profiles/" + uuid + "/names");
        Map<String, Long> history = new HashMap<>();
        arr.forEach(o ->
        {
            JSONObject obj = (JSONObject) o;
            history.put((String) obj.get("name"), obj.get("changedToAt") == null ? 0 : Long.parseLong(obj.get("changedToAt").toString()));
        });
        return history;
    }

    public PlayerProfile getPlayerProfile(UUID uuid) {
        JSONObject obj = getJSONObject("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
        String name = (String) obj.get("name");
        Set<PlayerProfile.Property> properties = (Set<PlayerProfile.Property>) ((JSONArray) obj.get("properties")).stream().map(o -> {
            PlayerProfile.Property p;
            JSONObject prop = (JSONObject) o;
            String propName = (String) prop.get("name");
            String propValue = (String) prop.get("value");
            if (propName.equals("textures")) {
                JSONObject tex;
                try {
                    tex = (JSONObject) new JSONParser().parse(new String(Base64.decodeBase64(propValue)));
                } catch (ParseException e2) {
                    throw new RuntimeException(e2);
                }
                PlayerProfile.TexturesProperty q = new PlayerProfile.TexturesProperty();
                q.timestamp = (Long) tex.get("timestamp");
                q.profileId = (String) tex.get("profileId");
                q.profileName = (String) tex.get("profileName");
                q.signatureRequired = Boolean.parseBoolean((String) tex.get("signatureRequired"));
                q.textures = ((Stream<Entry<Object, Object>>) ((JSONObject) tex.get("textures")).entrySet().stream()).collect(Collectors.toMap(
                        e -> (String) e.getKey(),
                        e -> {
                            try {
                                return new URL((String) ((JSONObject) e.getValue()).get("url"));
                            } catch (MalformedURLException e1) {
                                throw new RuntimeException("Wrapper for checked exception for lambda", e1);
                            }
                        }));
                p = q;
            } else
                p = new PlayerProfile.Property();
            p.name = propName;
            p.signature = (String) prop.get("signature");
            p.value = propValue;
            return p;
        }).collect(Collectors.toSet());
        return new PlayerProfile(uuid, name, properties);
    }

    public void updateSkin(UUID uuid, String token, SkinType skinType, String skinUrl) {
        try {
            Unirest.post("https://api.mojang.com/user/profile/" + uuid + "/skin").header("Authorization", "Bearer " + token).field("model", skinType.toString()).field("url", skinUrl).asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    @Beta
    public void updateAndUpload(UUID uuid, String token, SkinType skinType, String file) {
        try {
            Unirest.put("https://api.mojang.com/user/profile/" + uuid + "/skin").header("Authorization", "Bearer " + token).field("model", skinType.equals(SkinType.DEFAULT) ? "alex" : skinType.toString()).field("file", file).asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public void resetSkin(UUID uuid, String token) {
        try {
            Unirest.delete("https://api.mojang.com/user/profile/" + uuid + "/skin").header("Authorization", "Bearer " + token).asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public List<String> getServerBlacklist() {
        try {
            return Arrays.asList(Unirest.get("https://sessionserver.mojang.com/blockedservers").asString().getBody().split("\n"));
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Beta
    public SalesStats getSaleStatistics(SalesStats.Options... options) {
        JSONArray arr = new JSONArray();
        Collections.addAll(arr, options);
        SalesStats stats = null;
        try {
            JSONObject resp = (JSONObject) new JSONParser().parse(Unirest.post("https://api.mojang.com/orders/statistics").field("metricKeys", arr).asString().getBody());
            stats = new SalesStats(Integer.parseInt((String) resp.get("total")), Integer.parseInt((String) resp.get("last24h")), Integer.parseInt((String) resp.get("saleVelocityPerSeconds")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }

    public enum ServiceStatus {
        RED(),
        YELLOW(),
        GREEN(),
        ;

        ServiceStatus() {
        }
    }

    public enum ServiceType {
        MINECRAFT_NET("minecraft.net"),
        SESSION_MINECRAFT_NET("session.minecraft.net"),
        ACCOUNT_MOJANG_COM("account.mojang.com"),
        AUTHSERVER_MOJANG_COM("authserver.mojang.com"),
        SESSIONSERVER_MOJANG_COM("sessionserver.mojang.com"),
        API_MOJANG_COM("api.mojang.com"),
        TEXTURES_MINECRAFT_NET("textures.minecraft.net"),
        MOJANG_COM("mojang.com"),
        ;

        @Nonnull private final String name;

        ServiceType(@Nonnull String name) {
            this.name = name;
        }

        @Nonnull
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "ServiceType{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public enum SkinType {
        DEFAULT(),
        SLIM(),
        ;
    }

    private static JSONObject getJSONObject(String url) {
        JSONObject obj;
        try {
            obj = (JSONObject) new JSONParser().parse(Unirest.get(url).asString().getBody());
            String err = (String) (obj.get("error"));
            if (err != null) {
                if ("IllegalArgumentException".equals(err)) {
                    throw new IllegalArgumentException((String) obj.get("errorMessage"));
                }
                throw new RuntimeException(err);
            }
        } catch (ParseException | UnirestException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    private static JSONArray getJSONArray(String url) {
        JSONArray arr;
        try {
            arr = (JSONArray) new JSONParser().parse(Unirest.get(url).asString().getBody());

        } catch (ParseException | UnirestException e) {
            throw new RuntimeException(e);
        }
        return arr;
    }
}
