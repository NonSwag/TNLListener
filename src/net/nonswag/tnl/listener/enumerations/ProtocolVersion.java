package net.nonswag.tnl.listener.enumerations;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

public enum ProtocolVersion {
    UNKNOWN(-1, "Unknown"),
    LEGACY(-2, "Legacy"),
    MINECRAFT_1_7_2(4, "1.7.2"),
    MINECRAFT_1_7_6(5, "1.7.6"),
    MINECRAFT_1_8(47, "1.8"),
    MINECRAFT_1_9(107, "1.9"),
    MINECRAFT_1_9_1(108, "1.9.1"),
    MINECRAFT_1_9_2(109, "1.9.2"),
    MINECRAFT_1_9_4(110, "1.9.4"),
    MINECRAFT_1_10(210, "1.10"),
    MINECRAFT_1_11(315, "1.11"),
    MINECRAFT_1_11_1(316, "1.11.1"),
    MINECRAFT_1_12(335, "1.12"),
    MINECRAFT_1_12_1(338, "1.12.1"),
    MINECRAFT_1_12_2(340, "1.12.2"),
    MINECRAFT_1_13(393, "1.13"),
    MINECRAFT_1_13_1(401, "1.13.1"),
    MINECRAFT_1_13_2(404, "1.13.2"),
    MINECRAFT_1_14(477, "1.14"),
    MINECRAFT_1_14_1(480, "1.14.1"),
    MINECRAFT_1_14_2(485, "1.14.2"),
    MINECRAFT_1_14_3(490, "1.14.3"),
    MINECRAFT_1_14_4(498, "1.14.4"),
    MINECRAFT_1_15(573, "1.15"),
    MINECRAFT_1_15_1(575, "1.15.1"),
    MINECRAFT_1_15_2(578, "1.15.2"),
    MINECRAFT_1_16(735, "1.16"),
    MINECRAFT_1_16_1(736, "1.16.1"),
    MINECRAFT_1_16_2(751, "1.16.2"),
    MINECRAFT_1_16_3(753, "1.16.3"),
    MINECRAFT_1_16_4(754, 2, "1.16.4");

    private static final int SNAPSHOT_BIT = 30;
    private final int protocol;
    private final int snapshotProtocol;
    private final String name;
    public static final ProtocolVersion MINIMUM_VERSION = MINECRAFT_1_7_2;
    public static final ProtocolVersion MAXIMUM_VERSION = values()[values().length - 1];
    public static final String SUPPORTED_VERSION_STRING = String.format("%s-%s", MINIMUM_VERSION, MAXIMUM_VERSION);
    public static final ImmutableMap<Integer, ProtocolVersion> ID_TO_PROTOCOL_CONSTANT;
    public static final Set<ProtocolVersion> SUPPORTED_VERSIONS;

    ProtocolVersion(int protocol, String name) {
        this(protocol, -1, name);
    }

    ProtocolVersion(int protocol, int snapshotProtocol, String name) {
        if (snapshotProtocol != -1) {
            this.snapshotProtocol = 1073741824 | snapshotProtocol;
        } else {
            this.snapshotProtocol = -1;
        }

        this.protocol = protocol;
        this.name = name;
    }

    public int getProtocol() {
        return this.protocol;
    }

    public String getName() {
        return this.name;
    }

    public static ProtocolVersion getProtocolVersion(int protocol) {
        return ID_TO_PROTOCOL_CONSTANT.getOrDefault(protocol, UNKNOWN);
    }

    public static boolean isSupported(int protocol) {
        ProtocolVersion version = ID_TO_PROTOCOL_CONSTANT.get(protocol);
        return version != null && !version.isUnknown();
    }

    public static boolean isSupported(ProtocolVersion version) {
        return version != null && !version.isUnknown();
    }

    public boolean isUnknown() {
        return this == UNKNOWN;
    }

    public boolean isLegacy() {
        return this == LEGACY;
    }

    public String toString() {
        return this.name;
    }

    static {
        HashMap<Integer, ProtocolVersion> versions = new HashMap<>();
        ProtocolVersion[] var1 = values();
        int var2 = var1.length;
        int var3;
        ProtocolVersion value;
        for (var3 = 0; var3 < var2; ++var3) {
            value = var1[var3];
            versions.putIfAbsent(value.protocol, value);
            if (value.snapshotProtocol != -1) {
                versions.put(value.snapshotProtocol, value);
            }
        }
        ID_TO_PROTOCOL_CONSTANT = ImmutableMap.copyOf(versions);
        Set<ProtocolVersion> versionSet = EnumSet.noneOf(ProtocolVersion.class);
        var1 = values();
        var2 = var1.length;

        for (var3 = 0; var3 < var2; ++var3) {
            value = var1[var3];
            if (!value.isUnknown() && !value.isLegacy()) {
                versionSet.add(value);
            }
        }
        SUPPORTED_VERSIONS = Sets.immutableEnumSet(versionSet);
    }
}
