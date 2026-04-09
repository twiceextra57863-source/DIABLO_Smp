package com.diablo.smp.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtils {

    public static ItemStack getAbilityBook() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        
        // Detailed Name
        meta.displayName(Component.text("🗡️ DIABLO SOUL BOOK 🗡️")
                .color(NamedTextColor.GOLD)
                .decoration(TextDecoration.BOLD, true));
        
        // Detailed Lore
        meta.lore(List.of(
            Component.text("Right-click to absorb this soul!").color(NamedTextColor.GRAY),
            Component.text("Ability: Soul Sovereign").color(NamedTextColor.DARK_PURPLE)
        ));
        
        book.setItemMeta(meta);
        return book;
    }

    public static boolean isAbilityBook(ItemStack item) {
        if (item == null || item.getType() != Material.ENCHANTED_BOOK) return false;
        if (!item.hasItemMeta()) return false;
        return item.getItemMeta().getDisplayName().contains("DIABLO");
    }
}
