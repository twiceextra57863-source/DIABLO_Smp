package com.diablosmp.commands;

import com.diablosmp.DiabloSmpPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TrustCommand implements CommandExecutor, TabCompleter {
    
    private final DiabloSmpPlugin plugin;
    
    public TrustCommand(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /trust <player>");
            player.sendMessage(ChatColor.GRAY + "Trust another player to receive ability tomes for 5 minutes");
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }
        
        if (target.equals(player)) {
            player.sendMessage(ChatColor.RED + "You cannot trust yourself!");
            return true;
        }
        
        plugin.getTrustManager().createTrust(player, target);
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(partial))
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
