package com.diablo.smp;

import com.diablo.smp.commands.DiabloCommand;
import com.diablo.smp.commands.TrustCommand;
import com.diablo.smp.listeners.AbilityHandler;
import com.diablo.smp.listeners.SecurityHandler;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class DiabloSMP extends JavaPlugin {
    
    public final Map<UUID, Integer> playerStage = new HashMap<>();
    public final Map<UUID, Long> lastSneak = new HashMap<>();
    public final Map<UUID, UUID> trustContract = new HashMap<>(); // From -> To
    public final Map<UUID, Long> trustExpiry = new HashMap<>();
    public final Set<UUID> isStatue = new HashSet<>();

    @Override
    public void onEnable() {
        // Register Commands
        getCommand("diablo").setExecutor(new DiabloCommand(this));
        getCommand("trust").setExecutor(new TrustCommand(this));
        
        // Register Listeners
        getServer().getPluginManager().registerEvents(new AbilityHandler(this), this);
        getServer().getPluginManager().registerEvents(new SecurityHandler(this), this);
        
        getLogger().info("§6Diablo SMP Fixed and Loaded! 🗡️");
    }

    // Is method ki wajah se TrustCommand fail ho raha tha
    public boolean isTrusted(UUID owner, UUID target) {
        if (!trustContract.containsKey(owner)) return false;
        if (System.currentTimeMillis() > trustExpiry.getOrDefault(owner, 0L)) {
            trustContract.remove(owner);
            return false;
        }
        return trustContract.get(owner).equals(target);
    }
}
