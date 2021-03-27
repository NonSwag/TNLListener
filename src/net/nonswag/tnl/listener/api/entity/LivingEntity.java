package net.nonswag.tnl.listener.api.entity;

import net.nonswag.tnl.listener.api.object.Generic;

import javax.annotation.Nonnull;

public class LivingEntity<E> extends Generic<E> {

    public LivingEntity(@Nonnull E parameter) {
        super(parameter);
    }
}
