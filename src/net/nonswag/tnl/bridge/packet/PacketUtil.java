package net.nonswag.tnl.bridge.packet;

import net.nonswag.tnl.listener.api.reflection.Reflection;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class PacketUtil {

    public static Packet<?> decode(String string) {
        string = string.replaceFirst("PacketPlayOut", "PacketPlayIn");
        Class<?> clazz = Reflection.getClass("net.nonswag.tnl.bridge.packet.packets." + string.split("\\{")[0]);
        if (clazz != null) {
            try {
                Packet<?> packet = ((Packet<?>) clazz.newInstance());
                JSONObject jsonObject = (JSONObject) JSONValue.parse(string.replaceFirst(clazz.getSimpleName(), ""));
                for (Object value : jsonObject.keySet()) {
                    Reflection.setField(packet, value.toString(), jsonObject.get(value));
                }
                return packet;
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return null;
    }

    public static String encode(Packet<?> packet) {
        return packet.encode(packet);
    }
}
