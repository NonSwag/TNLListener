package net.nonswag.tnl.listener.api.bridgeAPI.packetAPI;

import net.nonswag.tnl.listener.api.reflectionAPI.Reflection;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/*******************************************************
 * Copyright (C) 2019-2023 NonSwag kirschnerdavid2466@gmail.com
 *
 * This file is part of TNLListener and was created at the 11/12/20
 *
 * TNLListener can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/

public class PacketUtil {

    public static Packet<?> decode(String string) {
        string = string.replaceFirst("PacketPlayIn", "PacketPlayOut");
        Class<?> clazz = Reflection.getClass("net.nonswag.tnl.listener.api.bridgeAPI.packetAPI.packets" + string.split("\\{")[0]);
        if (clazz != null) {
            try {
                Packet<?> packet = ((Packet<?>) clazz.newInstance());
                JSONObject jsonObject = (JSONObject) JSONValue.parse(string.replaceFirst(clazz.getName(), ""));
                for (Object value : jsonObject.keySet()) {
                    Reflection.setField(packet.getClass(), value.toString(), jsonObject.get(value));
                    System.out.println(Reflection.getField(packet.getClass(), value.toString()));
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
