import com.raikou.rauth.commands.*;
import com.raikou.rauth.config.ConfigManager;
import com.raikou.rauth.config.LanguageManager;
import com.raikou.rauth.database.DatabaseManager;
import com.raikou.rauth.listeners.PlayerListener;
import com.raikou.rauth.manager.AuthManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RAuth extends JavaPlugin {

    private static RAuth instance;
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private DatabaseManager databaseManager;
    private AuthManager authManager;

    @Override
    public void onEnable() {
        instance = this;

        // Load Config
        this.configManager = new ConfigManager(this);
        this.languageManager = new LanguageManager(this);

        // Initialize Database
        this.databaseManager = new DatabaseManager(this);

        // Initialize Managers
        this.authManager = new AuthManager(this);

        // Register Commands
        getCommand("register").setExecutor(new RegisterCommand(this));
        getCommand("login").setExecutor(new LoginCommand(this));
        getCommand("logout").setExecutor(new LogoutCommand(this));
        getCommand("changepassword").setExecutor(new ChangePasswordCommand(this));
        getCommand("rauth").setExecutor(new RAuthCommand(this));

        // Register Listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

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
}
