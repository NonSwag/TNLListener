package net.nonswag.tnl.listener.api.player;

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
