package com.diablosmp.listener;

import com.diablosmp.DiabloSmpPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrustListener implements Listener {
    private final DiabloSmpPlugin plugin;
    // trustMap: holder -> (trusted -> expiry)
    private final Map<UUID, Map<UUID, Long>> trustMap = new HashMap<>();

    public TrustListener(DiabloSmpPlugin plugin) { 
        this.plugin = plugin; 
    }

    public void addTrust(Player holder, Player trusted, int durationSeconds) {
        Map<UUID, Long> trustedMap = trustMap.computeIfAbsent(holder.getUniqueId(), k -> new HashMap<>());
        trustedMap.put(trusted.getUniqueId(), System.currentTimeMillis() + durationSeconds * 1000L);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        if (victim.getKiller() == null) return;
        Player killer = victim.getKiller();
        Map<UUID, Long> trustedMap = trustMap.get(victim.getUniqueId());
        if (trustedMap == null) return;
        Long expiry = trustedMap.get(killer.getUniqueId());
        if (expiry != null && expiry > System.currentTimeMillis()) {
            // Drop the ability book (if victim has any in inventory)
            for (ItemStack item : victim.getInventory().getContents()) {
                if (item != null && item.getType().toString().contains("ENCHANTED_BOOK") &&
                        item.getItemMeta() != null &&
                        item.getItemMeta().getPersistentDataContainer().has(
                                new NamespacedKey(plugin, "ability"), PersistentDataType.STRING)) {
                    victim.getWorld().dropItem(victim.getLocation(), item);
                    victim.getInventory().remove(item);
                    event.getDrops().remove(item); // ensure drops only once
                    break;
                }
            }
            trustedMap.remove(killer.getUniqueId());
            victim.sendMessage(plugin.getConfigUtils().getMessage("trust-expired").replace("%player%", killer.getName()));
        }
    }
}
