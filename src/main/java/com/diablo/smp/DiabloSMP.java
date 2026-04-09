package com.diablo.smp;

import com.diablo.smp.commands.DiabloCommand;
import com.diablo.smp.commands.TrustCommand;
import com.diablo.smp.listeners.AbilityListener;
import com.diablo.smp.listeners.SecurityListener;
import com.diablo.smp.manager.AbilityManager;
import com.diablo.smp.manager.TrustManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiabloSMP extends JavaPlugin {

    private static DiabloSMP instance;
    private AbilityManager abilityManager;
    private TrustManager trustManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // Initialize Managers
        this.abilityManager = new AbilityManager(this);
        this.trustManager = new TrustManager(this);

        // Register Commands
        getCommand("diablo").setExecutor(new DiabloCommand(this));
        getCommand("trust").setExecutor(new TrustCommand(this));

        // Register Listeners
        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new SecurityListener(this), this);

        getLogger().info("DiabloSMP has been enabled!");
    }

    @Override
    public void onDisable() {
        if (trustManager != null) {
            trustManager.saveTrusts();
        }
        getLogger().info("DiabloSMP has been disabled!");
    }

    public static DiabloSMP getInstance() {
        return instance;
    }

    public AbilityManager getAbilityManager() {
        return abilityManager;
    }

    public TrustManager getTrustManager() {
        return trustManager;
    }
}
