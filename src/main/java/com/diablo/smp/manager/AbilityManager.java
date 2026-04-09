package com.diablo.smp.manager;

import java.util.*;

public class AbilityManager {
    // Player UUID -> Ability Number -> Stage (1, 2, 3)
    private final Map<UUID, Integer> currentStage = new HashMap<>();
    // Player UUID -> (AbilityName_Stage -> CooldownTimestamp)
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public int getStage(UUID uuid) { return currentStage.getOrDefault(uuid, 1); }
    
    public void nextStage(UUID uuid) {
        int next = (getStage(uuid) % 3) + 1;
        currentStage.put(uuid, next);
    }

    public void setCooldown(UUID uuid, String key, int seconds) {
        cooldowns.computeIfAbsent(uuid, k -> new HashMap<>()).put(key, System.currentTimeMillis() + (seconds * 1000L));
    }

    public boolean isCooldown(UUID uuid, String key) {
        if (!cooldowns.containsKey(uuid)) return false;
        return cooldowns.get(uuid).getOrDefault(key, 0L) > System.currentTimeMillis();
    }
}
