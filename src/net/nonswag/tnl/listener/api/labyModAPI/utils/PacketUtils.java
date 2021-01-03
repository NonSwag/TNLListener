package net.nonswag.tnl.listener.api.labyModAPI.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PacketUtils {

    private final String version;

    private Class<?> packetClass;

    private Class<?> packetPlayOutCustomPayloadClass;
    private Constructor<?> customPayloadConstructor;
    private boolean customPayloadHasBytes;

    private Class<?> packetDataSerializerClass;
    private Constructor<?> packetDataSerializerConstructor;

    private Method getHandleMethod;
    private Field playerConnectionField;
    private Field networkManagerField;

    public Class<?> getPacketClass() {
        return packetClass;
    }

    public Class<?> getPacketDataSerializerClass() {
        return packetDataSerializerClass;
    }

    public Class<?> getPacketPlayOutCustomPayloadClass() {
        return packetPlayOutCustomPayloadClass;
    }

    public Constructor<?> getCustomPayloadConstructor() {
        return customPayloadConstructor;
    }

    public String getVersion() {
        return version;
    }

    public boolean isCustomPayloadHasBytes() {
        return customPayloadHasBytes;
    }

    public Constructor<?> getPacketDataSerializerConstructor() {
        return packetDataSerializerConstructor;
    }

    public Method getGetHandleMethod() {
        return getHandleMethod;
    }

    public Field getPlayerConnectionField() {
        return playerConnectionField;
    }

    public Field getNetworkManagerField() {
        return networkManagerField;
    }

    public void setPacketClass(Class<?> packetClass) {
        this.packetClass = packetClass;
    }

    public void setPacketPlayOutCustomPayloadClass(Class<?> packetPlayOutCustomPayloadClass) {
        this.packetPlayOutCustomPayloadClass = packetPlayOutCustomPayloadClass;
    }

    public void setCustomPayloadConstructor(Constructor<?> customPayloadConstructor) {
        this.customPayloadConstructor = customPayloadConstructor;
    }

    public void setCustomPayloadHasBytes(boolean customPayloadHasBytes) {
        this.customPayloadHasBytes = customPayloadHasBytes;
    }

    public void setPacketDataSerializerClass(Class<?> packetDataSerializerClass) {
        this.packetDataSerializerClass = packetDataSerializerClass;
    }

    public void setPacketDataSerializerConstructor(Constructor<?> packetDataSerializerConstructor) {
        this.packetDataSerializerConstructor = packetDataSerializerConstructor;
    }

    public void setGetHandleMethod(Method getHandleMethod) {
        this.getHandleMethod = getHandleMethod;
    }

    public void setPlayerConnectionField(Field playerConnectionField) {
        this.playerConnectionField = playerConnectionField;
    }

    public void setNetworkManagerField(Field networkManagerField) {
        this.networkManagerField = networkManagerField;
    }

    public PacketUtils() {
        this.version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        try {
            this.packetClass = getNmsClass("Packet");
            this.packetPlayOutCustomPayloadClass = getNmsClass("PacketPlayOutCustomPayload");
            this.networkManagerField = getNmsClass("PlayerConnection").getDeclaredField("networkManager");
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (this.packetPlayOutCustomPayloadClass != null) {
            for (Constructor<?> constructors : packetPlayOutCustomPayloadClass.getDeclaredConstructors()) {
                if (constructors.getParameterTypes().length == 2 && constructors.getParameterTypes()[1] == byte[].class) {
                    customPayloadHasBytes = true;
                    customPayloadConstructor = constructors;
                } else if (constructors.getParameterTypes().length == 2 && constructors.getParameterTypes()[1].getSimpleName().equals("PacketDataSerializer")) {
                    customPayloadConstructor = constructors;
                }
            }
            if (!customPayloadHasBytes) {
                try {
                    packetDataSerializerClass = getNmsClass("PacketDataSerializer");
                    packetDataSerializerConstructor = packetDataSerializerClass.getDeclaredConstructor(ByteBuf.class);
                } catch (Exception ex) {
                    NMSMain.stacktrace(ex, "Couldn't find a valid constructor for PacketPlayOutCustomPayload. Disabling the plugin.");
                    Bukkit.getPluginManager().disablePlugin(NMSMain.getPlugin());
                }
            }
        }
    }

    public Object getPlayerHandle(Player player) {
        try {
            if (getHandleMethod == null)
                getHandleMethod = player.getClass().getMethod("getHandle");
            return getHandleMethod.invoke(player);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Object getPlayerConnection(Object nmsPlayer) {
        try {
            if (playerConnectionField == null)
                playerConnectionField = nmsPlayer.getClass().getField("playerConnection");
            return playerConnectionField.get(nmsPlayer);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendPacket(Player player, Object packet) {
        try {
            Object nmsPlayer = getPlayerHandle(player);
            Object playerConnection = getPlayerConnection(nmsPlayer);
            playerConnection.getClass().getMethod("sendPacket", packetClass).invoke(playerConnection, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Object getPluginMessagePacket(String channel, byte[] bytes) {
        try {
            return customPayloadConstructor.newInstance(channel, customPayloadHasBytes ? bytes : packetDataSerializerConstructor.newInstance(Unpooled.wrappedBuffer(bytes)));
        } catch (NullPointerException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            NMSMain.stacktrace(e, "Couldn't construct a custom-payload packet (Channel: " + channel + "):");
        }

        return null;
    }

    public Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + version + "." + nmsClassName);
    }
}
