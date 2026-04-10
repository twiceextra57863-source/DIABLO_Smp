package com.diablosmp.managers;

import com.diablosmp.DiabloSmpPlugin;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleManager {
    
    private final DiabloSmpPlugin plugin;
    
    public ParticleManager(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void drawChainLink(Location loc1, Location loc2, Particle particle, Color color) {
        World world = loc1.getWorld();
        if (world == null) return;
        
        Vector direction = loc2.toVector().subtract(loc1.toVector());
        double distance = direction.length();
        direction.normalize();
        
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1.0f);
        
        for (double i = 0; i < distance; i += 0.5) {
            Location point = loc1.clone().add(direction.clone().multiply(i));
            
            if (particle == Particle.DUST) {
                world.spawnParticle(particle, point, 1, 0, 0, 0, 0, dustOptions);
            } else {
                world.spawnParticle(particle, point, 1, 0.1, 0.1, 0.1, 0);
            }
        }
    }
    
    public void createCrownEffect(Location location, Color color) {
        World world = location.getWorld();
        if (world == null) return;
        
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1.5f);
        
        for (int i = 0; i < 360; i += 30) {
            double angle = Math.toRadians(i);
            double x = Math.cos(angle) * 0.8;
            double z = Math.sin(angle) * 0.8;
            
            Location crownPoint = location.clone().add(x, 0.5, z);
            world.spawnParticle(Particle.DUST, crownPoint, 3, 0.1, 0.1, 0.1, 0, dustOptions);
            world.spawnParticle(Particle.END_ROD, crownPoint, 1, 0, 0, 0, 0.01);
        }
        
        // Fixed: Use correct Particle name
        world.spawnParticle(Particle.ENCHANT, location, 15, 0.5, 0.5, 0.5, 0.1);
    }
    
    public void createAbsorptionEffect(Player player, Color color) {
        Location location = player.getLocation();
        World world = location.getWorld();
        if (world == null) return;
        
        new org.bukkit.scheduler.BukkitRunnable() {
            double y = 0;
            int ticks = 0;
            
            @Override
            public void run() {
                ticks++;
                if (ticks > 40) {
                    this.cancel();
                    createCrownEffect(player.getEyeLocation(), color);
                    return;
                }
                
                y += 0.1;
                double radius = 1.5;
                
                for (int i = 0; i < 8; i++) {
                    double angle = (ticks * 0.3) + (i * Math.PI / 4);
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;
                    
                    Location particleLoc = location.clone().add(x, y, z);
                    
                    Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1.2f);
                    world.spawnParticle(Particle.DUST, particleLoc, 1, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.DRAGON_BREATH, particleLoc, 1, 0.1, 0.1, 0.1, 0.01);
                }
                
                world.playSound(location, org.bukkit.Sound.ENTITY_ENDER_DRAGON_FLAP, 0.5f, 1.5f);
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    public void createSmokeWarning(Player player) {
        Location location = player.getLocation();
        World world = location.getWorld();
        if (world == null) return;
        
        world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, 30, 0.5, 0.5, 0.5, 0.05);
        world.spawnParticle(Particle.SMOKE, location, 50, 0.8, 0.8, 0.8, 0.02);
        
        world.playSound(location, org.bukkit.Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1.0f, 0.5f);
        world.playSound(location, org.bukkit.Sound.BLOCK_FIRE_EXTINGUISH, 0.8f, 1.0f);
    }
}
