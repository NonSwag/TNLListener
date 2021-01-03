package net.nonswag.tnl.listener.utils;

import java.io.File;

public class FileUtil {

    private static File serverFolder;
    private static File logFolder;
    private static File pluginFolder;

    public static File getServerFolder() { return serverFolder; }
    public static File getLogFolder() { return logFolder; }
    public static File getPluginFolder() { return pluginFolder; }

    public static void setServerFolder(File serverFolder) { FileUtil.serverFolder = serverFolder; }
    public static void setLogFolder(File logFolder) { FileUtil.logFolder = logFolder; }
    public static void setPluginFolder(File pluginFolder) { FileUtil.pluginFolder = pluginFolder; }

    public static File getFile(String string) { return new File(getServerFolder().getAbsolutePath() + string); }

}
