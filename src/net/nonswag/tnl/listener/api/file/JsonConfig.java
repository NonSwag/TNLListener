package net.nonswag.tnl.listener.api.file;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.api.object.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * This Version of the Configuration API uses Googles Json (Gson)
 * @see net.nonswag.tnl.cloud.api.file.Config
 * @version 1.0
 * @apiNote Updated Configuration API
 * @see com.google.gson.Gson
 * @see GsonBuilder
 * @see JsonElement
 * @see JsonParser
 * @see com.google.gson.JsonObject
 * @see com.google.gson.JsonArray
 */

public class JsonConfig implements Config {

    @Nonnull
    private final File file;
    @Nonnull
    private final JsonElement jsonElement;

    public JsonConfig(@Nonnull String file) {
        this(new File(file));
    }

    public JsonConfig(@Nonnull String path, @Nonnull String file) {
        this(new File((path + "/" + file)));
    }

    public JsonConfig(@Nonnull File file) {
        this.file = file;
        JsonElement jsonElement = new JsonObject();
        try {
            JsonFile.create(getFile());
            jsonElement = JsonParser.parseReader(new FileReader(getFile()));
            if (!(jsonElement instanceof JsonObject)) {
                jsonElement = new JsonObject();
            }
        } catch (Exception ignored) {
        }
        this.jsonElement = jsonElement;
        save();
        if (!isValid()) {
            NMSMain.stacktrace("The file §8'§4" + file.getAbsolutePath() + "§8'§c is invalid");
        }
    }

    @Nonnull
    public File getFile() {
        return file;
    }

    @Nonnull
    public JsonElement getJsonElement() {
        return jsonElement;
    }

    private boolean isValid() {
        return getFile().exists() && getFile().isFile();
    }

    @Override
    public void set(@Nonnull String key, @Nonnull Number value) {
        getJsonElement().getAsJsonObject().addProperty(key, value);
    }

    @Override
    public void set(@Nonnull String key, @Nonnull String value) {
        getJsonElement().getAsJsonObject().addProperty(key, value);
    }

    @Override
    public void set(@Nonnull String key, @Nonnull Boolean value) {
        getJsonElement().getAsJsonObject().addProperty(key, value);
    }

    @Override
    public void set(@Nonnull String key, @Nonnull Character value) {
        getJsonElement().getAsJsonObject().addProperty(key, value);
    }

    @Override
    public void setIfAbsent(@Nonnull String key, @Nonnull Number value) {
        if (!getJsonElement().getAsJsonObject().has(key)) {
            set(key, value);
        }
    }

    @Override
    public void setIfAbsent(@Nonnull String key, @Nonnull String value) {
        if (!getJsonElement().getAsJsonObject().has(key)) {
            set(key, value);
        }
    }

    @Override
    public void setIfAbsent(@Nonnull String key, @Nonnull Boolean value) {
        if (!getJsonElement().getAsJsonObject().has(key)) {
            set(key, value);
        }
    }

    @Override
    public void setIfAbsent(@Nonnull String key, @Nonnull Character value) {
        if (!getJsonElement().getAsJsonObject().has(key)) {
            set(key, value);
        }
    }

    @Override
    public void remove(@Nonnull String key) {
        getJsonElement().getAsJsonObject().remove(key);
    }

    @Nonnull
    @Override
    public HashMap<String, JsonElement> getValues() {
        Set<String> keySet = getJsonElement().getAsJsonObject().keySet();
        HashMap<String, JsonElement> values = new HashMap<>();
        for (String key : keySet) {
            values.put(key, getValue(key));
        }
        return values;
    }

    @Nullable
    @Override
    public JsonElement getValue(@Nonnull String key) {
        if (getJsonElement().isJsonObject()) {
            return getJsonElement().getAsJsonObject().get(key);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public String getString(@Nonnull String key) {
        try {
            return new Objects<>(getValue(key)).nonnull().getAsString();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public List<String> getStringList(@Nonnull String key) {
        try {
            return Arrays.asList(new Objects<>(getString(key)).nonnull().split(", "));
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public Integer getInteger(@Nonnull String key) {
        try {
            return new Objects<>(getValue(key)).nonnull().getAsInt();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public Double getDouble(@Nonnull String key) {
        try {
            return new Objects<>(getValue(key)).nonnull().getAsDouble();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public Float getFloat(@Nonnull String key) {
        try {
            return new Objects<>(getValue(key)).nonnull().getAsFloat();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public Short getShort(@Nonnull String key) {
        try {
            return new Objects<>(getValue(key)).nonnull().getAsShort();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public Byte getByte(@Nonnull String key) {
        try {
            return new Objects<>(getValue(key)).nonnull().getAsByte();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public Long getLong(@Nonnull String key) {
        try {
            return new Objects<>(getValue(key)).nonnull().getAsLong();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public Character getCharacter(@Nonnull String key) {
        try {
            return new Objects<>(getString(key)).nonnull().charAt(0);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public char[] getCharacters(@Nonnull String key) {
        try {
            return new Objects<>(getString(key)).nonnull().toCharArray();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public Boolean getBoolean(@Nonnull String key) {
        try {
            return new Objects<>(getValue(key)).nonnull().getAsBoolean();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getFile()));
            String string = new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(getJsonElement().toString()));
            writer.write(string);
            writer.close();
        } catch (IOException e) {
            NMSMain.stacktrace(e);
        }
    }
}
