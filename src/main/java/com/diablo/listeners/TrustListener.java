package com.diablosmp.listeners;

import com.diablosmp.DiabloSmpPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class TrustListener implements Listener {
    
    private final DiabloSmpPlugin plugin;
    
    public TrustListener(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        
        if (killer != null) {
            plugin.getTrustManager().handleTrustKill(killer, victim);
        }
    }
    
    @EventHandler
    public void onItemDropDuringTrust(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        
        if (isAbilityBook(item)) {
            // Check if dropping to trusted player
            Player nearby = getNearbyPlayer(player, 3);
            
            if (nearby != null && plugin.getTrustManager().isTrusted(player, nearby)) {
                // Allow drop to trusted player
                event.setCancelled(false);
                
                player.sendMessage(ChatColor.GREEN + "Ability book shared with " + 
                    ChatColor.YELLOW + nearby.getName() + "!");
                nearby.sendMessage(ChatColor.GREEN + "You received an ability book from " + 
                    ChatColor.YELLOW + player.getName() + "!");
                
                // Cancel trust after successful trade
                plugin.getTrustManager().cancelTrust(player);
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can only share ability books with trusted players!");
            }
        }
    }
    
    private boolean isAbilityBook(ItemStack item) {
        if (item == null || item.getType() != Material.ENCHANTED_BOOK) return false;
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return false;
        
        String displayName = item.getItemMeta().getDisplayName();
        return displayName.contains("Ability Book") || displayName.contains("Soul Reaper");
    }
    
    private Player getNearbyPlayer(Player player, double radius) {
        for (org.bukkit.entity.Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof Player nearby) {
                return nearby;
            }
        }
        return null;
    }
}
