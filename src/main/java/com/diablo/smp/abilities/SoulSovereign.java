package com.diablo.smp.abilities;

import com.diablo.smp.DiabloSMP;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SoulSovereign {

    public static void stageOne(Player p, Entity target, DiabloSMP plugin) {
        if (!(target instanceof LivingEntity living)) return;
        p.sendMessage("§d§lSoul Swapped!");
        Location statueLoc = p.getLocation();
        
        new BukkitRunnable() {
            int timer = 30;
            public void run() {
                if (timer <= 0) { 
                    p.teleport(statueLoc);
                    this.cancel(); 
                    return; 
                }
                // Chain Particle
                p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, p.getLocation(), 5);
                timer--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public static void stageTwo(Player p, LivingEntity target) {
        target.setVelocity(p.getLocation().getDirection().multiply(2).setY(1));
        p.getWorld().spawnParticle(Particle.EXPLOSION, target.getLocation(), 10);
    }

    public static void stageThree(Player p) {
        for (Entity e : p.getNearbyEntities(80, 80, 80)) {
            if (e instanceof LivingEntity le && e != p) {
                le.damage(6.0);
                Location pLoc = p.getLocation();
                p.teleport(le.getLocation());
                le.teleport(pLoc);
                p.getWorld().spawnParticle(Particle.REVERSE_PORTAL, p.getLocation(), 100);
                break;
            }
        }
    }
}
