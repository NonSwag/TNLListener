package net.nonswag.tnl.listener.api.gui;

import net.nonswag.tnl.listener.api.gui.iterators.TypeIterator;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.event.inventory.ClickType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class Interaction implements Iterable<Interaction.Type>, Cloneable {

    @Nonnull
    public static final Interaction EMPTY = new Interaction(player -> {});

    @Nonnull
    private final List<Type> types = new ArrayList<>();
    @Nonnull
    private final Consumer<TNLPlayer> action;

    public Interaction(@Nonnull Consumer<TNLPlayer> action, @Nonnull Type... types) {
        this.action = action;
        for (@Nullable Type type : types.length == 0 ? Collections.singletonList(Type.GENERAL) : Arrays.asList(types)) {
            if (type != null && !this.types.contains(type)) this.types.add(type);
        }
    }

    public Interaction(@Nonnull Type type, @Nonnull Consumer<TNLPlayer> action) {
        this(action, type);
    }

    public Interaction(@Nonnull Type[] types, @Nonnull Consumer<TNLPlayer> action) {
        this(action, types);
    }

    public Interaction(@Nonnull Consumer<TNLPlayer> action) {
        this(Type.GENERAL, action);
    }

    @Nonnull
    public List<Type> getTypes() {
        return types;
    }

    @Nonnull
    public Consumer<TNLPlayer> getAction() {
        return action;
    }

    public enum Type {
        GENERAL,
        RIGHT(ClickType.RIGHT),
        LEFT(ClickType.LEFT),
        MIDDLE(ClickType.MIDDLE),
        SHIFT_RIGHT(ClickType.SHIFT_RIGHT),
        SHIFT_LEFT(ClickType.SHIFT_LEFT),
        NUMBER_KEY_GENERAL(ClickType.NUMBER_KEY),
        NUMBER_KEY_1(ClickType.NUMBER_KEY),
        NUMBER_KEY_2(ClickType.NUMBER_KEY),
        NUMBER_KEY_3(ClickType.NUMBER_KEY),
        NUMBER_KEY_4(ClickType.NUMBER_KEY),
        NUMBER_KEY_5(ClickType.NUMBER_KEY),
        NUMBER_KEY_6(ClickType.NUMBER_KEY),
        NUMBER_KEY_7(ClickType.NUMBER_KEY),
        NUMBER_KEY_8(ClickType.NUMBER_KEY),
        NUMBER_KEY_9(ClickType.NUMBER_KEY),
        OFF_HAND(ClickType.NUMBER_KEY),
        DOUBLE_CLICK(ClickType.DOUBLE_CLICK),
        DROP(ClickType.DROP),
        DROP_ALL(ClickType.CONTROL_DROP);

        @Nonnull
        private final ClickType type;

        Type(@Nonnull ClickType type) {
            this.type = type;
        }

        Type() {
            this.type = ClickType.UNKNOWN;
        }

        @Nonnull
        public ClickType getType() {
            return type;
        }

        public boolean isKeyboardClick() {
            return isNumberClick() || equals(DROP) || equals(DROP_ALL) || equals(OFF_HAND);
        }

        public boolean isNumberClick() {
            return equals(NUMBER_KEY_GENERAL) || equals(NUMBER_KEY_1) || equals(NUMBER_KEY_2)
                    || equals(NUMBER_KEY_3) || equals(NUMBER_KEY_4) || equals(NUMBER_KEY_5)
                    || equals(NUMBER_KEY_6) || equals(NUMBER_KEY_7) || equals(NUMBER_KEY_8)
                    || equals(NUMBER_KEY_9) || equals(GENERAL);
        }

        public boolean isRightClick() {
            return equals(RIGHT) || equals(SHIFT_RIGHT) || equals(GENERAL);
        }

        public boolean isLeftClick() {
            return equals(LEFT) || equals(SHIFT_LEFT) || equals(DOUBLE_CLICK) || equals(GENERAL);
        }

        public boolean isShiftClick() {
            return equals(SHIFT_LEFT) || equals(SHIFT_RIGHT) || equals(DROP_ALL) || equals(GENERAL);
        }

        public boolean equals(@Nonnull ClickType type) {
            if (equals(GENERAL)) return true;
            return getType().equals(type);
        }

        public boolean comparable(@Nonnull Type type) {
            if (equals(GENERAL) || type.equals(GENERAL)) return true;
            return equals(type);
        }

        @Nonnull
        public static Type fromNMS(int id, @Nonnull String nmsType) {
            if (id == 0) {
                switch (nmsType) {
                    case "PICKUP":
                        return Type.LEFT;
                    case "QUICK_MOVE":
                        return Type.SHIFT_LEFT;
                    case "SWAP":
                        return Type.NUMBER_KEY_1;
                    case "PICKUP_ALL":
                        return Type.DOUBLE_CLICK;
                    case "THROW":
                        return Type.DROP;
                }
            } else if (id == 1) {
                switch (nmsType) {
                    case "PICKUP":
                        return Type.RIGHT;
                    case "QUICK_MOVE":
                        return Type.SHIFT_RIGHT;
                    case "SWAP":
                        return Type.NUMBER_KEY_2;
                    case "THROW":
                        return Type.DROP_ALL;
                }
            } else if (id == 2) {
                if (nmsType.equals("CLONE")) {
                    return Interaction.Type.MIDDLE;
                } else if (nmsType.equals("SWAP")) {
                    return Interaction.Type.NUMBER_KEY_3;
                }
            } else if (id == 3 && nmsType.equals("SWAP")) {
                return Interaction.Type.NUMBER_KEY_4;
            } else if (id == 4 && nmsType.equals("SWAP")) {
                return Interaction.Type.NUMBER_KEY_5;
            } else if (id == 5 && nmsType.equals("SWAP")) {
                return Interaction.Type.NUMBER_KEY_6;
            } else if (id == 6 && nmsType.equals("SWAP")) {
                return Interaction.Type.NUMBER_KEY_7;
            } else if (id == 7 && nmsType.equals("SWAP")) {
                return Interaction.Type.NUMBER_KEY_8;
            } else if (id == 8 && nmsType.equals("SWAP")) {
                return Interaction.Type.NUMBER_KEY_9;
            } else if (id == 40 && nmsType.equals("SWAP")) {
                return Interaction.Type.OFF_HAND;
            }
            return Interaction.Type.GENERAL;
        }
    }

    @Override
    public Iterator<Type> iterator() {
        return new TypeIterator(this);
    }

    @Nonnull
    public ListIterator<Type> iterator(int i) {
        return new TypeIterator(this, i);
    }
}
