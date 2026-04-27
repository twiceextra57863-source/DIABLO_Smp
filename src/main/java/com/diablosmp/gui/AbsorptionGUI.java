package com.diablosmp.gui;

import com.diablosmp.DiabloSmpPlugin;
import com.diablosmp.utils.ParticleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class AbsorptionGUI implements Listener {
    private final DiabloSmpPlugin plugin;
    private final Player player;
    private final ItemStack originalBook;
    private Inventory gui;
    private boolean absorbed = false;
    private String abilityName;
    private int stage;
    private boolean isConfirmGui = false;  // track which GUI is open

    private static final String PLACE_TITLE = "§0§lPlace Ability Book";
    private static final String CONFIRM_TITLE = "§0§lConfirm Absorption";

    public AbsorptionGUI(DiabloSmpPlugin plugin, Player player, ItemStack book) {
        this.plugin = plugin;
        this.player = player;
        this.originalBook = book.clone();
        var pdc = book.getItemMeta().getPersistentDataContainer();
        this.abilityName = pdc.get(new NamespacedKey(plugin, "ability"), PersistentDataType.STRING);
        this.stage = pdc.getOrDefault(new NamespacedKey(plugin, "stage"), PersistentDataType.INTEGER, 1);
    }

    public void open() {
        isConfirmGui = false;
        gui = Bukkit.createInventory(null, 9, PLACE_TITLE);
        ItemStack table = new ItemStack(Material.ENCHANTING_TABLE);
        ItemMeta meta = table.getItemMeta();
        meta.setDisplayName("§a§lPlace Ability Book Here");
        meta.setLore(Arrays.asList("§7Click this slot while holding", "§7your ability book to begin."));
        table.setItemMeta(meta);
        gui.setItem(4, table);
        player.openInventory(gui);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(gui)) return;
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player p)) return;
        if (!p.equals(player)) return;

        int slot = event.getRawSlot();

        if (!isConfirmGui) {
            // First GUI: place book on enchanting table
            if (slot == 4) {
                ItemStack cursor = event.getCursor();
                if (cursor != null && cursor.isSimilar(originalBook)) {
                    event.setCursor(null);
                    gui.setItem(4, originalBook.clone());
                    p.updateInventory();
                    openConfirmGUI();
                } else {
                    p.sendMessage(plugin.getConfigUtils().getMessage("book-absorb-error"));
                }
            }
        } else {
            // Second GUI: confirmation
            if (slot == 0) {
                // Withdraw
                p.getInventory().addItem(originalBook.clone());
                p.closeInventory();
                p.sendMessage("§eYou withdrew the ability book.");
            } else if (slot == 8) {
                absorbAbility();
                p.closeInventory();
            }
        }
    }

    private void openConfirmGUI() {
        isConfirmGui = true;
        Inventory confirmInv = Bukkit.createInventory(null, 9, CONFIRM_TITLE);

        // Slot 4: placed book with lore
        ItemStack placedBook = originalBook.clone();
        ItemMeta meta = placedBook.getItemMeta();
        meta.setLore(Arrays.asList("§7Ability: §f" + abilityName,
                "§7Stage: §f" + stage,
                "§7Click §aAbsorb §7to consume this book."));
        placedBook.setItemMeta(meta);
        confirmInv.setItem(4, placedBook);

        // Slot 0: Withdraw button
        ItemStack withdraw = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta wMeta = withdraw.getItemMeta();
        wMeta.setDisplayName("§c§lWithdraw Book");
        wMeta.setLore(Arrays.asList("§7Click to get your book back", "§7and cancel absorption."));
        withdraw.setItemMeta(wMeta);
        confirmInv.setItem(0, withdraw);

        // Slot 8: Absorb button
        ItemStack absorb = new ItemStack(Material.NETHER_STAR);
        ItemMeta aMeta = absorb.getItemMeta();
        aMeta.setDisplayName("§a§lAbsorb Power");
        aMeta.setLore(Arrays.asList("§7Permanently learn this ability.", "§7The book will be consumed."));
        absorb.setItemMeta(aMeta);
        confirmInv.setItem(8, absorb);

        // Slot 7: Description book
        ItemStack desc = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta dMeta = desc.getItemMeta();
        dMeta.setDisplayName("§e§lAbsorption Info");
        dMeta.setLore(Arrays.asList("§7Once absorbed, you can",
                "§7use the ability by left-clicking.",
                "§7Switch stages by double-crouching.",
                "§7Each stage has its own cooldown."));
        desc.setItemMeta(dMeta);
        confirmInv.setItem(7, desc);

        this.gui = confirmInv;
        player.openInventory(confirmInv);
    }

    private void absorbAbility() {
        if (absorbed) return;
        absorbed = true;

        plugin.getAbilityManager().giveAbility(player, abilityName);
        plugin.getAbilityManager().setCurrentStage(player, abilityName, stage);

        String hex = plugin.getConfigUtils().getColourHex("effects.crown-colour.stage" + stage, "AA00FF");
        ParticleUtils.spawnCrown(player.getLocation().add(0, 1.5, 0), hex);

        player.sendMessage(plugin.getConfigUtils().getMessage("book-absorbed")
                .replace("%ability%", abilityName));

        player.getInventory().removeItem(originalBook);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!event.getInventory().equals(gui)) return;
        if (!(event.getPlayer() instanceof Player p)) return;
        if (!p.equals(player)) return;

        if (!absorbed && isConfirmGui) {
            ItemStack remainingBook = gui.getItem(4);
            if (remainingBook != null && remainingBook.getType() != Material.AIR) {
                p.getInventory().addItem(remainingBook);
                p.sendMessage("§cAbsorption cancelled. Book returned.");
            }
        }

        // Unregister listener
        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
    }
}
