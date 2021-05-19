package net.nonswag.tnl.listener.api.math;

import com.avaje.ebean.validation.ValidatorMeta;
import com.avaje.ebean.validation.factory.RangeValidatorFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ValidatorMeta(
        factory = RangeValidatorFactory.class
)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Range {
    long from() default Long.MIN_VALUE;

    long to() default Long.MAX_VALUE;
}
