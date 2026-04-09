package com.diablo.smp.listeners;

import com.diablo.smp.DiabloSMP;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class SecurityHandler implements Listener {
    private final DiabloSMP plugin;
    public SecurityHandler(DiabloSMP plugin) { this.plugin = plugin; }

    @EventHandler
    public void onStatueDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p && plugin.isStatue.contains(p.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (e.getItemDrop().getItemStack().getType() == Material.ENCHANTED_BOOK) {
            if (plugin.trustContract.containsKey(p.getUniqueId())) return;
            
            e.setCancelled(true);
            p.sendMessage("§c§lDIABLO §8» §7Your soul is bound to you!");
            p.getWorld().spawnParticle(Particle.SMOKE, p.getLocation(), 20);
        }
    }

    @EventHandler
    public void onAbsorb(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().name().contains("RIGHT") && e.getItem() != null && e.getItem().getType() == Material.ENCHANTED_BOOK) {
            e.setCancelled(true);
            e.getItem().setAmount(e.getItem().getAmount() - 1);
            playDragonAnimation(p);
        }
    }

    private void playDragonAnimation(Player p) {
        new BukkitRunnable() {
            double angle = 0;
            double y = 0;
            public void run() {
                angle += 0.3;
                y += 0.1;
                double x = Math.cos(angle) * 0.8;
                double z = Math.sin(angle) * 0.8;
                p.getWorld().spawnParticle(Particle.DRAGON_BREATH, p.getLocation().add(x, y, z), 2, 0,0,0,0);
                
                if (y >= 2.2) {
                    // Create Crown
                    for (double i = 0; i < 6.28; i += 0.4) {
                        p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation().add(Math.cos(i)*0.6, 2.3, Math.sin(i)*0.6), 1,0,0,0,0);
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
