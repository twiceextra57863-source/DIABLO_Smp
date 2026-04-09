package com.diablo.smp.commands;

import com.diablo.smp.DiabloSMP;
import com.diablo.smp.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.*;

public class DiabloCommand implements CommandExecutor, TabCompleter {
    private final DiabloSMP plugin;
    public DiabloCommand(DiabloSMP plugin) { this.plugin = plugin; }

    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (args.length >= 2 && args[0].equalsIgnoreCase("give")) {
            Player t = Bukkit.getPlayer(args[1]);
            if (t != null) t.getInventory().addItem(ItemUtils.getAbilityBook());
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String a, String[] args) {
        if (args.length == 1) return List.of("give", "reload");
        return null;
    }
}
