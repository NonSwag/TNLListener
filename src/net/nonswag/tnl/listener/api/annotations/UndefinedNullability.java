package net.nonswag.tnl.listener.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface UndefinedNullability {
    String value() default "";
}
