package net.nonswag.tnl.listener.utils;

import net.nonswag.tnl.listener.api.logger.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public final class LinuxUtil {

    private static boolean debug = false;

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        LinuxUtil.debug = debug;
    }

    public static void runShellCommand(@Nonnull String command, @Nullable File directory) throws IOException, InterruptedException {
        Process process = directory == null ? Runtime.getRuntime().exec(command) : Runtime.getRuntime().exec(command, null, directory);
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        if (isDebug()) {
            String string;
            Logger.info.println("Executing: " + command);
            while ((string = br.readLine()) != null) Logger.info.println(string);
        }
        process.waitFor();
        if (isDebug()) Logger.info.println("Finished program with exit code: " + process.exitValue());
        process.destroy();
    }

    public static void runSafeShellCommand(@Nonnull String command, @Nullable File directory) {
        try {
            runShellCommand(command, directory);
        } catch (IOException | InterruptedException e) {
            Logger.error.println(e.getMessage());
        }
    }
}
