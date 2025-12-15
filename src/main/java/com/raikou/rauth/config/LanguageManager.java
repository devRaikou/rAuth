package com.raikou.rauth.config;

import com.raikou.rauth.RAuth;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LanguageManager {

    private final RAuth plugin;
    private FileConfiguration messagesConfig;
    private final MiniMessage miniMessage;
    private String prefix;

    public LanguageManager(RAuth plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        loadMessages();
    }

    public void loadMessages() {
        if (!new File(plugin.getDataFolder(), "messages_en.yml").exists()) {
            plugin.saveResource("messages_en.yml", false);
        }
        if (!new File(plugin.getDataFolder(), "messages_tr.yml").exists()) {
            plugin.saveResource("messages_tr.yml", false);
        }

        String lang = plugin.getConfigManager().getLanguage();
        File messageFile = new File(plugin.getDataFolder(), "messages_" + lang + ".yml");

        messagesConfig = YamlConfiguration.loadConfiguration(messageFile);

        prefix = messagesConfig.getString("prefix", "");
    }

    public Component getMessage(String key) {
        String msg = messagesConfig.getString("messages." + key);
        if (msg == null)
            return Component.text("Missing message: " + key);

        return miniMessage.deserialize(prefix + msg);
    }

    public Component getMessage(String key, String... placeholders) {
        String msg = messagesConfig.getString("messages." + key);
        if (msg == null)
            return Component.text("Missing message: " + key);

        for (int i = 0; i < placeholders.length; i += 2) {
            if (i + 1 < placeholders.length) {
                msg = msg.replace("<" + placeholders[i] + ">", placeholders[i + 1]);
            }
        }

        return miniMessage.deserialize(prefix + msg);
    }

    public Component getMessageWithoutPrefix(String key) {
        String msg = messagesConfig.getString("messages." + key);
        if (msg == null)
            return Component.text("Missing message: " + key);

        return miniMessage.deserialize(msg);
    }

    public Component getMessageWithoutPrefix(String key, String... placeholders) {
        String msg = messagesConfig.getString("messages." + key);
        if (msg == null)
            return Component.text("Missing message: " + key);

        for (int i = 0; i < placeholders.length; i += 2) {
            if (i + 1 < placeholders.length) {
                msg = msg.replace("<" + placeholders[i] + ">", placeholders[i + 1]);
            }
        }

        return miniMessage.deserialize(msg);
    }
}
