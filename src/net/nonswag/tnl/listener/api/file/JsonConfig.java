package net.nonswag.tnl.listener.api.file;

import com.google.gson.*;
import net.nonswag.tnl.listener.api.logger.Logger;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * This Version of the Configuration API uses Googles Json (Gson)
 * @version 1.0
 * @apiNote Updated Configuration API
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
        this(new File(new File(path), file));
    }

    public JsonConfig(@Nonnull File file) {
        this.file = file;
        JsonElement jsonElement = new JsonObject();
        try {
            JsonFile.create(getFile());
            jsonElement = new JsonParser().parse(new BufferedReader(new InputStreamReader(new FileInputStream(getFile()), StandardCharsets.UTF_8)));
            if (jsonElement instanceof JsonNull || jsonElement instanceof JsonPrimitive) {
                jsonElement = new JsonObject();
            }
        } catch (Exception ignored) {
        }
        this.jsonElement = jsonElement;
        save();
        if (!isValid()) {
            System.err.println("The file '" + file.getAbsolutePath() + "' is invalid");
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
    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new PrintStream(getFile()), StandardCharsets.UTF_8));
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(getJsonElement().toString())));
            writer.close();
        } catch (Throwable e) {
            Logger.error.println("Failed to save file '" + getFile().getAbsolutePath() + "'", e);
        }
    }
}
