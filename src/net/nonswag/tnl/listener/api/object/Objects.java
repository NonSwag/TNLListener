package net.nonswag.tnl.listener.api.object;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Objects<Value> {

    @Nullable private final Value value;

    public Objects(@Nullable Value value) {
        this.value = value;
    }

    @Nullable
    public Value getValue() {
        return value;
    }

    @Nonnull
    public Value getOrDefault(@Nonnull Value defaultValue) {
        return getValue() == null ? defaultValue : getValue();
    }

    @Nullable
    public String toString() {
        return getValue() == null ? null : getValue().toString();
    }

    @Nonnull
    public Value nonnull() {
        if (getValue() != null) {
            return getValue();
        }
        throw new NullPointerException();
    }
}
