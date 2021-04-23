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
    public Object toNMS() {
        if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_5) || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_16_4)) {
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
        } else if (TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_10) || TNLListener.getInstance().getVersion().equals(ServerVersion.v1_7_2)) {
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
            Logger.error.println("§cVersion §8'§4" + TNLListener.getInstance().getVersion().getVersion() + "§8'§c is not registered please report this error to an contributor");
            throw new IllegalStateException();
        }
    }
}
