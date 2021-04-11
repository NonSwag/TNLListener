package net.nonswag.tnl.listener.api.config;

import com.sun.istack.internal.Nullable;
import net.nonswag.tnl.listener.api.file.FileCreator;
import net.nonswag.tnl.listener.api.logger.Logger;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PropertyConfig implements Config {

    @Nonnull
    private final File file;
    @Nonnull
    private final HashMap<String, String> properties = new HashMap<>();

    public PropertyConfig(@Nonnull String file) {
        this(new File(file));
    }

    public PropertyConfig(@Nonnull String path, @Nonnull String file) {
        this(new File(new File(path), file));
    }

    public PropertyConfig(@Nonnull File file) {
        this.file = file;
        try {
            FileCreator.create(file);
            getValues().clear();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(getFile()), StandardCharsets.UTF_8));
            reader.lines().forEach(s -> {
                try {
                    List<String> split = Arrays.asList(s.split("="));
                    if (split.size() >= 1 && !split.get(0).isEmpty()) {
                        getValues().put(split.get(0).toLowerCase(), String.join("=", split.subList(1, split.size())));
                    }
                } catch (Exception e) {
                    Logger.error.println(e);
                }
            });
            reader.close();
        } catch (Exception e) {
            Logger.error.println(e);
        }
        if (!isValid()) {
            Logger.error.println("§cThe file §8'§4" + file.getAbsolutePath() + "§8'§c is invalid");
        }
    }

    @Nonnull
    public File getFile() {
        return file;
    }

    @Override
    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getFile()));
            getValues().keySet().stream().sorted().forEach(key -> {
                try {
                    writer.write(key.toLowerCase() + "=" + getString(key) + "\n");
                } catch (Exception ignored) {
                }
            });
            writer.close();
        } catch (Exception e) {
            Logger.error.println(e);
        }
    }

    public void setValueIfAbsent(@Nonnull String key, @Nonnull String value) {
        getValues().putIfAbsent(key.toLowerCase(), value);
    }

    public void setValue(@Nonnull String key, @Nonnull String value) {
        getValues().put(key.toLowerCase(), value);
    }

    public void setValue(@Nonnull String key, @Nonnull Collection<String> value) {
        getValues().put(key.toLowerCase(), String.join(", ", value));
    }

    public void setValue(@Nonnull String key, @Nonnull Boolean value) {
        getValues().put(key.toLowerCase(), String.valueOf(value));
    }

    public void setValue(@Nonnull String key, @Nonnull Number value) {
        getValues().put(key.toLowerCase(), String.valueOf(value));
    }

    public void setValue(@Nonnull String key, @Nonnull Character value) {
        getValues().put(key.toLowerCase(), String.valueOf(value));
    }

    public boolean has(@Nonnull String key) {
        return getValues().containsKey(key.toLowerCase());
    }

    @Nonnull
    public String getOrDefault(@Nonnull String key, @Nonnull String defaultValue) {
        return getValues().getOrDefault(key.toLowerCase(), defaultValue);
    }

    @Nullable
    public String getString(@Nonnull String key) {
        return getValues().get(key.toLowerCase());
    }

    @Nonnull
    public List<String> getStringList(@Nonnull String key) {
        try {
            return Arrays.asList(getString(key.toLowerCase()).split(", "));
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
    }

    public int getInteger(@Nonnull String key) {
        try {
            return Integer.parseInt(getString(key.toLowerCase()));
        } catch (Exception ignored) {
            return 0;
        }
    }

    public double getDouble(@Nonnull String key) {
        try {
            return Double.parseDouble(getString(key.toLowerCase()));
        } catch (Exception e) {
            return 0D;
        }
    }

    public float getFloat(@Nonnull String key) {
        try {
            return Float.parseFloat(getString(key.toLowerCase()));
        } catch (Exception e) {
            return 0F;
        }
    }

    public short getShort(@Nonnull String key) {
        try {
            return Short.parseShort(getString(key.toLowerCase()));
        } catch (Exception e) {
            return 0;
        }
    }

    public byte getByte(@Nonnull String key) {
        try {
            return Byte.parseByte(getString(key.toLowerCase()));
        } catch (Exception e) {
            return 0;
        }
    }

    public long getLong(@Nonnull String key) {
        try {
            return Long.parseLong(getString(key.toLowerCase()));
        } catch (Exception e) {
            return 0L;
        }
    }

    @Nullable
    public Character getCharacter(@Nonnull String key) {
        try {
            return getString(key.toLowerCase()).charAt(0);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public char[] getCharacters(@Nonnull String key) {
        try {
            return getString(key.toLowerCase()).toCharArray();
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public boolean getBoolean(@Nonnull String key) {
        try {
            return Boolean.parseBoolean(getString(key.toLowerCase()));
        } catch (Exception e) {
            return false;
        }
    }

    public void removeValue(@Nonnull String key) {
        getValues().remove(key.toLowerCase());
    }

    @Nonnull
    private HashMap<String, String> getValues() {
        return properties;
    }
}
