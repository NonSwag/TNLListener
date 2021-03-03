package net.nonswag.tnl.listener.api.file;

import net.nonswag.tnl.listener.api.logger.Logger;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class JsonFile {

    public static void create(@Nonnull File directory, @Nonnull File file) throws IOException {
        create(directory);
        create(file);
    }

    public static void create(@Nonnull File file) throws IOException {
        if (!file.exists()) {
            if (file.getAbsoluteFile().getParentFile().mkdirs()) {
                Logger.info.println("§aGenerated directories §8'§6" + file.getAbsoluteFile().getPath() + "§8'");
            }
            if (file.createNewFile()) {
                Logger.info.println("§aGenerated file §8'§6" + file.getAbsolutePath() + "§8'");
            } else {
                Logger.error.println("§cCouldn't generate file §8'§4" + file.getAbsolutePath() + "§8'");
                throw new FileNotFoundException("Couldn't generate file");
            }
        }
    }
}
