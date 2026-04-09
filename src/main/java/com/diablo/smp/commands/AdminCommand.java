package com.diablo.smp.commands;

import com.diablo.smp.DiabloSMP;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

public class AdminCommand implements CommandExecutor, TabCompleter {
    private final DiabloSMP plugin;
    public AdminCommand(DiabloSMP plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (args.length >= 2 && args[0].equalsIgnoreCase("give")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target != null) {
                ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta m = book.getItemMeta();
                m.setDisplayName("§d§lDIABLO SOUL");
                book.setItemMeta(m);
                target.getInventory().addItem(book);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String a, String[] args) {
        if (args.length == 1) return List.of("give", "reload", "reset");
        if (args.length == 2) return null; // Returns online players
        return new ArrayList<>();
    }
}
