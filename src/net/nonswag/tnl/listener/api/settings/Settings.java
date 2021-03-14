package net.nonswag.tnl.listener.api.settings;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.nonswag.tnl.listener.api.file.JsonConfig;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.events.MessagesInitializeEvent;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Settings {

    @Nonnull
    private static final JsonConfig config = new JsonConfig("plugins/TNLListener", "settings.json");
    @Nonnull
    public static final Setting<Boolean> DEBUG = new Setting<>("debug", true);
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
    public static final Setting<String> TAB_COMPLETE_BYPASS_PERMISSION = new Setting<>("tab-complete-bypass-permission", "tnl.tab");
    @Nonnull
    public static final Setting<JsonArray> SERVERS = new Setting<>("servers", new JsonArray());

    protected Settings() {
    }

    @Nonnull
    public static JsonConfig getConfig() {
        return config;
    }

    public static void init() {
        Bukkit.getPluginManager().callEvent(new MessagesInitializeEvent());
        SERVERS.getValue().add("example-1");
        SERVERS.getValue().add("example-2");
        SERVERS.getValue().add("example-3");
        for (Setting<?> setting : Setting.getList()) {
            if (getConfig().getJsonElement().getAsJsonObject().has(setting.getKey())) {
                if (setting.getValue() instanceof String) {
                    String value = getConfig().getJsonElement().getAsJsonObject().get(setting.getKey()).getAsString();
                    if (value != null) {
                        ((Setting<String>) setting).setValue(value);
                    }
                    Logger.debug.println("§aLoaded setting §8'§6" + setting.getKey() + "§8'§a with value §8'§6" + setting.getValue() + "§8'");
                } else if (setting.getValue() instanceof Boolean) {
                    Boolean value = getConfig().getJsonElement().getAsJsonObject().get(setting.getKey()).getAsBoolean();
                    ((Setting<Boolean>) setting).setValue(value);
                    Logger.debug.println("§aLoaded setting §8'§6" + setting.getKey() + "§8'§a with value §8'§6" + setting.getValue() + "§8'");
                } else if (setting.getValue() instanceof Byte) {
                    Byte value = getConfig().getJsonElement().getAsJsonObject().get(setting.getKey()).getAsByte();
                    ((Setting<Byte>) setting).setValue(value);
                    Logger.debug.println("§aLoaded setting §8'§6" + setting.getKey() + "§8'§a with value §8'§6" + setting.getValue() + "§8'");
                } else if (setting.getValue() instanceof JsonArray) {
                    JsonArray value = getConfig().getJsonElement().getAsJsonObject().get(setting.getKey()).getAsJsonArray();
                    ((Setting<JsonArray>) setting).setValue(value);
                    List<String> strings = new ArrayList<>();
                    for (JsonElement jsonElement : ((Setting<JsonArray>) setting).getValue()) {
                        strings.add(jsonElement.getAsString());
                    }
                    Logger.debug.println("§aLoaded setting §8'§6" + setting.getKey() + "§8'§a with value §8'§6" + String.join(", ", strings) + "§8'");
                } else if (setting.getValue() instanceof Integer) {
                    Integer value = getConfig().getJsonElement().getAsJsonObject().get(setting.getKey()).getAsInt();
                    ((Setting<Integer>) setting).setValue(value);
                    Logger.debug.println("§aLoaded setting §8'§6" + setting.getKey() + "§8'§a with value §8'§6" + setting.getValue() + "§8'");
                } else {
                    Logger.warn.println("§cUnset Setting Type §8§4'" + setting.getValue().getClass().getSimpleName() + "§8'", new IOException("unset setting type"));
                }
            } else {
                if (setting.getValue() instanceof String) {
                    String value = (String) setting.getValue();
                    getConfig().getJsonElement().getAsJsonObject().addProperty(setting.getKey(), value);
                    Logger.debug.println("§aAdded setting §8'§6" + setting.getKey() + "§8'§a with value §8'§6" + value + "§8'");
                } else if (setting.getValue() instanceof Boolean) {
                    Boolean value = (Boolean) setting.getValue();
                    getConfig().getJsonElement().getAsJsonObject().addProperty(setting.getKey(), value);
                    Logger.debug.println("§aAdded setting §8'§6" + setting.getKey() + "§8'§a with value §8'§6" + value + "§8'");
                } else if (setting.getValue() instanceof Byte) {
                    Byte value = (Byte) setting.getValue();
                    getConfig().getJsonElement().getAsJsonObject().addProperty(setting.getKey(), value);
                    Logger.debug.println("§aAdded setting §8'§6" + setting.getKey() + "§8'§a with value §8'§6" + value + "§8'");
                } else if (setting.getValue() instanceof JsonArray) {
                    JsonArray value = (JsonArray) setting.getValue();
                    getConfig().getJsonElement().getAsJsonObject().add(setting.getKey(), value);
                    List<String> strings = new ArrayList<>();
                    for (JsonElement jsonElement : value) {
                        strings.add(jsonElement.getAsString());
                    }
                    Logger.debug.println("§aAdded setting §8'§6" + setting.getKey() + "§8'§a with value §8'§6" + String.join(", ", strings) + "§8'");
                } else if (setting.getValue() instanceof Integer) {
                    Integer value = (Integer) setting.getValue();
                    getConfig().getJsonElement().getAsJsonObject().addProperty(setting.getKey(), value);
                    Logger.debug.println("§aAdded setting §8'§6" + setting.getKey() + "§8'§a with value §8'§6" + value + "§8'");
                } else {
                    Logger.warn.println("§cUnset Setting Type §8§4'" + setting.getValue().getClass().getSimpleName() + "§8'", new IOException("unset setting type"));
                }
            }
        }
        getConfig().save();
    }
}
