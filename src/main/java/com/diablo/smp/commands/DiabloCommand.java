package com.diablo.smp.commands;

import com.diablo.smp.DiabloSMP;
import com.diablo.smp.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class DiabloCommand implements CommandExecutor {

    private final DiabloSMP plugin;

    public DiabloCommand(DiabloSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("diablosmp.admin")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /diablo give <player> <ability>");
            return true;
        }

        if (args[0].equalsIgnoreCase("give") && args.length == 3) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }

            String ability = args[2].toLowerCase();
            if (ability.equals("soul_sovereign")) {
                ItemStack item = ItemUtils.createAbilityItem(plugin, Material.NETHER_STAR, 
                        ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Soul Sovereign",
                        Arrays.asList(
                                ChatColor.GRAY + "Right-click to harvest the souls",
                                ChatColor.GRAY + "of your nearby enemies."
                        ),
                        "soul_sovereign");

                target.getInventory().addItem(item);
                sender.sendMessage(ChatColor.GREEN + "Gave Soul Sovereign to " + target.getName() + ".");
            } else {
                sender.sendMessage(ChatColor.RED + "Unknown ability: " + ability);
            }
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Invalid arguments.");
        return true;
    }
}
