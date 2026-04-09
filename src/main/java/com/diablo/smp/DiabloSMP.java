package com.diablo.smp;

import com.diablo.smp.commands.AdminCommand;
import com.diablo.smp.commands.TrustCommand;
import com.diablo.smp.listeners.AbilityHandler;
import com.diablo.smp.listeners.SecurityHandler;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class DiabloSMP extends JavaPlugin {
    
    // Player UUID -> Current Stage (1, 2, 3)
    public final Map<UUID, Integer> playerStage = new HashMap<>();
    // Player UUID -> AbilityName_Stage -> Cooldown End Time
    public final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();
    // Trust Contract: FromPlayer -> ToPlayer (Ends in 5 mins)
    public final Map<UUID, UUID> trustContract = new HashMap<>();
    public final Map<UUID, Long> trustExpiry = new HashMap<>();
    
    public final Set<UUID> isStatue = new HashSet<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new AbilityHandler(this), this);
        getServer().getPluginManager().registerEvents(new SecurityHandler(this), this);
        
        getCommand("diablo").setExecutor(new AdminCommand(this));
        getCommand("trust").setExecutor(new TrustCommand(this));
        
        getLogger().info("§4[DiabloSMP] §fPlugin Fully Loaded! 🗡️");
    }
}
