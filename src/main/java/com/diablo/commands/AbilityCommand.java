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
            sendGrimoire(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "bestow":
                if (!sender.hasPermission("abyssal.archon")) {
                    sender.sendMessage(ChatColor.DARK_RED + "⬡ " + ChatColor.RED + "You lack the Archon's authority! ⬡");
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.DARK_PURPLE + "⬡ " + ChatColor.LIGHT_PURPLE + "Usage: /abyssal bestow <player> <power> ⬡");
                    return true;
                }
                bestowPower(sender, args[1], args[2]);
                break;
                
            case "cleanse":
                if (!sender.hasPermission("abyssal.archon")) {
                    sender.sendMessage(ChatColor.DARK_RED + "⬡ " + ChatColor.RED + "You lack the Archon's authority! ⬡");
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.DARK_PURPLE + "⬡ " + ChatColor.LIGHT_PURPLE + "Usage: /abyssal cleanse <player> <power> ⬡");
                    return true;
                }
                cleansePower(sender, args[1], args[2]);
                break;
                
            case "compendium":
                if (args.length > 1) {
                    showPlayerCompendium(sender, args[1]);
                } else {
                    showCompendium(sender);
                }
                break;
                
            case "awakening":
                if (!sender.hasPermission("abyssal.archon")) {
                    sender.sendMessage(ChatColor.DARK_RED + "⬡ " + ChatColor.RED + "You lack the Archon's authority! ⬡");
                    return true;
                }
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.DARK_PURPLE + "⬡ " + ChatColor.LIGHT_PURPLE + "The Abyss has been awakened anew! ⬡");
                break;
                
            default:
                sendGrimoire(sender);
                break;
        }
        
        return true;
    }
    
    private void bestowPower(CommandSender sender, String playerName, String powerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(ChatColor.DARK_RED + "⬡ " + ChatColor.RED + "That soul does not exist in this realm! ⬡");
            return;
        }
        
        AbilityType type = getPowerByName(powerName);
        if (type == null) {
            sender.sendMessage(ChatColor.DARK_RED + "⬡ " + ChatColor.RED + "Unknown Abyssal Power! Available powers: " + getPowerList() + " ⬡");
            return;
        }
        
        plugin.getAbilityManager().giveAbility(target, type);
        sender.sendMessage(type.getChatColor() + "⬡ " + ChatColor.GOLD + "Bestowed " + 
            type.getDisplayName() + ChatColor.GOLD + " upon " + target.getName() + " ⬡");
    }
    
    private void cleansePower(CommandSender sender, String playerName, String powerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(ChatColor.DARK_RED + "⬡ " + ChatColor.RED + "That soul does not exist in this realm! ⬡");
            return;
        }
        
        AbilityType type = getPowerByName(powerName);
        if (type == null) {
            sender.sendMessage(ChatColor.DARK_RED + "⬡ " + ChatColor.RED + "Unknown Abyssal Power! ⬡");
            return;
        }
        
        plugin.getAbilityManager().removeAbility(target, type);
        sender.sendMessage(type.getChatColor() + "⬡ " + ChatColor.RED + "Cleansed " + 
            type.getDisplayName() + ChatColor.RED + " from " + target.getName() + "'s soul ⬡");
    }
    
    private void showCompendium(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_PURPLE + "⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "       ᑕOᗰᑭEᑎᗪIᑌᗰ Oᖴ ᗩᗷYᔕᔕᗩᒪ ᑭOᗯEᖇᔕ");
        sender.sendMessage(ChatColor.DARK_PURPLE + "⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡");
        sender.sendMessage("");
        
        for (AbilityType type : AbilityType.values()) {
            sender.sendMessage(type.getChatColor() + "  ⬡ " + ChatColor.GOLD + type.getSimpleName() + 
                ChatColor.GRAY + " - " + type.getDisplayName());
        }
        
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_GRAY + "Use /abyssal compendium <player> to view awakened powers");
        sender.sendMessage(ChatColor.DARK_PURPLE + "⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡");
    }
    
    private void showPlayerCompendium(CommandSender sender, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(ChatColor.DARK_RED + "⬡ " + ChatColor.RED + "That soul does not exist in this realm! ⬡");
            return;
        }
        
        List<AbilityType> abilities = plugin.getAbilityManager().getPlayerAbilities(target);
        
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_PURPLE + "⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "   " + target.getName() + "'ᔕ ᗩᗯᗩKEᑎEᗪ ᑭOᗯEᖇᔕ");
        sender.sendMessage(ChatColor.DARK_PURPLE + "⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡");
        
        if (abilities.isEmpty()) {
            sender.sendMessage(ChatColor.GRAY + "   This soul has not awakened any powers...");
        } else {
            for (AbilityType type : abilities) {
                sender.sendMessage(type.getChatColor() + "   ⬡ " + type.getDisplayName());
            }
        }
        sender.sendMessage(ChatColor.DARK_PURPLE + "⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡");
    }
    
    private void sendGrimoire(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_PURPLE + "⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "         GᖇIᗰOIᖇE Oᖴ TᕼE ᗩᗷYᔕᔕ");
        sender.sendMessage(ChatColor.DARK_PURPLE + "⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GOLD + "/abyssal bestow <player> <power> " + ChatColor.GRAY + "- Grant Abyssal Power");
        sender.sendMessage(ChatColor.GOLD + "/abyssal cleanse <player> <power> " + ChatColor.GRAY + "- Remove Abyssal Power");
        sender.sendMessage(ChatColor.GOLD + "/abyssal compendium [player] " + ChatColor.GRAY + "- View Powers");
        sender.sendMessage(ChatColor.GOLD + "/abyssal awakening " + ChatColor.GRAY + "- Reload the Abyss");
        sender.sendMessage(ChatColor.GOLD + "/soulbind <player> " + ChatColor.GRAY + "- Bind souls for 5 minutes");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_PURPLE + "⬡ " + ChatColor.LIGHT_PURPLE + "Double Crouch to cycle through power stages! " + ChatColor.DARK_PURPLE + "⬡");
        sender.sendMessage(ChatColor.DARK_PURPLE + "⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡⬡");
    }
    
    private AbilityType getPowerByName(String name) {
        for (AbilityType type : AbilityType.values()) {
            if (type.getSimpleName().equalsIgnoreCase(name) || 
                type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
    
    private String getPowerList() {
        return Arrays.stream(AbilityType.values())
            .map(AbilityType::getSimpleName)
            .collect(Collectors.joining(", "));
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.addAll(Arrays.asList("bestow", "cleanse", "compendium", "awakening"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("bestow") || args[0].equalsIgnoreCase("cleanse")) {
                Bukkit.getOnlinePlayers().forEach(p -> completions.add(p.getName()));
            } else if (args[0].equalsIgnoreCase("compendium")) {
                Bukkit.getOnlinePlayers().forEach(p -> completions.add(p.getName()));
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("bestow") || args[0].equalsIgnoreCase("cleanse")) {
                for (AbilityType type : AbilityType.values()) {
                    completions.add(type.getSimpleName());
                }
            }
        }
        
        return completions.stream()
            .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
            .collect(Collectors.toList());
    }
}
