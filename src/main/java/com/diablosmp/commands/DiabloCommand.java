package com.diablosmp.commands;

import com.diablosmp.DiabloSmpPlugin;
import com.diablosmp.ability.Ability;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiabloCommand implements CommandExecutor, TabCompleter {
    private final DiabloSmpPlugin plugin;

    public DiabloCommand(DiabloSmpPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /diablo give <player> <ability> [stage]");
            return true;
        }
        if (args[0].equalsIgnoreCase("give") && args.length >= 3) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§cPlayer not found.");
                return true;
            }
            String abilityName = args[2].toLowerCase();
            Ability ability = plugin.getAbilityManager().getAbility(abilityName);
            if (ability == null) {
                sender.sendMessage("§cUnknown ability.");
                return true;
            }
            int stage = 1;
            if (args.length >= 4) {
                try { stage = Integer.parseInt(args[3]); } catch (NumberFormatException ignored) {}
                if (stage < 1 || stage > ability.getMaxStage()) stage = 1;
            }
            // Create ability book
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = book.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    "&5&l" + ability.getDisplayName() + " &7(Stage " + stage + ")"));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "ability"),
                    PersistentDataType.STRING, ability.getName());
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stage"),
                    PersistentDataType.INTEGER, stage);
            book.setItemMeta(meta);
            target.getInventory().addItem(book);
            sender.sendMessage("§aGiven " + ability.getDisplayName() + " stage " + stage + " book to " + target.getName());
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage("§aConfig reloaded.");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("give");
            completions.add("reload");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            completions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            completions.addAll(plugin.getAbilityManager().getAllAbilityNames());
        } else if (args.length == 4 && args[0].equalsIgnoreCase("give")) {
            completions.add("1");
            completions.add("2");
            completions.add("3");
        }
        return completions.stream().filter(s -> s.toLowerCase().startsWith(args[args.length-1].toLowerCase())).collect(Collectors.toList());
    }
                                                                                                                       }
