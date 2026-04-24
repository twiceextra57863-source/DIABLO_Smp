package com.diablosmp.gui;

import com.diablosmp.DiabloSmpPlugin;
import com.diablosmp.utils.ParticleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;

public class AbsorptionGUI implements Listener {
    private final DiabloSmpPlugin plugin;
    private final Player player;
    private final ItemStack book;
    private Inventory gui;
    private boolean absorbed = false;

    public AbsorptionGUI(DiabloSmpPlugin plugin, Player player, ItemStack book) {
        this.plugin = plugin;
        this.player = player;
        this.book = book;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void open() {
        gui = Bukkit.createInventory(null, 9, "§0§lAbsorb Ability");
        ItemStack emptySlot = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = emptySlot.getItemMeta();
        meta.setDisplayName("§fPlace your ability book here");
        emptySlot.setItemMeta(meta);
        gui.setItem(4, emptySlot);
        player.openInventory(gui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(gui)) return;
        event.setCancelled(true);
        if (event.getRawSlot() == 4) {
            ItemStack cursor = event.getCursor();
            if (cursor != null && cursor.isSimilar(book)) {
                event.setCursor(null);
                event.getInventory().setItem(4, cursor);
                player.closeInventory();
                absorbAbility();
            } else {
                player.sendMessage("§cYou must place the exact ability book!");
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().equals(gui) && !absorbed && event.getPlayer().equals(player)) {
            ItemStack left = gui.getItem(4);
            if (left != null && left.getType() != Material.AIR) {
                player.getInventory().addItem(left);
            }
            gui.clear();
            InventoryClickEvent.getHandlerList().unregister(this);
            InventoryCloseEvent.getHandlerList().unregister(this);
        }
    }

    private void absorbAbility() {
        absorbed = true;
        // Get ability name from book's PDC
        String abilityName = book.getItemMeta().getPersistentDataContainer()
                .get(new NamespacedKey(plugin, "ability"), PersistentDataType.STRING);
        if (abilityName != null) {
            plugin.getAbilityManager().giveAbility(player, abilityName);
            player.sendMessage("§aYou absorbed the §5" + abilityName + " §aability!");
            // Dragon particles forming crown (custom method)
            ParticleUtils.spawnCrown(player.getLocation().add(0, 1.5, 0),
                    plugin.getAbilityManager().getAbility(abilityName).getParticleColorHex(1));
        } else {
            player.sendMessage("§cInvalid ability book!");
        }
        player.getInventory().removeItem(book);
        gui.clear();
        player.closeInventory();
        // Unregister events
        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
    }
}
