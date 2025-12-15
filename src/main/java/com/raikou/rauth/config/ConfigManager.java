package com.raikou.rauth.config;

import com.raikou.rauth.RAuth;
import java.io.File;

public class ConfigManager {

    private final RAuth plugin;

    public ConfigManager(RAuth plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
    }

    public String getLanguage() {
        return plugin.getConfig().getString("language", "en");
    }

    public int getMinPasswordLength() {
        return plugin.getConfig().getInt("security.min-password-length", 6);
    }

    public int getMaxPasswordLength() {
        return plugin.getConfig().getInt("security.max-password-length", 32);
    }

    public int getLoginTimeout() {
        return plugin.getConfig().getInt("security.login-timeout", 60);
    }

    public boolean isBungeeEnabled() {
        return plugin.getConfig().getBoolean("bungeecord.enabled", false);
    }

    public String getBungeeServer() {
        return plugin.getConfig().getString("bungeecord.server", "lobby");
    }
}
