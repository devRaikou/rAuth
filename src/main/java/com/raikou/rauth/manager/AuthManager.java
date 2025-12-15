package com.raikou.rauth.manager;

import com.raikou.rauth.RAuth;
import com.raikou.rauth.utils.SecurityUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AuthManager {

    private final RAuth plugin;
    private final Set<UUID> loggedInPlayers = new HashSet<>();
    private final SessionManager sessionManager;

    public AuthManager(RAuth plugin) {
        this.plugin = plugin;
        this.sessionManager = new SessionManager(plugin);
    }

    public boolean isLoggedIn(Player player) {
        return loggedInPlayers.contains(player.getUniqueId());
    }

    public void handleJoin(Player player) {
        if (plugin.getDatabaseManager().isRegistered(player.getUniqueId())) {
            // Check session
            if (sessionManager.hasSession(player.getUniqueId())) {
                String lastIp = player.getAddress().getAddress().getHostAddress();
                // We should verification against DB last IP if we want strict IP sessions,
                // but for now simple time-based session + re-login logic.
                // NOTE: Real implementation might verify IP from DB.

                loggedInPlayers.add(player.getUniqueId());
                plugin.getTimeoutManager().stopTimer(player);
                player.sendMessage(plugin.getLanguageManager().getMessage("login-success"));
            } else {
                player.sendMessage(plugin.getLanguageManager().getMessage("join-login"));
            }
        } else {
            player.sendMessage(plugin.getLanguageManager().getMessage("join-register"));
        }
    }

    public void handleQuit(Player player) {
        // If logged in, keep session
        if (isLoggedIn(player)) {
            // Updated Last Login time/IP in DB
            plugin.getDatabaseManager().updateLastLogin(
                    player.getUniqueId(),
                    player.getAddress().getAddress().getHostAddress());
        }
        loggedInPlayers.remove(player.getUniqueId());
    }

    public void register(Player player, String password) {
        if (plugin.getDatabaseManager().isRegistered(player.getUniqueId())) {
            player.sendMessage(plugin.getLanguageManager().getMessage("already-registered"));
            return;
        }

        String ip = player.getAddress().getAddress().getHostAddress();
        int maxReg = plugin.getConfig().getInt("security.max-registrations-per-ip", 3);

        if (plugin.getDatabaseManager().getRegistrationCount(ip) >= maxReg) {
            player.sendMessage(plugin.getLanguageManager().getMessage("max-registrations"));
            return;
        }

        String hash = SecurityUtil.hashPassword(password);
        plugin.getDatabaseManager().registerUser(player.getUniqueId(), player.getName(), hash, ip);

        loggedInPlayers.add(player.getUniqueId());
        sessionManager.createSession(player.getUniqueId());
        plugin.getTimeoutManager().stopTimer(player);

        player.sendMessage(plugin.getLanguageManager().getMessage("register-success"));
    }

    public void login(Player player, String password) {
        if (isLoggedIn(player)) {
            player.sendMessage(plugin.getLanguageManager().getMessage("already-logged-in"));
            return;
        }

        if (!plugin.getDatabaseManager().isRegistered(player.getUniqueId())) {
            player.sendMessage(plugin.getLanguageManager().getMessage("not-registered"));
            return;
        }

        String hash = plugin.getDatabaseManager().getPasswordHash(player.getUniqueId());
        if (SecurityUtil.checkPassword(password, hash)) {
            loggedInPlayers.add(player.getUniqueId());
            sessionManager.createSession(player.getUniqueId());
            plugin.getTimeoutManager().stopTimer(player);

            // Update IP
            plugin.getDatabaseManager().updateLastLogin(
                    player.getUniqueId(),
                    player.getAddress().getAddress().getHostAddress());

            player.sendMessage(plugin.getLanguageManager().getMessage("login-success"));
        } else {
            player.sendMessage(plugin.getLanguageManager().getMessage("login-failed"));
        }
    }

    public void logout(Player player) {
        if (!isLoggedIn(player)) {
            player.sendMessage(plugin.getLanguageManager().getMessage("not-logged-in"));
            return;
        }

        loggedInPlayers.remove(player.getUniqueId());
        sessionManager.endSession(player.getUniqueId());
        player.sendMessage(plugin.getLanguageManager().getMessage("logged-out"));
    }

    public void changePassword(Player player, String oldPassword, String newPassword) {
        if (!isLoggedIn(player)) {
            player.sendMessage(plugin.getLanguageManager().getMessage("not-logged-in"));
            return;
        }

        String hash = plugin.getDatabaseManager().getPasswordHash(player.getUniqueId());
        if (SecurityUtil.checkPassword(oldPassword, hash)) {
            String newHash = SecurityUtil.hashPassword(newPassword);
            plugin.getDatabaseManager().updatePassword(player.getUniqueId(), newHash);
            player.sendMessage(plugin.getLanguageManager().getMessage("password-changed"));
        } else {
            player.sendMessage(plugin.getLanguageManager().getMessage("password-mismatch"));
        }
    }
}
