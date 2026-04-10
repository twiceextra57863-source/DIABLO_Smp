package com.diablosmp;

import com.diablosmp.abilities.AbilityManager;
import com.diablosmp.commands.AbilityCommand;
import com.diablosmp.commands.TrustCommand;
import com.diablosmp.listeners.AbilityListener;
import com.diablosmp.listeners.BookListener;
import com.diablosmp.listeners.TrustListener;
import com.diablosmp.managers.CooldownManager;
import com.diablosmp.managers.TrustManager;
import com.diablosmp.managers.ParticleManager;
import com.diablosmp.utils.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DiabloSmpPlugin extends JavaPlugin {
    
    private static DiabloSmpPlugin instance;
    private AbilityManager abilityManager;
    private CooldownManager cooldownManager;
    private TrustManager trustManager;
    private ParticleManager particleManager;
    private ConfigManager configManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.cooldownManager = new CooldownManager();
        this.trustManager = new TrustManager(this);
        this.particleManager = new ParticleManager(this);
        this.abilityManager = new AbilityManager(this);
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new BookListener(this), this);
        getServer().getPluginManager().registerEvents(new TrustListener(this), this);
        
        // Register commands
        getCommand("diablo").setExecutor(new AbilityCommand(this));
        getCommand("ability").setExecutor(new AbilityCommand(this));
        getCommand("trust").setExecutor(new TrustCommand(this));
        
        // Register tab completers
        getCommand("diablo").setTabCompleter(new AbilityCommand(this));
        getCommand("ability").setTabCompleter(new AbilityCommand(this));
        getCommand("trust").setTabCompleter(new TrustCommand(this));
        
        getLogger().info("§6⚡ §eDiablo SMP Plugin §aactivated successfully!");
        getLogger().info("§6⚡ §eReady to unleash epic abilities!");
    }
    
    @Override
    public void onDisable() {
        // Save all data
        if (abilityManager != null) {
            abilityManager.saveAllData();
        }
        getLogger().info("§cDiablo SMP Plugin disabled!");
    }
    
    public static DiabloSmpPlugin getInstance() {
        return instance;
    }
    
    public AbilityManager getAbilityManager() {
        return abilityManager;
    }
    
    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
    
    public TrustManager getTrustManager() {
        return trustManager;
    }
    
    public ParticleManager getParticleManager() {
        return particleManager;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
