package net.nonswag.tnl.listener.v1_8_R3.adapter;

import io.netty.channel.*;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.v1_8_R3.eventhandler.PlayerPacketEvent;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketAdapter {

    public static void inject(Player player) {
        try {
            if (player == null || !player.isOnline()) {
                NMSMain.stacktrace("Failed to inject " + player);
                return;
            }
            Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
            if (channel.pipeline().get(player.getName() + "-TNLListener") != null) {
                channel.eventLoop().submit(() -> {
                    channel.pipeline().remove(player.getName() + "-TNLListener");
                });
            }

            ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

                @Override
                public void channelRead(ChannelHandlerContext channelHandlerContext, Object packetObject) {
                    if (!player.isOnline()
                            || !NMSMain.getPlugin().isEnabled()
                            || channelHandlerContext.isRemoved()
                            || ((CraftPlayer) player).getHandle().playerConnection.isDisconnected()) {
                        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
                        channel.eventLoop().submit(() -> {
                            channel.pipeline().remove(player.getName() + "-TNLListener");
                        });
                        return;
                    } else {
                        PlayerPacketEvent event = new PlayerPacketEvent(player, packetObject);
                        NMSMain.callEvent(event);
                        if (!event.isCancelled()) {
                            try {
                                super.channelRead(channelHandlerContext, event.getPacket());
                            } catch (Throwable t) {
                                NMSMain.stacktrace(t);
                            }
                        }
                    }
                }

                @Override
                public void write(ChannelHandlerContext channelHandlerContext, Object packetObject, ChannelPromise channelPromise) {
                    try {
                        if (!player.isOnline()
                                || !NMSMain.getPlugin().isEnabled()
                                || channelHandlerContext.isRemoved()
                                || ((CraftPlayer) player).getHandle().playerConnection.isDisconnected()) {
                            Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
                            channel.eventLoop().submit(() -> {
                                channel.pipeline().remove(player.getName() + "-TNLListener");
                            });
                            return;
                        } else {
                            PlayerPacketEvent event = new PlayerPacketEvent(player, packetObject);
                            NMSMain.callEvent(event);
                            if (!event.isCancelled()) {
                                super.write(channelHandlerContext, event.getPacket(), channelPromise);
                            }
                        }
                    } catch (Throwable t) {
                        NMSMain.stacktrace(t);
                    }
                }
            };
            try {
                ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
                pipeline.addBefore("packet_handler", player.getName() + "-TNLListener", channelDuplexHandler);
            } catch (Throwable t) {
                NMSMain.stacktrace(t);
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }
}
