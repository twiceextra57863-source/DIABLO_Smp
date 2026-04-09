package com.diablo.smp.listeners;

import com.diablo.smp.DiabloSMP;
import com.diablo.smp.abilities.SoulSovereign;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class AbilityListener implements Listener {
    private final DiabloSMP plugin;
    public AbilityListener(DiabloSMP plugin) { this.plugin = plugin; }

    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent e) {
        if (e.isSneaking()) {
            Player p = e.getPlayer();
            // Double crouch detection logic
            plugin.getAbilityManager().nextStage(p.getUniqueId());
            p.sendActionBar("§6§lStage: §e" + plugin.getAbilityManager().getStage(p.getUniqueId()));
        }
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent e) {
        if (e.getAction().name().contains("LEFT")) {
            Player p = e.getPlayer();
            int stage = plugin.getAbilityManager().getStage(p.getUniqueId());
            if (stage == 3) SoulSovereign.stageThree(p);
        }
    }
}
