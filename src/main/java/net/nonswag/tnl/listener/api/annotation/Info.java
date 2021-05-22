package net.nonswag.tnl.listener.api.annotation;

import javax.annotation.Nonnull;

public @interface Info {
    @Nonnull
    String info() default "";

    @Nonnull
    String value() default "";
}
