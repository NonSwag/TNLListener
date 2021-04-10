package net.nonswag.tnl.listener.api.object;

import javax.annotation.Nonnull;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "Generic{" +
                "parameter=" + parameter +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Generic<?> generic = (Generic<?>) o;
        return parameter.equals(generic.parameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameter);
    }
}
