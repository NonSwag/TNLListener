package net.nonswag.tnl.listener.api.mojang;

import net.nonswag.tnl.listener.NMSMain;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;

import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class PlayerProfile {

    private final String uuid;
    private final String username;
    private final Set<Property> properties;
    private final Optional<TexturesProperty> textures;

    public static class Property {
        String name;
        String value;
        String signature;

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getSignature() {
            return signature;
        }

        @Override
        public String toString() {
            return "Property{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    ", signature='" + signature + '\'' +
                    '}';
        }
    }

    public static class TexturesProperty extends Property {
        long timestamp;
        String profileId, profileName;
        boolean signatureRequired = false;
        Map<String, URL> textures;

        public long getTimestamp() {
            return timestamp;
        }

        public String getProfileId() {
            return profileId;
        }

        public String getProfileName() {
            return profileName;
        }

        public boolean isSignatureRequired() {
            return signatureRequired;
        }

        public Map<String, URL> getTextures() {
            return textures;
        }

        public Optional<URL> getSkin() {
            return Optional.ofNullable(textures.get("SKIN"));
        }

        public Optional<URL> getCape() {
            return Optional.ofNullable(textures.get("CAPE"));
        }

        public void setSkin(String url) {
            try {
                textures.put("SKIN", new URL(url));
            } catch (Throwable t) {
                NMSMain.stacktrace(t);
            }
        }

        public void setCape(String url) {
            try {
                textures.put("CAPE", new URL(url));
            } catch (Throwable t) {
                NMSMain.stacktrace(t);
            }
        }

        public String toBase64() {
            JSONObject profile = new JSONObject();
            profile.put("timestamp", getTimestamp());
            profile.put("profileId", getProfileId());
            profile.put("profileName", getProfileName());
            profile.put("signatureRequired", isSignatureRequired());
            JSONObject textures = new JSONObject();
            getTextures().forEach((name, url) -> {
                JSONObject urlObject = new JSONObject();
                urlObject.put("url", url.toString());
                textures.put(name, urlObject);
            });
            profile.put("textures", textures);
            return Base64.encodeBase64String(profile.toJSONString().replace("\\/", "/").getBytes());
        }

        public static String createBase64Profile(String skin,
                                                 String cape,
                                                 Mojang.SkinType skinType) {
            JSONObject profile = new JSONObject();
            JSONObject textures = new JSONObject();
            JSONObject skinObject = new JSONObject();
            JSONObject capeObject = new JSONObject();
            JSONObject skinTypeObject = new JSONObject();
            profile.put("timestamp", System.currentTimeMillis());
            profile.put("profileId", "05011e85501c432fb9a68683c5fbd479");
            profile.put("profileName", "NonSwag");
            profile.put("signatureRequired", true);
            skinTypeObject.put("model", skinType.name().toLowerCase());
            skinObject.put("url", skin);
            skinObject.put("metadata", skinTypeObject);
            capeObject.put("url", cape);
            textures.put("SKIN", skinObject);
            textures.put("CAPE", capeObject);
            profile.put("textures", textures);
            return Base64.encodeBase64String(profile.toJSONString().replace("\\/", "/").getBytes());
        }

        @Override
        public String toString() {
            return "TexturesProperty{" +
                    "timestamp=" + timestamp +
                    ", profileId='" + profileId + '\'' +
                    ", profileName='" + profileName + '\'' +
                    ", signatureRequired=" + signatureRequired +
                    ", textures=" + textures +
                    '}';
        }
    }

    public PlayerProfile(String uuid, String username, Set<Property> properties) {
        this.uuid = uuid;
        this.username = username;
        this.properties = properties;
        this.textures = properties.stream().filter(p -> p.getName().equals("textures")).map(p -> (TexturesProperty) p).findAny();
    }

    public String getUUID() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public Optional<TexturesProperty> getTextures() {
        return textures;
    }

    @Override
    public String toString() {
        return "PlayerProfile{" +
                "uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                ", properties=" + properties +
                ", textures=" + textures +
                '}';
    }
}