package net.nonswag.tnl.listener.api.labyModAPI;

import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.nonswag.tnl.listener.api.labyModAPI.enumerations.Permission;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class LabyModAPI {

    public byte[] getBytesToSend(Map<Permission, Boolean> permissions) {
        JsonObject object = new JsonObject();
        for (Map.Entry<Permission, Boolean> permissionEntry : permissions.entrySet()) {
            object.addProperty(permissionEntry.getKey().name(), permissionEntry.getValue());
        }
        return getBytesToSend("PERMISSIONS", object.toString());
    }

    public byte[] getBytesToSend(String messageKey, String messageContents) {
        ByteBuf byteBuf = Unpooled.buffer();
        writeString(byteBuf, messageKey);
        writeString(byteBuf, messageContents);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }

    private void writeVarIntToBuffer(ByteBuf buf, int input) {
        while ((input & -128) != 0) {
            buf.writeByte(input & 127 | 128);
            input >>>= 7;
        }
        buf.writeByte(input);
    }

    private void writeString(ByteBuf buf, String string) {
        byte[] aByte = string.getBytes(StandardCharsets.UTF_8);
        if (aByte.length > Short.MAX_VALUE) {
            throw new EncoderException("String too big (was " + string.length() + " bytes encoded, max " + Short.MAX_VALUE + ")");
        } else {
            writeVarIntToBuffer(buf, aByte.length);
            buf.writeBytes(aByte);
        }
    }

    public int readVarIntFromBuffer(ByteBuf buf) {
        int i = 0;
        int j = 0;
        byte b0;
        do {
            b0 = buf.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);
        return i;
    }

    public String readString(ByteBuf buf, int maxLength) {
        int i = this.readVarIntFromBuffer(buf);
        if (i > maxLength * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
        } else if (i < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            byte[] bytes = new byte[i];
            buf.readBytes(bytes);
            String s = new String(bytes, StandardCharsets.UTF_8);
            if (s.length() > maxLength) {
                throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
            } else {
                return s;
            }
        }
    }
}
