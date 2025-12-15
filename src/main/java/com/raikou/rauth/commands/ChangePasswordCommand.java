package com.raikou.rauth.commands;

import com.raikou.rauth.RAuth;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChangePasswordCommand implements CommandExecutor {

    private final RAuth plugin;

    public ChangePasswordCommand(RAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getLanguageManager().getMessage("only-players"));
            return true;
        }

        if (args.length != 2) {
            return false;
        }

        String oldPass = args[0];
        String newPass = args[1];

        // Length Validation for new password
        int min = plugin.getConfigManager().getMinPasswordLength();
        int max = plugin.getConfigManager().getMaxPasswordLength();

        if (newPass.length() < min) {
            player.sendMessage(
                    plugin.getLanguageManager().getMessage("password-too-short", "min", String.valueOf(min)));
            return true;
        }

        if (newPass.length() > max) {
            player.sendMessage(plugin.getLanguageManager().getMessage("password-too-long", "max", String.valueOf(max)));
            return true;
        }

        plugin.getAuthManager().changePassword(player, oldPass, newPass);
        return true;
    }
}
