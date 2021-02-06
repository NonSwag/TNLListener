package net.nonswag.tnl.listener.api.object;

import javax.annotation.Nonnull;

public class List<O> {

    @Nonnull private final O[] objects;

    @SafeVarargs
    public List(@Nonnull O... objects) {
        this.objects = objects;
    }

    @Nonnull
    public O[] getObjects() {
        return objects;
    }
}
