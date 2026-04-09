package com.diablo.smp.listeners;

import com.diablo.smp.DiabloSMP;
import com.diablo.smp.abilities.SoulSovereign;
import com.diablo.smp.utils.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AbilityListener implements Listener {

    private final DiabloSMP plugin;

    public AbilityListener(DiabloSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta()) return;

        String abilityId = ItemUtils.getAbilityId(plugin, item);
        if (abilityId == null) return;

        event.setCancelled(true);

        if ("soul_sovereign".equals(abilityId)) {
            SoulSovereign.execute(plugin, event.getPlayer());
        }
    }
}
