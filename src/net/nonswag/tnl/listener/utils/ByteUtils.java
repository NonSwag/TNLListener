package net.nonswag.tnl.listener.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.PacketDataSerializer;
import net.minecraft.server.v1_15_R1.PacketPlayOutCustomPayload;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import java.nio.charset.StandardCharsets;

public class ByteUtils {

    public static void writePacket(TNLPlayer player, String keyPrefix, String keySuffix, String messageContent) {
        try {
            byte[] bytes = getBytes(keySuffix, messageContent);
            PacketDataSerializer pds = new PacketDataSerializer(Unpooled.wrappedBuffer(bytes));
            PacketPlayOutCustomPayload payloadPacket = new PacketPlayOutCustomPayload(new MinecraftKey(keyPrefix), pds);
            player.sendPacket(payloadPacket);
        } catch (Throwable var6) {
            NMSMain.stacktrace(var6);
        }
    }

    private static byte[] getBytes(String messageKey, String messageContents) {
        try {
            ByteBuf byteBuf = Unpooled.buffer();
            stringWriter(byteBuf, messageKey);
            stringWriter(byteBuf, messageContents);
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            return bytes;
        } catch (Throwable var4) {
            NMSMain.stacktrace(var4);
            return null;
        }
    }

    private static void bufferWriter(ByteBuf buffer, int input) {
        while(true) {
            try {
                if ((input & -128) != 0) {
                    buffer.writeByte(input & 127 | 128);
                    input >>>= 7;
                    continue;
                }

                buffer.writeByte(input);
            } catch (Throwable var3) {
                NMSMain.stacktrace(var3);
            }
            return;
        }
    }

    private static void stringWriter(ByteBuf buffer, String string) {
        try {
            byte[] b = string.getBytes(StandardCharsets.UTF_8);
            if (b.length > 32767) {
                throw new EncoderException("String too big (was " + string.length() + " bytes encoded, max " + 32767 + ")");
            }

            bufferWriter(buffer, b.length);
            buffer.writeBytes(b);
        } catch (Throwable var3) {
            NMSMain.stacktrace(var3);
        }
    }

    private static int bufferReader(ByteBuf buffer) {
        try {
            int i = 0;
            int j = 0;

            byte b0;
            do {
                b0 = buffer.readByte();
                i |= (b0 & 127) << j++ * 7;
                if (j > 5) {
                    throw new RuntimeException("VarInt too big");
                }
            } while((b0 & 128) == 128);

            return i;
        } catch (Throwable var4) {
            NMSMain.stacktrace(var4);
            return 0;
        }
    }

    private static String stringReader(ByteBuf buffer, int maxLength) {
        try {
            int i = bufferReader(buffer);
            if (i > maxLength * 4) {
                throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
            } else if (i < 0) {
                throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
            } else {
                byte[] bytes = new byte[i];
                buffer.readBytes(bytes);
                String s = new String(bytes, StandardCharsets.UTF_8);
                if (s.length() > maxLength) {
                    throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
                } else {
                    return s;
                }
            }
        } catch (Throwable var5) {
            NMSMain.stacktrace(var5);
            return "";
        }
    }
}
