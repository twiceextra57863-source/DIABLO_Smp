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
    private final ItemStack book;
    private Inventory gui;
    private boolean absorbed = false;
    private String abilityName;
    private int stage;

    // GUI titles
    private static final String PLACE_TITLE = "§0§lPlace Ability Book";
    private static final String CONFIRM_TITLE = "§0§lConfirm Absorption";

    public AbsorptionGUI(DiabloSmpPlugin plugin, Player player, ItemStack book) {
        this.plugin = plugin;
        this.player = player;
        this.book = book.clone(); // clone to avoid modifications
        // Extract ability info from book PDC
        var pdc = book.getItemMeta().getPersistentDataContainer();
        this.abilityName = pdc.get(new NamespacedKey(plugin, "ability"), PersistentDataType.STRING);
        this.stage = pdc.getOrDefault(new NamespacedKey(plugin, "stage"), PersistentDataType.INTEGER, 1);
    }

    public void open() {
        // First GUI: enchanting table in center
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
        event.setCancelled(true); // prevent any item movement

        if (!(event.getWhoClicked() instanceof Player p)) return;
        if (!p.equals(player)) return;

        int slot = event.getRawSlot();

        // Stage 1: Place GUI
        if (gui.getTitle().equals(PLACE_TITLE)) {
            if (slot == 4) {
                // Player clicked the enchanting table slot – check if holding the correct book
                ItemStack cursor = event.getCursor();
                if (cursor != null && cursor.isSimilar(book)) {
                    // Place the book into the slot
                    event.setCursor(null);
                    gui.setItem(4, book.clone());
                    p.updateInventory();
                    // Now switch to second GUI (confirm)
                    openConfirmGUI();
                } else {
                    p.sendMessage(plugin.getConfigUtils().getMessage("book-absorb-error"));
                }
            }
        }
        // Stage 2: Confirm GUI
        else if (gui.getTitle().equals(CONFIRM_TITLE)) {
            // Withdraw slot (slot 0)
            if (slot == 0) {
                // Give back the book and close
                p.getInventory().addItem(book.clone());
                p.closeInventory();
                p.sendMessage("§eYou withdrew the ability book.");
            }
            // Absorb slot (slot 8)
            else if (slot == 8) {
                absorbAbility();
                p.closeInventory();
            }
            // Any other slot is ignored
        }
    }

    private void openConfirmGUI() {
        // Create new inventory for confirmation
        Inventory confirmInv = Bukkit.createInventory(null, 9, CONFIRM_TITLE);

        // Slot 4: the placed book (clone, with updated lore)
        ItemStack placedBook = book.clone();
        ItemMeta meta = placedBook.getItemMeta();
        meta.setLore(Arrays.asList("§7Ability: §f" + abilityName,
                "§7Stage: §f" + stage,
                "§7Click §aAbsorb §7to consume this book."));
        placedBook.setItemMeta(meta);
        confirmInv.setItem(4, placedBook);

        // Slot 0: Withdraw button (redstone torch)
        ItemStack withdraw = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta wMeta = withdraw.getItemMeta();
        wMeta.setDisplayName("§c§lWithdraw Book");
        wMeta.setLore(Arrays.asList("§7Click to get your book back", "§7and cancel absorption."));
        withdraw.setItemMeta(wMeta);
        confirmInv.setItem(0, withdraw);

        // Slot 8: Absorb button (nether star)
        ItemStack absorb = new ItemStack(Material.NETHER_STAR);
        ItemMeta aMeta = absorb.getItemMeta();
        aMeta.setDisplayName("§a§lAbsorb Power");
        aMeta.setLore(Arrays.asList("§7Permanently learn this ability.", "§7The book will be consumed."));
        absorb.setItemMeta(aMeta);
        confirmInv.setItem(8, absorb);

        // Slot 7: Description (written book)
        ItemStack desc = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta dMeta = desc.getItemMeta();
        dMeta.setDisplayName("§e§lAbsorption Info");
        dMeta.setLore(Arrays.asList("§7Once absorbed, you can",
                "§7use the ability by left-clicking.",
                "§7Switch stages by double-crouching.",
                "§7Each stage has its own cooldown."));
        desc.setItemMeta(dMeta);
        confirmInv.setItem(7, desc);

        // Replace current GUI
        this.gui = confirmInv;
        player.openInventory(confirmInv);
    }

    private void absorbAbility() {
        if (absorbed) return;
        absorbed = true;

        // Give ability to player
        plugin.getAbilityManager().giveAbility(player, abilityName);
        // Set stage if needed (default stage 1)
        plugin.getAbilityManager().setCurrentStage(player, abilityName, stage);

        // Dragon particles forming crown
        String hex = plugin.getConfigUtils().getColourHex("effects.crown-colour.stage" + stage, "AA00FF");
        ParticleUtils.spawnCrown(player.getLocation().add(0, 1.5, 0), hex);

        // Success message
        player.sendMessage(plugin.getConfigUtils().getMessage("book-absorbed")
                .replace("%ability%", abilityName));

        // Remove the book from inventory (already gone from GUI)
        player.getInventory().removeItem(book);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!event.getInventory().equals(gui)) return;
        if (!(event.getPlayer() instanceof Player p)) return;
        if (!p.equals(player)) return;

        // If not absorbed and we are in confirm GUI, and the book is still there, return it
        if (!absorbed && gui.getTitle().equals(CONFIRM_TITLE)) {
            ItemStack remainingBook = gui.getItem(4);
            if (remainingBook != null && remainingBook.getType() != Material.AIR) {
                p.getInventory().addItem(remainingBook);
                p.sendMessage("§cAbsorption cancelled. Book returned.");
            }
        }

        // Unregister listener to avoid memory leaks
        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
    }
    }
