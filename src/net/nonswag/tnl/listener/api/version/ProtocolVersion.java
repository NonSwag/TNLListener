package net.nonswag.tnl.listener.api.version;

import javax.annotation.Nonnull;

public enum ProtocolVersion {
    UNKNOWN("Unknown", -1),
    LEGACY("Legacy", -2),
    MINECRAFT_1_7_2("1.7.2", 4),
    MINECRAFT_1_7_6("1.7.6", 5),
    MINECRAFT_1_8("1.8", 47),
    MINECRAFT_1_9("1.9", 107),
    MINECRAFT_1_9_1("1.9.1", 108),
    MINECRAFT_1_9_2("1.9.2", 109),
    MINECRAFT_1_9_4("1.9.4", 110),
    MINECRAFT_1_10("1.10", 210),
    MINECRAFT_1_11("1.11", 315),
    MINECRAFT_1_11_1("1.11.1", 316),
    MINECRAFT_1_12("1.12", 335),
    MINECRAFT_1_12_1("1.12.1", 338),
    MINECRAFT_1_12_2("1.12.2", 340),
    MINECRAFT_1_13("1.13", 393),
    MINECRAFT_1_13_1("1.13.1", 401),
    MINECRAFT_1_13_2("1.13.2", 404),
    MINECRAFT_1_14("1.14", 477),
    MINECRAFT_1_14_1("1.14.1", 480),
    MINECRAFT_1_14_2("1.14.2", 485),
    MINECRAFT_1_14_3("1.14.3", 490),
    MINECRAFT_1_14_4("1.14.4", 498),
    MINECRAFT_1_15("1.15", 573),
    MINECRAFT_1_15_1("1.15.1", 575),
    MINECRAFT_1_15_2("1.15.2", 578),
    MINECRAFT_1_16("1.16", 735),
    MINECRAFT_1_16_1("1.16.1", 736),
    MINECRAFT_1_16_2("1.16.2", 751),
    MINECRAFT_1_16_3("1.16.3", 753),
    MINECRAFT_1_16_4("1.16.4", 754);

    @Nonnull
    private final String name;
    private final int protocol;

    ProtocolVersion(@Nonnull String name, int protocol) {
        this.name = name;
        this.protocol = protocol;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public int getProtocol() {
        return protocol;
    }

    @Nonnull
    public static ProtocolVersion getProtocolVersion(int protocol) {
        for (ProtocolVersion version : values()) {
            if (version.getProtocol() == protocol) {
                return version;
            }
        }
        return ProtocolVersion.UNKNOWN;
    }
}
