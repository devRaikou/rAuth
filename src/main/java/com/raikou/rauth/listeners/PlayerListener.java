package com.raikou.rauth.listeners;

import com.raikou.rauth.RAuth;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    private final RAuth plugin;

    public PlayerListener(RAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // Send join prompt
        plugin.getAuthManager().handleJoin(event.getPlayer());
        plugin.getTimeoutManager().startTimer(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getAuthManager().handleQuit(event.getPlayer());
        plugin.getTimeoutManager().stopTimer(event.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!plugin.getAuthManager().isLoggedIn(event.getPlayer())) {
            // Check if player actually moved (not just look)
            if (event.getFrom().getX() != event.getTo().getX() ||
                    event.getFrom().getZ() != event.getTo().getZ()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        if (!plugin.getAuthManager().isLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
            // Hint to login/register?
        }
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!plugin.getAuthManager().isLoggedIn(event.getPlayer())) {
            String msg = event.getMessage().toLowerCase();
            // Allow login/register commands
            if (!msg.startsWith("/login") && !msg.startsWith("/register") &&
                    !msg.startsWith("/l ") && !msg.startsWith("/reg ")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(plugin.getLanguageManager().getMessage("not-logged-in"));
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!plugin.getAuthManager().isLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.getAuthManager().isLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!plugin.getAuthManager().isLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player && !plugin.getAuthManager().isLoggedIn(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && !plugin.getAuthManager().isLoggedIn(player)) {
            event.setCancelled(true);
        }
        if (event.getEntity() instanceof Player player && !plugin.getAuthManager().isLoggedIn(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!plugin.getAuthManager().isLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerAttemptPickupItemEvent event) {
        if (!plugin.getAuthManager().isLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
