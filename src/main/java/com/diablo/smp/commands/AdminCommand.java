package com.diablo.smp.commands;

import com.diablo.smp.DiabloSMP;
import com.diablo.smp.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class DiabloCommand implements CommandExecutor, TabCompleter {
    private final DiabloSMP plugin;
    public DiabloCommand(DiabloSMP plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("diablo.admin")) return true;

        if (args.length >= 2 && args[0].equalsIgnoreCase("give")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target != null) {
                target.getInventory().addItem(ItemUtils.getAbilityBook());
                sender.sendMessage("§aAbility book given to " + target.getName());
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) return List.of("give", "reload");
        if (args.length == 2) return null; // Player list
        return Collections.emptyList();
    }
}
