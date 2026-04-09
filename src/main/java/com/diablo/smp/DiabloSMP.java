package com.diablo.smp;

import com.diablo.smp.commands.*;
import com.diablo.smp.listeners.*;
import com.diablo.smp.manager.*;
import org.bukkit.plugin.java.JavaPlugin;

public class DiabloSMP extends JavaPlugin {
    private AbilityManager abilityManager;
    private TrustManager trustManager;

    @Override
    public void onEnable() {
        this.abilityManager = new AbilityManager();
        this.trustManager = new TrustManager();

        getServer().getPluginManager().registerEvents(new SecurityListener(this), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);

        getCommand("diablo").setExecutor(new DiabloCommand(this));
        getCommand("trust").setExecutor(new TrustCommand(this));
    }

    public AbilityManager getAbilityManager() { return abilityManager; }
    public TrustManager getTrustManager() { return trustManager; }
}
