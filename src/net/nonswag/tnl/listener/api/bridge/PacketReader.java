package net.nonswag.tnl.listener.api.bridge;

import net.nonswag.tnl.listener.api.bridge.packet.Packet;

/*******************************************************
 * Copyright (C) 2019-2023 NonSwag kirschnerdavid2466@gmail.com
 *
 * This file is part of TNLListener and was created at the 10/31/20
 *
 * TNLListener can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/

class PacketReader {

    static void read(Packet<?> packet) {
        System.out.println(packet.encode(packet));
    }
}
