package net.nonswag.tnl.listener.v1_15_R1.api.serializerAPI;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_15_R1.PacketDataSerializer;

import java.nio.charset.StandardCharsets;

public class PacketSerializer {

    private final ByteBuf buf = Unpooled.buffer();
    private final byte[] result;

    public PacketSerializer(String string) {
        this.writeString(string);
        this.result = buf.array();
        buf.release();
    }

    private void writeString(String s) {
        if (s.length() > 32767) {
            throw new IllegalArgumentException(String.format("Cannot send string longer than Short.MAX_VALUE (got %s characters)", s.length()));
        } else {
            byte[] b = s.getBytes(StandardCharsets.UTF_8);
            this.writeVarInt(b.length, buf);
            buf.writeBytes(b);
        }
    }

    private void writeVarInt(int value, ByteBuf output) {
        do {
            int part = value & 127;
            value >>>= 7;
            if (value != 0) {
                part |= 128;
            }
            output.writeByte(part);
        } while(value != 0);
    }

    public PacketDataSerializer serialized() {
        return new PacketDataSerializer(buf);
    }

    public byte[] toArray() {
        return this.result;
    }
}
