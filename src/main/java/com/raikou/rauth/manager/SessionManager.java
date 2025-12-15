package com.raikou.rauth.manager;

import com.raikou.rauth.RAuth;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {

    private final RAuth plugin;
    private final Map<UUID, Long> sessions = new HashMap<>();

    public SessionManager(RAuth plugin) {
        this.plugin = plugin;
    }

    public void createSession(UUID uuid) {
        int timeout = plugin.getConfigManager().getMaxPasswordLength(); // Misused for now, need simpler config access
        // Correctly get timeout
        timeout = plugin.getConfig().getInt("security.session-timeout", 30);

        if (timeout > 0) {
            sessions.put(uuid, System.currentTimeMillis() + (timeout * 60 * 1000L));
        }
    }

    public boolean hasSession(UUID uuid) {
        if (!sessions.containsKey(uuid))
            return false;

        long expiry = sessions.get(uuid);
        if (System.currentTimeMillis() > expiry) {
            sessions.remove(uuid);
            return false;
        }

        // Refresh session
        createSession(uuid);
        return true;
    }

    public void endSession(UUID uuid) {
        sessions.remove(uuid);
    }
}
