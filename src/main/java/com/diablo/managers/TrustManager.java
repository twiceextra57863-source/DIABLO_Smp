package com.diablosmp.managers;

import com.diablosmp.DiabloSmpPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrustManager {
    
    private final DiabloSmpPlugin plugin;
    private final Map<UUID, TrustData> activeTrusts = new HashMap<>();
    
    public TrustManager(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void createTrust(Player truster, Player trustee) {
        UUID trusterId = truster.getUniqueId();
        
        // Check if trust already exists
        if (activeTrusts.containsKey(trusterId)) {
            truster.sendMessage(ChatColor.RED + "You already have an active trust!");
            return;
        }
        
        // Create trust
        TrustData trustData = new TrustData(truster, trustee);
        activeTrusts.put(trusterId, trustData);
        
        // Send messages
        truster.sendMessage(ChatColor.GREEN + "You have trusted " + 
            ChatColor.YELLOW + trustee.getName() + ChatColor.GREEN + " for 5 minutes!");
        truster.sendMessage(ChatColor.GRAY + "They can now receive your ability books.");
        
        trustee.sendMessage(ChatColor.GREEN + truster.getName() + 
            ChatColor.YELLOW + " has trusted you for 5 minutes!");
        trustee.sendMessage(ChatColor.GRAY + "You can now receive their ability books.");
        
        // Schedule trust expiration
        new BukkitRunnable() {
            @Override
            public void run() {
                expireTrust(trusterId);
            }
        }.runTaskLater(plugin, 6000L); // 5 minutes = 6000 ticks
        
        // Warning at 1 minute remaining
        new BukkitRunnable() {
            @Override
            public void run() {
                if (activeTrusts.containsKey(trusterId)) {
                    truster.sendMessage(ChatColor.YELLOW + "⚠ Trust with " + 
                        trustee.getName() + " expires in 1 minute!");
                    trustee.sendMessage(ChatColor.YELLOW + "⚠ Trust from " + 
                        truster.getName() + " expires in 1 minute!");
                }
            }
        }.runTaskLater(plugin, 4800L); // 4 minutes = 4800 ticks
    }
    
    public boolean isTrusted(Player truster, Player trustee) {
        TrustData trustData = activeTrusts.get(truster.getUniqueId());
        return trustData != null && trustData.getTrustee().getUniqueId().equals(trustee.getUniqueId());
    }
    
    public void expireTrust(UUID trusterId) {
        TrustData trustData = activeTrusts.remove(trusterId);
        if (trustData != null) {
            Player truster = trustData.getTruster();
            Player trustee = trustData.getTrustee();
            
            if (truster.isOnline()) {
                truster.sendMessage(ChatColor.RED + "Trust with " + 
                    ChatColor.YELLOW + trustee.getName() + ChatColor.RED + " has expired!");
            }
            if (trustee.isOnline()) {
                trustee.sendMessage(ChatColor.RED + "Trust from " + 
                    ChatColor.YELLOW + truster.getName() + ChatColor.RED + " has expired!");
            }
        }
    }
    
    public void cancelTrust(Player truster) {
        expireTrust(truster.getUniqueId());
        truster.sendMessage(ChatColor.RED + "Trust cancelled!");
    }
    
    public void handleTrustKill(Player killer, Player victim) {
        // Check if victim was trusted by anyone
        for (Map.Entry<UUID, TrustData> entry : activeTrusts.entrySet()) {
            TrustData trustData = entry.getValue();
            if (trustData.getTrustee().getUniqueId().equals(victim.getUniqueId())) {
                // Drop ability books on death
                victim.getWorld().dropItem(victim.getLocation(), 
                    plugin.getAbilityManager().createAbilityBook(
                        com.diablosmp.abilities.AbilityType.SOUL_REAPER));
                
                killer.sendMessage(ChatColor.DARK_PURPLE + "✧ " + ChatColor.LIGHT_PURPLE + 
                    "You defeated a trusted player! Their ability book drops!");
                
                // Expire the trust
                expireTrust(entry.getKey());
            }
        }
    }
    
    private static class TrustData {
        private final Player truster;
        private final Player trustee;
        private final long startTime;
        
        public TrustData(Player truster, Player trustee) {
            this.truster = truster;
            this.trustee = trustee;
            this.startTime = System.currentTimeMillis();
        }
        
        public Player getTruster() { return truster; }
        public Player getTrustee() { return trustee; }
        public long getStartTime() { return startTime; }
    }
}
