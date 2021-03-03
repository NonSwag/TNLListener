package net.nonswag.tnl.listener.api.file;

import net.nonswag.tnl.cloud.api.system.Console;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class JsonFile {

    public static void create(@Nonnull File file) throws IOException {
        if (!file.exists()) {
            if (file.createNewFile()) {
                Console.print("§aGenerated file §8'§6" + file.getAbsolutePath() + "§8'");
            } else {
                Console.stacktrace("§cCouldn't generate file §8'§4" + file.getAbsolutePath() + "§8'");
                throw new FileNotFoundException("Couldn't generate file");
            }
        }
    }
}
