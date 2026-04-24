package com.diablosmp.ability;

import com.diablosmp.DiabloSmpPlugin;
import com.diablosmp.utils.ParticleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoulWeaverAbility implements Ability {
    private final DiabloSmpPlugin plugin;
    // Active possessions: key = possessed soul (target), value = controller
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
        player.sendMessage("§eSoul Weaver stage changed to §a" + newStage);
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
            case 1 -> "#AA00FF"; // purple
            case 2 -> "#00AAFF"; // blue
            case 3 -> "#FF5500"; // orange
            default -> "#FFFFFF";
        };
    }

    // ---------- Stage 1: Soul Exchange ----------
    private void activateSoulExchange(Player controller) {
        // Find target entity in front (max 5 blocks)
        LivingEntity target = getTargetEntity(controller, 5);
        if (target == null) {
            controller.sendMessage("§cNo entity in front!");
            return;
        }
        if (target instanceof Player && activePossessions.containsKey(target.getUniqueId())) {
            controller.sendMessage("§cThis player is already possessed!");
            return;
        }

        // Store original body as statue (invincible, no movement)
        Player originalBody = null;
        if (target instanceof Player) {
            originalBody = (Player) target;
            originalBody.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 600, 255, false, false, true));
            originalBody.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 600, 255, false, false, true));
            originalBody.setInvulnerable(true);
            originalBody.setAI(false);
            statueMap.put(originalBody.getUniqueId(), controller.getUniqueId());
        } else {
            // For mobs: freeze and invincible
            target.setAI(false);
            target.setInvulnerable(true);
            statueMap.put(target.getUniqueId(), controller.getUniqueId());
        }

        // Controller becomes the soul (invisible, can fly)
        controller.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0, false, false, true));
        controller.setAllowFlight(true);
        controller.setFlying(true);
        // Lock controller's inventory except hotbar (barrier slots)
        lockInventoryExceptHotbar(controller);

        // Start possession
        activePossessions.put(controller.getUniqueId(), target.getUniqueId());

        // Create particle line between controller and target (runs every 5 ticks for 30 sec)
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (!activePossessions.containsKey(controller.getUniqueId()) ||
                        !controller.isOnline() || target.isDead()) {
                    cancel();
                    return;
                }
                Location loc1 = controller.getEyeLocation();
                Location loc2 = target.getEyeLocation();
                ParticleUtils.drawLine(loc1, loc2, "SOUL_FIRE_FLAME", 0.1);
                ticks++;
                if (ticks >= 120) { // 30 sec = 600 ticks, but we run every 5 ticks -> 120 runs
                    endSoulExchange(controller, target);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);

        controller.sendMessage("§5You have possessed " + target.getName() + " for 30 seconds!");
        if (originalBody != null) originalBody.sendMessage("§cYour body has been taken over!");
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

        controller.sendMessage("§5Soul exchange ended.");
    }

    // ---------- Stage 2: Soul Link (placeholder) ----------
    private void activateSoulLink(Player player) {
        // For demo: link with nearest player, share 50% of damage
        player.sendMessage("§bSoul Link activated! (placeholder)");
        // implementation omitted for brevity, can be added later
    }

    // ---------- Stage 3: Soul Drain (placeholder) ----------
    private void activateSoulDrain(Player player) {
        player.sendMessage("§6Soul Drain activated! (placeholder)");
        // e.g., steal health from nearby entities
    }

    // Helper: get target entity
    private LivingEntity getTargetEntity(Player player, double maxDistance) {
        return player.getTargetEntity(maxDistance) instanceof LivingEntity le ? le : null;
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
