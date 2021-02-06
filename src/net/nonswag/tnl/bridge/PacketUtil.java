package net.nonswag.tnl.bridge;

import net.nonswag.tnl.api.event.EventManager;
import net.nonswag.tnl.bridge.events.MessageDecodeEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class PacketUtil {

    @Nullable
    public static Packet<? extends PacketListenerPlayIn> decode(@Nonnull String string) {
        try {
            JSONObject parse = ((JSONObject) new JSONParser().parse(string));
            MessageDecodeEvent event = new MessageDecodeEvent(parse);
            EventManager.callEvent(event);
            return event.getPacket();
        } catch (Throwable ignored) {
        }
        return null;
    }

    @Nonnull
    public static String encode(@Nonnull Packet<? extends PacketListenerPlayOut> packet) {
        return packet.encode(packet);
    }
}
