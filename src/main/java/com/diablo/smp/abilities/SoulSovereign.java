package com.diablo.smp.abilities;

import com.diablo.smp.DiabloSMP;
import com.diablo.smp.manager.AbilityManager;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SoulSovereign {

    private static final String ABILITY_NAME = "soul_sovereign";
    private static final int COOLDOWN = 45; // seconds
    private static final double RADIUS = 7.0;

    public static void execute(DiabloSMP plugin, Player player) {
        AbilityManager manager = plugin.getAbilityManager();

        if (manager.isOnCooldown(player, ABILITY_NAME)) {
            player.sendMessage(ChatColor.RED + "Soul Sovereign is on cooldown for " 
                    + manager.getRemainingCooldown(player, ABILITY_NAME) + " seconds.");
            return;
        }

        // Ability Logic: Steal health from nearby entities
        int soulsStolen = 0;
        for (Entity entity : player.getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
            if (entity instanceof LivingEntity && entity != player) {
                LivingEntity target = (LivingEntity) entity;
                
                // Prevent damaging trusted players
                if (target instanceof Player) {
                    Player tPlayer = (Player) target;
                    if (plugin.getTrustManager().isTrusted(player.getUniqueId(), tPlayer.getUniqueId())) {
                        continue;
                    }
                }

                target.damage(4.0, player);
                target.getWorld().spawnParticle(Particle.SOUL, target.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0.05);
                soulsStolen++;
            }
        }

        if (soulsStolen > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, soulsStolen - 1));
            player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.5f);
            player.sendMessage(ChatColor.DARK_PURPLE + "You have harvested " + soulsStolen + " souls!");
            manager.setCooldown(player, ABILITY_NAME, COOLDOWN);
        } else {
            player.sendMessage(ChatColor.GRAY + "No souls nearby to harvest...");
        }
    }
}
