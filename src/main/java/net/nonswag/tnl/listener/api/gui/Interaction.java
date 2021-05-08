package net.nonswag.tnl.listener.api.gui;

import javax.annotation.Nonnull;

public class Interaction {

    public final static Interaction EMPTY = new Interaction(() -> {});

    @Nonnull
    private final Type type;
    @Nonnull
    private final Runnable action;

    public Interaction(@Nonnull Type type, @Nonnull Runnable action) {
        this.type = type;
        this.action = action;
    }

    public Interaction(@Nonnull Runnable action) {
        this(Type.GENERAL, action);
    }

    @Nonnull
    public Type getType() {
        return type;
    }

    @Nonnull
    public Runnable getAction() {
        return action;
    }

    public enum Type {
        GENERAL,
        RIGHT,
        LEFT,
        MIDDLE,
        SHIFT_RIGHT,
        SHIFT_LEFT,
        OUTSIDE_LEFT,
        OUTSIDE_RIGHT,
        NUMBER_KEY,
        DOUBLE_CLICK,
        DROP,
        DROP_ALL,
        CREATIVE;

        Type() {
        }

        public boolean isKeyboardClick() {
            return this.equals(NUMBER_KEY) || this.equals(DROP) || this.equals(DROP_ALL) || this.equals(GENERAL);
        }

        public boolean isCreativeAction() {
            return this.equals(MIDDLE) || this.equals(CREATIVE) || this.equals(GENERAL);
        }

        public boolean isRightClick() {
            return this.equals(RIGHT) || this.equals(SHIFT_RIGHT) || this.equals(GENERAL);
        }

        public boolean isLeftClick() {
            return this.equals(LEFT) || this.equals(SHIFT_LEFT) || this.equals(DOUBLE_CLICK) || this.equals(CREATIVE) || this.equals(GENERAL);
        }

        public boolean isShiftClick() {
            return this.equals(SHIFT_LEFT) || this.equals(SHIFT_RIGHT) || this.equals(DROP_ALL) || this.equals(GENERAL);
        }
    }
}
