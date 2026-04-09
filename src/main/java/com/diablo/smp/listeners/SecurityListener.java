package com.diablo.smp.listeners;

import com.diablo.smp.DiabloSMP;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SecurityListener implements Listener {

    private final DiabloSMP plugin;

    public SecurityListener(DiabloSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        // Check if victim trusts the attacker (friendly fire prevention)
        if (plugin.getTrustManager().isTrusted(victim.getUniqueId(), attacker.getUniqueId())) {
            event.setCancelled(true);
            attacker.sendMessage(ChatColor.RED + "You cannot hurt " + victim.getName() + " because they trust you!");
            return;
        }

        // Check if attacker trusts the victim (mutual safety)
        if (plugin.getTrustManager().isTrusted(attacker.getUniqueId(), victim.getUniqueId())) {
            event.setCancelled(true);
            attacker.sendMessage(ChatColor.RED + "You cannot hurt " + victim.getName() + " because you trust them!");
        }
    }
}
