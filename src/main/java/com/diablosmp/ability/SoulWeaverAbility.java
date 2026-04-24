package com.diablosmp.ability;

import com.diablosmp.DiabloSmpPlugin;
import com.diablosmp.utils.ParticleUtils;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoulWeaverAbility implements Ability {
    private final DiabloSmpPlugin plugin;
    // Active possessions: key = controller, value = target uuid
    private final Map<UUID, UUID> activePossessions = new HashMap<>();
    // Statue entities (original bodies)
    private final Map<UUID, UUID> statueMap = new HashMap<>();

    public SoulWeaverAbility(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "soulweaver"; }
    @Override
    public String getDisplayName() { return "§5Soul Weaver"; }
    @Override
    public int getMaxStage() { return 3; }

    @Override
    public void onLeftClick(Player player, int stage) {
        switch (stage) {
            case 1 -> activateSoulExchange(player);
            case 2 -> activateSoulLink(player);
            case 3 -> activateSoulDrain(player);
        }
    }

    @Override
    public void onStageSwitch(Player player, int oldStage, int newStage) {
        player.sendMessage(plugin.getConfigUtils().getMessage("stage-switched")
                .replace("%ability%", getDisplayName())
                .replace("%stage%", String.valueOf(newStage)));
    }

    @Override
    public int getCooldownSeconds(int stage) {
        return switch (stage) {
            case 1 -> 45;
            case 2 -> 30;
            case 3 -> 20;
            default -> 0;
        };
    }

    @Override
    public String getParticleColorHex(int stage) {
        return switch (stage) {
            case 1 -> "#AA00FF";
            case 2 -> "#00AAFF";
            case 3 -> "#FF5500";
            default -> "#FFFFFF";
        };
    }

    // ---------- Stage 1: Soul Exchange ----------
    private void activateSoulExchange(Player controller) {
        LivingEntity target = getTargetEntity(controller, 5);
        if (target == null) {
            controller.sendMessage(plugin.getConfigUtils().getMessage("invalid-ability-book")); // or custom
            return;
        }
        if (activePossessions.containsKey(controller.getUniqueId())) {
            controller.sendMessage("§cYou are already possessing someone!");
            return;
        }

        // Store original body as statue (invincible, no movement)
        if (target instanceof Player) {
            Player originalBody = (Player) target;
            originalBody.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 600, 255, false, false, true));
            originalBody.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 600, 255, false, false, true));
            originalBody.setInvulnerable(true);
            originalBody.setAI(false);
            statueMap.put(originalBody.getUniqueId(), controller.getUniqueId());
        } else {
            target.setAI(false);
            target.setInvulnerable(true);
            statueMap.put(target.getUniqueId(), controller.getUniqueId());
        }

        // Controller becomes the soul (invisible, can fly)
        controller.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0, false, false, true));
        controller.setAllowFlight(true);
        controller.setFlying(true);
        lockInventoryExceptHotbar(controller);

        activePossessions.put(controller.getUniqueId(), target.getUniqueId());

        // Create particle line between controller and target (runs every 5 ticks for 30 sec)
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (!activePossessions.containsKey(controller.getUniqueId()) ||
                        !controller.isOnline() || target.isDead() || !target.isValid()) {
                    endSoulExchange(controller, target);
                    cancel();
                    return;
                }
                Location loc1 = controller.getEyeLocation();
                Location loc2 = target.getEyeLocation();
                ParticleUtils.drawLine(loc1, loc2, "SOUL_FIRE_FLAME", 0.1);
                ticks++;
                if (ticks >= 120) { // 30 sec (20 ticks/sec * 30 = 600 ticks; every 5 ticks => 120 runs)
                    endSoulExchange(controller, target);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);

        controller.sendMessage(plugin.getConfigUtils().getMessage("soul-exchange-start")
                .replace("%target%", target.getName()));
        if (target instanceof Player) {
            target.sendMessage(plugin.getConfigUtils().getMessage("soul-exchange-body-lost"));
        }
    }

    private void endSoulExchange(Player controller, LivingEntity target) {
        // Restore controller
        controller.removePotionEffect(PotionEffectType.INVISIBILITY);
        controller.setAllowFlight(false);
        controller.setFlying(false);
        unlockInventory(controller);

        // Restore original body
        if (target instanceof Player) {
            Player body = (Player) target;
            body.removePotionEffect(PotionEffectType.SLOWNESS);
            body.removePotionEffect(PotionEffectType.WEAKNESS);
            body.setInvulnerable(false);
            body.setAI(true);
        } else {
            target.setAI(true);
            target.setInvulnerable(false);
        }

        activePossessions.remove(controller.getUniqueId());
        statueMap.remove(target.getUniqueId());

        controller.sendMessage(plugin.getConfigUtils().getMessage("soul-exchange-end"));
    }

    // ---------- Stage 2: Soul Link (placeholder) ----------
    private void activateSoulLink(Player player) {
        player.sendMessage("§bSoul Link activated! (placeholder)");
        // Implementation can be added later
    }

    // ---------- Stage 3: Soul Drain (placeholder) ----------
    private void activateSoulDrain(Player player) {
        player.sendMessage("§6Soul Drain activated! (placeholder)");
    }

    private LivingEntity getTargetEntity(Player player, double maxDistance) {
        return player.getTargetEntity((int) Math.floor(maxDistance)) instanceof LivingEntity le ? le : null;
    }

    private void lockInventoryExceptHotbar(Player player) {
        for (int i = 9; i < 36; i++) {
            ItemStack barrier = new ItemStack(Material.BARRIER);
            player.getInventory().setItem(i, barrier);
        }
    }

    private void unlockInventory(Player player) {
        for (int i = 9; i < 36; i++) {
            player.getInventory().setItem(i, null);
        }
    }
}
