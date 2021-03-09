package net.nonswag.tnl.listener.api.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_15_R1.PacketDataSerializer;

import javax.annotation.Nonnull;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PacketSerializer {

    public static void writeString(@Nonnull DataOutputStream outputStream, @Nonnull String value) {
        try {
            byte[] bytes = value.getBytes(Charset.defaultCharset());
            writeVarInt(outputStream, bytes.length);
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeVarInt(@Nonnull DataOutputStream outputStream, int value) {
        try {
            while (true) {
                if ((value & 0xFFFFFF80) == 0) {
                    outputStream.writeByte(value);
                    return;
                }
                outputStream.writeByte(value & 0x7F | 0x80);
                value >>>= 7;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int readVarInt(@Nonnull DataInputStream inputStream) {
        int i = 0, j = 0;
        try {
            while (true) {
                int k = inputStream.readByte();
                i |= (k & 0x7F) << j++ * 7;
                if (j > 5) throw new RuntimeException("VarInt too big");
                if ((k & 0x80) != 128) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return i;
    }

    @Nonnull
    private final ByteBuf buf = Unpooled.buffer();
    private final byte[] result;

    public PacketSerializer(@Nonnull String string) {
        this.writeString(string);
        this.result = buf.array();
        buf.release();
    }

    private void writeString(@Nonnull String s) {
        if (s.length() > 32767) {
            throw new IllegalArgumentException(String.format("Cannot send string longer than Short.MAX_VALUE (got %s characters)", s.length()));
        } else {
            byte[] b = s.getBytes(StandardCharsets.UTF_8);
            this.writeVarInt(b.length, buf);
            buf.writeBytes(b);
        }
    }

    private void writeVarInt(int value, @Nonnull ByteBuf output) {
        do {
            int part = value & 127;
            value >>>= 7;
            if (value != 0) {
                part |= 128;
            }
            output.writeByte(part);
        } while(value != 0);
    }

    @Nonnull
    public PacketDataSerializer serialized() {
        return new PacketDataSerializer(buf);
    }

    public byte[] toArray() {
        return this.result;
    }
}
