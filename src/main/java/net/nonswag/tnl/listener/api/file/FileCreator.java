package net.nonswag.tnl.listener.api.file;

import net.nonswag.tnl.listener.api.logger.Logger;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class FileCreator {

    public static void create(@Nonnull File directory, @Nonnull File file) throws IOException {
        create(directory);
        create(file);
    }

    public static void create(@Nonnull File file) throws IOException {
        if (!file.exists()) {
            if (file.getAbsoluteFile().getParentFile().mkdirs()) {
                Logger.info.println("Generated directories <'" + file.getAbsoluteFile().getParent() + "'>");
            }
            if (file.createNewFile()) {
                Logger.info.println("Generated file <'" + file.getAbsolutePath() + "'>");
            } else {
                Logger.error.println("Couldn't generate file <'" + file.getAbsolutePath() + "'>");
                throw new FileNotFoundException("Couldn't generate file");
            }
        }
    }
}
