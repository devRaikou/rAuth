package com.raikou.rauth.managers;

import com.raikou.rauth.RAuth;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimeoutManager {

    private final RAuth plugin;
    private final Map<UUID, BukkitTask> timeoutTasks;

    public TimeoutManager(RAuth plugin) {
        this.plugin = plugin;
        this.timeoutTasks = new HashMap<>();
    }

    public void startTimer(Player player) {
        // If already running, don't start another
        if (timeoutTasks.containsKey(player.getUniqueId()))
            return;

        // If player is already logged in, don't start
        if (plugin.getAuthManager().isLoggedIn(player))
            return;

        int timeoutSeconds = plugin.getConfigManager().getLoginTimeout();
        if (timeoutSeconds <= 0)
            return; // Disabled

        BukkitTask task = new BukkitRunnable() {
            int timeLeft = timeoutSeconds;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    stopTimer(player);
                    return;
                }

                // If player is authenticated, stop
                if (plugin.getAuthManager().isLoggedIn(player)) {
                    stopTimer(player);
                    return;
                }

                if (timeLeft <= 5 && timeLeft > 0) {
                    // Countdown Logic
                    String timeStr = String.valueOf(timeLeft);

                    // Chat Message
                    player.sendMessage(plugin.getLanguageManager().getMessage("timeout-chat", "time", timeStr));

                    // Title
                    Component title = plugin.getLanguageManager().getMessageWithoutPrefix("timeout-title");
                    Component subtitle = plugin.getLanguageManager().getMessageWithoutPrefix("timeout-subtitle", "time",
                            timeStr);

                    Title.Times times = Title.Times.times(Duration.ofMillis(100), Duration.ofMillis(800),
                            Duration.ofMillis(100));
                    Title titleObj = Title.title(title, subtitle, times);
                    player.showTitle(titleObj);

                    // Sound
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                }

                if (timeLeft <= 0) {
                    // Kick Logic
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.kick(plugin.getLanguageManager().getMessageWithoutPrefix("timeout-kick"));
                    });
                    stopTimer(player);
                }

                timeLeft--;
            }
        }.runTaskTimer(plugin, 20L, 20L);

        timeoutTasks.put(player.getUniqueId(), task);
    }

    public void stopTimer(Player player) {
        if (timeoutTasks.containsKey(player.getUniqueId())) {
            timeoutTasks.get(player.getUniqueId()).cancel();
            timeoutTasks.remove(player.getUniqueId());
        }
    }
}
