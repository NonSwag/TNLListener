package net.nonswag.tnl.bridge.receiver;

import net.nonswag.tnl.bridge.Packet;

import javax.annotation.Nonnull;

class PacketReader {

    static void read(@Nonnull Packet<?> packet) {
        System.out.println(packet.encode(packet));
    }
}
