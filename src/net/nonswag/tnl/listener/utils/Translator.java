package net.nonswag.tnl.listener.utils;

import com.google.gson.JsonParser;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.enumerations.Language;

import java.io.InputStreamReader;
import java.net.URL;

public class Translator {

    public static String translate(String string, Language from, Language to) {
        try {
            if (from.equals(to)) {
                return string;
            }
            URL url = new URL("http://api.mymemory.translated.net/get?q=" + string.replace(" ", "%20").toLowerCase() + "&langpair=" + from.getId() + "|" + to.getId());
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String translatedText = new JsonParser().parse(reader).getAsJsonObject().get("responseData").getAsJsonObject().get("translatedText").getAsString();
            return translatedText.replace("\"\"", "").replace(",", "").replace("match", "").replace("-", "").replace("\\\\u", "").replace("\\u", "").replace("~", "");
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
        return string;
    }
}
