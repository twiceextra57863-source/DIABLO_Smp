package com.diablo.smp.listeners;

import com.diablo.smp.DiabloSMP;
import com.diablo.smp.utils.ItemUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class SecurityListener implements Listener {
    private final DiabloSMP plugin;
    public SecurityListener(DiabloSMP plugin) { this.plugin = plugin; }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (ItemUtils.isAbilityBook(e.getItemDrop().getItemStack())) {
            Player p = e.getPlayer();
            if (plugin.getTrustManager().isTrusted(p.getUniqueId())) return;

            e.setCancelled(true);
            p.getWorld().spawnParticle(Particle.SMOKE, p.getLocation().add(0, 1, 0), 20);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0);
        }
    }

    @EventHandler
    public void onAbsorb(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().name().contains("RIGHT") && ItemUtils.isAbilityBook(e.getItem())) {
            e.setCancelled(true);
            e.getItem().setAmount(e.getItem().getAmount() - 1);
            playAbsorbAnimation(p);
        }
    }

    private void playAbsorbAnimation(Player p) {
        new BukkitRunnable() {
            double y = 0;
            public void run() {
                y += 0.2;
                double x = Math.sin(y * 5) * 0.5;
                double z = Math.cos(y * 5) * 0.5;
                p.getWorld().spawnParticle(Particle.DRAGON_BREATH, p.getLocation().add(x, y, z), 1, 0, 0, 0, 0);
                if (y >= 2.2) {
                    for(double i=0; i<6.28; i+=0.5) 
                        p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation().add(Math.cos(i)*0.7, 2.3, Math.sin(i)*0.7), 1, 0,0,0,0);
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
