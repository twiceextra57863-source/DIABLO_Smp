package com.diablosmp.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    
    private final Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();
    
    public void setCooldown(UUID playerId, String ability, int seconds) {
        Map<UUID, Long> abilityCooldowns = cooldowns.computeIfAbsent(ability, k -> new HashMap<>());
        abilityCooldowns.put(playerId, System.currentTimeMillis() + (seconds * 1000L));
    }
    
    public boolean hasCooldown(UUID playerId, String ability) {
        Map<UUID, Long> abilityCooldowns = cooldowns.get(ability);
        if (abilityCooldowns == null) return false;
        
        Long cooldownEnd = abilityCooldowns.get(playerId);
        if (cooldownEnd == null) return false;
        
        return System.currentTimeMillis() < cooldownEnd;
    }
    
    public long getRemainingCooldown(UUID playerId, String ability) {
        Map<UUID, Long> abilityCooldowns = cooldowns.get(ability);
        if (abilityCooldowns == null) return 0;
        
        Long cooldownEnd = abilityCooldowns.get(playerId);
        if (cooldownEnd == null) return 0;
        
        long remaining = (cooldownEnd - System.currentTimeMillis()) / 1000;
        return Math.max(0, remaining);
    }
    
    public void removeCooldown(UUID playerId, String ability) {
        Map<UUID, Long> abilityCooldowns = cooldowns.get(ability);
        if (abilityCooldowns != null) {
            abilityCooldowns.remove(playerId);
        }
    }
    
    public void clearAllCooldowns(UUID playerId) {
        for (Map<UUID, Long> abilityCooldowns : cooldowns.values()) {
            abilityCooldowns.remove(playerId);
        }
    }
}
