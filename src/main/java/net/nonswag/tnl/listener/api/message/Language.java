package net.nonswag.tnl.listener.api.message;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Language {

    @Nonnull
    private static final List<Language> languages = new ArrayList<>();

    @Nonnull
    public static final Language ROOT = new Language("system", "");
    @Nonnull
    public static final Language UNKNOWN = new Language("Unknown", "");

    @Nonnull
    public static final Language AFRIKAANS = new Language("Afrikaans", "af_za");
    @Nonnull
    public static final Language ARABIC = new Language("Arabic", "ar_sa");
    @Nonnull
    public static final Language ASTURIAN = new Language("Asturian", "ast_es");
    @Nonnull
    public static final Language AZERBAIJANI = new Language("Azerbaijani", "az_az");
    @Nonnull
    public static final Language BASHKIR = new Language("Bashkir", "ba_ru");
    @Nonnull
    public static final Language BAVARIAN = new Language("Bavarian", "bar");
    @Nonnull
    public static final Language BELARUSIAN = new Language("Belarusian", "be_by");
    @Nonnull
    public static final Language BULGARIAN = new Language("Bulgarian", "bg_bg");
    @Nonnull
    public static final Language BRETON = new Language("Breton", "br_fr");
    @Nonnull
    public static final Language BRABANTIAN = new Language("Brabantian", "brb");
    @Nonnull
    public static final Language BOSNIAN = new Language("Bosnian", "bs_ba");
    @Nonnull
    public static final Language CATALAN = new Language("Catalan", "ca_es");
    @Nonnull
    public static final Language CZECH = new Language("Czech", "cs_cz");
    @Nonnull
    public static final Language WELSH = new Language("Welsh", "cy_gb");
    @Nonnull
    public static final Language DANISH = new Language("Danish", "da_dk");
    @Nonnull
    public static final Language AUSTRIAN_GERMAN = new Language("Austrian German", "de_at");
    @Nonnull
    public static final Language SWISS_GERMAN = new Language("Swiss German", "de_ch");
    @Nonnull
    public static final Language GERMAN = new Language("German", "de_de");
    @Nonnull
    public static final Language GREEK = new Language("Greek", "el_gr");
    @Nonnull
    public static final Language AUSTRALIAN_ENGLISH = new Language("Australian English", "en_au");
    @Nonnull
    public static final Language CANADIAN_ENGLISH = new Language("Canadian English", "en_ca");
    @Nonnull
    public static final Language BRITISH_ENGLISH = new Language("British English", "en_gb");
    @Nonnull
    public static final Language NEW_ZEALAND_ENGLISH = new Language("New Zealand English", "en_nz");
    @Nonnull
    public static final Language SOUTH_AFRICAN_ENGLISH = new Language("South African English", "en_za");
    @Nonnull
    public static final Language PIRATE_ENGLISH = new Language("Pirate English", "en_pt");
    @Nonnull
    public static final Language BRITISH_ENGLISH_UPSIDE_DOWN = new Language("British English (upside down)", "en_ud");
    @Nonnull
    public static final Language AMERICAN_ENGLISH = new Language("American English", "en_us");
    @Nonnull
    public static final Language OLD_ENGLISH = new Language("Old English", "enp");
    @Nonnull
    public static final Language EARLY_MODERN_ENGLISH = new Language("Early Modern English", "enws");
    @Nonnull
    public static final Language ESPERANTO = new Language("Esperanto", "eo_uy");
    @Nonnull
    public static final Language ARGENTINIAN_SPANISH = new Language("Argentinian Spanish", "es_ar");
    @Nonnull
    public static final Language CHILEAN_SPANISH = new Language("Chilean Spanish", "es_cl");
    @Nonnull
    public static final Language ECUADORIAN_SPANISH = new Language("Ecuadorian Spanish", "es_ec");
    @Nonnull
    public static final Language SPANISH = new Language("Spanish", "es_es");
    @Nonnull
    public static final Language MEXICAN_SPANISH = new Language("Mexican Spanish", "es_mx");
    @Nonnull
    public static final Language URUGUAYAN_SPANISH = new Language("Uruguayan Spanish", "es_uy");
    @Nonnull
    public static final Language VENEZUELAN_SPANISH = new Language("Venezuelan Spanish", "es_ve");
    @Nonnull
    public static final Language ANDALUSIAN = new Language("Andalusian", "esan");
    @Nonnull
    public static final Language ESTONIAN = new Language("Estonian", "et_ee");
    @Nonnull
    public static final Language BASQUE = new Language("Basque", "eu_es");
    @Nonnull
    public static final Language PERSIAN = new Language("Persian", "fa_ir");
    @Nonnull
    public static final Language FINNISH = new Language("Finnish", "fi_fi");
    @Nonnull
    public static final Language FILIPINO = new Language("Filipino", "fil_ph");
    @Nonnull
    public static final Language FAROESE = new Language("Faroese", "fo_fo");
    @Nonnull
    public static final Language CANADIAN_FRENCH = new Language("Canadian French", "fr_ca");
    @Nonnull
    public static final Language FRENCH = new Language("French", "fr_fr");
    @Nonnull
    public static final Language EAST_FRANCONIAN = new Language("East Franconian", "fra_de");
    @Nonnull
    public static final Language FRISIAN = new Language("Frisian", "fy_nl");
    @Nonnull
    public static final Language IRISH = new Language("Irish", "ga_ie");
    @Nonnull
    public static final Language SCOTTISH_GAELIC = new Language("Scottish Gaelic", "gd_gb");
    @Nonnull
    public static final Language GALICIAN = new Language("Galician", "gl_es");
    @Nonnull
    public static final Language MANX = new Language("Manx", "gv_im");
    @Nonnull
    public static final Language HAWAIIAN = new Language("Hawaiian", "haw_us");
    @Nonnull
    public static final Language HEBREW = new Language("Hebrew", "he_il");
    @Nonnull
    public static final Language HINDI = new Language("Hindi", "hi_in");
    @Nonnull
    public static final Language CROATIAN = new Language("Croatian", "hr_hr");
    @Nonnull
    public static final Language HUNGARIAN = new Language("Hungarian", "hu_hu");
    @Nonnull
    public static final Language ARMENIAN = new Language("Armenian", "hy_am");
    @Nonnull
    public static final Language INDONESIAN = new Language("Indonesian", "id_id");
    @Nonnull
    public static final Language IGBO = new Language("Igbo", "ig_ng");
    @Nonnull
    public static final Language IDO = new Language("Ido", "io_en");
    @Nonnull
    public static final Language ICELANDIC = new Language("Icelandic", "is_is");
    @Nonnull
    public static final Language INTERSLAVIC = new Language("Interslavic", "isv");
    @Nonnull
    public static final Language ITALIAN = new Language("Italian", "isv");
    @Nonnull
    public static final Language JAPANESE = new Language("Japanese", "ja_jp");
    @Nonnull
    public static final Language LOJBAN = new Language("Lojban", "jbo_en");
    @Nonnull
    public static final Language GEORGIAN = new Language("Georgian", "ka_ge");
    @Nonnull
    public static final Language KAZAKH = new Language("Kazakh", "kk_kz");
    @Nonnull
    public static final Language KANNADA = new Language("Kannada", "kn_in");
    @Nonnull
    public static final Language KOREAN = new Language("Korean", "kn_in");
    @Nonnull
    public static final Language KOELSCH = new Language("Koelsch", "ksh");
    @Nonnull
    public static final Language CORNISH = new Language("Cornish", "kw_gb");
    @Nonnull
    public static final Language LATIN = new Language("Latin", "la_la");
    @Nonnull
    public static final Language LUXEMBOURGISH = new Language("Luxembourgish", "lb_lu");
    @Nonnull
    public static final Language LIMBURGISH = new Language("Limburgish", "li_li");
    @Nonnull
    public static final Language LOLCAT = new Language("LOLCAT", "lol_us");
    @Nonnull
    public static final Language LITHUANIAN = new Language("Lithuanian", "lt_lt");
    @Nonnull
    public static final Language LATVIAN = new Language("Latvian", "lv_lv");
    @Nonnull
    public static final Language MACEDONIAN = new Language("Macedonian", "mk_mk");
    @Nonnull
    public static final Language MONGOLIAN = new Language("Mongolian", "mk_mk");
    @Nonnull
    public static final Language MALAY = new Language("Malay", "ms_my");
    @Nonnull
    public static final Language MALTESE = new Language("Maltese", "mt_mt");
    @Nonnull
    public static final Language LOW_GERMAN = new Language("Low German", "nds_de");
    @Nonnull
    public static final Language FLEMISH = new Language("Flemish", "nl_be");
    @Nonnull
    public static final Language DUTCH = new Language("Dutch", "nl_nl");
    @Nonnull
    public static final Language NORWEGIAN = new Language("Norwegian", "nn_no");
    @Nonnull
    public static final Language OCCITAN = new Language("Occitan", "oc_fr");
    @Nonnull
    public static final Language ELFDALIAN = new Language("Elfdalian", "ovd");
    @Nonnull
    public static final Language POLISH = new Language("Polish", "pl_pl");
    @Nonnull
    public static final Language BRAZILIAN_PORTUGUESE = new Language("Brazilian Portuguese", "pt_br");
    @Nonnull
    public static final Language PORTUGUESE = new Language("Portuguese", "pt_pt");
    @Nonnull
    public static final Language QUENYA = new Language("Quenya", "qya_aa");
    @Nonnull
    public static final Language ROMANIAN = new Language("Romanian", "ro_ro");
    @Nonnull
    public static final Language RUSSIAN_PRE_REVOLUTIONARY = new Language("Russian (Pre-revolutionary)", "rpr");
    @Nonnull
    public static final Language RUSSIAN = new Language("Russian", "ru_ru");
    @Nonnull
    public static final Language SICILIAN = new Language("Sicilian", "scn");
    @Nonnull
    public static final Language NORTHERN_SAMI = new Language("Northern Sami", "se_no");
    @Nonnull
    public static final Language SLOVAK = new Language("Slovak", "sk_sk");
    @Nonnull
    public static final Language SLOVENIAN = new Language("Slovenian", "sl_si");
    @Nonnull
    public static final Language SOMALI = new Language("Somali", "so_so");
    @Nonnull
    public static final Language ALBANIAN = new Language("Albanian", "sq_al");
    @Nonnull
    public static final Language SERBIAN = new Language("Serbian", "sr_sp");
    @Nonnull
    public static final Language SWEDISH = new Language("Swedish", "sv_se");
    @Nonnull
    public static final Language ALLGOVIAN_GERMAN = new Language("Allgovian German", "swg");
    @Nonnull
    public static final Language UPPER_SAXON_GERMAN = new Language("Upper Saxon German", "sxu");
    @Nonnull
    public static final Language SILESIAN = new Language("Silesian", "szl");
    @Nonnull
    public static final Language TAMIL = new Language("Tamil", "ta_in");
    @Nonnull
    public static final Language THAI = new Language("Thai", "th_th");
    @Nonnull
    public static final Language TAGALOG = new Language("Tagalog", "tl_ph");
    @Nonnull
    public static final Language KLINGON = new Language("Klingon", "tlh_aa");
    @Nonnull
    public static final Language TURKISH = new Language("Turkish", "tr_tr");
    @Nonnull
    public static final Language TATAR = new Language("Tatar", "tt_ru");
    @Nonnull
    public static final Language UKRAINIAN = new Language("Ukrainian", "uk_ua");
    @Nonnull
    public static final Language VALENCIAN = new Language("Valencian", "val_es");
    @Nonnull
    public static final Language VENETIAN = new Language("Venetian", "vec_it");
    @Nonnull
    public static final Language VIETNAMESE = new Language("Vietnamese", "vi_vn");
    @Nonnull
    public static final Language YIDDISH = new Language("Yiddish", "yi_de");
    @Nonnull
    public static final Language YORUBA = new Language("Yoruba", "yo_ng");
    @Nonnull
    public static final Language SIMPLIFIED_CHINESE = new Language("Simplified Chinese", "zh_cn");
    @Nonnull
    public static final Language TRADITIONAL_CHINESE_HONG_KONG = new Language("Traditional Chinese (Hong Kong)", "zh_hk");
    @Nonnull
    public static final Language TRADITIONAL_CHINESE_TAIWAN = new Language("Traditional Chinese (Taiwan)", "zh_tw");

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
        getLanguages().add(this);
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

    @Nonnull
    public static Language fromLocale(@Nonnull String locale) {
        try {
            for (Language language : getLanguages()) {
                if (language.getShorthand().equalsIgnoreCase(locale) || language.getName().equalsIgnoreCase(locale)) {
                    return language;
                }
            }
        } catch (IllegalArgumentException | NullPointerException ignored) {
        }
        return Language.UNKNOWN;
    }

    @Nonnull
    public static List<Language> getLanguages() {
        return languages;
    }
}
