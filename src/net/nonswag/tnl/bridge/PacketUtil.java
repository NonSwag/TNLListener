package net.nonswag.tnl.bridge;

import net.nonswag.tnl.api.event.EventManager;
import net.nonswag.tnl.bridge.events.MessageDecodeEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.Socket;

public abstract class PacketUtil {

    @Nullable
    public static Packet<PacketListener> decode(@Nonnull String string, @Nonnull Socket socket) {
        try {
            MessageDecodeEvent event = new MessageDecodeEvent((JSONObject) new JSONParser().parse(string), socket);
            EventManager.callEvent(event);
            return event.getPacket();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    @Nonnull
    public static String encode(@Nonnull Packet<PacketListener> packet) {
        return packet.encode(packet);
    }
}
