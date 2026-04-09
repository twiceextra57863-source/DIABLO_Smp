package com.diablo.smp.listeners;

import com.diablo.smp.DiabloSMP;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class AbilityHandler implements Listener {
    private final DiabloSMP plugin;
    private final Map<UUID, Long> lastSneak = new HashMap<>();

    public AbilityHandler(DiabloSMP plugin) { this.plugin = plugin; }

    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) return;
        Player p = e.getPlayer();
        long now = System.currentTimeMillis();

        if (now - lastSneak.getOrDefault(p.getUniqueId(), 0L) < 500) {
            int stage = plugin.playerStage.getOrDefault(p.getUniqueId(), 1);
            stage = (stage % 3) + 1;
            plugin.playerStage.put(p.getUniqueId(), stage);
            p.sendActionBar(Component.text("§6§lMODE: §eStage " + stage));
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1.5f);
        }
        lastSneak.put(p.getUniqueId(), now);
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent e) {
        if (!e.getAction().name().contains("LEFT")) return;
        Player p = e.getPlayer();
        int stage = plugin.playerStage.getOrDefault(p.getUniqueId(), 1);

        if (stage == 3) {
            executeStageThree(p);
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (!(e.getRightClicked() instanceof LivingEntity target)) return;
        int stage = plugin.playerStage.getOrDefault(p.getUniqueId(), 1);

        if (stage == 1) executeStageOne(p, target);
        if (stage == 2) executeStageTwo(p, target);
    }

    // --- STAGE 1: SOUL POSSESSION ---
    private void executeStageOne(Player user, LivingEntity target) {
        Location statueLoc = user.getLocation();
        plugin.isStatue.add(user.getUniqueId());
        user.setGameMode(GameMode.SPECTATOR);
        user.setSpectatorTarget(target);

        // Inventory Lock Logic
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemStack[] originalInv = user.getInventory().getContents();
        for (int i = 9; i < 36; i++) user.getInventory().setItem(i, barrier);

        new BukkitRunnable() {
            int timer = 30;
            public void run() {
                if (timer <= 0 || !user.isOnline()) {
                    user.setGameMode(GameMode.SURVIVAL);
                    user.teleport(statueLoc);
                    user.getInventory().setContents(originalInv);
                    plugin.isStatue.remove(user.getUniqueId());
                    this.cancel();
                    return;
                }
                // Soul Chain Particle
                drawChain(statueLoc, target.getLocation());
                timer--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    // --- STAGE 2: CURSOR CANNON ---
    private void executeStageTwo(Player p, LivingEntity target) {
        new BukkitRunnable() {
            int ticks = 0;
            public void run() {
                if (ticks > 60 || !p.isSneaking()) {
                    target.setVelocity(p.getLocation().getDirection().multiply(3));
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
                    this.cancel();
                    return;
                }
                Vector targetPos = p.getLocation().add(p.getLocation().getDirection().multiply(3)).toVector();
                target.setVelocity(targetPos.subtract(target.getLocation().toVector()).multiply(0.5));
                ticks++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    // --- STAGE 3: RADIUS SWAP ---
    private void executeStageThree(Player p) {
        p.getNearbyEntities(80, 80, 80).stream()
            .filter(e -> e instanceof LivingEntity && e != p)
            .findFirst().ifPresent(entity -> {
                Location pLoc = p.getLocation();
                Location eLoc = entity.getLocation();
                p.teleport(eLoc);
                entity.teleport(pLoc);
                p.getWorld().spawnParticle(Particle.SOUL, pLoc, 100);
                p.getWorld().spawnParticle(Particle.SOUL, eLoc, 100);
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0.5f);
            });
    }

    private void drawChain(Location loc1, Location loc2) {
        double distance = loc1.distance(loc2);
        Vector vector = loc2.toVector().subtract(loc1.toVector()).normalize().multiply(0.5);
        for (double i = 0; i < distance; i += 0.5) {
            loc1.add(vector);
            loc1.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc1, 1, 0, 0, 0, 0);
        }
    }
}
