package net.nonswag.tnl.listener.api.message;

import javax.annotation.Nonnull;

public enum Language {
    ROOT("system", ""),
    ENGLISH("english", "en_us"),
    GERMAN("german", "de_de"),
    WELSH("welsh", "cy"),
    XHOSA("xhosa", "xh"),
    YIDDISH("yiddish", "yi"),
    YORUBA("yoruba", "yo"),
    URDU("urdu", "ur"),
    UYGHUR("uyghur", "ug"),
    UZBEK("uzbek", "uz"),
    VIETNAMESE("vietnamese", "vi"),
    TAJIK("tajik", "tg"),
    TAMIL("tamil", "ta"),
    TATAR("tatar", "tt"),
    TELUGU("telugu", "te"),
    THAI("thai", "th"),
    TURKISH("turkish", "tr"),
    TURKMEN("turkmen", "tk"),
    UKRAINIAN("ukrainian", "uk"),
    SINDHI("sindhi", "sd"),
    SINHALA("sinhala", "si"),
    SLOVAK("slovak", "sk"),
    SLOVENIAN("slovenian", "sl"),
    SOMALI("somali", "so"),
    SPANISH("spanish", "es"),
    SUNDANESE("sudanese", "su"),
    SWAHILI("swahili", "sw"),
    SWEDISH("swedish", "sv"),
    TAGALOG("tagalog", "tl"),
    AFRIKAANS("afrikaans", "af"),
    ALBANIA("albania", "sq"),
    AMHARIC("amharic", "am"),
    ARABIC("arabic", "ar"),
    ARMENIAN("armenian", "hy"),
    AZERBAIJANI("azerbaijani", "az"),
    BASQUE("basque", "eu"),
    BELARUSIAN("belarusian", "be"),
    BENGALI("bengali", "bn"),
    BOSNIAN("bosnian", "bs"),
    BULGARIAN("bulgarian", "bg"),
    CATALAN("catalan", "ca"),
    CEBUANO("cebuano", "ceb"),
    CHINESE_SIMPLIFIED("simplified chinese", "zh-CN"),
    CHINESE_TRADITIONAL("traditional chinese", "zh-TW"),
    CORSICAN("corsican", "co"),
    CROATIAN("croatian", "hr"),
    CZECH("czech", "cs"),
    DANISH("danish", "da"),
    DUTCH("dutch", "nl"),
    ESPERANTO("esperanto", "eo"),
    ESTONIAN("estonian", "et"),
    FINNISH("finnish", "fi"),
    FRENCH("french", "fr"),
    FRISIAN("frisian", "fy"),
    GALICIAN("galician", "gl"),
    GEORGIAN("georgian", "ka"),
    GREEK("greek", "el"),
    GUJARATI("gujarati", "gu"),
    HAITIAN("haitian", "creole-ht"),
    HAUSA("hausa", "ha"),
    HAWAIIAN("hawaiian", "haw"),
    HEBREW("hebrew", "he"),
    HINDI("hindi", "hi"),
    HMONG("hmong", "hmn"),
    HUNGARIAN("hungarian", "hu"),
    ICELANDIC("icelandic", "is"),
    IGBO("igbo", "ig"),
    INDONESIAN("indonesian", "id"),
    IRISH("irish", "ga"),
    ITALIAN("italian", "it"),
    JAPANESE("japanese", "ja"),
    JAVANESE("javanese", "jv"),
    KANNADA("kannada", "kn"),
    KAZAKH("kazakh", "kk"),
    KHMER("khmer", "km"),
    KINYARWANDA("kinyarwanda", "rw"),
    KOREAN("korean", "ko"),
    KURDISH("kurdish", "ku"),
    KYRGYZ("kyrgyz", "ky"),
    LAO("lao", "lo"),
    LATIN("latin", "la"),
    LATVIAN("latvian", "lv"),
    LITHUANIAN("lithuanian", "lt"),
    LUXENBOURGISH("luxenbourgish", "lb"),
    MACEDONIAN("macedonian", "mk"),
    MALAGASY("malagasy", "mg"),
    MALAY("malay", "ms"),
    MALAYALAM("malayalam", "ml"),
    MALTESE("maltese", "mt"),
    MAORI("maori", "mi"),
    MARATHI("marathi", "mr"),
    MONGOLIAN("mongolian", "mn"),
    MYANMAR("myanmar", "my"),
    NEPALI("nepali", "ne"),
    NORWEGIAN("norwegian", "no"),
    NYANJA("nyanja", "ny"),
    ODIA("odia", "or"),
    PASHTO("pashto", "ps"),
    PERSIAN("persian", "fa"),
    POLISH("polish", "pl"),
    PORTUGUESE("portuguese", "pt"),
    PUNJABI("punjabi", "pa"),
    ROMANIA("romania", "ro"),
    RUSSIAN("russian", "ru"),
    SAMOAN("samoan", "sm"),
    SCOTS_GAELIC("scots gaelic", "gd"),
    SERBIAN("serbian", "sr"),
    SESOTHO("sesotho", "st"),
    SHONA("shona", "sn"),
    ZULU("zulu", "zu");

    @Nonnull
    private final String name;
    @Nonnull
    private final String shorthand;
    @Nonnull
    private final String file;

    Language(@Nonnull String name, @Nonnull String shorthand) {
        this.name = name;
        this.shorthand = shorthand;
        this.file = ("messages" + (getShorthand().isEmpty() ? "" : "-" + getShorthand()) + ".json");
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public String getShorthand() {
        return shorthand;
    }

    @Nonnull
    public String getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "Language{" +
                "name='" + name + '\'' +
                ", shorthand='" + shorthand + '\'' +
                ", file='" + file + '\'' +
                '}';
    }

    @Nonnull
    public static Language fromLocale(@Nonnull String locale) {
        try {
            for (Language language : values()) {
                if (language.getShorthand().equalsIgnoreCase(locale) || language.getName().equalsIgnoreCase(locale)) {
                    return language;
                }
            }
            return Language.ENGLISH;
        } catch (IllegalArgumentException | NullPointerException e) {
            return Language.ENGLISH;
        }
    }
}
