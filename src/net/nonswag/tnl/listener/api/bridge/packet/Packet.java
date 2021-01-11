package net.nonswag.tnl.listener.api.bridge.packet;

import net.nonswag.tnl.listener.api.reflection.Reflection;

/*******************************************************
 * Copyright (C) 2019-2023 NonSwag kirschnerdavid2466@gmail.com
 *
 * This file is part of TNLListener and was created at the 10/31/20
 *
 * TNLListener can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/

public interface Packet<T> {

    default String getPath() {
        return getClass().getName();
    }

    default String getName() {
        return getClass().getSimpleName();
    }

    default String encode(Object clazz) {
        return getName() + Reflection.toJsonObject(clazz).toJSONString();
    }
}
