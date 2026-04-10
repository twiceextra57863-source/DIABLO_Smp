package com.diablosmp.commands;

import com.diablosmp.DiabloSmpPlugin;
import com.diablosmp.abilities.AbilityType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AbilityCommand implements CommandExecutor, TabCompleter {
    
    private final DiabloSmpPlugin plugin;
    
    public AbilityCommand(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "give":
                if (!sender.hasPermission("diablosmp.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission!");
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /ability give <player> <ability>");
                    return true;
                }
                giveAbility(sender, args[1], args[2]);
                break;
                
            case "remove":
                if (!sender.hasPermission("diablosmp.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission!");
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /ability remove <player> <ability>");
                    return true;
                }
                removeAbility(sender, args[1], args[2]);
                break;
                
            case "list":
                listAbilities(sender, args.length > 1 ? args[1] : null);
                break;
                
            case "reload":
                if (!sender.hasPermission("diablosmp.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission!");
                    return true;
                }
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Diablo SMP Plugin reloaded!");
                break;
                
            case "info":
                showInfo(sender);
                break;
                
            default:
                sendHelp(sender);
                break;
        }
        
        return true;
    }
    
    private void giveAbility(CommandSender sender, String playerName, String abilityName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }
        
        AbilityType type = getAbilityType(abilityName);
        if (type == null) {
            sender.sendMessage(ChatColor.RED + "Invalid ability! Available: " + getAbilityList());
            return;
        }
        
        plugin.getAbilityManager().giveAbility(target, type);
        sender.sendMessage(ChatColor.GREEN + "Gave " + type.getDisplayName() + 
            ChatColor.GREEN + " to " + target.getName());
    }
    
    private void removeAbility(CommandSender sender, String playerName, String abilityName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }
        
        AbilityType type = getAbilityType(abilityName);
        if (type == null) {
            sender.sendMessage(ChatColor.RED + "Invalid ability!");
            return;
        }
        
        plugin.getAbilityManager().removeAbility(target, type);
        sender.sendMessage(ChatColor.RED + "Removed " + type.getDisplayName() + 
            ChatColor.RED + " from " + target.getName());
    }
    
    private void listAbilities(CommandSender sender, String playerName) {
        if (playerName != null) {
            Player target = Bukkit.getPlayer(playerName);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return;
            }
            sender.sendMessage(ChatColor.GOLD + "=== " + target.getName() + "'s Abilities ===");
            // Show player's abilities
        } else {
            sender.sendMessage(ChatColor.GOLD + "=== Available Abilities ===");
            for (AbilityType type : AbilityType.values()) {
                sender.sendMessage(type.getChatColor() + "✦ " + type.getDisplayName());
            }
        }
    }
    
    private void showInfo(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== Diablo SMP Plugin Info ===");
        sender.sendMessage(ChatColor.YELLOW + "Version: " + ChatColor.WHITE + "1.0.0");
        sender.sendMessage(ChatColor.YELLOW + "Author: " + ChatColor.WHITE + "YourName");
        sender.sendMessage(ChatColor.YELLOW + "Abilities: " + ChatColor.WHITE + AbilityType.values().length);
        sender.sendMessage(ChatColor.GRAY + "Use /abilities to see all powers!");
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== Diablo SMP Commands ===");
        sender.sendMessage(ChatColor.YELLOW + "/ability give <player> <ability> " + ChatColor.GRAY + "- Give ability");
        sender.sendMessage(ChatColor.YELLOW + "/ability remove <player> <ability> " + ChatColor.GRAY + "- Remove ability");
        sender.sendMessage(ChatColor.YELLOW + "/ability list [player] " + ChatColor.GRAY + "- List abilities");
        sender.sendMessage(ChatColor.YELLOW + "/trust <player> " + ChatColor.GRAY + "- Trust player for 5 min");
        sender.sendMessage(ChatColor.YELLOW + "/diablo reload " + ChatColor.GRAY + "- Reload plugin");
        sender.sendMessage(ChatColor.YELLOW + "/diablo info " + ChatColor.GRAY + "- Plugin info");
    }
    
    private AbilityType getAbilityType(String name) {
        for (AbilityType type : AbilityType.values()) {
            if (type.name().equalsIgnoreCase(name) || 
                type.getDisplayName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
    
    private String getAbilityList() {
        return Arrays.stream(AbilityType.values())
            .map(AbilityType::getDisplayName)
            .collect(Collectors.joining(", "));
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.addAll(Arrays.asList("give", "remove", "list", "reload", "info"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("remove")) {
                Bukkit.getOnlinePlayers().forEach(p -> completions.add(p.getName()));
            } else if (args[0].equalsIgnoreCase("list")) {
                Bukkit.getOnlinePlayers().forEach(p -> completions.add(p.getName()));
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("remove")) {
                for (AbilityType type : AbilityType.values()) {
                    completions.add(type.getDisplayName());
                }
            }
        }
        
        return completions.stream()
            .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
            .collect(Collectors.toList());
    }
}
