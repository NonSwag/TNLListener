package net.nonswag.tnl.listener.api.item;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.version.ServerVersion;

import javax.annotation.Nonnull;

public enum SlotType {
    MAIN_HAND,
    OFF_HAND,
    BOOTS,
    LEGGINGS,
    CHESTPLATE,
    HELMET;

    @Nonnull
    private static final SlotType[] legacySlots  = new SlotType[]{SlotType.HELMET, SlotType.CHESTPLATE, SlotType.LEGGINGS, SlotType.BOOTS};

    @Nonnull
    public Object toNMS() {
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            if (equals(MAIN_HAND)) {
                return net.minecraft.server.v1_16_R3.EnumItemSlot.MAINHAND;
            } else if (equals(OFF_HAND)) {
                return net.minecraft.server.v1_16_R3.EnumItemSlot.OFFHAND;
            } else if (equals(BOOTS)) {
                return net.minecraft.server.v1_16_R3.EnumItemSlot.FEET;
            } else if (equals(LEGGINGS)) {
                return net.minecraft.server.v1_16_R3.EnumItemSlot.LEGS;
            } else if (equals(CHESTPLATE)) {
                return net.minecraft.server.v1_16_R3.EnumItemSlot.CHEST;
            } else if (equals(HELMET)) {
                return net.minecraft.server.v1_16_R3.EnumItemSlot.HEAD;
            } else {
                throw new UnsupportedOperationException("method is not supported in this version");
            }
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            if (equals(MAIN_HAND)) {
                return net.minecraft.server.v1_15_R1.EnumItemSlot.MAINHAND;
            } else if (equals(OFF_HAND)) {
                return net.minecraft.server.v1_15_R1.EnumItemSlot.OFFHAND;
            } else if (equals(BOOTS)) {
                return net.minecraft.server.v1_15_R1.EnumItemSlot.FEET;
            } else if (equals(LEGGINGS)) {
                return net.minecraft.server.v1_15_R1.EnumItemSlot.LEGS;
            } else if (equals(CHESTPLATE)) {
                return net.minecraft.server.v1_15_R1.EnumItemSlot.CHEST;
            } else if (equals(HELMET)) {
                return net.minecraft.server.v1_15_R1.EnumItemSlot.HEAD;
            } else {
                throw new UnsupportedOperationException("method is not supported in this version");
            }
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_6) || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            if (equals(MAIN_HAND)) {
                return 0;
            } else if (equals(BOOTS)) {
                return 1;
            } else if (equals(LEGGINGS)) {
                return 2;
            } else if (equals(CHESTPLATE)) {
                return 3;
            } else if (equals(HELMET)) {
                return 4;
            } else {
                throw new UnsupportedOperationException("method is not supported in this version");
            }
        } else {
            Logger.error.println("Version <'" + TNLListener.getInstance().getVersion().getRecentVersion() + "'> is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }

    @Nonnull
    public static SlotType fromNMS(@Nonnull Object object) {
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
            if (object.equals(net.minecraft.server.v1_16_R3.EnumItemSlot.MAINHAND)) {
                return MAIN_HAND;
            } else if (object.equals(net.minecraft.server.v1_16_R3.EnumItemSlot.OFFHAND)) {
                return OFF_HAND;
            } else if (object.equals(net.minecraft.server.v1_16_R3.EnumItemSlot.FEET)) {
                return BOOTS;
            } else if (object.equals(net.minecraft.server.v1_16_R3.EnumItemSlot.LEGS)) {
                return LEGGINGS;
            } else if (object.equals(net.minecraft.server.v1_16_R3.EnumItemSlot.CHEST)) {
                return CHESTPLATE;
            } else if (object.equals(net.minecraft.server.v1_16_R3.EnumItemSlot.HEAD)) {
                return HELMET;
            } else {
                throw new UnsupportedOperationException("method is not supported in this version");
            }
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_15_2)) {
            if (object.equals(net.minecraft.server.v1_15_R1.EnumItemSlot.MAINHAND)) {
                return MAIN_HAND;
            } else if (object.equals(net.minecraft.server.v1_15_R1.EnumItemSlot.OFFHAND)) {
                return OFF_HAND;
            } else if (object.equals(net.minecraft.server.v1_15_R1.EnumItemSlot.FEET)) {
                return BOOTS;
            } else if (object.equals(net.minecraft.server.v1_15_R1.EnumItemSlot.LEGS)) {
                return LEGGINGS;
            } else if (object.equals(net.minecraft.server.v1_15_R1.EnumItemSlot.CHEST)) {
                return CHESTPLATE;
            } else if (object.equals(net.minecraft.server.v1_15_R1.EnumItemSlot.HEAD)) {
                return HELMET;
            } else {
                throw new UnsupportedOperationException("method is not supported in this version");
            }
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_6) || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
            if (object.equals(0)) {
                return MAIN_HAND;
            } else if (object.equals(1)) {
                return BOOTS;
            } else if (object.equals(2)) {
                return LEGGINGS;
            } else if (object.equals(3)) {
                return CHESTPLATE;
            } else if (object.equals(4)) {
                return HELMET;
            } else {
                throw new UnsupportedOperationException("method is not supported in this version");
            }
        } else {
            Logger.error.println("Version <'" + TNLListener.getInstance().getVersion().getRecentVersion() + "'> is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }

    @Nonnull
    private static SlotType[] getLegacySlots() {
        return legacySlots;
    }

    @Nonnull
    public static SlotType[] values(boolean legacy) {
        return legacy ? getLegacySlots() : values();
    }
}
