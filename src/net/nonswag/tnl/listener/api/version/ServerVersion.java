package net.nonswag.tnl.listener.api.version;

import javax.annotation.Nonnull;

public enum ServerVersion {
    UNKNOWN,
    v1_7_2("1.7.2"),
    v1_7_10("1.7.10"),
    v1_8_8("1.8.8"),
    v1_12_2("1.12.2"),
    v1_13_2("1.13.2"),
    v1_14_4("1.14.4"),
    v1_15_2("1.15.2"),
    v1_16_5("1.16.5"),
    ;

    @Nonnull
    private final String version;

    ServerVersion() {
        this.version = "unknown";
    }

    ServerVersion(@Nonnull String version) {
        this.version = version;
    }

    @Nonnull
    public String getVersion() {
        return version;
    }
}
