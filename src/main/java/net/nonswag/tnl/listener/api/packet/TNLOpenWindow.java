package net.nonswag.tnl.listener.api.packet;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.math.Range;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.version.ServerVersion;

import javax.annotation.Nonnull;

public abstract class TNLOpenWindow {

    @Nonnull
    public static Object create(@Nonnull Type type, @Nonnull String title) {
        Object packet;
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            packet = new net.minecraft.server.v1_16_R3.PacketPlayOutOpenWindow();
            Reflection.setField(packet, "c", new net.minecraft.server.v1_16_R3.ChatMessage(title));
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            packet = new net.minecraft.server.v1_15_R1.PacketPlayOutOpenWindow();
            Reflection.setField(packet, "c", new net.minecraft.server.v1_15_R1.ChatMessage(title));
        } else {
            Logger.error.println("Version <'" + TNLListener.getInstance().getVersion().getRecentVersion() + "'> is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
        Reflection.setField(packet, "a", 1);
        Reflection.setField(packet, "b", type.getId());
        return packet;
    }

    @Nonnull
    public static Object create(int size, @Nonnull String title) {
        return create(Type.chest(size), title);
    }

    public enum Type {
        CHEST_9X1(0),
        CHEST_9X2(1),
        CHEST_9X3(2),
        CHEST_9X4(3),
        CHEST_9X5(4),
        CHEST_9X6(5),
        DISPENSER(6),
        ANVIL(7),
        BEACON(8),
        BLAST_FURNACE(9),
        BREWING_STAND(10),
        WORKBENCH(11),
        ENCHANTER(12),
        FURNACE(13),
        GRINDSTONE(14),
        HOPPER(15),
        LECTERN(16),
        LOOM(17),
        MERCHANT(18),
        SHULKER_BOX(19),
        SMITHING_TABLE(20),
        SMOKER(21),
        CARTOGRAPHY_TABLE(22),
        STONECUTTER(23),
        ;

        private final int id;

        Type(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Nonnull
        public static Type chest(@Range(from = 1, to = 6) int size) {
            int validate = Math.min(Math.max(size, 1), 6);
            if (validate == 1) return CHEST_9X1;
            else if (validate == 2) return CHEST_9X2;
            else if (validate == 3) return CHEST_9X3;
            else if (validate == 4) return CHEST_9X4;
            else if (validate == 5) return CHEST_9X5;
            else return CHEST_9X6;
        }
    }
}
