package net.nonswag.tnl.listener.api.player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.annotation.Nonnull;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class Skin {

    @Nonnull
    private String value;
    @Nonnull
    private String signature;

    public Skin(@Nonnull String value, @Nonnull String signature) {
        this.signature = signature;
        this.value = value;
    }

    @Nonnull
    public String getValue() {
        return value;
    }

    @Nonnull
    public String getSignature() {
        return signature;
    }

    public void setValue(@Nonnull String value) {
        this.value = value;
    }

    public void setSignature(@Nonnull String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "Skin{" +
                "value='" + value + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skin skin = (Skin) o;
        return value.equals(skin.value) && signature.equals(skin.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, signature);
    }

    @Nonnull
    public static Skin getSkin(@Nonnull String player) {
        Skin skin = new Skin("", "");
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + player);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
            URL url1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader1 = new InputStreamReader(url1.openStream());
            JsonObject property = new JsonParser().parse(reader1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            skin.setValue(property.get("value").getAsString());
            skin.setSignature(property.get("signature").getAsString());
        } catch (Exception ignored) {
        }
        return skin;
    }
}
