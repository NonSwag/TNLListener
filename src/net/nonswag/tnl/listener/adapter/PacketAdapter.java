package net.nonswag.tnl.listener.adapter;

import io.netty.channel.*;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.eventhandler.PlayerPacketEvent;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;

public class PacketAdapter {

    public static void uninject(@Nonnull TNLPlayer player) {
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

    public static void inject(@Nonnull TNLPlayer player) {
        try {
            if (player.getNetworkManager() == null || !player.isOnline()) {
                if (player.getPlayerConnection() == null) {
                    Logger.error.println("Failed to inject " + player.getName(), "The player connection can't be null");
                    player.disconnect(TNLListener.getInstance().getPrefix() + "\n" +
                            "§cThere are some unaccepted values in your connection");
                } else if (!player.isOnline()) {
                    Logger.error.println("Failed to inject " + player.getName(), "The player can't be offline");
                    player.disconnect(TNLListener.getInstance().getPrefix() + "\n" +
                            "§cYou are online but your connection is offline?!");
                } else {
                    Logger.error.println("Failed to inject " + player, "Reached an unreachable statement?!");
                    player.disconnect(TNLListener.getInstance().getPrefix() + "\n§cReached an unreachable statement?!");
                }
                return;
            }
            uninject(player);

            ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

                @Override
                public void channelRead(ChannelHandlerContext channelHandlerContext, Object packetObject) {
                    try {
                        PlayerPacketEvent event = new PlayerPacketEvent(player, packetObject);
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            super.channelRead(channelHandlerContext, event.getPacket());
                        }
                    } catch (Exception e) {
                        Logger.error.println(e);
                        uninject(player);
                    }
                }

                @Override
                public void write(ChannelHandlerContext channelHandlerContext, Object packetObject, ChannelPromise channelPromise) {
                    try {
                        PlayerPacketEvent event = new PlayerPacketEvent(player, packetObject);
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            super.write(channelHandlerContext, event.getPacket(), channelPromise);
                        }
                    } catch (Exception e) {
                        Logger.error.println(e);
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
        } catch (Exception e) {
            uninject(player);
            Logger.error.println(e);
        }
    }
}
