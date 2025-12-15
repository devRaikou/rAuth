package com.raikou.rauth.commands;

import com.raikou.rauth.RAuth;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LogoutCommand implements CommandExecutor {

    private final RAuth plugin;

    public LogoutCommand(RAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getLanguageManager().getMessage("only-players"));
            return true;
        }

        plugin.getAuthManager().logout(player);
        return true;
    }
}
