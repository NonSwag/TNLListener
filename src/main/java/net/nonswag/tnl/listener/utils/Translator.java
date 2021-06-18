package net.nonswag.tnl.listener.utils;

import com.google.gson.JsonParser;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.message.Language;

import javax.annotation.Nonnull;
import java.io.InputStreamReader;
import java.net.URL;

public class Translator {

    @Nonnull
    public static String translate(@Nonnull String string, @Nonnull Language from, @Nonnull Language to) {
        try {
            if (from.equals(to)) {
                return string;
            }
            URL url = new URL("http://api.mymemory.translated.net/get?q=" + string.replace(" ", "%20").toLowerCase() + "&langpair=" + from.getShorthand() + "|" + to.getShorthand());
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String translatedText = new JsonParser().parse(reader).getAsJsonObject().get("responseData").getAsJsonObject().get("translatedText").getAsString();
            return translatedText.replace("\"\"", "").replace(",", "").replace("match", "").replace("-", "").replace("\\\\u", "").replace("\\u", "").replace("~", "");
        } catch (Exception e) {
            Logger.error.println(e.getMessage());
        }
        return string;
    }
}
