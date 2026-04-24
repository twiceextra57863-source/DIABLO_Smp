package com.diablosmp.listener;

import com.diablosmp.DiabloSmpPlugin;
import com.diablosmp.gui.AbsorptionGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.Sound;
import org.bukkit.Particle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookListener implements Listener {
    private final DiabloSmpPlugin plugin;
    private final Map<UUID, Long> lastDropWarning = new HashMap<>();

    public BookListener(DiabloSmpPlugin plugin) { this.plugin = plugin; }

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
        ItemStack item = event.getItemDrop().getItemStack();
        if (item.getType() != Material.ENCHANTED_BOOK) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(
                new NamespacedKey(plugin, "ability"), PersistentDataType.STRING)) return;

        Player p = event.getPlayer();
        UUID uid = p.getUniqueId();
        long now = System.currentTimeMillis();
        if (!lastDropWarning.containsKey(uid) || (now - lastDropWarning.get(uid)) > 60000) {
            lastDropWarning.put(uid, now);
            p.getWorld().spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 30, 0.5, 1, 0.5);
            p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.2f);
            p.sendMessage("§c§l⚠ You cannot drop this ancient book! It will vanish if you try again.");
            event.setCancelled(true);
        } else {
            p.sendMessage("§cThe book is bound to you – it cannot be thrown away.");
            event.setCancelled(true);
        }
    }
}
