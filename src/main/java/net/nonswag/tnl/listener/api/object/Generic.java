package net.nonswag.tnl.listener.api.object;

import javax.annotation.Nonnull;

public class Generic<T> {

    @Nonnull
    private final T parameter;

    public Generic(@Nonnull T parameter) {
        this.parameter = parameter;
    }

    @Nonnull
    public T getParameter() {
        return parameter;
    }

    @Nonnull
    public Class<T> getParameterType() {
        return (Class<T>) getParameter().getClass();
    }
}
