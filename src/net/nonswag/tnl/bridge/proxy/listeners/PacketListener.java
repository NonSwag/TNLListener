package net.nonswag.tnl.bridge.proxy.listeners;

import net.nonswag.tnl.api.event.EventHandler;
import net.nonswag.tnl.api.event.Listener;
import net.nonswag.tnl.bridge.ChannelDirection;
import net.nonswag.tnl.bridge.events.PacketEvent;
import net.nonswag.tnl.bridge.packets.LoginPacket;
import net.nonswag.tnl.bridge.packets.LogoutPacket;
import net.nonswag.tnl.bridge.proxy.Bridge;
import net.nonswag.tnl.bridge.proxy.ConnectedServer;
import net.nonswag.tnl.cloud.utils.ServerUtil;

public class PacketListener implements Listener {

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (!event.isCancelled()) {
            if (event.getChannelDirection().equals(ChannelDirection.BRIDGE_IN)) {
                try {
                    if (event.getPacket() instanceof LoginPacket) {
                        LoginPacket packet = (LoginPacket) event.getPacket();
                        if (ServerUtil.getServer(packet.getServerName()) != null) {
                            if (packet.getForwardingSecret().equals(Bridge.getForwardingSecret())) {
                                new ConnectedServer(event.getSocket(), ServerUtil.getServer(packet.getServerName()));
                                Bridge.print("§8[§fconnected-remote§8] §aServer §8'§6" + packet.getServerName() + "§8'§a has connected");
                            } else {
                                event.getSocket().close();
                                Bridge.stacktrace("§8[§fremote-connection§8] §cServer §8'§4" + packet.getServerName() + "§8'§c sent an invalid forwarding secret");
                            }
                        } else {
                            event.getSocket().close();
                            Bridge.stacktrace("§8[§fremote-connection§8] §cServer §8'§4" + packet.getServerName() + "§8'§c is not Registered");
                        }
                    } else if (event.getPacket() instanceof LogoutPacket) {
                        event.getSocket().close();
                    }
                } catch (Throwable t) {
                    Bridge.stacktrace(t);
                }
            }
        }
    }
}
