package net.nonswag.tnl.bridge.proxy.listeners;

import net.nonswag.tnl.api.event.EventHandler;
import net.nonswag.tnl.api.event.Listener;
import net.nonswag.tnl.bridge.events.MessageDecodeEvent;
import net.nonswag.tnl.bridge.packets.LoginPacket;
import net.nonswag.tnl.bridge.packets.LogoutPacket;
import net.nonswag.tnl.bridge.proxy.Bridge;
import net.nonswag.tnl.bridge.proxy.ConnectedServer;
import net.nonswag.tnl.listener.api.server.Server;

public class MessageDecodeListener implements Listener {

    @EventHandler
    public void onMessageDecode(MessageDecodeEvent event) {
        try {
            if (event.getKey().equalsIgnoreCase(LoginPacket.class.getSimpleName())) {
                String serverName = event.getValue().get("serverName").toString();
                String forwardingSecret = event.getValue().get("forwardingSecret").toString();
                if (forwardingSecret.equals(Bridge.getForwardingSecret())) {
                    new ConnectedServer(event.getSocket(), Server.getOrDefault(serverName, new Server(serverName, event.getSocket().getPort())));
                    Bridge.print("§8[§fconnected-remote§8] §aServer §8'§6" + serverName + "§8'§a has connected");
                } else {
                    event.getSocket().close();
                    Bridge.stacktrace("§8[§fremote-connection§8] §cServer §8'§6" + serverName + "§8'§c sent an invalid forwarding secret");
                }
            } else if (event.getKey().equalsIgnoreCase(LogoutPacket.class.getSimpleName())) {
                event.getSocket().close();
                Bridge.stacktrace("§8[§fremote-connection§8] §cThe Remote §8'§6" + event.getSocket().getInetAddress().toString() + "§8'§c sent an invalid forwarding secret");
            }
        } catch (Throwable t) {
            Bridge.stacktrace(t);
        }
    }
}
