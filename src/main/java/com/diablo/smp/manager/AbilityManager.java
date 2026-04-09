package com.diablo.smp.manager;

import com.diablo.smp.DiabloSMP;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityManager {

    private final DiabloSMP plugin;
    private final Map<UUID, Map<String, Long>> cooldowns;

    public AbilityManager(DiabloSMP plugin) {
        this.plugin = plugin;
        this.cooldowns = new HashMap<>();
    }

    /**
     * Set cooldown for a player's ability.
     *
     * @param player  The player
     * @param ability The ability name
     * @param seconds Cooldown in seconds
     */
    public void setCooldown(Player player, String ability, int seconds) {
        cooldowns.putIfAbsent(player.getUniqueId(), new HashMap<>());
        cooldowns.get(player.getUniqueId()).put(ability, System.currentTimeMillis() + (seconds * 1000L));
    }

    /**
     * Check if a player has an active cooldown for an ability.
     *
     * @param player  The player
     * @param ability The ability name
     * @return true if on cooldown
     */
    public boolean isOnCooldown(Player player, String ability) {
        if (!cooldowns.containsKey(player.getUniqueId())) return false;
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (!playerCooldowns.containsKey(ability)) return false;
        return System.currentTimeMillis() < playerCooldowns.get(ability);
    }

    /**
     * Get remaining cooldown time.
     *
     * @param player  The player
     * @param ability The ability name
     * @return seconds remaining
     */
    public int getRemainingCooldown(Player player, String ability) {
        if (!isOnCooldown(player, ability)) return 0;
        long expiry = cooldowns.get(player.getUniqueId()).get(ability);
        return (int) ((expiry - System.currentTimeMillis()) / 1000L);
    }
}
