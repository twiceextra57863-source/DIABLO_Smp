package com.diablo.smp.commands;

import com.diablo.smp.DiabloSMP;
import com.diablo.smp.manager.TrustManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TrustCommand implements CommandExecutor {

    private final DiabloSMP plugin;

    public TrustCommand(DiabloSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can manage trusts.");
            return true;
        }
        Player player = (Player) sender;
        TrustManager tm = plugin.getTrustManager();

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /trust <add|remove|list> [player]");
            return true;
        }

        String action = args[0].toLowerCase();
        
        if (action.equals("list")) {
            player.sendMessage(ChatColor.GOLD + "Trusted Players:");
            for (UUID id : tm.getTrusted(player)) {
                String name = Bukkit.getOfflinePlayer(id).getName();
                player.sendMessage(ChatColor.YELLOW + "- " + (name != null ? name : id.toString()));
            }
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Please specify a player.");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player must be online to trust them initially.");
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(ChatColor.RED + "You cannot trust yourself.");
            return true;
        }

        if (action.equals("add")) {
            tm.addTrust(player, target);
            player.sendMessage(ChatColor.GREEN + "You have trusted " + target.getName() + ". They can no longer damage you.");
            target.sendMessage(ChatColor.GREEN + player.getName() + " has trusted you.");
        } else if (action.equals("remove")) {
            tm.removeTrust(player, target);
            player.sendMessage(ChatColor.YELLOW + "You have removed trust from " + target.getName() + ".");
        } else {
            player.sendMessage(ChatColor.RED + "Unknown action.");
        }

        return true;
    }
}
