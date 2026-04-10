package com.diablosmp.listeners;

import com.diablosmp.DiabloSmpPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookListener implements Listener {
    
    private final DiabloSmpPlugin plugin;
    private final Map<UUID, Long> lastDropWarning = new HashMap<>();
    
    public BookListener(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        
        if (isAbilityBook(item)) {
            UUID playerId = player.getUniqueId();
            Long lastWarning = lastDropWarning.get(playerId);
            long currentTime = System.currentTimeMillis();
            
            // Check if it's been more than 1 minute since last warning
            if (lastWarning == null || currentTime - lastWarning > 60000) {
                event.setCancelled(true);
                
                // Create smoke effect
                plugin.getParticleManager().createSmokeWarning(player);
                
                // Send warning message
                player.sendMessage(ChatColor.RED + "⚠ " + ChatColor.YELLOW + 
                    "Ability books cannot be dropped!");
                player.sendMessage(ChatColor.GRAY + "Use " + ChatColor.GREEN + 
                    "/trust <player>" + ChatColor.GRAY + " to share abilities");
                
                lastDropWarning.put(playerId, currentTime);
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot drop ability books!");
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();
        
        // Prevent storing ability books in containers
        if (event.getInventory().getType() != InventoryType.PLAYER) {
            if (isAbilityBook(current) || isAbilityBook(cursor)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Ability books cannot be stored in containers!");
            }
        }
        
        // Prevent shift-clicking into containers
        if (event.isShiftClick() && isAbilityBook(event.getCurrentItem())) {
            if (event.getInventory().getType() != InventoryType.PLAYER) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Ability books cannot be stored in containers!");
            }
        }
    }
    
    private boolean isAbilityBook(ItemStack item) {
        if (item == null || item.getType() != Material.ENCHANTED_BOOK) return false;
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return false;
        
        String displayName = item.getItemMeta().getDisplayName();
        return displayName.contains("Ability Book") || displayName.contains("Soul Reaper");
    }
}
