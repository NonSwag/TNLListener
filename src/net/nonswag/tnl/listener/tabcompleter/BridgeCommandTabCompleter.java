package net.nonswag.tnl.listener.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

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
