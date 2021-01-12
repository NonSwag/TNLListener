package net.nonswag.tnl.listener.api.bridge;

import net.nonswag.tnl.listener.api.bridge.packet.Packet;

class PacketReader {

    static void read(Packet<?> packet) {
        System.out.println(packet.encode(packet));
    }
}
