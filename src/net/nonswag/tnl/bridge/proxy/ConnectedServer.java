package net.nonswag.tnl.bridge.proxy;

import net.nonswag.tnl.listener.api.server.Server;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class ConnectedServer {

    @Nonnull private final Socket socket;
    @Nonnull private final Server server;
    @Nonnull private final OutputStream outputStream;
    @Nonnull private final PrintWriter printWriter;

    public ConnectedServer(@Nonnull Socket socket, @Nonnull Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.outputStream = getSocket().getOutputStream();
        this.printWriter = new PrintWriter(outputStream);
    }

    @Nonnull
    public Socket getSocket() {
        return socket;
    }

    @Nonnull
    public Server getServer() {
        return server;
    }

    @Nonnull
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Nonnull
    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    @Override
    public String toString() {
        return "ConnectedServer{" +
                "socket=" + socket +
                ", server=" + server +
                ", outputStream=" + outputStream +
                ", printWriter=" + printWriter +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectedServer that = (ConnectedServer) o;
        return socket.equals(that.socket) && server.equals(that.server) && outputStream.equals(that.outputStream) && printWriter.equals(that.printWriter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, server, outputStream, printWriter);
    }
}
