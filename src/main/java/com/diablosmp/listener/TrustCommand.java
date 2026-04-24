package com.diablosmp.commands;

import com.diablosmp.DiabloSmpPlugin;
import com.diablosmp.listener.TrustListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrustCommand implements CommandExecutor {
    private final DiabloSmpPlugin plugin;
    private final TrustListener trustListener;

    public TrustCommand(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
        this.trustListener = new TrustListener(plugin);
        plugin.getServer().getPluginManager().registerEvents(trustListener, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use /trust.");
            return true;
        }
        if (args.length != 1) {
            player.sendMessage("§cUsage: /trust <player>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§cPlayer not found.");
            return true;
        }
        trustListener.addTrust(player, target, 300); // 5 minutes
        player.sendMessage("§aYou have trusted §e" + target.getName() + "§a for 5 minutes. If they kill you, your ability book will drop.");
        target.sendMessage("§e" + player.getName() + "§a has trusted you for 5 minutes. Killing them will make their ability book drop.");
        return true;
    }
}
