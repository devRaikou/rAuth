package com.raikou.rauth.commands;

import com.raikou.rauth.RAuth;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RegisterCommand implements CommandExecutor {

    private final RAuth plugin;

    public RegisterCommand(RAuth plugin) {
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

        String pass1 = args[0];
        String pass2 = args[1];

        // Length Validation
        int min = plugin.getConfigManager().getMinPasswordLength();
        int max = plugin.getConfigManager().getMaxPasswordLength();

        if (pass1.length() < min) {
            player.sendMessage(
                    plugin.getLanguageManager().getMessage("password-too-short", "min", String.valueOf(min)));
            return true;
        }

        if (pass1.length() > max) {
            player.sendMessage(plugin.getLanguageManager().getMessage("password-too-long", "max", String.valueOf(max)));
            return true;
        }

        if (!pass1.equals(pass2)) {
            player.sendMessage(plugin.getLanguageManager().getMessage("password-mismatch"));
            return true;
        }

        plugin.getAuthManager().register(player, pass1);
        return true;
    }
}
