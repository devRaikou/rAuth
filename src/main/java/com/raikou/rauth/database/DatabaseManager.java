package com.raikou.rauth.database;

import com.raikou.rauth.RAuth;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseManager {

    private final RAuth plugin;
    private HikariDataSource dataSource;

    public DatabaseManager(RAuth plugin) {
        this.plugin = plugin;
        initDatabase();
        createTables();
    }

    private void initDatabase() {
        String type = plugin.getConfig().getString("database.type", "SQLITE");
        HikariConfig config = new HikariConfig();

        if (type.equalsIgnoreCase("MYSQL")) {
            config.setJdbcUrl("jdbc:mysql://" +
                    plugin.getConfig().getString("database.host") + ":" +
                    plugin.getConfig().getString("database.port") + "/" +
                    plugin.getConfig().getString("database.database"));
            config.setUsername(plugin.getConfig().getString("database.username"));
            config.setPassword(plugin.getConfig().getString("database.password"));
        } else {
            File file = new File(plugin.getDataFolder(), "database.db");
            config.setJdbcUrl("jdbc:sqlite:" + file.getAbsolutePath());
        }

        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
    }

    private void createTables() {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS rauth_users (" +
                                "uuid VARCHAR(36) PRIMARY KEY," +
                                "username VARCHAR(16) NOT NULL," +
                                "password_hash VARCHAR(255) NOT NULL," +
                                "reg_ip VARCHAR(45) NOT NULL," +
                                "last_ip VARCHAR(45) NOT NULL," +
                                "reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                                "last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                                ")")) {
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not create database tables!");
            e.printStackTrace();
        }
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public boolean isRegistered(UUID uuid) {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM rauth_users WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void registerUser(UUID uuid, String username, String passwordHash, String ip) {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO rauth_users (uuid, username, password_hash, reg_ip, last_ip) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, uuid.toString());
            ps.setString(2, username);
            ps.setString(3, passwordHash);
            ps.setString(4, ip);
            ps.setString(5, ip);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPasswordHash(UUID uuid) {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT password_hash FROM rauth_users WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("password_hash");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateLastLogin(UUID uuid, String ip) {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE rauth_users SET last_ip = ?, last_login = CURRENT_TIMESTAMP WHERE uuid = ?")) {
            ps.setString(1, ip);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(UUID uuid, String newHash) {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn
                        .prepareStatement("UPDATE rauth_users SET password_hash = ? WHERE uuid = ?")) {
            ps.setString(1, newHash);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getRegistrationCount(String ip) {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM rauth_users WHERE reg_ip = ?")) {
            ps.setString(1, ip);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
