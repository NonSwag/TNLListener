package net.nonswag.tnl.listener.api.network;

import javax.annotation.Nonnull;
import java.util.Objects;

public class InternetProtocolAddress {

    @Nonnull
    private final String hostname;
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

    @Nonnull
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
