package net.nonswag.tnl.listener.v1_16_R1.api.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.EncoderException;
import net.minecraft.server.v1_16_R1.PacketDataSerializer;
import net.nonswag.tnl.listener.NMSMain;

import java.nio.charset.StandardCharsets;

public class PacketSerializer {

    private final ByteBuf buf = Unpooled.buffer();
    private final byte[] result;

    public PacketSerializer(String string) {
        stringWriter(string);
        result = buf.array();
        buf.release();
    }

    private void stringWriter(String string) {
        try {
            byte[] b = string.getBytes(StandardCharsets.UTF_8);
            if (b.length > Short.MAX_VALUE) {
                throw new EncoderException("String too big (was " + string.length() + " bytes encoded, max " + Short.MAX_VALUE + ")");
            } else {
                bufferWriter(b.length);
                buf.writeBytes(b);
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    private void bufferWriter(int input) {
        try {
            while ((input & -128) != 0) {
                buf.writeByte(input & 127 | 128);
                input >>>= 7;
            }
            buf.writeByte(input);
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    public PacketDataSerializer serialized() {
        return new PacketDataSerializer(buf);
    }

    public byte[] toArray() {
        return this.result;
    }
}
