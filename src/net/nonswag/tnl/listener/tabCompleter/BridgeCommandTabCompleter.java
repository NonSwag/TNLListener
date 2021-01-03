package net.nonswag.tnl.listener.tabCompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

/*******************************************************
 * Copyright (C) 2019-2023 NonSwag kirschnerdavid2466@gmail.com
 *
 * This file is part of TNLListener and was created at the 11/12/20
 *
 * TNLListener can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/

public class BridgeCommandTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (sender.hasPermission("tnl.admin")) {
            if (args.length <= 1) {
                suggestions.add("write");
                suggestions.add("reconnect");
                suggestions.add("disconnect");
                suggestions.add("connect");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("write")) {
                    suggestions.add("Packet<PacketListenerPlayIn>");
                    suggestions.add("Packet<PacketListenerPlayOut>");
                }
            }
            if (!suggestions.isEmpty()) {
                suggestions.removeIf(suggestion -> !suggestion.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
            }
        }
        return suggestions;
    }
}
