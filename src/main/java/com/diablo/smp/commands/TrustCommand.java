package com.diablo.smp.commands;

import com.diablo.smp.DiabloSMP;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TrustCommand implements CommandExecutor {
    private final DiabloSMP plugin;
    public TrustCommand(DiabloSMP plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player p)) return true;

        if (args.length < 1) {
            p.sendMessage("§cUsage: /trust <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            p.sendMessage("§cPlayer not found!");
            return true;
        }

        plugin.trustContract.put(p.getUniqueId(), target.getUniqueId());
        plugin.trustExpiry.put(p.getUniqueId(), System.currentTimeMillis() + (5 * 60 * 1000));
        
        p.sendMessage("§a§lDIABLO §8» §fYou now trust §e" + target.getName() + " §ffor 5 minutes.");
        target.sendMessage("§a§lDIABLO §8» §e" + p.getName() + " §fnow trusts you with their soul!");
        
        return true;
    }
}
