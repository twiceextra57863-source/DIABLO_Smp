package com.diablosmp.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public interface Ability {
    String getName();
    String getDisplayName();
    int getMaxStage(); // should be 3
    void onLeftClick(Player player, int stage);
    void onStageSwitch(Player player, int oldStage, int newStage);
    int getCooldownSeconds(int stage);
    String getParticleColorHex(int stage); // for crown colour
}
