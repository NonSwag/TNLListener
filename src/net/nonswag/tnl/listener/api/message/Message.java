package net.nonswag.tnl.listener.api.message;

import net.nonswag.tnl.listener.api.file.JsonConfig;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.events.MessagesInitializeEvent;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Message {

    @Nonnull
    public static final ChatComponent PREFIX = new ChatComponent(new LanguageKey(Language.ROOT, MessageKey.PREFIX), "§8[§f§lTNL§8]§r");
    @Nonnull
    public static final ChatComponent LOG_INFO = new ChatComponent(new LanguageKey(Language.ROOT, MessageKey.LOG_INFO), "§8[§1%time% §8|§1 Info §8| §1%thread%§8]§a");
    @Nonnull
    public static final ChatComponent LOG_WARN = new ChatComponent(new LanguageKey(Language.ROOT, MessageKey.LOG_WARN), "§8[§e%time% §8|§e Warning §8| §e%thread%§8]§e");
    @Nonnull
    public static final ChatComponent LOG_ERROR = new ChatComponent(new LanguageKey(Language.ROOT, MessageKey.LOG_ERROR), "§8[§4%time% §8|§4 Error §8| §4%thread%§8]§c");
    @Nonnull
    public static final ChatComponent LOG_DEBUG = new ChatComponent(new LanguageKey(Language.ROOT, MessageKey.LOG_DEBUG), "§8[§6%time% §8|§6 Debug §8| §6%thread%§8]§a");
    @Nonnull
    public static final ChatComponent CHAT_FORMAT = new ChatComponent(new LanguageKey(Language.ROOT, MessageKey.CHAT_FORMAT), "§8[§f%world%§8] §f%player% §8» §f%message%");
    @Nonnull
    public static final ChatComponent SERVER_BRAND = new ChatComponent(new LanguageKey(Language.ROOT, MessageKey.SERVER_BRAND), "§8» §f%version%§7");
    @Nonnull
    public static final ChatComponent ITEM_NAME_STANDARD = new ChatComponent(new LanguageKey(Language.ROOT, MessageKey.ITEM_NAME_STANDARD), "§8* §3%item_name%");
    @Nonnull
    public static final ChatComponent ITEM_NAME_RARE = new ChatComponent(new LanguageKey(Language.ROOT, MessageKey.ITEM_NAME_RARE), "§8* §5%item_name%");
    @Nonnull
    public static final ChatComponent ITEM_NAME_EPIC = new ChatComponent(new LanguageKey(Language.ROOT, MessageKey.ITEM_NAME_EPIC), "§8* §d%item_name%");
    @Nonnull
    public static final ChatComponent KICKED_SPAMMING = new ChatComponent(new LanguageKey(Language.ROOT, MessageKey.KICKED_SPAMMING), "§cSpamming is prohibited");

    @Nonnull
    public static final ChatComponent NO_PERMISSION_EN = new ChatComponent(new LanguageKey(Language.ENGLISH, MessageKey.NO_PERMISSION), "§4%prefix%§c You have no Rights §8(§4%permission%§8)");
    @Nonnull
    public static final ChatComponent DISABLED_COMMAND_EN = new ChatComponent(new LanguageKey(Language.ENGLISH, MessageKey.DISABLED_COMMAND), "§4%prefix%§c The Command §8(§4%command%§8)§c is disabled");
    @Nonnull
    public static final ChatComponent UNKNOWN_COMMAND_EN = new ChatComponent(new LanguageKey(Language.ENGLISH, MessageKey.UNKNOWN_COMMAND), "§4%prefix%§c The Command §8(§4%command%§8)§c doesn't exist");
    @Nonnull
    public static final ChatComponent PLAYER_COMMAND_EN = new ChatComponent(new LanguageKey(Language.ENGLISH, MessageKey.PLAYER_COMMAND), "§4%prefix%§c This is a player command");
    @Nonnull
    public static final ChatComponent CONSOLE_COMMAND_EN = new ChatComponent(new LanguageKey(Language.ENGLISH, MessageKey.CONSOLE_COMMAND), "§4%prefix%§c This is a console command");
    @Nonnull
    public static final ChatComponent KICKED_EN = new ChatComponent(new LanguageKey(Language.ENGLISH, MessageKey.KICKED), "§4%prefix% §cYou got kicked%nl%§4%reason%");
    @Nonnull
    public static final ChatComponent FIRST_JOIN_MESSAGE_EN = new ChatComponent(new LanguageKey(Language.ENGLISH, MessageKey.FIRST_JOIN_MESSAGE), "§6%prefix%§6 %player%§a joined the game §8(§7the first time§8)");
    @Nonnull
    public static final ChatComponent JOIN_MESSAGE_EN = new ChatComponent(new LanguageKey(Language.ENGLISH, MessageKey.JOIN_MESSAGE), "§6%prefix%§6 %player%§a joined the game");
    @Nonnull
    public static final ChatComponent QUIT_MESSAGE_EN = new ChatComponent(new LanguageKey(Language.ENGLISH, MessageKey.QUIT_MESSAGE), "§4%prefix%§4 %player%§c left the game");
    @Nonnull
    public static final ChatComponent WORLD_SAVED_EN = new ChatComponent(new LanguageKey(Language.ENGLISH, MessageKey.WORLD_SAVED), "§6%prefix%§a Saved the world §6%world%");
    @Nonnull
    public static final ChatComponent CHANGED_GAMEMODE_EN = new ChatComponent(new LanguageKey(Language.ENGLISH, MessageKey.CHANGED_GAMEMODE), "§6%prefix%§a Your gamemode is now §6%gamemode%");
    @Nonnull
    public static final ChatComponent PLAYER_NOT_ONLINE_EN = new ChatComponent(new LanguageKey(Language.ENGLISH, MessageKey.PLAYER_NOT_ONLINE), "§4%prefix%§4 %player%§c is not online");
    @Nonnull
    public static final ChatComponent NOT_A_PLAYER_EN = new ChatComponent(new LanguageKey(Language.ENGLISH, MessageKey.NOT_A_PLAYER), "§4%prefix%§4 %player%§c is not a player");

    @Nonnull
    public static final ChatComponent NO_PERMISSION_DE = new ChatComponent(new LanguageKey(Language.GERMAN, MessageKey.NO_PERMISSION), "§4%prefix%§c Darauf hast du keine Rechte §8(§4%permission%§8)");
    @Nonnull
    public static final ChatComponent UNKNOWN_COMMAND_DE = new ChatComponent(new LanguageKey(Language.GERMAN, MessageKey.UNKNOWN_COMMAND), "§4%prefix%§c Der Command §8(§4%command%§8)§c existiert nicht");
    @Nonnull
    public static final ChatComponent DISABLED_COMMAND_DE = new ChatComponent(new LanguageKey(Language.GERMAN, MessageKey.DISABLED_COMMAND), "§4%prefix%§c Der Command §8(§4%command%§8)§c ist deaktiviert");
    @Nonnull
    public static final ChatComponent PLAYER_COMMAND_DE = new ChatComponent(new LanguageKey(Language.GERMAN, MessageKey.PLAYER_COMMAND), "§4%prefix%§c Das ist ein spieler command");
    @Nonnull
    public static final ChatComponent CONSOLE_COMMAND_DE = new ChatComponent(new LanguageKey(Language.GERMAN, MessageKey.CONSOLE_COMMAND), "§4%prefix%§c Das ist ein konsolen command");
    @Nonnull
    public static final ChatComponent KICKED_DE = new ChatComponent(new LanguageKey(Language.GERMAN, MessageKey.KICKED), "§4%prefix% §cDu wurdest gekickt%nl%§4%reason%");
    @Nonnull
    public static final ChatComponent FIRST_JOIN_MESSAGE_DE = new ChatComponent(new LanguageKey(Language.GERMAN, MessageKey.FIRST_JOIN_MESSAGE), "§6%prefix%§6 %player%§a ist dem server beigetreten §8(§7zum ersten mal§8)");
    @Nonnull
    public static final ChatComponent JOIN_MESSAGE_DE = new ChatComponent(new LanguageKey(Language.GERMAN, MessageKey.JOIN_MESSAGE), "§6%prefix%§6 %player%§a ist dem server beigetreten");
    @Nonnull
    public static final ChatComponent QUIT_MESSAGE_DE = new ChatComponent(new LanguageKey(Language.GERMAN, MessageKey.QUIT_MESSAGE), "§4%prefix%§4 %player%§c hat den server verlassen");
    @Nonnull
    public static final ChatComponent WORLD_SAVED_DE = new ChatComponent(new LanguageKey(Language.GERMAN, MessageKey.WORLD_SAVED), "§6%prefix%§a Die welt §6%world%§a wurde gespeichert");
    @Nonnull
    public static final ChatComponent CHANGED_GAMEMODE_DE = new ChatComponent(new LanguageKey(Language.GERMAN, MessageKey.CHANGED_GAMEMODE), "§6%prefix%§a Dein gamemode ist jetzt §6%gamemode%");
    @Nonnull
    public static final ChatComponent PLAYER_NOT_ONLINE_DE = new ChatComponent(new LanguageKey(Language.GERMAN, MessageKey.PLAYER_NOT_ONLINE), "§4%prefix%§4 %player%§c ist nicht online");
    @Nonnull
    public static final ChatComponent NOT_A_PLAYER_DE = new ChatComponent(new LanguageKey(Language.GERMAN, MessageKey.NOT_A_PLAYER), "§4%prefix%§4 %player%§c ist kein spieler");

    protected Message() {
    }

    @Nullable
    public static ChatComponent valueOf(@Nonnull LanguageKey languageKey) {
        for (ChatComponent message : ChatComponent.getMessages()) {
            if (message.getLanguageKey().equals(languageKey)) {
                return message;
            }
        }
        return null;
    }

    public static void init() {
        Bukkit.getPluginManager().callEvent(new MessagesInitializeEvent());
        for (Language language : Language.values()) {
            JsonConfig jsonConfig = new JsonConfig("plugins/TNLListener/Messages/", language.getFile());
            for (MessageKey key : MessageKey.getKeys()) {
                String string = "§4%prefix%§c Undefined message §8§4'" + key.getKey() + "§8'§c for language §8'§4" + language.getName() + "§8'";
                if (!language.getShorthand().isEmpty()) {
                    string = string + " §8(§4" + language.getShorthand() + "§8)";
                }
                if ((key.isSystemMessage() && !language.equals(Language.ROOT)) || (!key.isSystemMessage() && language.equals(Language.ROOT))) {
                    continue;
                }
                boolean exists = false;
                if (jsonConfig.getJsonElement().getAsJsonObject().has(key.getKey())) {
                    for (ChatComponent message : ChatComponent.getMessages()) {
                        if (language.equals(message.getLanguageKey().getLanguage()) && message.getLanguageKey().getMessageKey().equals(key)) {
                            String value = jsonConfig.getJsonElement().getAsJsonObject().get(key.getKey()).getAsString();
                            if (value != null) {
                                message.setText(value);
                                Logger.debug.println("§aLoaded component §8'§6" + key.getKey() + "§8'§a with value §8'§6" + value + "§8'§6 for language §8'§f" + language.getName() + "§8'");
                            } else {
                                message.setText(message.getText());
                                jsonConfig.getJsonElement().getAsJsonObject().addProperty(key.getKey(), message.getText());
                                Logger.debug.println("§aAdded component §8'§6" + key.getKey() + "§8'§a with value §8'§6" + message.getText() + "§8'§6 for language §8'§f" + language.getName() + "§8'");
                            }
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        String value = jsonConfig.getJsonElement().getAsJsonObject().get(key.getKey()).getAsString();
                        if (value != null) {
                            new ChatComponent(new LanguageKey(language, key), value);
                            Logger.debug.println("§aLoaded component §8'§6" + key.getKey() + "§8'§a with value §8'§6" + value + "§8'§6 for language §8'§f" + language.getName() + "§8'");
                        } else {
                            new ChatComponent(new LanguageKey(language, key), string);
                            Logger.debug.println("§aAdded component §8'§6" + key.getKey() + "§8'§a with value §8'§6" + string + "§8'§6 for language §8'§f" + language.getName() + "§8'");
                        }
                    }
                } else {
                    for (ChatComponent message : ChatComponent.getMessages()) {
                        if (message.getLanguageKey().getLanguage().equals(language) && message.getLanguageKey().getMessageKey().equals(key)) {
                            jsonConfig.getJsonElement().getAsJsonObject().addProperty(key.getKey(), message.getText());
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        jsonConfig.getJsonElement().getAsJsonObject().addProperty(key.getKey(), string);
                    }
                    Logger.debug.println("§aAdded component §8'§6" + key.getKey() + "§8'§a with value §8'§6" + jsonConfig.getJsonElement().getAsJsonObject().get(key.getKey()).getAsString() + "§8'§6 for language §8'§f" + language.getName() + "§8'");
                }
            }
            jsonConfig.save();
        }
        Placeholder.Registry.updateValue(new Placeholder("prefix", Message.PREFIX.getText()));
    }
}
