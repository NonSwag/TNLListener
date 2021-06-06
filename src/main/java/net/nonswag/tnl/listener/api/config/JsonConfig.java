package net.nonswag.tnl.listener.api.config;

import com.google.gson.*;
import net.nonswag.tnl.listener.api.file.FileCreator;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.utils.LinuxUtil;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonConfig implements Config {

    @Nonnull
    private final File file;
    @Nonnull
    private final JsonElement jsonElement;

    public JsonConfig(@Nonnull String file) {
        this(new File(file));
    }

    public JsonConfig(@Nonnull String path, @Nonnull String file) {
        this(new File(new File(path), file));
    }

    public JsonConfig(@Nonnull File file) {
        this.file = file;
        JsonElement jsonElement = new JsonObject();
        try {
            FileCreator.create(getFile());
            jsonElement = new JsonParser().parse(new BufferedReader(new InputStreamReader(new FileInputStream(getFile()), StandardCharsets.UTF_8)));
            if (jsonElement instanceof JsonNull) jsonElement = new JsonObject();
        } catch (Exception e) {
            LinuxUtil.runSafeShellCommand("cp " + getFile().getName() + " broken-" + getFile().getName(), getFile().getAbsoluteFile().getParentFile());
            Logger.error.println("Failed to load file §8'§4" + getFile().getAbsolutePath() + "§8'", "Creating Backup of the old file");
        }
        this.jsonElement = jsonElement;
        save();
        if (!isValid()) Logger.error.println("The file §8'§4" + file.getAbsolutePath() + "§8'§c is invalid");
    }

    @Nonnull
    public File getFile() {
        return file;
    }

    @Nonnull
    public JsonElement getJsonElement() {
        return jsonElement;
    }

    @Override
    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new PrintStream(getFile()), StandardCharsets.UTF_8));
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(getJsonElement()));
            writer.close();
        } catch (Exception e) {
            Logger.error.println("Failed to save file §8'§4" + getFile().getAbsolutePath() + "§4'", e);
        }
    }
}

