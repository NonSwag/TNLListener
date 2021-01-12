package net.nonswag.tnl.listener.api.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class PacketSerializer {

    public static void writeString(DataOutputStream outputStream, String value) throws IOException {
        byte [] bytes = value.getBytes(Charset.defaultCharset());
        writeVarInt(outputStream, bytes.length);
        outputStream.write(bytes);
    }

    public static void writeVarInt(DataOutputStream outputStream, int value) throws IOException {
        while (true) {
            if ((value & 0xFFFFFF80) == 0) {
                outputStream.writeByte(value);
                return;
            }
            outputStream.writeByte(value & 0x7F | 0x80);
            value >>>= 7;
        }
    }

    public static int readVarInt(DataInputStream inputStream) throws IOException {
        int i = 0, j = 0;
        while (true) {
            int k = inputStream.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) throw new RuntimeException("VarInt too big");
            if ((k & 0x80) != 128) break;
        }
        return i;
    }
}
