package com.diablosmp.utils;

import com.diablosmp.DiabloSmpPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    
    private final DiabloSmpPlugin plugin;
    private FileConfiguration config;
    
    public ConfigManager(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        setupConfig();
    }
    
    private void setupConfig() {
        config.options().header("Diablo SMP Plugin Configuration\n" +
            "Configure all aspects of the plugin here");
        
        // Default config values
        config.addDefault("settings.prefix", "&5✧ &dDiablo SMP &5✧ &7»");
        config.addDefault("settings.language", "en");
        config.addDefault("settings.debug", false);
        
        config.addDefault("abilities.soul_reaper.cooldown.stage1", 45);
        config.addDefault("abilities.soul_reaper.cooldown.stage2", 30);
        config.addDefault("abilities.soul_reaper.cooldown.stage3", 60);
        config.addDefault("abilities.soul_reaper.damage.stage3", 8.0);
        config.addDefault("abilities.soul_reaper.duration.stage1", 30);
        
        config.addDefault("trust.duration", 300); // 5 minutes in seconds
        config.addDefault("trust.warning_time", 60); // Warning 1 minute before expiry
        
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
    
    public String getPrefix() {
        return config.getString("settings.prefix", "&5✧ &dDiablo SMP &5✧ &7»")
            .replace("&", "§");
    }
    
    public int getCooldown(String ability, int stage) {
        return config.getInt("abilities." + ability + ".cooldown.stage" + stage);
    }
    
    public double getDamage(String ability, int stage) {
        return config.getDouble("abilities." + ability + ".damage.stage" + stage);
    }
    
    public int getDuration(String ability, int stage) {
        return config.getInt("abilities." + ability + ".duration.stage" + stage);
    }
    
    public int getTrustDuration() {
        return config.getInt("trust.duration", 300);
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }
}
