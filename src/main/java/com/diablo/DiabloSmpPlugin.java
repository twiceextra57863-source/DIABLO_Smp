package com.diablosmp;

import com.diablosmp.ability.AbilityManager;
import com.diablosmp.commands.DiabloCommand;
import com.diablosmp.commands.TrustCommand;
import com.diablosmp.listener.AbilityListener;
import com.diablosmp.listener.BookListener;
import com.diablosmp.listener.TrustListener;
import com.diablosmp.utils.ConfigUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class DiabloSmpPlugin extends JavaPlugin {
    private static DiabloSmpPlugin instance;
    private AbilityManager abilityManager;
    private ConfigUtils configUtils;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        configUtils = new ConfigUtils(this);
        abilityManager = new AbilityManager(this);

        // Register commands
        getCommand("diablo").setExecutor(new DiabloCommand(this));
        getCommand("trust").setExecutor(new TrustCommand(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new BookListener(this), this);
        getServer().getPluginManager().registerEvents(new TrustListener(this), this);

        getLogger().info("DiabloSmpPlugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("DiabloSmpPlugin disabled.");
    }

    public static DiabloSmpPlugin getInstance() { return instance; }
    public AbilityManager getAbilityManager() { return abilityManager; }
    public ConfigUtils getConfigUtils() { return configUtils; }
}
