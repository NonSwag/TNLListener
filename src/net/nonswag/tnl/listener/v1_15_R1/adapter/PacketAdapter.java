package net.nonswag.tnl.listener.v1_15_R1.adapter;

import io.netty.channel.*;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.v1_15_R1.api.playerAPI.TNLPlayer;
import net.nonswag.tnl.listener.v1_15_R1.eventHandler.PlayerPacketEvent;

public class PacketAdapter {

    public static void uninject(TNLPlayer player) {
        try {
            Channel channel = player.getNetworkManager().channel;
            if (channel.pipeline().get(player.getName() + "-TNLListener") != null) {
                channel.eventLoop().submit(() -> {
                    channel.pipeline().remove(player.getName() + "-TNLListener");
                });
            }
        } catch (Throwable ignored) {
        }
    }

    public static void inject(TNLPlayer player) {
        try {
            if (player == null || player.getNetworkManager() == null || !player.isOnline()) {
                if (player == null) {
                    NMSMain.stacktrace("Failed to inject '-/-', the player can't be null");
                } else if (player.getPlayerConnection() == null) {
                    NMSMain.stacktrace("Failed to inject " + player.getName(), "The player connection can't be null");
                    player.disconnect(NMSMain.getPrefix() + "\n" +
                            "§cThere are some unaccepted values in your connection");
                } else if (!player.isOnline()) {
                    NMSMain.stacktrace("Failed to inject " + player.getName(), "The player can't be offline");
                    player.disconnect(NMSMain.getPrefix() + "\n" +
                            "§cYou are online but your connection is offline?!");
                } else {
                    NMSMain.stacktrace("Failed to inject " + player, "Reached an unreachable statement?!");
                    player.disconnect(NMSMain.getPrefix() + "\n§cReached an unreachable statement?!");
                }
                return;
            }
            uninject(player);

            ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

                @Override
                public void channelRead(ChannelHandlerContext channelHandlerContext, Object packetObject) {
                    try {
                        PlayerPacketEvent event = new PlayerPacketEvent(player, packetObject);
                        NMSMain.callEvent(event);
                        if (!event.isCancelled()) {
                            super.channelRead(channelHandlerContext, event.getPacket());
                        }
                    } catch (Throwable t) {
                        NMSMain.stacktrace(t);
                        uninject(player);
                    }
                }

                @Override
                public void write(ChannelHandlerContext channelHandlerContext, Object packetObject, ChannelPromise channelPromise) {
                    try {
                        PlayerPacketEvent event = new PlayerPacketEvent(player, packetObject);
                        NMSMain.callEvent(event);
                        if (!event.isCancelled()) {
                            super.write(channelHandlerContext, event.getPacket(), channelPromise);
                        }
                    } catch (Throwable t) {
                        NMSMain.stacktrace(t);
                        uninject(player);
                    }
                }
            };

            ChannelPipeline pipeline = player.getNetworkManager().channel.pipeline();
            try {
                pipeline.addBefore("packet_handler", player.getName() + "-TNLListener", channelDuplexHandler);
            } catch (Throwable ignored) {
                uninject(player);
            }
        } catch (Throwable t) {
            uninject(player);
            NMSMain.stacktrace(t);
        }
    }
}
