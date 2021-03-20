package net.nonswag.tnl.listener.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.PacketDataSerializer;
import net.minecraft.server.v1_15_R1.PacketPlayOutCustomPayload;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.v1_15.R1.NMSPlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;

public class ByteUtils {

    public static void writePacket(@Nonnull NMSPlayer player, @Nonnull String keyPrefix, @Nonnull String keySuffix, @Nonnull String messageContent) {
        try {
            byte[] bytes = getBytes(keySuffix, messageContent);
            if (bytes != null) {
                PacketDataSerializer pds = new PacketDataSerializer(Unpooled.wrappedBuffer(bytes));
                PacketPlayOutCustomPayload payloadPacket = new PacketPlayOutCustomPayload(new MinecraftKey(keyPrefix), pds);
                player.sendPacket(payloadPacket);
            }
        } catch (Exception e) {
            Logger.error.println(e);
        }
    }

    @Nullable
    private static byte[] getBytes(@Nonnull String messageKey, @Nonnull String messageContents) {
        try {
            ByteBuf byteBuf = Unpooled.buffer();
            stringWriter(byteBuf, messageKey);
            stringWriter(byteBuf, messageContents);
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            return bytes;
        } catch (Exception e) {
            Logger.error.println(e);
            return null;
        }
    }

    private static void bufferWriter(@Nonnull ByteBuf buffer, int input) {
        while(true) {
            try {
                if ((input & -128) != 0) {
                    buffer.writeByte(input & 127 | 128);
                    input >>>= 7;
                    continue;
                }

                buffer.writeByte(input);
            } catch (Exception e) {
                Logger.error.println(e);
            }
            return;
        }
    }

    private static void stringWriter(@Nonnull ByteBuf buffer, @Nonnull String string) {
        try {
            byte[] b = string.getBytes(StandardCharsets.UTF_8);
            if (b.length > 32767) {
                throw new EncoderException("String too big (was " + string.length() + " bytes encoded, max " + 32767 + ")");
            }
            bufferWriter(buffer, b.length);
            buffer.writeBytes(b);
        } catch (Exception e) {
            Logger.error.println(e);
        }
    }

    private static int bufferReader(@Nonnull ByteBuf buffer) {
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
        } catch (Exception e) {
            Logger.error.println(e);
            return 0;
        }
    }

    @Nonnull
    private static String stringReader(@Nonnull ByteBuf buffer, int maxLength) {
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
        } catch (Exception e) {
            Logger.error.println(e);
            return "";
        }
    }
}
