package net.nonswag.tnl.listener.enumerations;

import java.util.Objects;

public class InternetProtocolAddress {

    private final String hostname;
    private final int port;

    public InternetProtocolAddress(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public InternetProtocolAddress(String string) {
        this(string.split(":")[0], Integer.parseInt(string.split(":")[1]));
    }

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
