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

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.getConfigManager().loadConfig();
                plugin.getLanguageManager().loadMessages();
                sender.sendMessage(plugin.getLanguageManager().getMessage("admin-reload"));
                return true;
            }

            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(plugin.getLanguageManager().getMessageWithoutPrefix("admin-help-list"));
                return true;
            }
        }

        sender.sendMessage(plugin.getLanguageManager().getMessage("admin-usage"));
        return true;
    }
}
