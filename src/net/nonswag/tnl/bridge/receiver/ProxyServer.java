package net.nonswag.tnl.bridge.receiver;

import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.enumerations.InternetProtocolAddress;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ProxyServer {

    @Nonnull private final InternetProtocolAddress address;
    @Nullable private Socket socket;
    @Nullable private OutputStream outputStream;
    @Nullable private PrintWriter writer;

    public ProxyServer(InternetProtocolAddress address) {
        this.address = address;
        this.connect();
    }

    @Nonnull
    public InternetProtocolAddress getAddress() {
        return address;
    }

    @Nullable
    public Socket getSocket() {
        return socket;
    }

    @Nullable
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Nullable
    public PrintWriter getWriter() {
        return writer;
    }

    public void setSocket(@Nonnull Socket socket) {
        this.socket = socket;
    }

    public void setOutputStream(@Nonnull OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setWriter(@Nonnull PrintWriter writer) {
        this.writer = writer;
    }

    public boolean isConnected() {
        if (getSocket() == null || getSocket().isClosed()) {
            return false;
        }
        return getSocket().isConnected() && getSocket().isBound();
    }

    public void disconnect() {
        assert getSocket() != null;
        try {
            if (!getSocket().isBound()) {
                NMSMain.stacktrace("Error while stopping the bridge (already stopped)");
            } else {
                if (getWriter() != null) {
                    getWriter().close();
                }
                getSocket().close();
                NMSMain.print("Stopped bridge on '" + getAddress().getAsString() + "'");
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t, "Error while stopping the bridge");
        }
    }

    public void connect() {
        if (getSocket() == null || !isConnected()) {
            try {
                setSocket(new Socket(address.getHostname(), address.getPort()));
                setOutputStream(getSocket().getOutputStream());
                if (getOutputStream() != null) {
                    setWriter(new PrintWriter(getOutputStream()));
                }
                new PacketAdapter(this);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } else {
            NMSMain.stacktrace("Error while starting the bridge (already started)");
        }
    }

    public void sendPacket(Packet<?> packet) {
        assert getWriter() != null;
        try {
            getWriter().write(packet.encode(packet) + "\n");
        } catch (Throwable t) {
            NMSMain.stacktrace(t, "Error while writing packet '" + packet.getName() + "'");
        }
    }

    public void readPacket(@Nonnull Packet<?> packet) {
        PacketReader.read(packet);
    }

    public static ProxyServer getInstance() {
        return NMSMain.getProxyServer();
    }
}
