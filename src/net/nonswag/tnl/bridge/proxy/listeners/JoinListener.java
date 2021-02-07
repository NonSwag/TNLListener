package net.nonswag.tnl.bridge.proxy.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListener;
import net.nonswag.tnl.bridge.packets.PlayerInfoPacket;
import net.nonswag.tnl.bridge.proxy.Bridge;
import net.nonswag.tnl.bridge.proxy.ConnectedServer;
import net.nonswag.tnl.bridge.proxy.PacketHandler;
import net.nonswag.tnl.cloud.utils.ServerUtil;
import net.nonswag.tnl.listener.api.object.Set;

import java.util.ArrayList;
import java.util.List;

public class JoinListener {

    @Subscribe
    public void onJoin(ServerPostConnectEvent event) {
        RegisteredServer server = ServerUtil.getServer(event.getPlayer());
        if (server != null) {
            ConnectedServer connectedServer = Bridge.getConnectedServer(server);
            if (connectedServer != null) {
                List<Packet<PacketListener>> packets = new ArrayList<>();
                packets.add(new PlayerInfoPacket(event.getPlayer().getUniqueId(), new Set<>("version", event.getPlayer().getProtocolVersion().getProtocol())));
                PacketHandler.sendPackets(connectedServer, packets);
            } else {
                event.getPlayer().disconnect(Component.text(Bridge.getPrefix() + "\n" +
                        "§cThe Server §4" + server.getServerInfo().getName() + "§c didn't answered"));
            }
        } else {
            event.getPlayer().disconnect(Component.text(Bridge.getPrefix() + "\n" +
                    "§cThe Server didn't answered"));
        }
    }
}
