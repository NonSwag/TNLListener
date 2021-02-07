package net.nonswag.tnl.bridge.proxy.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.nonswag.tnl.bridge.Packet;
import net.nonswag.tnl.bridge.PacketListener;
import net.nonswag.tnl.bridge.packets.PlayerInfoPacket;
import net.nonswag.tnl.bridge.proxy.Bridge;
import net.nonswag.tnl.bridge.proxy.ConnectedServer;
import net.nonswag.tnl.bridge.proxy.PacketHandler;
import net.nonswag.tnl.cloud.utils.ServerUtil;
import net.nonswag.tnl.listener.api.object.List;
import net.nonswag.tnl.listener.api.object.Set;

public class JoinListener {

    @Subscribe
    public void onJoin(ServerPreConnectEvent event) {
        RegisteredServer server = ServerUtil.getServer(event.getPlayer());
        assert server != null;
        ConnectedServer connectedServer = Bridge.getConnectedServer(server);
        assert connectedServer != null;
        List<Packet<PacketListener>> packets = new List<>();
        packets.getObjects().add(new PlayerInfoPacket(event.getPlayer().getUniqueId(), new Set<>("version", event.getPlayer().getProtocolVersion().getProtocol())));
        PacketHandler.sendPackets(connectedServer, packets);
    }
}
