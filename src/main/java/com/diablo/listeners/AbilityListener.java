package com.diablosmp.listeners;

import com.diablosmp.DiabloSmpPlugin;
import com.diablosmp.abilities.AbilityType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityListener implements Listener {
    
    private final DiabloSmpPlugin plugin;
    private final Map<UUID, Long> lastSneakTime = new HashMap<>();
    private final Map<UUID, Integer> sneakCount = new HashMap<>();
    
    public AbilityListener(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        
        // Check for left click with ability active
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            if (plugin.getAbilityManager().hasAbility(player, AbilityType.SOUL_REAPER)) {
                plugin.getAbilityManager().activateCurrentStage(player);
            }
        }
        
        // Check for right click with ability book
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = player.getInventory().getItemInMainHand();
            
            if (item != null && item.getType() == Material.ENCHANTED_BOOK) {
                if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                    String displayName = item.getItemMeta().getDisplayName();
                    
                    // Check which ability book
                    if (displayName.contains("Soul Reaper")) {
                        event.setCancelled(true);
                        absorbAbility(player, AbilityType.SOUL_REAPER);
                    }
                    // Add more ability checks here
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        
        if (!event.isSneaking()) return; // Only handle when player starts sneaking
        
        if (!plugin.getAbilityManager().hasAbility(player, AbilityType.SOUL_REAPER)) return;
        
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        Long lastSneak = lastSneakTime.get(playerId);
        
        if (lastSneak != null && currentTime - lastSneak < 1000) {
            // Double sneak detected
            int count = sneakCount.getOrDefault(playerId, 0) + 1;
            sneakCount.put(playerId, count);
            
            if (count >= 2) {
                // Switch ability stage
                plugin.getAbilityManager().switchStage(player);
                sneakCount.put(playerId, 0);
                
                // Visual effect
                player.getWorld().spawnParticle(org.bukkit.Particle.SPELL_WITCH, 
                    player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0);
                player.playSound(player.getLocation(), 
                    org.bukkit.Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.5f);
            }
            
            lastSneakTime.put(playerId, currentTime);
        } else {
            sneakCount.put(playerId, 1);
            lastSneakTime.put(playerId, currentTime);
        }
        
        // Reset sneak count after 1.5 seconds
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            sneakCount.remove(playerId);
        }, 30L);
    }
    
    private void absorbAbility(Player player, AbilityType type) {
        plugin.getAbilityManager().absorbAbility(player, type);
        plugin.getParticleManager().createAbsorptionEffect(player, type.getParticleColor());
        
        player.sendMessage(type.getChatColor() + "✧ " + type.getDisplayName() + 
            ChatColor.GREEN + " has been absorbed into your being! ✧");
    }
}
