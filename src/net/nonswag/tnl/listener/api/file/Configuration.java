package net.nonswag.tnl.listener.api.file;

import net.nonswag.tnl.listener.NMSMain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Configuration {

    private final File file;

    public Configuration(String file) {
        this(new File(file));
    }

    public Configuration(String path, String file) {
        File configPath = new File(path);
        File configFile = new File((path + "/" + file));
        try {
            if (!configPath.getCanonicalFile().exists()) {
                if (!configPath.mkdirs()) {
                    NMSMain.stacktrace("An error has occurred while creating the directory '" + configPath.getAbsolutePath() + "'");
                }
            }
            if (!configFile.exists()) {
                if (!configFile.createNewFile()) {
                    NMSMain.stacktrace("An error has occurred while creating the file '" + configFile.getAbsolutePath() + "'");
                } else {
                    NMSMain.print("Successfully created the file '" + configFile.getAbsolutePath() + "'");
                }
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        this.file = configFile;
        if (!isValid()) {
            NMSMain.stacktrace("The file '" + this.file.getAbsolutePath() + "' is invalid");
        }
    }

    public Configuration(File file) {
        this.file = file;
        if (!file.exists()) {
            try {
                if (!file.getCanonicalFile().exists()) {
                    if (!file.mkdirs()) {
                        NMSMain.stacktrace("An error has occurred while creating the directory '" + file.getAbsolutePath() + "'");
                    }
                }
                if (!file.createNewFile()) {
                    NMSMain.stacktrace("An error has occurred while creating the file '" + file.getAbsolutePath() + "'");
                } else {
                    NMSMain.print("Successfully created the file '" + file.getAbsolutePath() + "'");
                }
            } catch (Throwable ignored) {
            }
        }
        if (!isValid()) {
            NMSMain.stacktrace("The file '" + file.getAbsolutePath() + "' is invalid");
        }
    }

    public File getFile() {
        return file;
    }

    private boolean isValid() {
        return getFile().exists() && getFile().isFile();
    }

    public void setValueIfAbsent(Object var, Object var2) {
        if (getValue(var) == null) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(getFile().getAbsolutePath(), true));
                writer.write(var + "=" + var2 + "\n");
                writer.close();
                NMSMain.print("Added new configuration section '" + var.toString() + "' with the value '" + var2.toString() + "'");
            } catch (Throwable t) {
                NMSMain.stacktrace(t);
            }
        }
    }

    public void setValue(Object var, Object var2) {
        try {
            if (!isValid()) {
                throw new IllegalArgumentException("Invalid Configuration");
            }
            if (getValue(var) == null) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(getFile().getAbsolutePath(), true));
                writer.write(var + "=" + var2 + "\n");
                writer.close();
                NMSMain.print("Added new configuration section '" + var.toString() + "' with the value '" + var2.toString() + "'");
            } else {
                File temp = new File(".cache.temp");
                File old = getFile();
                if (!temp.exists() && !temp.createNewFile()) {
                    NMSMain.stacktrace("Failed to create the temp file");
                } else {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(temp, true));
                    getValues().forEach((o, o2) -> {
                        try {
                            if (var.toString().equals(o.toString())) {
                                if (var2 != null) {
                                    writer.write(o.toString() + "=" + var2.toString() + "\n");
                                }
                            } else {
                                if (o2 != null) {
                                    writer.write(o.toString() + "=" + o2.toString() + "\n");
                                }
                            }
                        } catch (Throwable t) {
                            NMSMain.stacktrace(t);
                        }
                    });
                    if (!old.delete() || !temp.renameTo(old)) {
                        NMSMain.stacktrace("Failed to convert temp file to final file");
                    }
                    writer.close();
                }
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public Object getOrDefault(Object var, Object defaultValue) {
        return new net.nonswag.tnl.cloud.api.object.Object<>(getValue(var)).getOrDefault(defaultValue);
    }

    @Nullable
    public Object getValue(Object var) {
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
                } catch (Throwable t) {
                    NMSMain.stacktrace(t);
                }
            }
            reader.close();
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        return null;
    }

    @Nullable
    public String getString(Object var) {
        try {
            return new net.nonswag.tnl.listener.api.object.Object<>(getValue(var)).toString();
        } catch (Throwable t) {
            return null;
        }
    }

    @Nonnull
    public List<String> getStringList(Object var) {
        String value = getString(var);
        if (value != null) {
            return Arrays.asList(value.split(", "));
        }
        return new ArrayList<>();
    }

    @Nullable
    public Integer getInteger(Object var) {
        String value = getString(var);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return null;
    }

    @Nullable
    public Double getDouble(Object var) {
        String value = getString(var);
        if (value != null) {
            return Double.parseDouble(value);
        }
        return null;
    }

    @Nullable
    public Float getFloat(Object var) {
        String value = getString(var);
        if (value != null) {
            return Float.parseFloat(value);
        }
        return null;
    }

    @Nullable
    public Short getShort(Object var) {
        String value = getString(var);
        if (value != null) {
            return Short.parseShort(value);
        }
        return null;
    }

    @Nullable
    public Byte getByte(Object var) {
        String value = getString(var);
        if (value != null) {
            return Byte.parseByte(value);
        }
        return null;
    }

    @Nullable
    public Long getLong(Object var) {
        String value = getString(var);
        if (value != null) {
            return Long.parseLong(value);
        }
        return null;
    }

    @Nullable
    public Character getCharacter(Object var) {
        String value = getString(var);
        if (value != null) {
            if (value.length() > 1) {
                throw new IllegalStateException("A single character can't have more than one chars");
            }
            return value.charAt(0);
        }
        return null;
    }

    @Nullable
    public char[] getCharacters(Object var) {
        String value = getString(var);
        if (value != null) {
            return value.toCharArray();
        }
        return null;
    }

    @Nullable
    public Boolean getBoolean(Object var) {
        String value = getString(var);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return null;
    }

    @Nullable
    public void removeValue(Object var) {
        try {
            if (isValid()) {
                File temp = new File(".cache.temp");
                File old = getFile();
                if (!temp.exists() && !temp.createNewFile()) {
                    NMSMain.stacktrace("Failed to create the temp file");
                } else {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(temp, true));
                    getValues().forEach((o, o2) -> {
                        try {
                            if (!var.equals(o)) {
                                writer.write(o.toString() + "=" + o2.toString() + "\n");
                            }
                        } catch (Throwable t) {
                            NMSMain.stacktrace(t);
                        }
                    });
                    if (!old.delete() || !temp.renameTo(old)) {
                        NMSMain.stacktrace("Failed to convert temp file to final file");
                    }
                    writer.close();
                }
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    @Nonnull
    public HashMap<Object, Object> getValues() {
        HashMap<Object, Object> values = new HashMap<>();
        if (!isValid()) {
            return values;
        }
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(getFile().getAbsolutePath()));
            for (Object o : reader.lines().toArray()) {
                try {
                    List<String> split = Arrays.asList(o.toString().split("="));
                    if (split.size() > 1) {
                        values.put(split.get(0), String.join("=", split.subList(1, split.size())));
                    }
                } catch (Throwable t) {
                    NMSMain.stacktrace(t);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }
}
