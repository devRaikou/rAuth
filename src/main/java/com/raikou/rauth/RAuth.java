package com.raikou.rauth;

import com.raikou.rauth.commands.*;
import com.raikou.rauth.config.ConfigManager;
import com.raikou.rauth.config.LanguageManager;
import com.raikou.rauth.database.DatabaseManager;
import com.raikou.rauth.listeners.PlayerListener;
import com.raikou.rauth.manager.AuthManager;
import com.raikou.rauth.managers.TimeoutManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.raikou.rauth.utils.EmptyTabCompleter;

public class RAuth extends JavaPlugin {

    private static RAuth instance;
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private DatabaseManager databaseManager;
    private AuthManager authManager;
    private TimeoutManager timeoutManager;

    @Override
    public void onEnable() {
        instance = this;

        // Load Config
        this.configManager = new ConfigManager(this);
        this.languageManager = new LanguageManager(this);

        // Initialize Database
        this.databaseManager = new DatabaseManager(this);

        // Initialize Managers
        // Initialize Managers
        this.authManager = new AuthManager(this);
        this.timeoutManager = new TimeoutManager(this);

        // Register Commands
        getCommand("register").setExecutor(new RegisterCommand(this));
        getCommand("register").setTabCompleter(new EmptyTabCompleter());

        getCommand("login").setExecutor(new LoginCommand(this));
        getCommand("login").setTabCompleter(new EmptyTabCompleter());

        getCommand("logout").setExecutor(new LogoutCommand(this));
        getCommand("logout").setTabCompleter(new EmptyTabCompleter());

        getCommand("changepassword").setExecutor(new ChangePasswordCommand(this));
        getCommand("changepassword").setTabCompleter(new EmptyTabCompleter());

        getCommand("rauth").setExecutor(new RAuthCommand(this));
        getCommand("rauth").setTabCompleter(new RAuthTabCompleter());

        // Register Listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        // Register BungeeCord Channel
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getLogger().info("rAuth has been enabled!");
    }

    @Override
    public void onDisable() {
        // Close Database connections
        if (databaseManager != null) {
            databaseManager.close();
        }

        getLogger().info("rAuth has been disabled!");
    }

    public static RAuth getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public TimeoutManager getTimeoutManager() {
        return timeoutManager;
    }
}
