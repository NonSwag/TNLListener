package net.nonswag.tnl.listener.api.bridge;

import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.api.bridge.packet.Packet;
import net.nonswag.tnl.listener.enumerations.InternetProtocolAddress;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/*******************************************************
 * Copyright (C) 2019-2023 NonSwag kirschnerdavid2466@gmail.com
 *
 * This file is part of TNLListener and was created at the 10/31/20
 *
 * TNLListener can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/

public class ProxyServer {

    private final InternetProtocolAddress address;
    private Socket socket;
    private OutputStream outputStream;
    private PrintWriter writer;

    public ProxyServer(InternetProtocolAddress address) {
        this.address = address;
        this.connect();
    }

    private OutputStream getOutputStream() {
        return outputStream;
    }

    private PrintWriter getWriter() {
        return writer;
    }

    public boolean isConnected() {
        if (getSocket() == null || getSocket().isClosed()) {
            return false;
        }
        return getSocket().isConnected() && getSocket().isBound();
    }

    public void disconnect() {
        try {
            if (!getSocket().isBound()) {
                NMSMain.stacktrace("Error while stopping the bridge (already stopped)");
            } else {
                getWriter().close();
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
                setWriter(new PrintWriter(getOutputStream()));
                new PacketAdapter(this);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } else {
            NMSMain.stacktrace("Error while starting the bridge (already started)");
        }
    }

    public void sendPacket(Packet<?> packet) {
        try {
            getWriter().write(packet.encode(packet) + "\n");
        } catch (Throwable t) {
            NMSMain.stacktrace(t, "Error while writing packet '" + packet.getName() + "'");
        }
    }

    public void readPacket(Packet<?> packet) {
        PacketReader.read(packet);
    }

    public InternetProtocolAddress getAddress() {
        return address;
    }

    public Socket getSocket() {
        return socket;
    }

    public static ProxyServer getInstance() {
        return NMSMain.getProxyServer();
    }

    private void setSocket(Socket socket) {
        this.socket = socket;
    }

    private void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    private void setWriter(PrintWriter writer) {
        this.writer = writer;
    }
}
