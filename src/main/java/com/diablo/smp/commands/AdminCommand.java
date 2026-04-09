package com.diablo.smp.commands;

import com.diablo.smp.DiabloSMP;
import com.diablo.smp.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminCommand implements CommandExecutor, TabCompleter {
    private final DiabloSMP plugin;

    public AdminCommand(DiabloSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // Sirf admin hi use kar sake
        if (!sender.hasPermission("diablo.admin")) {
            sender.sendMessage(Component.text("You don't have permission!").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Component.text("§8§m---------------------------------"));
            sender.sendMessage(Component.text("§6§lDIABLO SMP ADMIN HELP"));
            sender.sendMessage(Component.text("§e/diablo give <player> §7- Give Ability Book"));
            sender.sendMessage(Component.text("§e/diablo reload §7- Reload Config"));
            sender.sendMessage(Component.text("§8§m---------------------------------"));
            return true;
        }

        // /diablo give <player>
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 2) {
                sender.sendMessage(Component.text("§cUsage: /diablo give <player>"));
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Component.text("§cPlayer not found!"));
                return true;
            }

            // ItemUtils se book le kar target ko dena
            target.getInventory().addItem(ItemUtils.getAbilityBook());
            sender.sendMessage(Component.text("§a§lSUCCESS! §eGiven Diablo Soul Book to " + target.getName()));
            target.sendMessage(Component.text("§d§lDIABLO §8» §fAn admin has gifted you a Soul Book!"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(Component.text("§aDiablo SMP Config Reloaded!"));
            return true;
        }

        return true;
    }

    // Tab Completion: Isse commands adha likhne par aage ka show hoga
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("give");
            suggestions.add("reload");
            return suggestions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            // Online players ke naam show karega
            return null; // Null return karne se Bukkit default online players ki list dikhata hai
        }

        return Collections.emptyList();
    }
}
