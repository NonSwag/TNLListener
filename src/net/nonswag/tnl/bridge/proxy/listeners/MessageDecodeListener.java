package net.nonswag.tnl.bridge.proxy.listeners;

import net.nonswag.tnl.api.event.EventHandler;
import net.nonswag.tnl.api.event.Listener;
import net.nonswag.tnl.bridge.events.MessageDecodeEvent;
import net.nonswag.tnl.bridge.packets.LoginPacket;
import net.nonswag.tnl.bridge.packets.LogoutPacket;
import net.nonswag.tnl.bridge.proxy.Bridge;

public class MessageDecodeListener implements Listener {

    @EventHandler
    public void onMessageDecode(MessageDecodeEvent event) {
        try {
            if (event.getKey().equalsIgnoreCase("LoginPacket")) {
                String serverName = event.getValue().get("serverName").toString();
                String forwardingSecret = event.getValue().get("forwardingSecret").toString();
                event.setPacket(new LoginPacket(serverName, forwardingSecret));
            } else if (event.getKey().equalsIgnoreCase("LogoutPacket")) {
                event.setPacket(new LogoutPacket());
            }
        } catch (Throwable t) {
            Bridge.stacktrace(t);
        }
    }
}
