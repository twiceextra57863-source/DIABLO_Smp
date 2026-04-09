package com.diablo.smp.manager;

import com.diablo.smp.DiabloSMP;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class TrustManager {

    private final DiabloSMP plugin;
    private final Map<UUID, Set<UUID>> trustedPlayers;

    public TrustManager(DiabloSMP plugin) {
        this.plugin = plugin;
        this.trustedPlayers = new HashMap<>();
        loadTrusts();
    }

    public void addTrust(Player owner, Player target) {
        trustedPlayers.computeIfAbsent(owner.getUniqueId(), k -> new HashSet<>()).add(target.getUniqueId());
        saveTrusts();
    }

    public void removeTrust(Player owner, Player target) {
        if (trustedPlayers.containsKey(owner.getUniqueId())) {
            trustedPlayers.get(owner.getUniqueId()).remove(target.getUniqueId());
            saveTrusts();
        }
    }

    public boolean isTrusted(UUID owner, UUID target) {
        return trustedPlayers.containsKey(owner) && trustedPlayers.get(owner).contains(target);
    }

    public Set<UUID> getTrusted(Player owner) {
        return trustedPlayers.getOrDefault(owner.getUniqueId(), Collections.emptySet());
    }

    private void loadTrusts() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("trusts");
        if (section == null) return;

        for (String ownerIdStr : section.getKeys(false)) {
            UUID ownerId = UUID.fromString(ownerIdStr);
            List<String> trustedIds = section.getStringList(ownerIdStr);
            Set<UUID> trusts = new HashSet<>();
            for (String tid : trustedIds) {
                trusts.add(UUID.fromString(tid));
            }
            trustedPlayers.put(ownerId, trusts);
        }
    }

    public void saveTrusts() {
        plugin.getConfig().set("trusts", null); // clear old
        for (Map.Entry<UUID, Set<UUID>> entry : trustedPlayers.entrySet()) {
            List<String> list = new ArrayList<>();
            for (UUID tid : entry.getValue()) {
                list.add(tid.toString());
            }
            plugin.getConfig().set("trusts." + entry.getKey().toString(), list);
        }
        plugin.saveConfig();
    }
}
