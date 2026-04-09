package com.diablo.smp.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ItemUtils {

    /**
     * Creates a custom ability item.
     *
     * @param plugin    The plugin instance
     * @param material  The material of the item
     * @param name      Display name
     * @param lore      Lore lines
     * @param abilityId Unique ID for the ability
     * @return Formatted ItemStack
     */
    public static ItemStack createAbilityItem(JavaPlugin plugin, Material material, String name, List<String> lore, String abilityId) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            
            NamespacedKey key = new NamespacedKey(plugin, "ability_id");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, abilityId);
            
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Retrieves the ability ID from an ItemStack.
     *
     * @param plugin The plugin instance
     * @param item   The item to check
     * @return Ability ID or null if not present
     */
    public static String getAbilityId(JavaPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "ability_id");
        
        if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            return meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        }
        return null;
    }
}
