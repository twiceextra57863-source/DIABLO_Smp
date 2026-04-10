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
        
        String subCommand = args[0].toLowerCase();
        
        if (subCommand.equals("give")) {
            if (!sender.hasPermission("diablosmp.admin")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission!");
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /ability give <player> <ability>");
                return true;
            }
            giveAbility(sender, args[1], args[2]);
        }
        else if (subCommand.equals("remove")) {
            if (!sender.hasPermission("diablosmp.admin")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission!");
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /ability remove <player> <ability>");
                return true;
            }
            removeAbility(sender, args[1], args[2]);
        }
        else if (subCommand.equals("list")) {
            if (args.length > 1) {
                listPlayerAbilities(sender, args[1]);
            } else {
                listAllAbilities(sender);
            }
        }
        else {
            sendHelp(sender);
        }
        
        return true;
    }
    
    private void giveAbility(CommandSender sender, String playerName, String abilityName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }
        
        AbilityType type = getAbilityByName(abilityName);
        if (type == null) {
            sender.sendMessage(ChatColor.RED + "Unknown ability! Available: " + getAbilityList());
            return;
        }
        
        plugin.getAbilityManager().giveAbility(target, type);
        sender.sendMessage(ChatColor.GREEN + "Gave " + type.getDisplayName() + ChatColor.GREEN + " to " + target.getName());
    }
    
    private void removeAbility(CommandSender sender, String playerName, String abilityName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }
        
        AbilityType type = getAbilityByName(abilityName);
        if (type == null) {
            sender.sendMessage(ChatColor.RED + "Unknown ability!");
            return;
        }
        
        plugin.getAbilityManager().removeAbility(target, type);
        sender.sendMessage(ChatColor.RED + "Removed " + type.getDisplayName() + ChatColor.RED + " from " + target.getName());
    }
    
    private void listAllAbilities(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GOLD + "=== Available Diablo SMP Abilities ===");
        sender.sendMessage("");
        
        for (AbilityType type : AbilityType.values()) {
            sender.sendMessage(type.getChatColor() + "✦ " + type.getDisplayName() + 
                ChatColor.GRAY + " [" + type.getCommandName() + "]");
        }
        
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GRAY + "Total: " + AbilityType.values().length + " Abilities");
        sender.sendMessage(ChatColor.GOLD + "=====================================");
    }
    
    private void listPlayerAbilities(CommandSender sender, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }
        
        List<AbilityType> abilities = plugin.getAbilityManager().getPlayerAbilities(target);
        
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GOLD + "=== " + target.getName() + "'s Abilities ===");
        
        if (abilities.isEmpty()) {
            sender.sendMessage(ChatColor.GRAY + "No abilities awakened");
        } else {
            for (AbilityType type : abilities) {
                sender.sendMessage(type.getChatColor() + "✦ " + type.getDisplayName());
            }
        }
        sender.sendMessage(ChatColor.GOLD + "=============================");
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GOLD + "=== Diablo SMP Ability Commands ===");
        sender.sendMessage(ChatColor.YELLOW + "/ability give <player> <ability> " + ChatColor.GRAY + "- Give an ability");
        sender.sendMessage(ChatColor.YELLOW + "/ability remove <player> <ability> " + ChatColor.GRAY + "- Remove an ability");
        sender.sendMessage(ChatColor.YELLOW + "/ability list [player] " + ChatColor.GRAY + "- List abilities");
        sender.sendMessage(ChatColor.YELLOW + "/trust <player> " + ChatColor.GRAY + "- Trust player for 5 minutes");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GRAY + "Example: /ability give Steve SoulReaper");
        sender.sendMessage(ChatColor.GOLD + "====================================");
    }
    
    private AbilityType getAbilityByName(String name) {
        for (AbilityType type : AbilityType.values()) {
            if (type.getCommandName().equalsIgnoreCase(name) || 
                type.name().equalsIgnoreCase(name) ||
                type.getDisplayName().toLowerCase().contains(name.toLowerCase())) {
                return type;
            }
        }
        return null;
    }
    
    private String getAbilityList() {
        return Arrays.stream(AbilityType.values())
            .map(AbilityType::getCommandName)
            .collect(Collectors.joining(", "));
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            List<String> subs = Arrays.asList("give", "remove", "list");
            for (String sub : subs) {
                if (sub.startsWith(partial)) completions.add(sub);
            }
        }
        else if (args.length == 2) {
            String partial = args[1].toLowerCase();
            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("list")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(partial)) completions.add(p.getName());
                }
            }
        }
        else if (args.length == 3) {
            String partial = args[2].toLowerCase();
            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("remove")) {
                for (AbilityType type : AbilityType.values()) {
                    if (type.getCommandName().toLowerCase().startsWith(partial)) {
                        completions.add(type.getCommandName());
                    }
                }
            }
        }
        
        return completions;
    }
}
