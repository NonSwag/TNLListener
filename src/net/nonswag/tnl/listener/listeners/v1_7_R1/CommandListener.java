package net.nonswag.tnl.listener.listeners.v1_7_R1;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.message.MessageKey;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0];
        TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> player = TNLListener.getInstance().getPlayer(event.getPlayer());
        if (command.equalsIgnoreCase("/reload") || command.equalsIgnoreCase("/rl") || command.equalsIgnoreCase("/spigot")) {
            event.setCancelled(true);
            player.sendMessage(MessageKey.DISABLED_COMMAND, new Placeholder("command", command.toLowerCase()));
        } else {
            if (Settings.BETTER_COMMANDS.getValue()) {
                if (Bukkit.getServer().getHelpMap().getHelpTopic(command) == null) {
                    event.setCancelled(true);
                    player.sendMessage(MessageKey.UNKNOWN_COMMAND, new Placeholder("command", command.toLowerCase()));
                }
            }
        }
    }
}
