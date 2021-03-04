package net.nonswag.tnl.listener.api.logger;

import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.object.List;
import net.nonswag.tnl.listener.api.object.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {

    @Nonnull public static final Logger info = new Logger("§8[§1%time% §8|§1 %logger% §8| §1%thread%§8]§r", "Info");
    @Nonnull public static final Logger warn = new Logger("§8[§e%time% §8|§e %logger% §8| §e%thread%§8]§r", "Warn");
    @Nonnull public static final Logger debug = new Logger("§8[§6%time% §8|§6 %logger% §8| §6%thread%§8]§r", "Debug");
    @Nonnull public static final Logger error = new Logger("§8[§4%time% §8|§4 %logger% §8| §4%thread%§8]§r", "Error");

    static {
        FileOutputStream outputStream = new FileOutputStream(FileDescriptor.out);
        System.setOut(new PrintStream(outputStream, true));

        FileOutputStream errorStream = new FileOutputStream(FileDescriptor.err);
        System.setErr(new PrintStream(errorStream, true));

        FileInputStream inputStream = new FileInputStream(FileDescriptor.in);
        System.setIn(new BufferedInputStream(inputStream));
    }

    @Nonnull protected final String prefix;
    @Nonnull protected final String name;

    protected Logger(@Nonnull String prefix, @Nonnull String name) {
        for (Color color : Color.values()) {
            if (prefix.contains("<" + color.name().toLowerCase() + ">")) {
                prefix = prefix.replace("<" + color.name().toLowerCase() + ">", color.getAnsi());
            }
            if (prefix.contains(color.getCode())) {
                prefix = prefix.replace(color.getCode(), color.getAnsi());
            }
        }
        this.prefix = prefix;
        this.name = name;
    }

    @Nonnull
    protected String getPrefix() {
        return prefix;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public void printf(@Nonnull Object value, @Nonnull Placeholder... placeholders) {
        println(Placeholder.replace(value, placeholders));
    }

    public void printf(@Nonnull List<Set<Object, Placeholder[]>> values) {
        for (Set<Object, Placeholder[]> set : values.getObjects()) {
            printf(set.getKey(), set.getValue());
        }
    }

    public void println(@Nonnull Object... values) {
        for (@Nullable Object value : values) {
            if (value != null) {
                if (value instanceof Throwable) {
                    ((Throwable) value).printStackTrace();
                } else {
                    String string = value.toString() + "§r";
                    if (((getPrefix().contains("<") && prefix.contains(">")) || prefix.contains("§"))
                            || ((string.contains("<") && string.contains(">")) || string.contains("§"))) {
                        for (Color color : Color.values()) {
                            if (string.contains("<" + color.name().toLowerCase() + ">")) {
                                string = string.replace("<" + color.name().toLowerCase() + ">", color.getAnsi());
                            }
                            if (string.contains(color.getCode())) {
                                string = string.replace(color.getCode(), color.getAnsi());
                            }
                        }
                    }
                    if (getPrefix().isEmpty()) {
                        System.out.println(string);
                    } else {
                        System.out.println(Placeholder.replace(prefix, new Placeholder("logger", getName()), new Placeholder("thread", Thread.currentThread().getName()), new Placeholder("time", new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()))) + " " + string);
                    }
                }
            }
        }
    }
}
