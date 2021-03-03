package net.nonswag.tnl.listener.listeners;

import net.nonswag.tnl.listener.TNLListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0];
        switch (command.toLowerCase()) {
            case "/reload":
            case "/rl":
            case "/spigot":
                event.setCancelled(true);
                event.getPlayer().sendMessage(TNLListener.getInstance().getPrefix() + " §cThe Command §8(§4" + command.toLowerCase() + "§8) §cis disabled");
                return;
        }
        if (!TNLListener.getInstance().isBetterCommands()) {
            return;
        }
        command = event.getMessage();
        command = command.replaceAll(" ", "");
        command = command.replaceAll("/", "");
        if (command.equalsIgnoreCase("")) {
            event.setCancelled(true);
        } else {
            if (!event.isCancelled()) {
                if (Bukkit.getServer().getHelpMap().getHelpTopic(event.getMessage().split(" ")[0]) == null) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(TNLListener.getInstance().getPrefix() + " " + TNLListener.getInstance().getUnknownCommandMessage().replace("%command%", event.getMessage().split(" ")[0].toLowerCase()));
                }
            }
        }
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        String command = event.getCommand().split(" ")[0];
        switch (command.toLowerCase()) {
            case "reload":
            case "rl":
            case "spigot":
                event.setCancelled(true);
                event.getSender().sendMessage(TNLListener.getInstance().getPrefix() + " §cThe Command §8(§4" + command.toLowerCase() + "§8) §cis disabled");
                return;
        }
        if (!TNLListener.getInstance().isBetterCommands()) {
            return;
        }
        command = "/" + event.getCommand();
        command = command.replaceAll(" ", "");
        command = command.replaceAll("/", "");
        if (command.equalsIgnoreCase("")) {
            event.setCancelled(true);
        } else {
            command = "/" + event.getCommand();
            if (!event.isCancelled()) {
                if (Bukkit.getServer().getHelpMap().getHelpTopic(command.split(" ")[0]) == null) {
                    event.setCancelled(true);
                    event.getSender().sendMessage(TNLListener.getInstance().getPrefix() + " " + TNLListener.getInstance().getUnknownCommandMessage().replace("%command%", event.getCommand().split(" ")[0].toLowerCase()));
                }
            }
        }
    }
}
