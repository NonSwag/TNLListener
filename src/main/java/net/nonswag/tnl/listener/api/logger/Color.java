package net.nonswag.tnl.listener.api.logger;

import javax.annotation.Nonnull;

public enum Color {

    BOLD_BLACK("\033[1;30m", "§o§l"),
    BOLD_RED("\033[1;31m", "§c§l"),
    BOLD_GREEN("\033[1;32m", "§2§l"),
    BOLD_GOLD("\033[1;33m", "§6§l"),
    BOLD_AQUA("\033[1;34m", "§b§l"),
    BOLD_DARK_PURPLE("\033[1;35m", "§5§l"),
    BOLD_DARK_CYAN("\033[1;36m", "§9§l"),
    BOLD_GRAY("\033[1;90m", "§8§l"),
    BOLD_DARK_RED("\033[1;91m", "§4§l"),
    BOLD_LIME("\033[1;92m", "§a§l"),
    BOLD_YELLOW("\033[1;93m", "§e§l"),
    BOLD_BLUE("\033[1;94m", "§1§l"),
    BOLD_PURPLE("\033[1;95m", "§d§l"),
    BOLD_CYAN("\033[1;96m", "§3§l"),
    BOLD_WHITE("\033[1;97m", "§f§l"),

    UNDERLINED_BLACK("\033[4;30m", "§0§n"),
    UNDERLINED_RED("\033[4;31m", "§c§n"),
    UNDERLINED_DARK_GREEN("\033[4;32m", "§2§n"),
    UNDERLINED_GOLD("\033[4;33m", "§6§n"),
    UNDERLINED_AQUA("\033[4;34m", "§b§n"),
    UNDERLINED_DARK_PURPLE("\033[4;35m", "§5§n"),
    UNDERLINED_DARK_CYAN("\033[4;36m", "§9§n"),

    BACKGROUND_BLACK("\033[40m", "§0§k"),
    BACKGROUND_RED("\033[41m", "§c§k"),
    BACKGROUND_DARK_GREEN("\033[42m", "§2§k"),
    BACKGROUND_GOLD("\033[43m", "§6§k"),
    BACKGROUND_AQUA("\033[44m", "§b§k"),
    BACKGROUND_DARK_PURPLE("\033[45m", "§5§k"),
    BACKGROUND_DARK_CYAN("\033[46m", "§9§k"),
    BACKGROUND_GRAY("\033[0;100m", "§8§k"),
    BACKGROUND_DARK_RED("\033[0;101m", "§4§k"),
    BACKGROUND_LIME("\033[0;102m", "§a§k"),
    BACKGROUND_YELLOW("\033[0;103m", "§e§k"),
    BACKGROUND_BLUE("\033[0;104m", "§1§k"),
    BACKGROUND_PURPLE("\033[0;105m", "§d§k"),
    BACKGROUND_CYAN("\033[0;106m", "§3§k"),
    BACKGROUND_WHITE("\033[0;107m", "§f§k"),

    RESET("\033[0m", "§r"),
    BLACK("\033[0;30m", "§0"),
    RED("\033[0;31m", "§c"),
    LIME("\033[0;32m", "§a"),
    GREEN("\033[0;92m", "§2"),
    GOLD("\033[0;93m", "§6"),
    AQUA("\033[0;34m", "§b"),
    DARK_PURPLE("\033[0;35m", "§5"),
    DARK_CYAN("\033[0;96m", "§9"),
    DARK_GRAY("\033[0;90m", "§8"),
    GRAY("\033[0;37m", "§7"),
    DARK_RED("\033[0;91m", "§4"),
    YELLOW("\033[0;33m", "§e"),
    BLUE("\033[0;94m", "§1"),
    PURPLE("\033[0;95m", "§d"),
    CYAN("\033[0;36m", "§3"),
    WHITE("\033[0;97m", "§f"),
    ;

    @Nonnull
    private final String ansi;
    @Nonnull
    private final String code;

    Color(@Nonnull String ansi, @Nonnull String code) {
        this.ansi = ansi;
        this.code = code;
    }

    @Nonnull
    public String getAnsi() {
        return ansi;
    }

    @Nonnull
    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return getAnsi();
    }

    @Nonnull
    public static String replace(@Nonnull String string) {
        if ((string.contains("<") && string.contains(">")) || string.contains("§")) {
            for (Color color : Color.values()) {
                if (string.contains("<" + color.name().toLowerCase() + ">")) {
                    string = string.replace("<" + color.name().toLowerCase() + ">", color.getAnsi());
                }
                if (string.contains(color.getCode())) {
                    string = string.replace(color.getCode(), color.getAnsi());
                }
            }
        }
        return string;
    }

    public enum Minecraft {
        BLACK('0'),
        DARK_BLUE('1'),
        DARK_GREEN('2'),
        DARK_AQUA('3'),
        DARK_RED('4'),
        DARK_PURPLE('5'),
        GOLD('6'),
        GRAY('7'),
        DARK_GRAY('8'),
        BLUE('9'),
        GREEN('a'),
        AQUA('b'),
        RED('c'),
        LIGHT_PURPLE('d'),
        YELLOW('e'),
        WHITE('f'),
        MATRIX('k'),
        BOLD('l'),
        STRIKETHROUGH('m'),
        UNDERLINE('n'),
        ITALIC('o'),
        RESET('r'),
        ;

        private final char identifier;

        Minecraft(char identifier) {
            this.identifier = identifier;
        }

        public char getIdentifier() {
            return identifier;
        }

        @Nonnull
        public static String unColorize(@Nonnull String string, char prefix) {
            for (Minecraft minecraft : Minecraft.values()) {
                string = string.replace(String.valueOf(prefix) + minecraft.getIdentifier(), "");
            }
            return string;
        }

        @Nonnull
        public static String colorize(@Nonnull String string, char prefix) {
            for (Minecraft minecraft : Minecraft.values()) {
                string = string.replace(String.valueOf(prefix) + minecraft.getIdentifier(), "§" + minecraft.getIdentifier());
            }
            return string;
        }
    }
}
