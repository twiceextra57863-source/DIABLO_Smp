package com.diablosmp.ability;

import com.diablosmp.DiabloSmpPlugin;
import com.diablosmp.data.PlayerAbilityData;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages all abilities, player data, cooldowns, and stage cycling.
 */
public class AbilityManager {
    private final DiabloSmpPlugin plugin;
    private final Map<String, Ability> abilities = new HashMap<>();
    private final Map<UUID, PlayerAbilityData> playerData = new ConcurrentHashMap<>();

    public AbilityManager(DiabloSmpPlugin plugin) {
        this.plugin = plugin;
        // Register the first ability (Soul Weaver)
        registerAbility(new SoulWeaverAbility(plugin));
        // Additional abilities will be registered here later
    }

    /**
     * Registers a new ability.
     * @param ability the ability to register
     */
    public void registerAbility(Ability ability) {
        abilities.put(ability.getName().toLowerCase(), ability);
        plugin.getLogger().info("Registered ability: " + ability.getName());
    }

    /**
     * Gets an ability by its name (case-insensitive).
     * @param name the ability name
     * @return the Ability object, or null if not found
     */
    public Ability getAbility(String name) {
        return abilities.get(name.toLowerCase());
    }

    /**
     * Returns all registered ability names.
     * @return list of ability names
     */
    public List<String> getAllAbilityNames() {
        return new ArrayList<>(abilities.keySet());
    }

    /**
     * Checks if a player owns a specific ability.
     * @param player the player
     * @param abilityName the ability name
     * @return true if owned
     */
    public boolean hasAbility(Player player, String abilityName) {
        PlayerAbilityData data = getOrCreateData(player);
        return data.getOwnedAbilities().contains(abilityName);
    }

    /**
     * Gives an ability to a player (adds to owned abilities and sets default stage to 1).
     * @param player the player
     * @param abilityName the ability name
     */
    public void giveAbility(Player player, String abilityName) {
        PlayerAbilityData data = getOrCreateData(player);
        if (data.getOwnedAbilities().add(abilityName)) {
            data.setCurrentStage(abilityName, 1);
            saveData(player, data);
            player.sendMessage(plugin.getConfigUtils().getMessage("book-absorbed")
                    .replace("%ability%", abilityName));
        }
    }

    /**
     * Gets the current stage of an ability for a player.
     * @param player the player
     * @param abilityName the ability name
     * @return stage number (1..maxStage)
     */
    public int getCurrentStage(Player player, String abilityName) {
        return getOrCreateData(player).getCurrentStage(abilityName);
    }

    /**
     * Sets the current stage of an ability for a player.
     * @param player the player
     * @param abilityName the ability name
     * @param stage new stage (1..maxStage)
     */
    public void setCurrentStage(Player player, String abilityName, int stage) {
        PlayerAbilityData data = getOrCreateData(player);
        data.setCurrentStage(abilityName, stage);
        saveData(player, data);
    }

    /**
     * Cycles to the next stage (1 -> 2 -> 3 -> 1) for a given ability.
     * @param player the player
     * @param abilityName the ability name
     */
    public void cycleStage(Player player, String abilityName) {
        Ability ability = getAbility(abilityName);
        if (ability == null) return;
        int current = getCurrentStage(player, abilityName);
        int next = (current % ability.getMaxStage()) + 1;
        setCurrentStage(player, abilityName, next);
        ability.onStageSwitch(player, current, next);
        // Send message
        String msg = plugin.getConfigUtils().getMessage("stage-switched")
                .replace("%ability%", ability.getDisplayName())
                .replace("%stage%", String.valueOf(next));
        player.sendMessage(msg);
    }

    /**
     * Checks if an ability stage is on cooldown for a player.
     * Cooldowns are stored in the player's PersistentDataContainer.
     * @param player the player
     * @param abilityName the ability name
     * @param stage the stage number
     * @return true if still on cooldown
     */
    public boolean isOnCooldown(Player player, String abilityName, int stage) {
        NamespacedKey key = getCooldownKey(abilityName, stage);
        Long lastUse = player.getPersistentDataContainer().get(key, PersistentDataType.LONG);
        if (lastUse == null) return false;
        int cooldown = getCooldownSeconds(abilityName, stage);
        return (System.currentTimeMillis() - lastUse) < cooldown * 1000L;
    }

    /**
     * Sets the cooldown for an ability stage (marks current time).
     * @param player the player
     * @param abilityName the ability name
     * @param stage the stage number
     */
    public void setCooldown(Player player, String abilityName, int stage) {
        NamespacedKey key = getCooldownKey(abilityName, stage);
        player.getPersistentDataContainer().set(key, PersistentDataType.LONG, System.currentTimeMillis());
    }

    /**
     * Returns the remaining cooldown seconds for a player's ability stage, or 0 if not on cooldown.
     * @param player the player
     * @param abilityName the ability name
     * @param stage the stage number
     * @return seconds remaining (0 if none)
     */
    public int getRemainingCooldown(Player player, String abilityName, int stage) {
        NamespacedKey key = getCooldownKey(abilityName, stage);
        Long lastUse = player.getPersistentDataContainer().get(key, PersistentDataType.LONG);
        if (lastUse == null) return 0;
        int cooldown = getCooldownSeconds(abilityName, stage);
        long elapsed = System.currentTimeMillis() - lastUse;
        long remaining = (cooldown * 1000L) - elapsed;
        return (int) Math.max(0, remaining / 1000);
    }

    /**
     * Gets the cooldown duration (in seconds) for a specific ability stage,
     * respecting config overrides.
     * @param abilityName the ability name
     * @param stage the stage number
     * @return cooldown in seconds
     */
    public int getCooldownSeconds(String abilityName, int stage) {
        Ability ability = getAbility(abilityName);
        if (ability == null) return plugin.getConfig().getInt("plugin.default-cooldown", 30);
        int abilityCooldown = ability.getCooldownSeconds(stage);
        // Allow config override
        String path = "cooldowns." + abilityName + ".stage" + stage;
        if (plugin.getConfig().contains(path)) {
            return plugin.getConfig().getInt(path);
        }
        return abilityCooldown;
    }

    // ------------------- Internal helpers -------------------

    private NamespacedKey getCooldownKey(String abilityName, int stage) {
        return new NamespacedKey(plugin, "cooldown_" + abilityName + "_" + stage);
    }

    /**
     * Gets or creates player data from memory.
     * In a full implementation you would also load from disk here.
     */
    public PlayerAbilityData getOrCreateData(Player player) {
        return playerData.computeIfAbsent(player.getUniqueId(),
                id -> new PlayerAbilityData(id));
    }

    private void saveData(Player player, PlayerAbilityData data) {
        playerData.put(player.getUniqueId(), data);
        // Optional: persist to YAML/JSON file
        // plugin.getDataStorage().savePlayerData(data);
    }

    /**
     * Reloads config values (useful for /diablo reload).
     */
    public void reload() {
        // Nothing to reload here except maybe cooldown overrides are read from config on the fly
        plugin.getLogger().info("AbilityManager reloaded.");
    }
}
