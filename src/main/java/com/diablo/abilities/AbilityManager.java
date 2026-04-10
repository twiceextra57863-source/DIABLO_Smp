package com.diablosmp.abilities;

import com.diablosmp.DiabloSmpPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
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
        
        // Check if player already has this ability absorbed
        for (PlayerAbility ability : abilities) {
            if (ability.getType() == type && ability.isAbsorbed()) {
                player.sendMessage(ChatColor.RED + "You already possess this Abyssal Power!");
                return;
            }
        }
        
        // Check if player already has the book
        for (PlayerAbility ability : abilities) {
            if (ability.getType() == type && !ability.isAbsorbed()) {
                player.sendMessage(ChatColor.YELLOW + "You already have this Tome of Power!");
                return;
            }
        }
        
        PlayerAbility newAbility = new PlayerAbility(type);
        abilities.add(newAbility);
        
        // Create and give the ability book
        ItemStack abilityBook = createAbilityBook(type);
        HashMap<Integer, ItemStack> overflow = player.getInventory().addItem(abilityBook);
        
        if (!overflow.isEmpty()) {
            player.getWorld().dropItem(player.getLocation(), abilityBook);
            player.sendMessage(ChatColor.YELLOW + "Your inventory is full! The Tome has been dropped at your feet.");
        }
        
        player.sendMessage(type.getChatColor() + "⬡ " + ChatColor.GOLD + "You have received the " + 
            type.getDisplayName() + ChatColor.GOLD + " ⬡");
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
    }
    
    public ItemStack createAbilityBook(AbilityType type) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        
        // Set unique display name
        meta.setDisplayName(type.getChatColor() + "⬡ " + type.getDisplayName() + " " + type.getChatColor() + "⬡");
        
        // Create lore
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "◈ " + ChatColor.GOLD + "ᗩᑎᑕIEᑎT TOᗰE Oᖴ ᑭOᗯEᖇ" + ChatColor.DARK_GRAY + " ◈");
        lore.add("");
        lore.add(ChatColor.GRAY + "✧ " + ChatColor.LIGHT_PURPLE + "ᖇIGᕼT-ᑕᒪIᑕK" + ChatColor.GRAY + " to absorb the Abyssal Essence");
        lore.add(ChatColor.GRAY + "✧ " + ChatColor.LIGHT_PURPLE + "ᗷIᑎᗪᔕ" + ChatColor.GRAY + " to your very soul");
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "◈ " + ChatColor.DARK_PURPLE + "ᗩᗷYᔕᔕᗩᒪ ᑭOᗯEᖇᔕ" + ChatColor.DARK_GRAY + " ◈");
        lore.add("");
        
        // Add description lines
        for (String line : type.getDescription()) {
            lore.add(line);
        }
        
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "◈ " + ChatColor.RED + "ᗯᗩᖇᑎIᑎGᔕ" + ChatColor.DARK_GRAY + " ◈");
        lore.add(ChatColor.DARK_RED + "⚠ " + ChatColor.GRAY + "Cannot be thrown to the void");
        lore.add(ChatColor.DARK_RED + "⚠ " + ChatColor.GRAY + "Cannot be stored in vessels");
        lore.add(ChatColor.DARK_RED + "⚠ " + ChatColor.GRAY + "Use " + ChatColor.GREEN + "/soulbind <player>" + 
            ChatColor.GRAY + " to transfer");
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "◈ " + type.getChatColor() + "TᕼE ᗩᗷYᔕᔕ ᗯᗩTᑕᕼEᔕ" + ChatColor.DARK_GRAY + " ◈");
        
        meta.setLore(lore);
        
        // Add enchantment glint
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        
        // Custom model data for texture (optional)
        meta.setCustomModelData(1000 + type.ordinal());
        
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
        
        // Remove book from hand
        player.getInventory().setItemInMainHand(null);
        
        // Epic absorption message
        player.sendMessage("");
        player.sendMessage(type.getChatColor() + "⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡");
        player.sendMessage(type.getChatColor() + "  " + type.getDisplayName());
        player.sendMessage(ChatColor.LIGHT_PURPLE + "  ᕼᗩᔕ ᗷEEᑎ ᗩᗷᔕOᖇᗷEᗪ IᑎTO YOᑌᖇ ᔕOᑌᒪ!");
        player.sendMessage(type.getChatColor() + "⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡");
        player.sendMessage("");
        
        // Visual and sound effects
        plugin.getParticleManager().createAbsorptionEffect(player, type.getParticleColor());
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1.8f);
        player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1.0f, 1.5f);
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
    
    public void removeAbility(Player player, AbilityType type) {
        UUID playerId = player.getUniqueId();
        List<PlayerAbility> abilities = playerAbilities.get(playerId);
        
        if (abilities != null) {
            abilities.removeIf(ability -> ability.getType() == type);
        }
        
        // Remove any ability books of this type
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.ENCHANTED_BOOK && item.hasItemMeta()) {
                String displayName = item.getItemMeta().getDisplayName();
                if (displayName.contains(type.getSimpleName())) {
                    player.getInventory().remove(item);
                }
            }
        }
        
        player.sendMessage(type.getChatColor() + "⬡ " + ChatColor.RED + "The " + 
            type.getDisplayName() + ChatColor.RED + " has been cleansed from your soul! ⬡");
    }
    
    public void switchStage(Player player) {
        UUID playerId = player.getUniqueId();
        int stage = currentStage.getOrDefault(playerId, 1);
        
        stage = stage == 3 ? 1 : stage + 1;
        currentStage.put(playerId, stage);
        
        String stageName = stage == 1 ? "§dᔕOᑌᒪ ᔕᗯᗩᑭ" : stage == 2 ? "§dGᖇᗩᐯITY ᒪIᑎK" : "§dᔕOᑌᒪ ᔕTOᖇᗰ";
        player.sendMessage(ChatColor.DARK_PURPLE + "⬡ " + ChatColor.LIGHT_PURPLE + 
            "ᔕOᑌᒪ ᖇEᗩᑭEᖇ: " + ChatColor.WHITE + "ᔕTᗩGE " + convertToRoman(stage) + " - " + stageName);
        
        // Sound feedback
        player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_CHIME, 0.8f, 1.0f + (stage * 0.2f));
    }
    
    public void activateCurrentStage(Player player) {
        if (!hasAbility(player, AbilityType.SOUL_REAPER)) {
            player.sendMessage(ChatColor.RED + "You have not awakened any Abyssal Powers!");
            return;
        }
        
        int stage = currentStage.getOrDefault(player.getUniqueId(), 1);
        
        if (stage == 3 && !soulReaperAbility.isSoulStormReady(player)) {
            soulReaperAbility.handleCrouch(player);
            return;
        }
        
        Entity target = player.getTargetEntity(30);
        
        switch (stage) {
            case 1:
                if (target != null) {
                    soulReaperAbility.activateStage1(player, target);
                } else {
                    player.sendMessage(ChatColor.RED + "No target in sight! Gaze upon a living being!");
                }
                break;
            case 2:
                if (target != null) {
                    soulReaperAbility.activateStage2(player, target);
                } else {
                    player.sendMessage(ChatColor.RED + "No target to bind! Look at a living entity!");
                }
                break;
            case 3:
                soulReaperAbility.activateStage3(player);
                soulReaperAbility.clearSoulStormReady(player);
                break;
        }
    }
    
    public List<AbilityType> getPlayerAbilities(Player player) {
        UUID playerId = player.getUniqueId();
        List<PlayerAbility> abilities = playerAbilities.get(playerId);
        List<AbilityType> types = new ArrayList<>();
        
        if (abilities != null) {
            for (PlayerAbility ability : abilities) {
                if (ability.isAbsorbed()) {
                    types.add(ability.getType());
                }
            }
        }
        return types;
    }
    
    private String convertToRoman(int num) {
        String[] romans = {"I", "II", "III"};
        return romans[num - 1];
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
