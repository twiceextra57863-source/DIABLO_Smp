package com.diablosmp.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

public enum AbilityType {
    
    SOUL_REAPER(
        "Soul Reaper",
        ChatColor.DARK_PURPLE,
        Color.PURPLE,
        Material.NETHER_STAR,
        "§5✧ Soul Reaper Ability ✧",
        "§7» Stage 1: §dSoul Swap - Exchange souls with target",
        "§7» Stage 2: §dGravity Link - Attach target to cursor",
        "§7» Stage 3: §dSoul Storm - Area damage & position swap"
    ),
    
    // More abilities will be added here
    PHANTOM_STRIKE("Phantom Strike", ChatColor.DARK_AQUA, Color.AQUA, Material.DIAMOND_SWORD, "", ""),
    INFERNO_BLAST("Inferno Blast", ChatColor.RED, Color.RED, Material.BLAZE_POWDER, "", ""),
    VOID_WALKER("Void Walker", ChatColor.DARK_GRAY, Color.BLACK, Material.ENDER_PEARL, "", ""),
    STORM_CALLER("Storm Caller", ChatColor.YELLOW, Color.YELLOW, Material.LIGHTNING_ROD, "", ""),
    EARTH_SHATTER("Earth Shatter", ChatColor.GREEN, Color.GREEN, Material.EMERALD, "", ""),
    FROST_BITE("Frost Bite", ChatColor.AQUA, Color.AQUA, Material.ICE, "", ""),
    BLOOD_MAGE("Blood Mage", ChatColor.DARK_RED, Color.RED, Material.REDSTONE, "", ""),
    SHADOW_STEP("Shadow Step", ChatColor.BLACK, Color.BLACK, Material.ENDER_EYE, "", ""),
    DIVINE_SHIELD("Divine Shield", ChatColor.GOLD, Color.YELLOW, Material.GOLDEN_APPLE, "", ""),
    NATURE_WRATH("Nature's Wrath", ChatColor.DARK_GREEN, Color.GREEN, Material.OAK_SAPLING, "", ""),
    TIME_WARP("Time Warp", ChatColor.LIGHT_PURPLE, Color.PURPLE, Material.CLOCK, "", ""),
    METEOR_SHOWER("Meteor Shower", ChatColor.GOLD, Color.ORANGE, Material.FIRE_CHARGE, "", ""),
    ILLUSION_MASTER("Illusion Master", ChatColor.BLUE, Color.BLUE, Material.ENDER_EYE, "", ""),
    CHAOS_BRINGER("Chaos Bringer", ChatColor.DARK_PURPLE, Color.PURPLE, Material.WITHER_SKELETON_SKULL, "", "");
    
    private final String displayName;
    private final ChatColor chatColor;
    private final Color particleColor;
    private final Material iconMaterial;
    private final String[] description;
    
    AbilityType(String displayName, ChatColor chatColor, Color particleColor, Material iconMaterial, String... description) {
        this.displayName = displayName;
        this.chatColor = chatColor;
        this.particleColor = particleColor;
        this.iconMaterial = iconMaterial;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public ChatColor getChatColor() {
        return chatColor;
    }
    
    public Color getParticleColor() {
        return particleColor;
    }
    
    public Material getIconMaterial() {
        return iconMaterial;
    }
    
    public String[] getDescription() {
        return description;
    }
}
