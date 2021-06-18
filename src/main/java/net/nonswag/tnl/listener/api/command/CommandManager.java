package net.nonswag.tnl.listener.api.command;

import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.message.Message;
import net.nonswag.tnl.listener.api.message.Placeholder;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CommandManager {

    @Nonnull private final JavaPlugin plugin;

    public CommandManager(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Nonnull
    public JavaPlugin getPlugin() {
        return plugin;
    }

    public void registerCommand(@Nonnull String command, @Nonnull CommandExecutor commandExecutor) {
        registerCommand(command, null, commandExecutor, null);
    }

    public void registerCommand(@Nonnull String command, @Nonnull String permission, @Nonnull CommandExecutor commandExecutor) {
        registerCommand(command, permission, commandExecutor, null);
    }

    public void registerCommand(@Nonnull String command, @Nonnull CommandExecutor commandExecutor, @Nullable TabCompleter tabCompleter) {
        registerCommand(command, null, commandExecutor, tabCompleter);
    }

    public void registerCommand(@Nonnull String command, @Nullable String permission, @Nonnull CommandExecutor commandExecutor, @Nullable TabCompleter tabCompleter) {
        PluginCommand pluginCommand = getPlugin().getCommand(command);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(commandExecutor);
            pluginCommand.setAliases(pluginCommand.getAliases());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Registered command <'").append(command).append("'> with aliases <'").append(String.join(", ", pluginCommand.getAliases())).append("'>");
            if (permission != null) {
                pluginCommand.setPermission(permission);
                pluginCommand.setPermissionMessage(Message.NO_PERMISSION_EN.getText(new Placeholder("permission", permission)));
                stringBuilder.append(" with permission <'").append(permission).append("'>");
            }
            if (tabCompleter != null) {
                pluginCommand.setTabCompleter(tabCompleter);
            }
            Logger.debug.println(stringBuilder.toString());
        } else {
            Logger.error.println("The command <'" + command + "'> is not registered in your plugin.yml");
        }
    }
}
