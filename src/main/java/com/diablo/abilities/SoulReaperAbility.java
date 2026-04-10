package com.diablosmp.abilities;

import com.diablosmp.DiabloSmpPlugin;
import com.diablosmp.managers.ParticleManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class SoulReaperAbility {
    
    private final DiabloSmpPlugin plugin;
    private final Map<UUID, SoulSwapData> activeSoulSwaps = new HashMap<>();
    private final Map<UUID, GravityLinkData> activeGravityLinks = new HashMap<>();
    private final Map<UUID, Integer> crouchCount = new HashMap<>();
    private final Set<UUID> soulStormReady = new HashSet<>();
    
    public SoulReaperAbility(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void activateStage1(Player player, Entity target) {
        if (!(target instanceof LivingEntity livingTarget)) return;
        
        UUID playerId = player.getUniqueId();
        
        // Check cooldown
        if (plugin.getCooldownManager().hasCooldown(playerId, "soul_reaper_1")) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(playerId, "soul_reaper_1");
            player.sendMessage(ChatColor.RED + "Soul Reaper Stage 1 on cooldown! " + 
                ChatColor.YELLOW + remaining + "s remaining");
            return;
        }
        
        // Start soul swap
        startSoulSwap(player, livingTarget);
        
        // Set cooldown (45 seconds)
        plugin.getCooldownManager().setCooldown(playerId, "soul_reaper_1", 45);
    }
    
    private void startSoulSwap(Player player, LivingEntity target) {
        UUID playerId = player.getUniqueId();
        UUID targetId = target.getUniqueId();
        
        // Save original states
        Location playerLoc = player.getLocation().clone();
        Location targetLoc = target.getLocation().clone();
        
        // Create statue for player's body
        ArmorStand statue = player.getWorld().spawn(playerLoc, ArmorStand.class);
        statue.setVisible(false);
        statue.setInvulnerable(true);
        statue.setGravity(false);
        statue.setMarker(true);
        statue.setCustomName(ChatColor.DARK_PURPLE + "⚡ " + player.getName() + "'s Body ⚡");
        statue.setCustomNameVisible(true);
        
        // Store player's inventory
        Inventory storedInventory = Bukkit.createInventory(null, 54);
        storedInventory.setContents(player.getInventory().getContents());
        
        // Clear player's inventory except hotbar
        for (int i = 9; i < 36; i++) {
            player.getInventory().setItem(i, createBarrierItem());
        }
        
        // Make player invisible and invulnerable
        player.setInvisible(true);
        player.setInvulnerable(true);
        
        // Store swap data
        SoulSwapData data = new SoulSwapData(player, target, statue, storedInventory, playerLoc, targetLoc);
        activeSoulSwaps.put(playerId, data);
        
        // Create chain particles between bodies
        startChainParticles(player, target, statue);
        
        // Send messages
        player.sendMessage(ChatColor.DARK_PURPLE + "✧ " + ChatColor.LIGHT_PURPLE + 
            "Soul Swap activated! You now control " + target.getName() + "'s body for 30 seconds!");
        player.sendMessage(ChatColor.GRAY + "Your body is protected and cannot be damaged.");
        
        // Schedule revert after 30 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                revertSoulSwap(playerId);
            }
        }.runTaskLater(plugin, 600L); // 30 seconds = 600 ticks
    }
    
    private void startChainParticles(Player player, LivingEntity target, ArmorStand statue) {
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                ticks++;
                UUID playerId = player.getUniqueId();
                
                if (!activeSoulSwaps.containsKey(playerId) || ticks > 600) {
                    this.cancel();
                    return;
                }
                
                Location loc1 = statue.getLocation().add(0, 1, 0);
                Location loc2 = target.getLocation().add(0, 1, 0);
                
                // Draw chain particles
                plugin.getParticleManager().drawChainLink(loc1, loc2, Particle.SOUL_FIRE_FLAME, Color.PURPLE);
                plugin.getParticleManager().drawChainLink(loc1, loc2, Particle.ENCHANTMENT_TABLE, Color.PURPLE);
                
                // Add crown effect on target
                if (ticks % 10 == 0) {
                    plugin.getParticleManager().createCrownEffect(target.getLocation().add(0, 2.5, 0), Color.PURPLE);
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
    
    public void activateStage2(Player player, Entity target) {
        if (!(target instanceof LivingEntity livingTarget)) return;
        
        UUID playerId = player.getUniqueId();
        
        // Check cooldown
        if (plugin.getCooldownManager().hasCooldown(playerId, "soul_reaper_2")) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(playerId, "soul_reaper_2");
            player.sendMessage(ChatColor.RED + "Soul Reaper Stage 2 on cooldown! " + 
                ChatColor.YELLOW + remaining + "s remaining");
            return;
        }
        
        // Check if already has active gravity link
        if (activeGravityLinks.containsKey(playerId)) {
            player.sendMessage(ChatColor.RED + "You already have an active Gravity Link!");
            return;
        }
        
        // Start gravity link
        startGravityLink(player, livingTarget);
        
        // Set cooldown (30 seconds)
        plugin.getCooldownManager().setCooldown(playerId, "soul_reaper_2", 30);
    }
    
    private void startGravityLink(Player player, LivingEntity target) {
        UUID playerId = player.getUniqueId();
        
        GravityLinkData data = new GravityLinkData(player, target);
        activeGravityLinks.put(playerId, data);
        
        player.sendMessage(ChatColor.DARK_PURPLE + "✧ " + ChatColor.LIGHT_PURPLE + 
            "Gravity Link activated! " + target.getName() + " is now attached to your cursor!");
        player.sendMessage(ChatColor.GRAY + "Left click again to launch the target!");
        
        // Start tracking cursor
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!activeGravityLinks.containsKey(playerId)) {
                    this.cancel();
                    return;
                }
                
                GravityLinkData linkData = activeGravityLinks.get(playerId);
                if (linkData.isLaunched()) {
                    this.cancel();
                    return;
                }
                
                Location cursorLoc = player.getTargetBlockExact(30).getLocation().add(0.5, 1, 0.5);
                linkData.getTarget().teleport(cursorLoc);
                
                // Spawn particles at target location
                player.getWorld().spawnParticle(Particle.PORTAL, 
                    linkData.getTarget().getLocation().add(0, 1, 0), 10, 0.3, 0.3, 0.3, 0.1);
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    public void launchGravityLinked(Player player) {
        UUID playerId = player.getUniqueId();
        GravityLinkData data = activeGravityLinks.get(playerId);
        
        if (data == null || data.isLaunched()) return;
        
        LivingEntity target = data.getTarget();
        Vector direction = player.getLocation().getDirection().multiply(2.5);
        
        target.setVelocity(direction);
        data.setLaunched(true);
        
        // Visual effects
        player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, target.getLocation(), 1);
        player.getWorld().playSound(target.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 1.5f);
        
        player.sendMessage(ChatColor.DARK_PURPLE + "✧ " + ChatColor.LIGHT_PURPLE + 
            target.getName() + " launched!");
        
        // Remove after launch
        new BukkitRunnable() {
            @Override
            public void run() {
                activeGravityLinks.remove(playerId);
            }
        }.runTaskLater(plugin, 40L);
    }
    
    public void activateStage3(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Check cooldown
        if (plugin.getCooldownManager().hasCooldown(playerId, "soul_reaper_3")) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(playerId, "soul_reaper_3");
            player.sendMessage(ChatColor.RED + "Soul Reaper Stage 3 on cooldown! " + 
                ChatColor.YELLOW + remaining + "s remaining");
            return;
        }
        
        // Find target in line of sight
        Entity target = getTargetEntity(player, 50);
        
        if (target instanceof LivingEntity livingTarget) {
            executeSoulStorm(player, livingTarget);
        } else {
            // If no target, execute at player's location
            executeSoulStorm(player, null);
        }
        
        // Set cooldown (60 seconds)
        plugin.getCooldownManager().setCooldown(playerId, "soul_reaper_3", 60);
    }
    
    private void executeSoulStorm(Player player, LivingEntity primaryTarget) {
        Location center = primaryTarget != null ? primaryTarget.getLocation() : player.getLocation();
        World world = player.getWorld();
        
        player.sendMessage(ChatColor.DARK_PURPLE + "✧ " + ChatColor.LIGHT_PURPLE + 
            "Soul Storm unleashed!");
        
        // Play sound
        world.playSound(center, Sound.ENTITY_WITHER_SPAWN, 2.0f, 1.0f);
        
        // Area damage effect
        for (Entity entity : world.getNearbyEntities(center, 80, 40, 80)) {
            if (entity instanceof LivingEntity livingEntity && entity != player) {
                // Apply damage
                livingEntity.damage(8.0, player);
                
                // Visual effect on each hit entity
                world.spawnParticle(Particle.SOUL, livingEntity.getLocation().add(0, 1, 0), 
                    20, 0.5, 0.5, 0.5, 0.1);
                world.spawnParticle(Particle.SOUL_FIRE_FLAME, livingEntity.getLocation().add(0, 1, 0), 
                    10, 0.5, 0.5, 0.5, 0.05);
            }
        }
        
        // If there's a primary target, swap positions
        if (primaryTarget != null) {
            Location playerLoc = player.getLocation().clone();
            Location targetLoc = primaryTarget.getLocation().clone();
            
            player.teleport(targetLoc);
            primaryTarget.teleport(playerLoc);
            
            // Swap effect
            world.spawnParticle(Particle.PORTAL, playerLoc, 50, 1, 1, 1, 0.5);
            world.spawnParticle(Particle.PORTAL, targetLoc, 50, 1, 1, 1, 0.5);
            world.playSound(playerLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.5f, 1.2f);
            world.playSound(targetLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.5f, 1.2f);
            
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Position swapped with " + primaryTarget.getName() + "!");
        }
        
        // Create soul particle storm
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                ticks++;
                if (ticks > 60) {
                    this.cancel();
                    return;
                }
                
                for (int i = 0; i < 3; i++) {
                    Location particleLoc = center.clone().add(
                        Math.random() * 160 - 80,
                        Math.random() * 40,
                        Math.random() * 160 - 80
                    );
                    world.spawnParticle(Particle.SOUL, particleLoc, 1, 0, 0, 0, 0);
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    public void handleCrouch(Player player) {
        UUID playerId = player.getUniqueId();
        int count = crouchCount.getOrDefault(playerId, 0) + 1;
        crouchCount.put(playerId, count);
        
        if (count >= 3) {
            // Ready Soul Storm
            soulStormReady.add(playerId);
            crouchCount.put(playerId, 0);
            
            player.sendMessage(ChatColor.DARK_PURPLE + "✧ " + ChatColor.LIGHT_PURPLE + 
                "Soul Storm ready! Left click to unleash!");
            
            // Visual effect
            plugin.getParticleManager().createCrownEffect(player.getLocation().add(0, 2.5, 0), Color.PURPLE);
            player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.0f, 1.5f);
        } else {
            player.sendMessage(ChatColor.GRAY + "Crouch " + (3 - count) + " more times for Soul Storm");
        }
        
        // Reset crouch count after 2 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                crouchCount.remove(playerId);
            }
        }.runTaskLater(plugin, 40L);
    }
    
    public boolean isSoulStormReady(Player player) {
        return soulStormReady.contains(player.getUniqueId());
    }
    
    public void clearSoulStormReady(Player player) {
        soulStormReady.remove(player.getUniqueId());
    }
    
    private void revertSoulSwap(UUID playerId) {
        SoulSwapData data = activeSoulSwaps.remove(playerId);
        if (data == null) return;
        
        Player player = data.getPlayer();
        LivingEntity target = data.getTarget();
        ArmorStand statue = data.getStatue();
        
        // Restore player
        player.setInvisible(false);
        player.setInvulnerable(false);
        
        // Restore inventory
        player.getInventory().setContents(data.getStoredInventory().getContents());
        
        // Remove statue
        statue.remove();
        
        // Teleport player back if needed
        if (player.getLocation().distance(data.getPlayerOriginalLocation()) > 10) {
            player.teleport(data.getPlayerOriginalLocation());
        }
        
        player.sendMessage(ChatColor.DARK_PURPLE + "✧ " + ChatColor.LIGHT_PURPLE + 
            "Soul Swap ended! You have returned to your body.");
        
        // Visual effect
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 50, 1, 1, 1, 0.5);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.5f, 0.8f);
    }
    
    private Entity getTargetEntity(Player player, int range) {
        return player.getTargetEntity(range);
    }
    
    private ItemStack createBarrierItem() {
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta meta = barrier.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "✖ Locked by Soul Swap ✖");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "This slot is locked during",
            ChatColor.GRAY + "Soul Swap ability"
        ));
        barrier.setItemMeta(meta);
        return barrier;
    }
    
    // Data classes
    private static class SoulSwapData {
        private final Player player;
        private final LivingEntity target;
        private final ArmorStand statue;
        private final Inventory storedInventory;
        private final Location playerOriginalLocation;
        private final Location targetOriginalLocation;
        
        public SoulSwapData(Player player, LivingEntity target, ArmorStand statue, 
                           Inventory storedInventory, Location playerLoc, Location targetLoc) {
            this.player = player;
            this.target = target;
            this.statue = statue;
            this.storedInventory = storedInventory;
            this.playerOriginalLocation = playerLoc;
            this.targetOriginalLocation = targetLoc;
        }
        
        public Player getPlayer() { return player; }
        public LivingEntity getTarget() { return target; }
        public ArmorStand getStatue() { return statue; }
        public Inventory getStoredInventory() { return storedInventory; }
        public Location getPlayerOriginalLocation() { return playerOriginalLocation; }
        public Location getTargetOriginalLocation() { return targetOriginalLocation; }
    }
    
    private static class GravityLinkData {
        private final Player player;
        private final LivingEntity target;
        private boolean launched = false;
        
        public GravityLinkData(Player player, LivingEntity target) {
            this.player = player;
            this.target = target;
        }
        
        public Player getPlayer() { return player; }
        public LivingEntity getTarget() { return target; }
        public boolean isLaunched() { return launched; }
        public void setLaunched(boolean launched) { this.launched = launched; }
    }
                  }
