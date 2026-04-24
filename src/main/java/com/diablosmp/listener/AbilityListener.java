package com.diablosmp.listener;

import com.diablosmp.DiabloSmpPlugin;
import com.diablosmp.ability.Ability;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityListener implements Listener {
    private final DiabloSmpPlugin plugin;
    private final Map<UUID, Long> lastSneakTime = new HashMap<>();

    public AbilityListener(DiabloSmpPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        // Check each ability the player owns
        for (String abilityName : plugin.getAbilityManager().getOrCreateData(player).getOwnedAbilities()) {
            Ability ability = plugin.getAbilityManager().getAbility(abilityName);
            int stage = plugin.getAbilityManager().getCurrentStage(player, abilityName);
            if (plugin.getAbilityManager().isOnCooldown(player, abilityName, stage)) {
                player.sendMessage("§c" + ability.getDisplayName() + " stage " + stage + " is on cooldown!");
                continue;
            }
            ability.onLeftClick(player, stage);
            plugin.getAbilityManager().setCooldown(player, abilityName, stage);
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) return; // only when starting to sneak
        Player player = event.getPlayer();
        UUID uid = player.getUniqueId();
        long now = System.currentTimeMillis();
        if (lastSneakTime.containsKey(uid) && (now - lastSneakTime.get(uid)) < 500) {
            // Double crouch detected
            lastSneakTime.remove(uid);
            for (String abilityName : plugin.getAbilityManager().getOrCreateData(player).getOwnedAbilities()) {
                plugin.getAbilityManager().cycleStage(player, abilityName);
                player.sendMessage("§e" + abilityName + " stage changed to "
                        + plugin.getAbilityManager().getCurrentStage(player, abilityName));
            }
        } else {
            lastSneakTime.put(uid, now);
        }
    }
}
