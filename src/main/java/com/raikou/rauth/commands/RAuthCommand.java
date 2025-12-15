package com.raikou.rauth.commands;

import com.raikou.rauth.RAuth;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RAuthCommand implements CommandExecutor {

    private final RAuth plugin;

    public RAuthCommand(RAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (!sender.hasPermission("rauth.admin")) {
            sender.sendMessage(plugin.getLanguageManager().getMessage("no-permission"));
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            plugin.getConfigManager().loadConfig();
            plugin.getLanguageManager().loadMessages();
            sender.sendMessage(plugin.getLanguageManager().getMessage("prefix")
                    .append(net.kyori.adventure.text.Component.text("Reloaded configuration.")));
            return true;
        }

        sender.sendMessage(net.kyori.adventure.text.Component.text("Usage: /rauth <reload|help>"));
        return true;
    }
}
