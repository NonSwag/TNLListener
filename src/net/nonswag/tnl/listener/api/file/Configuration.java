package net.nonswag.tnl.listener.api.file;

import com.sun.istack.internal.Nullable;
import net.nonswag.tnl.listener.api.logger.Logger;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @deprecated Configuration API
 * @see JsonConfig
 * @apiNote outdated
 */

@Deprecated
public class Configuration {

    @Nonnull private final File file;

    @Deprecated
    public Configuration(@Nonnull String file) {
        this(new File(file));
    }

    @Deprecated
    public Configuration(@Nonnull String path, @Nonnull String file) {
        File configPath = new File(path);
        File configFile = new File((path + "/" + file));
        try {
            if (!configPath.getCanonicalFile().exists()) {
                if (!configPath.mkdirs()) {
                    Logger.error.println("An error has occurred while creating the directory §8'§4" + configPath.getAbsolutePath() + "§8'");
                }
            }
            if (!configFile.exists()) {
                if (!configFile.createNewFile()) {
                    Logger.error.println("An error has occurred while creating the file §8'§4" + configFile.getAbsolutePath() + "§8'");
                } else {
                    Logger.info.println("Successfully created the file §8'§6" + configFile.getAbsolutePath() + "§8'");
                }
            }
        } catch (Exception e) {
            Logger.error.println(e);
        }
        this.file = configFile;
        if (!isValid()) {
            Logger.error.println("The file §8'§4" + this.file.getAbsolutePath() + "§8'§c is invalid");
        }
    }

    @Deprecated
    public Configuration(@Nonnull File file) {
        this.file = file;
        if (!file.exists()) {
            try {
                if (!file.getCanonicalFile().exists()) {
                    if (!file.mkdirs()) {
                        Logger.error.println("An error has occurred while creating the directory §8'§4" + file.getAbsolutePath() + "§8'");
                    }
                }
                if (!file.createNewFile()) {
                    Logger.error.println("An error has occurred while creating the file §8'§4" + file.getAbsolutePath() + "§8'");
                } else {
                    Logger.info.println("Successfully created the file §8'§6" + file.getAbsolutePath() + "§8'");
                }
            } catch (Exception ignored) {
            }
        }
        if (!isValid()) {
            Logger.error.println("The file §8'§4" + file.getAbsolutePath() + "§8'§c is invalid");
        }
    }

    @Deprecated
    @Nonnull
    public File getFile() {
        return file;
    }

    @Deprecated
    private boolean isValid() {
        return getFile().exists() && getFile().isFile();
    }

    @Deprecated
    public void setValueIfAbsent(@Nonnull Object var, @Nonnull Object var2) {
        if (getValue(var) == null) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(getFile().getAbsolutePath(), true));
                writer.write(var + "=" + var2 + "\n");
                writer.close();
                Logger.info.println("Added new configuration section §8'§6" + var.toString() + "§8'§a with the value §8'§6" + var2.toString() + "§8'");
            } catch (Throwable t) {
                Logger.error.println(t);
            }
        }
    }

    @Deprecated
    public void setValue(@Nonnull Object var, @Nonnull Object var2) {
        try {
            if (!isValid()) {
                throw new IllegalArgumentException("Invalid Configuration");
            }
            if (getValue(var) == null) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(getFile().getAbsolutePath(), true));
                writer.write(var + "=" + var2 + "\n");
                writer.close();
                Logger.info.println("Added new configuration section §8'§6" + var.toString() + "§8'§a with the value §8'§6" + var2.toString() + "§8'");
            } else {
                File temp = new File(".cache.temp");
                File old = getFile();
                if (!temp.exists() && !temp.createNewFile()) {
                    Logger.error.println("Failed to create the temp file");
                } else {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(temp, true));
                    getValues().forEach((o, o2) -> {
                        try {
                            if (var.toString().equals(o.toString())) {
                                writer.write(o.toString() + "=" + var2.toString() + "\n");
                            } else {
                                if (o2 != null) {
                                    writer.write(o.toString() + "=" + o2.toString() + "\n");
                                }
                            }
                        } catch (Throwable t) {
                            Logger.error.println(t);
                        }
                    });
                    if (!old.delete() || !temp.renameTo(old)) {
                        Logger.error.println("Failed to convert temp file to final file");
                    }
                    writer.close();
                }
            }
        } catch (Exception e) {
            Logger.error.println(e);
        }
    }

    @Deprecated
    public Object getOrDefault(@Nonnull Object var, @Nonnull Object defaultValue) {
        return new net.nonswag.tnl.listener.api.object.Objects<>(getValue(var)).getOrDefault(defaultValue);
    }

    @Deprecated
    @Nullable
    public Object getValue(@Nonnull Object var) {
        try {
            if (!isValid()) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new FileReader(getFile().getAbsolutePath()));
            for (Object o : reader.lines().toArray()) {
                try {
                    List<String> split = Arrays.asList(o.toString().split("="));
                    if (split.size() > 1 && split.get(0).equals(var.toString())) {
                        return String.join("=", split.subList(1, split.size()));
                    }
                } catch (Exception e) {
                    Logger.error.println(e);
                }
            }
            reader.close();
        } catch (Exception e) {
            Logger.error.println(e);
        }
        return null;
    }

    @Deprecated
    @Nullable
    public String getString(@Nonnull Object var) {
        try {
            return getValue(var).toString();
        } catch (Throwable t) {
            return null;
        }
    }

    @Deprecated
    @Nullable
    public List<String> getStringList(@Nonnull Object var) {
        try {
            return Arrays.asList(getValue(var).toString().split(", "));
        } catch (Throwable t) {
            return null;
        }
    }

    @Deprecated
    @Nullable
    public Integer getInteger(@Nonnull Object var) {
        try {
            return Integer.parseInt(getValue(var).toString());
        } catch (Throwable t) {
            return null;
        }
    }

    @Deprecated
    @Nullable
    public Double getDouble(@Nonnull Object var) {
        try {
            return Double.parseDouble(getValue(var).toString());
        } catch (Throwable t) {
            return null;
        }
    }

    @Deprecated
    @Nullable
    public Float getFloat(@Nonnull Object var) {
        try {
            return Float.parseFloat(getValue(var).toString());
        } catch (Throwable t) {
            return null;
        }
    }

    @Deprecated
    @Nullable
    public Short getShort(@Nonnull Object var) {
        try {
            return Short.parseShort(getValue(var).toString());
        } catch (Throwable t) {
            return null;
        }
    }

    @Deprecated
    @Nullable
    public Byte getByte(@Nonnull Object var) {
        try {
            return Byte.parseByte(getValue(var).toString());
        } catch (Throwable t) {
            return null;
        }
    }

    @Deprecated
    @Nullable
    public Long getLong(@Nonnull Object var) {
        try {
            return Long.parseLong(getValue(var).toString());
        } catch (Throwable t) {
            return null;
        }
    }

    @Deprecated
    @Nullable
    public Character getCharacter(@Nonnull Object var) {
        try {
            String value = getValue(var).toString();
            if (value.length() > 1) {
                throw new IllegalStateException("A single character can't have more than one chars");
            }
            return value.charAt(0);
        } catch (Throwable t) {
            return null;
        }
    }

    @Deprecated
    @Nullable
    public char[] getCharacters(@Nonnull Object var) {
        try {
            return getValue(var).toString().toCharArray();
        } catch (Throwable t) {
            return null;
        }
    }

    @Deprecated
    @Nullable
    public Boolean getBoolean(@Nonnull Object var) {
        try {
            return Boolean.parseBoolean(getValue(var).toString());
        } catch (Throwable t) {
            return null;
        }
    }

    @Deprecated
    @Nullable
    public void removeValue(@Nonnull Object var) {
        try {
            if (isValid()) {
                File temp = new File(".cache.temp");
                File old = getFile();
                if (!temp.exists() && !temp.createNewFile()) {
                    Logger.error.println("Failed to create the temp file");
                } else {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(temp, true));
                    getValues().forEach((o, o2) -> {
                        try {
                            if (!var.toString().equals(o.toString())) {
                                writer.write(o.toString() + "=" + o2.toString() + "\n");
                            }
                        } catch (Exception e) {
                            Logger.error.println(e);
                        }
                    });
                    if (!old.delete() || !temp.renameTo(old)) {
                        Logger.error.println("Failed to convert temp file to final file");
                    }
                    writer.close();
                }
            }
        } catch (Exception e) {
            Logger.error.println(e);
        }
    }

    @Deprecated
    @Nonnull
    public HashMap<Object, Object> getValues() {
        HashMap<Object, Object> values = new HashMap<>();
        try {
            if (!isValid()) {
                return values;
            }
            BufferedReader reader = new BufferedReader(new FileReader(getFile().getAbsolutePath()));
            for (Object o : reader.lines().toArray()) {
                try {
                    List<String> split = Arrays.asList(o.toString().split("="));
                    if (split.size() > 1) {
                        values.put(split.get(0), String.join("=", split.subList(1, split.size())));
                    }
                } catch (Exception e) {
                    Logger.error.println(e);
                }
            }
            reader.close();
        } catch (Exception e) {
            Logger.error.println(e);
        }
        return values;
    }
}
