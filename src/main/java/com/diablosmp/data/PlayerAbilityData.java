package com.diablosmp.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerAbilityData {
    private final UUID uuid;
    private final Set<String> ownedAbilities = new HashSet<>();
    private final Map<String, Integer> currentStage = new HashMap<>();

    public PlayerAbilityData(UUID uuid) { this.uuid = uuid; }

    public UUID getUuid() { return uuid; }
    public Set<String> getOwnedAbilities() { return ownedAbilities; }
    public int getCurrentStage(String ability) { return currentStage.getOrDefault(ability, 1); }
    public void setCurrentStage(String ability, int stage) { currentStage.put(ability, stage); }
}
