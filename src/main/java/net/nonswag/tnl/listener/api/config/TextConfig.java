package net.nonswag.tnl.listener.api.config;

import net.nonswag.tnl.listener.api.file.FileCreator;
import net.nonswag.tnl.listener.api.logger.Logger;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class TextConfig implements Config {

    @Nonnull
    private final File file;
    @Nonnull
    private String[] content = new String[]{};

    public TextConfig(@Nonnull String file) {
        this(new File(file));
    }

    public TextConfig(@Nonnull String path, @Nonnull String file) {
        this(new File(new File(path), file));
    }

    public TextConfig(@Nonnull File file) {
        this.file = file;
        try {
            FileCreator.create(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(getFile()), StandardCharsets.UTF_8));
            Object[] array = bufferedReader.lines().toArray();
            this.content = new String[array.length];
            for (int i = 0; i < array.length; i++) {
                content[i] = (String) array[i];
            }
        } catch (Exception ignored) {
        }
        save();
        if (!isValid()) {
            Logger.error.println("The file '" + file.getAbsolutePath() + "' is invalid");
        }
    }

    @Nonnull
    public File getFile() {
        return file;
    }

    public void setContent(@Nonnull String[] content) {
        this.content = content;
    }

    @Nonnull
    public String[] getContent() {
        return content;
    }

    @Override
    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new PrintStream(getFile()), StandardCharsets.UTF_8));
            for (String s : content) {
                writer.write(s + "\n");
            }
            writer.close();
        } catch (Exception e) {
            Logger.error.println("Failed to save file '" + getFile().getAbsolutePath() + "'", e);
        }
    }
}
