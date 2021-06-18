package net.nonswag.tnl.listener.api.version;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public enum ServerVersion {
    UNKNOWN(-1, "Unknown"),
    v1_7_2(4, "1.7.2", "1.7.3", "1.7.4", "1.7.5"),
    v1_7_6(5, "1.7.6", "1.7.7", "1.7.8", "1.7.9", "1.7.10"),
    v1_15_2(578, "1.15.2"),
    v1_16_4(754, "1.16.4", "1.16.5"),
    ;

    @Nonnull
    private final List<String> versions;
    private final int protocol;

    ServerVersion(int protocol, @Nonnull String... versions) {
        this.protocol = protocol;
        this.versions = Arrays.asList(versions);
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

    public boolean isAtLeast(@Nonnull ServerVersion version) {
        return getProtocol() >= version.getProtocol();
    }

    @Nonnull
    @Override
    public String toString() {
        return getRecentVersion();
    }
}
