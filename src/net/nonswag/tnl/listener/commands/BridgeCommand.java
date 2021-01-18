package net.nonswag.tnl.listener.commands;

import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BridgeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("write")) {
                sender.sendMessage("§8[§f§lTNL§8] §cThis feature is in development");
            } else if (args[0].equalsIgnoreCase("reconnect")) {
                if (NMSMain.getProxyServer().isConnected()) {
                    NMSMain.getProxyServer().disconnect();
                    if (NMSMain.getProxyServer().isConnected()) {
                        sender.sendMessage("§8[§f§lTNL§8] §cAn error has occurred while closing the bridge");
                    } else {
                        NMSMain.getProxyServer().connect();
                        sender.sendMessage("§8[§f§lTNL§8] §aSuccessfully reconnected");
                    }
                } else {
                    sender.sendMessage("§8[§f§lTNL§8] §cThe bridge is not connected");
                }
            } else if (args[0].equalsIgnoreCase("disconnect")) {
                if (NMSMain.getProxyServer().isConnected()) {
                    NMSMain.getProxyServer().disconnect();
                    sender.sendMessage("§8[§f§lTNL§8] §aSuccessfully disconnected");
                } else {
                    sender.sendMessage("§8[§f§lTNL§8] §cThe bridge is not connected");
                }
            } else if (args[0].equalsIgnoreCase("connect")) {
                if (!NMSMain.getProxyServer().isConnected()) {
                    NMSMain.getProxyServer().connect();
                    sender.sendMessage("§8[§f§lTNL§8] §aSuccessfully connected");
                } else {
                    sender.sendMessage("§8[§f§lTNL§8] §cThe bridge is already connected");
                }
            } else {
                sender.sendMessage("§8[§f§lTNL§8] §c/bridge write §8[§6Packet§8] §8[§6Fields§8]");
                sender.sendMessage("§8[§f§lTNL§8] §c/bridge disconnect");
                sender.sendMessage("§8[§f§lTNL§8] §c/bridge reconnect");
                sender.sendMessage("§8[§f§lTNL§8] §c/bridge connect");
            }
        } else {
            sender.sendMessage("§8[§f§lTNL§8] §c/bridge write §8[§6Packet§8] §8[§6Fields§8]");
            sender.sendMessage("§8[§f§lTNL§8] §c/bridge disconnect");
            sender.sendMessage("§8[§f§lTNL§8] §c/bridge reconnect");
            sender.sendMessage("§8[§f§lTNL§8] §c/bridge connect");
        }
        return false;
    }
}
