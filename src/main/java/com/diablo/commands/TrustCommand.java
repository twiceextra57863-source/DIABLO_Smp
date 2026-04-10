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
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.DARK_RED + "⬡ " + ChatColor.RED + "Only living souls can perform the Soul Binding! ⬡");
            return true;
        }
        
        if (args.length < 1) {
            player.sendMessage("");
            player.sendMessage(ChatColor.DARK_PURPLE + "⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡");
            player.sendMessage(ChatColor.LIGHT_PURPLE + "   ᔕOᑌᒪ ᗷIᑎᗪIᑎG ᖇITᑌᗩᒪ");
            player.sendMessage(ChatColor.DARK_PURPLE + "⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡");
            player.sendMessage("");
            player.sendMessage(ChatColor.GOLD + "/soulbind <player> " + ChatColor.GRAY + "- Bind your soul to another");
            player.sendMessage(ChatColor.GRAY + "Allows sharing of Abyssal Tomes for 5 minutes");
            player.sendMessage(ChatColor.DARK_RED + "⚠ " + ChatColor.GRAY + "Beware: If you fall in combat during the binding...");
            player.sendMessage(ChatColor.DARK_RED + "   " + ChatColor.GRAY + "Your Tome may fall into enemy hands!");
            player.sendMessage("");
            player.sendMessage(ChatColor.DARK_PURPLE + "⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡");
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.DARK_RED + "⬡ " + ChatColor.RED + "That soul does not exist in this realm! ⬡");
            return true;
        }
        
        if (target.equals(player)) {
            player.sendMessage(ChatColor.DARK_RED + "⬡ " + ChatColor.RED + "You cannot bind your soul to yourself! ⬡");
            return true;
        }
        
        plugin.getTrustManager().createTrust(player, target);
        
        // Visual effect for soul binding
        player.getWorld().spawnParticle(org.bukkit.Particle.SOUL_FIRE_FLAME, 
            player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.05);
        player.getWorld().spawnParticle(org.bukkit.Particle.SOUL_FIRE_FLAME, 
            target.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.05);
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
