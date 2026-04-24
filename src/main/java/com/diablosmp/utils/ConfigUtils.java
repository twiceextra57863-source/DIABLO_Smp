package com.diablosmp.utils;

import com.diablosmp.DiabloSmpPlugin;
import org.bukkit.ChatColor;

/**
 * Utility class for accessing configuration values and formatted messages.
 */
public class ConfigUtils {
    private final DiabloSmpPlugin plugin;

    public ConfigUtils(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets a message from config.yml and translates colour codes (& -> §).
     * @param key the message key (e.g., "messages.ability-used")
     * @return formatted message string, or a fallback if missing
     */
    public String getMessage(String key) {
        String path = "messages." + key;
        String msg = plugin.getConfig().getString(path);
        if (msg == null) {
            plugin.getLogger().warning("Missing config message key: " + path);
            return "&cMissing message: " + key;
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * Gets a particle name from config, with a default fallback.
     * @param key the config path (e.g., "effects.soul-line-particle")
     * @param defaultValue default particle name if not found
     * @return particle name as string
     */
    public String getParticleName(String key, String defaultValue) {
        return plugin.getConfig().getString(key, defaultValue);
    }

    /**
     * Gets a colour hex string (without #) from config.
     * @param key the config path (e.g., "effects.crown-colour.stage1")
     * @param defaultValue default hex (e.g., "AA00FF")
     * @return hex string
     */
    public String getColourHex(String key, String defaultValue) {
        return plugin.getConfig().getString(key, defaultValue);
    }

    /**
     * Gets an integer value from config.
     * @param key the config path
     * @param defaultValue fallback value
     * @return integer
     */
    public int getInt(String key, int defaultValue) {
        return plugin.getConfig().getInt(key, defaultValue);
    }

    /**
     * Gets a boolean value from config.
     * @param key the config path
     * @param defaultValue fallback value
     * @return boolean
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return plugin.getConfig().getBoolean(key, defaultValue);
    }
}
