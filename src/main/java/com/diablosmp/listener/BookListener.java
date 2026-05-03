package com.diablosmp.listener;

import com.diablosmp.DiabloSmpPlugin;
import com.diablosmp.gui.AbsorptionGUI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class BookListener implements Listener {
    private final DiabloSmpPlugin plugin;

    public BookListener(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClickBook(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.ENCHANTED_BOOK) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(
                new NamespacedKey(plugin, "ability"), PersistentDataType.STRING)) return;

        event.setCancelled(true);
        new AbsorptionGUI(plugin, event.getPlayer(), item).open();
    }

    @EventHandler
    public void onDropBook(PlayerDropItemEvent event) {
        // No restrictions – allow dropping
        // We still need to ensure the book is an ability book, but we don't cancel.
        ItemStack item = event.getItemDrop().getItemStack();
        if (item.getType() != Material.ENCHANTED_BOOK) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(
                new NamespacedKey(plugin, "ability"), PersistentDataType.STRING)) return;
        // Allow drop – do nothing, just let it happen.
    }
}
