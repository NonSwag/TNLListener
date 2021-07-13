package net.nonswag.tnl.listener.api.version;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Version {
    @Nonnull
    private static final List<Version> values = new ArrayList<>();

    @Nonnull
    public static final Version UNKNOWN = new Version(-1, "Unknown");
    @Nonnull
    public static final Version v1_7_2 = new Version(4, "1.7.2", "1.7.3", "1.7.4", "1.7.5");
    @Nonnull
    public static final Version v1_7_6 = new Version(5, "1.7.6", "1.7.7", "1.7.8", "1.7.9", "1.7.10");
    @Nonnull
    public static final Version v1_8 = new Version(47, "1.8", "1.8.1", "1.8.2", "1.8.3", "1.8.4", "1.8.5", "1.8.6", "1.8.7", "1.8.8", "1.8.9");
    @Nonnull
    public static final Version v1_9 = new Version(107, "1.9");
    @Nonnull
    public static final Version v1_9_1 = new Version(108, "1.9.1");
    @Nonnull
    public static final Version v1_9_2 = new Version(109, "1.9.2");
    @Nonnull
    public static final Version v1_9_4 = new Version(110, "1.9.3", "1.9.4");
    @Nonnull
    public static final Version v1_10 = new Version(210, "1.10", "1.10.1", "1.10.2");
    @Nonnull
    public static final Version v1_11 = new Version(315, "1.11");
    @Nonnull
    public static final Version v1_11_1 = new Version(316, "1.11.1", "1.11.2");
    @Nonnull
    public static final Version v1_12 = new Version(335, "1.12");
    @Nonnull
    public static final Version v1_12_1 = new Version(338, "1.12.1");
    @Nonnull
    public static final Version v1_12_2 = new Version(340, "1.12.2");
    @Nonnull
    public static final Version v1_13 = new Version(393, "1.13");
    @Nonnull
    public static final Version v1_13_1 = new Version(401, "1.13.1");
    @Nonnull
    public static final Version v1_13_2 = new Version(404, "1.13.2");
    @Nonnull
    public static final Version v1_14 = new Version(477, "1.14");
    @Nonnull
    public static final Version v1_14_1 = new Version(480, "1.14.1");
    @Nonnull
    public static final Version v1_14_2 = new Version(485, "1.14.2");
    @Nonnull
    public static final Version v1_14_3 = new Version(490, "1.14.3");
    @Nonnull
    public static final Version v1_14_4 = new Version(498, "1.14.4");
    @Nonnull
    public static final Version v1_15 = new Version(573, "1.15");
    @Nonnull
    public static final Version v1_15_1 = new Version(575, "1.15.1");
    @Nonnull
    public static final Version v1_15_2 = new Version(578, "1.15.2");
    @Nonnull
    public static final Version v1_16 = new Version(735, "1.16");
    @Nonnull
    public static final Version v1_16_1 = new Version(736, "1.16.1");
    @Nonnull
    public static final Version v1_16_2 = new Version(751, "1.16.2");
    @Nonnull
    public static final Version v1_16_3 = new Version(753, "1.16.3");
    @Nonnull
    public static final Version v1_16_4 = new Version(754, "1.16.4", "1.16.5");
    @Nonnull
    public static final Version v1_17 = new Version(755, "1.17");
    @Nonnull
    public static final Version v1_17_1 = new Version(756, "1.17.1");

    @Nonnull
    private final List<String> versions;
    private final int protocol;

    protected Version(int protocol, @Nonnull String... versions) {
        this(protocol, Arrays.asList(versions));
    }

    protected Version(int protocol, @Nonnull List<String> versions) {
        this.protocol = protocol;
        this.versions = versions;
        if (!values().contains(this)) values().add(this);
    }

    @Nonnull
    public List<String> getVersions() {
        return versions;
    }

    public int getProtocol() {
        return protocol;
    }

    @Nonnull
    public String getIntroducedVersion() {
        return getVersions().get(0);
    }

    @Nonnull
    public String getRecentVersion() {
        return getVersions().get(getVersions().size() - 1);
    }

    public boolean isAtLeast(@Nonnull Version Version) {
        return getProtocol() >= Version.getProtocol();
    }

    @Nonnull
    @Override
    public String toString() {
        return getRecentVersion();
    }

    @Nonnull
    public static Version valueOf(int protocol, @Nonnull Version defaultValue) {
        for (Version Version : values()) if (Version.getProtocol() == protocol) return Version;
        return defaultValue;
    }

    @Nonnull
    public static Version valueOf(int protocol) {
        return valueOf(protocol, UNKNOWN);
    }

    @Nonnull
    public static List<Version> values() {
        return values;
    }
}
