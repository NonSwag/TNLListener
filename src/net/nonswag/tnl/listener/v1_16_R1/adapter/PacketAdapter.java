package net.nonswag.tnl.listener.v1_16_R1.adapter;

import io.netty.channel.*;
import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.v1_16_R1.api.playerAPI.TNLPlayer;
import net.nonswag.tnl.listener.v1_16_R1.eventHandler.PlayerPacketEvent;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketAdapter {

    public static void uninject(Player player) {
        try {
            Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
            if (channel.pipeline().get(player.getName() + "-TNLListener") != null) {
                channel.eventLoop().submit(() -> {
                    channel.pipeline().remove(player.getName() + "-TNLListener");
                });
            }
        } catch (Throwable ignored) {
        }
    }

    public static void inject(Player player) {
        Thread packetHandlerThread = new Thread(() -> {
            try {
                if (player == null || ((CraftPlayer) player).getHandle().playerConnection == null || !player.isOnline()) {
                    if (player == null) {
                        NMSMain.stacktrace("Failed to inject '-/-', the player can't be null");
                    } else if (((CraftPlayer) player).getHandle().playerConnection == null) {
                        NMSMain.stacktrace("Failed to inject " + player.getName(), "The player connection can't be null");
                        new TNLPlayer(player).disconnect(NMSMain.getPrefix() + "\n" +
                                "§cThere are some unaccepted values in your connection");
                    } else if (!player.isOnline()) {
                        NMSMain.stacktrace("Failed to inject " + player.getName(), "The player can't be offline");
                        new TNLPlayer(player).disconnect(NMSMain.getPrefix() + "\n" +
                                "§cYou are online but your connection is offline?!");
                    } else {
                        NMSMain.stacktrace("Failed to inject " + player, "Reached an unreachable statement?!");
                        new TNLPlayer(player).disconnect(NMSMain.getPrefix() + "\n§cReached an unreachable statement?!");
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
                            return;
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
                            return;
                        }
                    }
                };
                ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
                try {
                    pipeline.addBefore("packet_handler", player.getName() + "-TNLListener", channelDuplexHandler);
                } catch (Throwable ignored) {
                    uninject(player);
                }
            } catch (Throwable t) {
                uninject(player);
                NMSMain.stacktrace(t);
            }
        });
        packetHandlerThread.setName("Injection thread");
        packetHandlerThread.start();
    }
}
