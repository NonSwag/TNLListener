package net.nonswag.tnl.listener.v1_15_R1.api.playerAPI;

import net.nonswag.tnl.listener.enumerations.ProtocolVersion;

import java.util.HashMap;

public class VersionCache {

    private static final HashMap<String, ProtocolVersion> versionHashMap = new HashMap<>();

    public static HashMap<String, ProtocolVersion> getVersionHashMap() {
        return versionHashMap;
    }

    public static ProtocolVersion getVersion(TNLPlayer player) {
        return getVersionHashMap().getOrDefault(player.getName(), ProtocolVersion.UNKNOWN);
    }

    public static void setVersion(TNLPlayer player, ProtocolVersion version) {
        getVersionHashMap().put(player.getName(), version);
    }
}
