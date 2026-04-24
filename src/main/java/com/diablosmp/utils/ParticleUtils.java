package com.diablosmp.utils;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class ParticleUtils {

    // Draw a line between two locations with given particle
    public static void drawLine(Location from, Location to, String particleName, double step) {
        World w = from.getWorld();
        if (w == null) return;
        Vector direction = to.toVector().subtract(from.toVector());
        double length = direction.length();
        direction.normalize();
        for (double i = 0; i < length; i += step) {
            Vector point = from.toVector().add(direction.clone().multiply(i));
            w.spawnParticle(Particle.valueOf(particleName), point.getX(), point.getY(), point.getZ(), 1, 0, 0, 0, 0);
        }
    }

    // Spawn a dragon-like crown of particles (circle)
    public static void spawnCrown(Location center, String hexColor) {
        World world = center.getWorld();
        if (world == null) return;
        // Convert hex to RGB
        Color color = Color.fromRGB(Integer.parseInt(hexColor.substring(1), 16));
        Particle.DustOptions dust = new Particle.DustOptions(color, 1.5f);
        double radius = 0.6;
        for (double angle = 0; angle < 360; angle += 10) {
            double rad = Math.toRadians(angle);
            double x = center.getX() + radius * Math.cos(rad);
            double z = center.getZ() + radius * Math.sin(rad);
            double y = center.getY() + 0.3; // slightly above head
            world.spawnParticle(Particle.DUST, x, y, z, 1, 0, 0, 0, 0, dust);
            // also a higher arc
            y = center.getY() + 0.7;
            world.spawnParticle(Particle.DUST, x, y, z, 1, 0, 0, 0, 0, dust);
        }
    }
}
