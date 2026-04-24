package com.diablosmp.utils;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class ParticleUtils {

    public static void drawLine(Location from, Location to, String particleName, double step) {
        World w = from.getWorld();
        if (w == null) return;
        Vector direction = to.toVector().subtract(from.toVector());
        double length = direction.length();
        direction.normalize();
        Particle particle;
        try {
            particle = Particle.valueOf(particleName);
        } catch (IllegalArgumentException e) {
            particle = Particle.DRAGON_BREATH; // fallback
        }
        for (double i = 0; i < length; i += step) {
            Vector point = from.toVector().add(direction.clone().multiply(i));
            w.spawnParticle(particle, point.getX(), point.getY(), point.getZ(), 1, 0, 0, 0, 0);
        }
    }

    public static void spawnCrown(Location center, String hexColor) {
        World world = center.getWorld();
        if (world == null) return;
        // Convert hex to RGB
        java.awt.Color awtColor = java.awt.Color.decode(hexColor);
        Color color = Color.fromRGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
        Particle.DustOptions dust = new Particle.DustOptions(color, 1.5f);
        double radius = 0.6;
        for (double angle = 0; angle < 360; angle += 10) {
            double rad = Math.toRadians(angle);
            double x = center.getX() + radius * Math.cos(rad);
            double z = center.getZ() + radius * Math.sin(rad);
            double y = center.getY() + 0.3;
            world.spawnParticle(Particle.DUST, x, y, z, 1, 0, 0, 0, 0, dust);
            y = center.getY() + 0.7;
            world.spawnParticle(Particle.DUST, x, y, z, 1, 0, 0, 0, 0, dust);
        }
    }
}
