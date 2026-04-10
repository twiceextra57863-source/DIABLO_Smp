package com.diablosmp.abilities;

import com.diablosmp.DiabloSmpPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class AbilityManager {
    
    private final DiabloSmpPlugin plugin;
    private final Map<UUID, List<PlayerAbility>> playerAbilities = new HashMap<>();
    private final Map<UUID, Integer> currentStage = new HashMap<>();
    private final SoulReaperAbility soulReaperAbility;
    
    public AbilityManager(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
        this.soulReaperAbility = new SoulReaperAbility(plugin);
    }
    
    public void giveAbility(Player player, AbilityType type) {
        UUID playerId = player.getUniqueId();
        List<PlayerAbility> abilities = playerAbilities.computeIfAbsent(playerId, k -> new ArrayList<>());
        
        for (PlayerAbility ability : abilities) {
            if (ability.getType() == type) {
                return;
            }
        }
        
        PlayerAbility newAbility = new PlayerAbility(type);
        abilities.add(newAbility);
        
        ItemStack abilityBook = createAbilityBook(type);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(abilityBook);
        } else {
            player.getWorld().dropItem(player.getLocation(), abilityBook);
        }
        
        player.sendMessage(ChatColor.GREEN + "You received the " + 
            type.getChatColor() + type.getDisplayName() + ChatColor.GREEN + " ability!");
    }
    
    public void removeAbility(Player player, AbilityType type) {
        UUID playerId = player.getUniqueId();
        List<PlayerAbility> abilities = playerAbilities.get(playerId);
        
        if (abilities != null) {
            abilities.removeIf(ability -> ability.getType() == type);
        }
        
        player.getInventory().remove(Material.ENCHANTED_BOOK);
        
        player.sendMessage(ChatColor.RED + "Your " + type.getDisplayName() + 
            ChatColor.RED + " ability has been removed!");
    }
    
    public ItemStack createAbilityBook(AbilityType type) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        
        meta.setDisplayName(type.getChatColor() + "✧ " + type.getDisplayName() + " Ability Book ✧");
        
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GOLD + "► Right-click to absorb");
        lore.add(ChatColor.GRAY + "Gain the power of " + type.getDisplayName());
        lore.add("");
        lore.addAll(Arrays.asList(type.getDescription()));
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "◆ Cannot be thrown or stored");
        lore.add(ChatColor.DARK_GRAY + "◆ Use /trust <player> to share");
        
        meta.setLore(lore);
        meta.setEnchantmentGlintOverride(true);
        
        book.setItemMeta(meta);
        return book;
    }
    
    public void absorbAbility(Player player, AbilityType type) {
        UUID playerId = player.getUniqueId();
        List<PlayerAbility> abilities = playerAbilities.get(playerId);
        
        if (abilities != null) {
            for (PlayerAbility ability : abilities) {
                if (ability.getType() == type) {
                    ability.setAbsorbed(true);
                    break;
                }
            }
        }
        
        player.getInventory().setItemInMainHand(null);
        
        player.sendMessage(type.getChatColor() + "✧ " + type.getDisplayName() + 
            ChatColor.GREEN + " absorbed into your soul! ✧");
    }
    
    public boolean hasAbility(Player player, AbilityType type) {
        UUID playerId = player.getUniqueId();
        List<PlayerAbility> abilities = playerAbilities.get(playerId);
        
        if (abilities != null) {
            for (PlayerAbility ability : abilities) {
                if (ability.getType() == type && ability.isAbsorbed()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void switchStage(Player player) {
        UUID playerId = player.getUniqueId();
        int stage = currentStage.getOrDefault(playerId, 1);
        
        stage = stage == 3 ? 1 : stage + 1;
        currentStage.put(playerId, stage);
        
        String stageName = stage == 1 ? "Soul Swap" : stage == 2 ? "Gravity Link" : "Soul Storm";
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Soul Reaper: " + 
            ChatColor.WHITE + "Stage " + stage + " - " + stageName);
    }
    
    public void activateCurrentStage(Player player) {
        if (!hasAbility(player, AbilityType.SOUL_REAPER)) return;
        
        int stage = currentStage.getOrDefault(player.getUniqueId(), 1);
        
        if (stage == 3 && !soulReaperAbility.isSoulStormReady(player)) {
            soulReaperAbility.handleCrouch(player);
            return;
        }
        
        // Fixed: Added Entity import and proper target acquisition
        Entity target = player.getTargetEntity(30);
        
        switch (stage) {
            case 1:
                if (target != null) {
                    soulReaperAbility.activateStage1(player, target);
                }
                break;
            case 2:
                if (target != null) {
                    soulReaperAbility.activateStage2(player, target);
                }
                break;
            case 3:
                soulReaperAbility.activateStage3(player);
                soulReaperAbility.clearSoulStormReady(player);
                break;
        }
    }
    
    public void saveAllData() {
        // Save player abilities to config
    }
    
    public SoulReaperAbility getSoulReaperAbility() {
        return soulReaperAbility;
    }
    
    private static class PlayerAbility {
        private final AbilityType type;
        private boolean absorbed = false;
        
        public PlayerAbility(AbilityType type) {
            this.type = type;
        }
        
        public AbilityType getType() { return type; }
        public boolean isAbsorbed() { return absorbed; }
        public void setAbsorbed(boolean absorbed) { this.absorbed = absorbed; }
    }
}
