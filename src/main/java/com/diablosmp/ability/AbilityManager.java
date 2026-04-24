package com.diablosmp.ability;

import com.diablosmp.DiabloSmpPlugin;
import com.diablosmp.data.PlayerAbilityData;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityManager {
    private final DiabloSmpPlugin plugin;
    private final Map<String, Ability> abilities = new HashMap<>();
    private final Map<UUID, PlayerAbilityData> playerData = new HashMap<>();

    public AbilityManager(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
        // Register the first ability
        registerAbility(new SoulWeaverAbility(plugin));
    }

    public void registerAbility(Ability ability) {
        abilities.put(ability.getName().toLowerCase(), ability);
    }

    public Ability getAbility(String name) {
        return abilities.get(name.toLowerCase());
    }

    public boolean hasAbility(Player player, String abilityName) {
        PlayerAbilityData data = getOrCreateData(player);
        return data.getOwnedAbilities().contains(abilityName);
    }

    public void giveAbility(Player player, String abilityName) {
        PlayerAbilityData data = getOrCreateData(player);
        data.getOwnedAbilities().add(abilityName);
        data.setCurrentStage(abilityName, 1);
        saveData(player, data);
    }

    public int getCurrentStage(Player player, String abilityName) {
        return getOrCreateData(player).getCurrentStage(abilityName);
    }

    public void setCurrentStage(Player player, String abilityName, int stage) {
        PlayerAbilityData data = getOrCreateData(player);
        data.setCurrentStage(abilityName, stage);
        saveData(player, data);
    }

    public void cycleStage(Player player, String abilityName) {
        Ability ability = getAbility(abilityName);
        if (ability == null) return;
        int current = getCurrentStage(player, abilityName);
        int next = (current % ability.getMaxStage()) + 1;
        setCurrentStage(player, abilityName, next);
        ability.onStageSwitch(player, current, next);
    }

    public boolean isOnCooldown(Player player, String abilityName, int stage) {
        NamespacedKey key = new NamespacedKey(plugin, "cooldown_" + abilityName + "_" + stage);
        Long lastUse = player.getPersistentDataContainer().get(key, PersistentDataType.LONG);
        if (lastUse == null) return false;
        Ability ability = getAbility(abilityName);
        int cooldown = ability.getCooldownSeconds(stage);
        return (System.currentTimeMillis() - lastUse) < cooldown * 1000L;
    }

    public void setCooldown(Player player, String abilityName, int stage) {
        NamespacedKey key = new NamespacedKey(plugin, "cooldown_" + abilityName + "_" + stage);
        player.getPersistentDataContainer().set(key, PersistentDataType.LONG, System.currentTimeMillis());
    }

    private PlayerAbilityData getOrCreateData(Player player) {
        return playerData.computeIfAbsent(player.getUniqueId(),
                id -> new PlayerAbilityData(id));
    }

    private void saveData(Player player, PlayerAbilityData data) {
        playerData.put(player.getUniqueId(), data);
        // Optional: persist to file
    }
}
