package net.nonswag.tnl.listener.api.settings;

import net.nonswag.tnl.listener.api.config.PropertyConfig;
import net.nonswag.tnl.listener.api.logger.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public abstract class Settings {

    @Nonnull
    public static final Setting<Boolean> DEBUG = new Setting<>("debug", true);
    @Nonnull
    public static final Setting<Boolean> DELETE_OLD_LOGS = new Setting<>("delete-old-logs", true);
    @Nonnull
    public static final Setting<Boolean> BETTER_PERMISSIONS = new Setting<>("better-permissions", true);
    @Nonnull
    public static final Setting<Boolean> BETTER_COMMANDS = new Setting<>("better-commands", true);
    @Nonnull
    public static final Setting<Boolean> BETTER_CHAT = new Setting<>("better-chat", true);
    @Nonnull
    public static final Setting<Boolean> BETTER_TNT = new Setting<>("better-tnt", true);
    @Nonnull
    public static final Setting<Boolean> BETTER_FALLING_BLOCKS = new Setting<>("better-falling-blocks", true);
    @Nonnull
    public static final Setting<Boolean> PUNISH_SPAMMING = new Setting<>("punish-spamming", true);
    @Nonnull
    public static final Setting<Boolean> AUTO_UPDATER = new Setting<>("auto-updater", true);
    @Nonnull
    public static final Setting<Boolean> TAB_COMPLETER = new Setting<>("tab-completer", true);
    @Nonnull
    public static final Setting<Boolean> CHAT_COMPLETER = new Setting<>("chat-completer", true);
    @Nonnull
    public static final Setting<Boolean> FIRST_JOIN_MESSAGE = new Setting<>("first-join-message", true);
    @Nonnull
    public static final Setting<Boolean> JOIN_MESSAGE = new Setting<>("join-message", true);
    @Nonnull
    public static final Setting<Boolean> QUIT_MESSAGE = new Setting<>("quit-message", true);
    @Nonnull
    public static final Setting<Boolean> CUSTOM_ITEM_NAMES = new Setting<>("custom-item-names", true);
    @Nonnull
    public static final Setting<Boolean> CUSTOM_TEAMS = new Setting<>("custom-teams", true);
    @Nonnull
    public static final Setting<String> TAB_COMPLETE_BYPASS_PERMISSION = new Setting<>("tab-complete-bypass-permission", "tnl.tab");
    @Nonnull
    public static final Setting<List<String>> SERVERS = new Setting<>("servers", Arrays.asList("example-1", "example-2", "example-3"));

    @Nonnull
    private static final PropertyConfig config = new PropertyConfig("plugins/Listener", "settings.properties");

    protected Settings() {
    }

    @Nonnull
    public static PropertyConfig getConfig() {
        return config;
    }

    public static void init() {
        for (Setting<?> setting : Setting.getList()) {
            if (getConfig().has(setting.getKey())) {
                if (setting.getValue() instanceof String) {
                    String value = getConfig().getString(setting.getKey());
                    if (value != null) {
                        ((Setting<String>) setting).setValue(value);
                    }
                    Logger.debug.println("Loaded setting <'" + setting.getKey() + "'> with value <'" + setting.getValue() + "'>");
                } else if (setting.getValue() instanceof Boolean) {
                    Boolean value = getConfig().getBoolean(setting.getKey());
                    ((Setting<Boolean>) setting).setValue(value);
                    Logger.debug.println("Loaded setting <'" + setting.getKey() + "'> with value <'" + setting.getValue() + "'>");
                } else if (setting.getValue() instanceof Byte) {
                    Byte value = getConfig().getByte(setting.getKey());
                    ((Setting<Byte>) setting).setValue(value);
                    Logger.debug.println("Loaded setting <'" + setting.getKey() + "'> with value <'" + setting.getValue() + "'>");
                } else if (setting.getValue() instanceof List) {
                    List<String> value = getConfig().getStringList(setting.getKey());
                    ((Setting<List<String>>) setting).setValue(value);
                    Logger.debug.println("Loaded setting <'" + setting.getKey() + "'> with value <'" + String.join(", ", value) + "'>");
                } else if (setting.getValue() instanceof Integer) {
                    Integer value = getConfig().getInteger(setting.getKey());
                    ((Setting<Integer>) setting).setValue(value);
                    Logger.debug.println("Loaded setting <'" + setting.getKey() + "'> with value <'" + setting.getValue() + "'>");
                } else {
                    Logger.warn.println("Unset Setting Type <'" + setting.getValue().getClass().getSimpleName() + "'>", new IOException("unset setting type"));
                }
            } else {
                if (setting.getValue() instanceof String) {
                    String value = (String) setting.getValue();
                    getConfig().setValue(setting.getKey(), value);
                    Logger.debug.println("Added setting <'" + setting.getKey() + "'> with value <'" + value + "'>");
                } else if (setting.getValue() instanceof Boolean) {
                    Boolean value = (Boolean) setting.getValue();
                    getConfig().setValue(setting.getKey(), value);
                    Logger.debug.println("Added setting <'" + setting.getKey() + "'> with value <'" + value + "'>");
                } else if (setting.getValue() instanceof Byte) {
                    Byte value = (Byte) setting.getValue();
                    getConfig().setValue(setting.getKey(), value);
                    Logger.debug.println("Added setting <'" + setting.getKey() + "'> with value <'" + value + "'>");
                } else if (setting.getValue() instanceof List) {
                    List<String> value = (List<String>) setting.getValue();
                    getConfig().setValue(setting.getKey(), value);
                    Logger.debug.println("Added setting <'" + setting.getKey() + "'> with value <'" + String.join(", ", value) + "'>");
                } else if (setting.getValue() instanceof Integer) {
                    Integer value = (Integer) setting.getValue();
                    getConfig().setValue(setting.getKey(), value);
                    Logger.debug.println("Added setting <'" + setting.getKey() + "'> with value <'" + value + "'>");
                } else {
                    Logger.error.println("Unset Setting Type <'" + setting.getValue().getClass().getSimpleName() + "'>", new IOException("unset setting type"));
                }
            }
        }
        getConfig().save();
    }
}
