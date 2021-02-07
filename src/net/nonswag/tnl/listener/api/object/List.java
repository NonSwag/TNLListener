package net.nonswag.tnl.listener.api.object;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class List<O> {

    @Nonnull private final java.util.List<O> objects;

    @SafeVarargs
    public List(@Nonnull O... objects) {
        this.objects = Arrays.asList(objects);
    }

    @Nonnull
    public java.util.List<O> getObjects() {
        return objects;
    }
}
