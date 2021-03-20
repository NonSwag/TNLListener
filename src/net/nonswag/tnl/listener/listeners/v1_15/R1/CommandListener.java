package net.nonswag.tnl.listener.listeners.v1_15.R1;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.message.Message;
import net.nonswag.tnl.listener.api.message.MessageKey;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> player = TNLListener.getInstance().getPlayer(event.getPlayer());
        String command = event.getMessage().split(" ")[0];
        switch (command.toLowerCase()) {
            case "/reload":
            case "/rl":
            case "/spigot":
                event.setCancelled(true);
                player.sendMessage(MessageKey.DISABLED_COMMAND, new Placeholder("command", command.toLowerCase()));
                return;
        }
        if (Settings.BETTER_COMMANDS.getValue()) {
            command = event.getMessage();
            command = command.replaceAll(" ", "");
            command = command.replaceAll("/", "");
            if (command.equalsIgnoreCase("")) {
                event.setCancelled(true);
            } else {
                if (!event.isCancelled()) {
                    if (Bukkit.getServer().getHelpMap().getHelpTopic(event.getMessage().split(" ")[0]) == null) {
                        event.setCancelled(true);
                        player.sendMessage(MessageKey.UNKNOWN_COMMAND, new Placeholder("command", command.toLowerCase()));
                    }
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
                event.getSender().sendMessage(Message.DISABLED_COMMAND_EN.getText(new Placeholder("command", command.toLowerCase())));
                return;
        }
        if (Settings.BETTER_COMMANDS.getValue()) {
            if (!command.isEmpty()) {
                command = "/" + event.getCommand();
                if (!event.isCancelled()) {
                    if (Bukkit.getServer().getHelpMap().getHelpTopic(command.split(" ")[0]) == null) {
                        event.setCancelled(true);
                        event.getSender().sendMessage(Message.UNKNOWN_COMMAND_EN.getText(new Placeholder("command", command.toLowerCase())));
                    }
                }
            } else {
                event.setCancelled(true);
            }
        }
    }
}
