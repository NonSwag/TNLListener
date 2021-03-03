package net.nonswag.tnl.listener.api.file;

import com.google.gson.JsonElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public interface Config {

    void setIfAbsent(@Nonnull String key, @Nonnull String value);

    void setIfAbsent(@Nonnull String key, @Nonnull Number value);

    void setIfAbsent(@Nonnull String key, @Nonnull Boolean value);

    void setIfAbsent(@Nonnull String key, @Nonnull Character value);

    void set(@Nonnull String key, @Nonnull String value);

    void set(@Nonnull String key, @Nonnull Number value);

    void set(@Nonnull String key, @Nonnull Boolean value);

    void set(@Nonnull String key, @Nonnull Character value);

    void remove(@Nonnull String key);

    @Nonnull
    HashMap<String, JsonElement> getValues();

    @Nullable
    JsonElement getValue(@Nonnull String key);

    @Nullable
    String getString(@Nonnull String key);

    @Nullable
    List<String> getStringList(@Nonnull String key);

    @Nullable
    Integer getInteger(@Nonnull String key);

    @Nullable
    Double getDouble(@Nonnull String key);

    @Nullable
    Float getFloat(@Nonnull String key);

    @Nullable
    Short getShort(@Nonnull String key);

    @Nullable
    Byte getByte(@Nonnull String key);

    @Nullable
    Long getLong(@Nonnull String key);

    @Nullable
    Character getCharacter(@Nonnull String key);

    @Nullable
    char[] getCharacters(@Nonnull String key);

    @Nullable
    Boolean getBoolean(@Nonnull String key);

    void save();
}
