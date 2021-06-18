package net.nonswag.tnl.listener.api.logger;

import net.nonswag.tnl.listener.api.message.ChatComponent;
import net.nonswag.tnl.listener.api.message.Message;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.object.Pair;
import net.nonswag.tnl.listener.api.settings.Settings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Logger {

    @Nonnull
    public static final Logger info = new Logger("info", Message.LOG_INFO.getText()).colorize(Color.LIME, Color.GOLD);
    @Nonnull
    public static final Logger warn = new Logger("warn", Message.LOG_WARN.getText()).colorize(Color.YELLOW, Color.WHITE);
    @Nonnull
    public static final Logger debug = new Logger("debug", Message.LOG_DEBUG.getText()).colorize(Color.YELLOW, Color.GOLD);
    @Nonnull
    public static final Logger error = new Logger("error", Message.LOG_ERROR.getText()).colorize(Color.RED, Color.DARK_RED);

    static {
        FileOutputStream outputStream = new FileOutputStream(FileDescriptor.out);
        System.setOut(new PrintStream(outputStream, true));

        FileOutputStream errorStream = new FileOutputStream(FileDescriptor.err);
        System.setErr(new PrintStream(errorStream, true));

        FileInputStream inputStream = new FileInputStream(FileDescriptor.in);
        System.setIn(new BufferedInputStream(inputStream));
    }

    @Nonnull
    private final String name;
    @Nonnull
    private final String prefix;
    @Nonnull
    private Color mainColor = Color.RESET;
    @Nonnull
    private Color secondaryColor = Color.RESET;

    public Logger(@Nonnull String name, @Nonnull String prefix) {
        this.name = name;
        this.prefix = Color.replace(prefix);
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    protected String getPrefix() {
        return prefix;
    }

    @Nonnull
    public Color getMainColor() {
        return mainColor;
    }

    @Nonnull
    public Color getSecondaryColor() {
        return secondaryColor;
    }

    @Nonnull
    public Logger setMainColor(@Nonnull Color mainColor) {
        this.mainColor = mainColor;
        return this;
    }

    @Nonnull
    public Logger setSecondaryColor(@Nonnull Color secondaryColor) {
        this.secondaryColor = secondaryColor;
        return this;
    }

    @Nonnull
    public Logger colorize(@Nonnull Color mainColor, @Nonnull Color secondaryColor) {
        return setMainColor(mainColor).setSecondaryColor(secondaryColor);
    }

    public void printf(@Nonnull Object value, @Nonnull Placeholder... placeholders) {
        println(Placeholder.replace(value, placeholders));
    }

    public void printf(@Nonnull List<Pair<Object, Placeholder[]>> values) {
        for (Pair<Object, Placeholder[]> pair : values) {
            if (pair.getValue() != null) {
                printf(pair.getKey(), pair.getValue());
            } else {
                println(pair.getKey());
            }
        }
    }

    public void println(@Nonnull Object... values) {
        if (!Settings.DEBUG.getValue() && equals(debug)) return;
        for (@Nullable Object value : values) {
            if (value != null) {
                if (value instanceof Throwable) ((Throwable) value).printStackTrace();
                else {
                    String text = getMainColor().getCode() + value.toString().replace(".", "§8.%1%").
                            replace(",", "§8,%1%").replace("<'", "§8'%2%").replace("'>", "§8'%1%").
                            replace(":", "§8:%2%").replace("[", "§8[%2%").replace("]", "§8]%1%").
                            replace("(", "§8(%2%").replace(")", "§8)%1%").replace("{", "§8{%2%").
                            replace("}", "§8}%1%").replace("\"", "§8\"%1%").replace("/", "§8/%2%").
                            replace("\\", "§8\\%2%").replace("|", "§8|%2%").replace(">", "§8>%1%").
                            replace("<", "§8<%1%").replace("»", "§8»%1%").replace("«", "§8«%1%").
                            replace("%1%", getMainColor().getCode()).replace("%2%", getSecondaryColor().getCode());
                    if (getPrefix().isEmpty()) System.out.println(Color.replace(ChatComponent.getText(text + "§r")));
                    else {
                        System.out.println(Color.replace(ChatComponent.getText(prefix, new Placeholder("thread", Thread.currentThread().getName()), new Placeholder("time", new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()))) + " " + ChatComponent.getText(text + "§r")));
                    }
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Logger logger = (Logger) o;
        return name.equals(logger.name);
    }
}
