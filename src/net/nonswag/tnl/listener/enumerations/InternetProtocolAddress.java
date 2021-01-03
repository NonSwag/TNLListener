package net.nonswag.tnl.listener.enumerations;

import javax.annotation.Nonnull;
import java.util.Objects;

/*******************************************************
 * Copyright (C) 2019-2023 NonSwag kirschnerdavid2466@gmail.com
 *
 * This file is part of TNLListener and was created at the 10/31/20
 *
 * TNLListener can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/

public class InternetProtocolAddress {

    @Nonnull private final String hostname;
    private final int port;

    public InternetProtocolAddress(@Nonnull String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public InternetProtocolAddress(@Nonnull String string) {
        this(string.split(":")[0], Integer.parseInt(string.split(":")[1]));
    }

    @Nonnull
    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getAsString() {
        return getHostname() + ":" + getPort();
    }

    @Override
    public String toString() {
        return "InternetProtocolAddress{" +
                "hostname='" + hostname + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InternetProtocolAddress that = (InternetProtocolAddress) o;
        return port == that.port &&
                hostname.equals(that.hostname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostname, port);
    }
}
