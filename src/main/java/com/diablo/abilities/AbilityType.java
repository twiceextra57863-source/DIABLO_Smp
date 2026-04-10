package com.diablosmp.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

public enum AbilityType {
    
    SOUL_REAPER(
        "SoulReaper",
        "§5§l⚔ §d§lSOUL REAPER §5§l⚔",
        ChatColor.DARK_PURPLE,
        Color.PURPLE,
        Material.NETHER_STAR,
        new String[]{
            "§8▸ §7§m------------------------§8 ◂",
            "§5§l✦ §dPRIMORDIAL ESSENCE §5§l✦",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §5Stage I: §dSoul Swap",
            "§8  §7Exchange your soul with the target",
            "§8  §7Control their vessel for 30 seconds",
            "§8  §7Your body becomes impervious",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §5Stage II: §dGravity Link",
            "§8  §7Attach target to your cursor",
            "§8  §7Control their movement with your gaze",
            "§8  §7Launch them like a cannon",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §5Stage III: §dSoul Storm",
            "§8  §7Unleash a devastating soul storm",
            "§8  §7Damage all enemies in 80 blocks",
            "§8  §7Swap positions with your target",
            "§8▸ §7§m------------------------§8 ◂",
            "§5§l✦ §dDouble Crouch to Switch Stages §5§l✦"
        }
    ),
    
    VOID_WALKER(
        "VoidWalker",
        "§8§l⬡ §7§lVOID WALKER §8§l⬡",
        ChatColor.DARK_GRAY,
        Color.BLACK,
        Material.ENDER_EYE,
        new String[]{
            "§8▸ §7§m------------------------§8 ◂",
            "§8§l✦ §7ABYSSAL ESSENCE §8§l✦",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §8Stage I: §7Void Step",
            "§8  §7Teleport through the void",
            "§7▶ §8Stage II: §7Dimensional Rift",
            "§8  §7Create rifts to traverse reality",
            "§7▶ §8Stage III: §7Void Collapse",
            "§8  §7Collapse reality around you",
            "§8▸ §7§m------------------------§8 ◂"
        }
    ),
    
    BLOOD_MAGE(
        "BloodMage",
        "§4§l⬡ §c§lBLOOD MAGE §4§l⬡",
        ChatColor.DARK_RED,
        Color.RED,
        Material.REDSTONE,
        new String[]{
            "§8▸ §7§m------------------------§8 ◂",
            "§4§l✦ §cSANGUINE ESSENCE §4§l✦",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §4Stage I: §cBlood Siphon",
            "§8  §7Drain life from your enemies",
            "§7▶ §4Stage II: §cHemorrhage",
            "§8  §7Sacrifice health for immense power",
            "§7▶ §4Stage III: §cBlood Ritual",
            "§8  §7Unleash a devastating blood ritual",
            "§8▸ §7§m------------------------§8 ◂"
        }
    ),
    
    PHANTOM_STRIKE(
        "PhantomStrike",
        "§b§l⚡ §3§lPHANTOM STRIKE §b§l⚡",
        ChatColor.AQUA,
        Color.AQUA,
        Material.DIAMOND_SWORD,
        new String[]{
            "§8▸ §7§m------------------------§8 ◂",
            "§b§l✦ §3ETHEREAL ESSENCE §b§l✦",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §bStage I: §3Phantom Dash",
            "§8  §7Dash through enemies unseen",
            "§7▶ §bStage II: §3Spectral Blade",
            "§8  §7Summon spectral blades",
            "§7▶ §bStage III: §3Phantom Army",
            "§8  §7Summon an army of phantoms",
            "§8▸ §7§m------------------------§8 ◂"
        }
    ),
    
    INFERNO_BLAST(
        "InfernoBlast",
        "§c§l🔥 §6§lINFERNO BLAST §c§l🔥",
        ChatColor.RED,
        Color.ORANGE,
        Material.BLAZE_POWDER,
        new String[]{
            "§8▸ §7§m------------------------§8 ◂",
            "§c§l✦ §6VOLCANIC ESSENCE §c§l✦",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §cStage I: §6Flame Burst",
            "§8  §7Unleash a burst of flames",
            "§7▶ §cStage II: §6Meteor Strike",
            "§8  §7Call down a meteor",
            "§7▶ §cStage III: §6Inferno Nova",
            "§8  §7Create a massive fire nova",
            "§8▸ §7§m------------------------§8 ◂"
        }
    ),
    
    FROST_BITE(
        "FrostBite",
        "§b§l❄ §f§lFROST BITE §b§l❄",
        ChatColor.AQUA,
        Color.WHITE,
        Material.ICE,
        new String[]{
            "§8▸ §7§m------------------------§8 ◂",
            "§b§l✦ §fGLACIAL ESSENCE §b§l✦",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §bStage I: §fFrost Bolt",
            "§8  §7Launch a freezing bolt",
            "§7▶ §bStage II: §fIce Prison",
            "§8  §7Trap enemies in ice",
            "§7▶ §bStage III: §fBlizzard",
            "§8  §7Summon a devastating blizzard",
            "§8▸ §7§m------------------------§8 ◂"
        }
    ),
    
    EARTH_SHATTER(
        "EarthShatter",
        "§a§l🌍 §2§lEARTH SHATTER §a§l🌍",
        ChatColor.GREEN,
        Color.GREEN,
        Material.EMERALD,
        new String[]{
            "§8▸ §7§m------------------------§8 ◂",
            "§a§l✦ §2TERRAN ESSENCE §a§l✦",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §aStage I: §2Ground Slam",
            "§8  §7Slam the ground with force",
            "§7▶ §aStage II: §2Earth Spike",
            "§8  §7Raise spikes from the earth",
            "§7▶ §aStage III: §2Earthquake",
            "§8  §7Create a massive earthquake",
            "§8▸ §7§m------------------------§8 ◂"
        }
    ),
    
    STORM_CALLER(
        "StormCaller",
        "§e§l⚡ §6§lSTORM CALLER §e§l⚡",
        ChatColor.YELLOW,
        Color.YELLOW,
        Material.LIGHTNING_ROD,
        new String[]{
            "§8▸ §7§m------------------------§8 ◂",
            "§e§l✦ §6TEMPEST ESSENCE §e§l✦",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §eStage I: §6Lightning Bolt",
            "§8  §7Strike enemies with lightning",
            "§7▶ §eStage II: §6Thunder Clap",
            "§8  §7Create a thunderous shockwave",
            "§7▶ §eStage III: §6Storm Fury",
            "§8  §7Unleash nature's fury",
            "§8▸ §7§m------------------------§8 ◂"
        }
    ),
    
    SHADOW_STEP(
        "ShadowStep",
        "§8§l🌑 §7§lSHADOW STEP §8§l🌑",
        ChatColor.DARK_GRAY,
        Color.BLACK,
        Material.ENDER_PEARL,
        new String[]{
            "§8▸ §7§m------------------------§8 ◂",
            "§8§l✦ §7UMBRAL ESSENCE §8§l✦",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §8Stage I: §7Shadow Dash",
            "§8  §7Dash through shadows",
            "§7▶ §8Stage II: §7Shadow Clone",
            "§8  §7Create a shadow clone",
            "§7▶ §8Stage III: §7Shadow Realm",
            "§8  §7Pull enemies into shadows",
            "§8▸ §7§m------------------------§8 ◂"
        }
    ),
    
    DIVINE_SHIELD(
        "DivineShield",
        "§e§l✨ §6§lDIVINE SHIELD §e§l✨",
        ChatColor.GOLD,
        Color.YELLOW,
        Material.GOLDEN_APPLE,
        new String[]{
            "§8▸ §7§m------------------------§8 ◂",
            "§e§l✦ §6CELESTIAL ESSENCE §e§l✦",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §eStage I: §6Holy Barrier",
            "§8  §7Create a protective barrier",
            "§7▶ §eStage II: §6Divine Healing",
            "§8  §7Heal yourself and allies",
            "§7▶ §eStage III: §6Sacred Nova",
            "§8  §7Release divine energy",
            "§8▸ §7§m------------------------§8 ◂"
        }
    ),
    
    NATURE_WRATH(
        "NatureWrath",
        "§a§l🌿 §2§lNATURE'S WRATH §a§l🌿",
        ChatColor.DARK_GREEN,
        Color.GREEN,
        Material.OAK_SAPLING,
        new String[]{
            "§8▸ §7§m------------------------§8 ◂",
            "§a§l✦ §2VERDANT ESSENCE §a§l✦",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §aStage I: §2Thorn Whip",
            "§8  §7Lash enemies with thorns",
            "§7▶ §aStage II: §2Root Entangle",
            "§8  §7Entangle enemies in roots",
            "§7▶ §aStage III: §2Forest Rage",
            "§8  §7Awaken the forest's rage",
            "§8▸ §7§m------------------------§8 ◂"
        }
    ),
    
    TIME_WARP(
        "TimeWarp",
        "§d§l⌛ §5§lTIME WARP §d§l⌛",
        ChatColor.LIGHT_PURPLE,
        Color.PURPLE,
        Material.CLOCK,
        new String[]{
            "§8▸ §7§m------------------------§8 ◂",
            "§d§l✦ §5TEMPORAL ESSENCE §d§l✦",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §dStage I: §5Time Slow",
            "§8  §7Slow down time around you",
            "§7▶ §dStage II: §5Time Freeze",
            "§8  §7Freeze enemies in time",
            "§7▶ §dStage III: §5Time Rewind",
            "§8  §7Rewind your position",
            "§8▸ §7§m------------------------§8 ◂"
        }
    ),
    
    METEOR_SHOWER(
        "MeteorShower",
        "§c§l☄ §6§lMETEOR SHOWER §c§l☄",
        ChatColor.GOLD,
        Color.ORANGE,
        Material.FIRE_CHARGE,
        new String[]{
            "§8▸ §7§m------------------------§8 ◂",
            "§c§l✦ §6COSMIC ESSENCE §c§l✦",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §cStage I: §6Falling Star",
            "§8  §7Call down a falling star",
            "§7▶ §cStage II: §6Meteor Swarm",
            "§8  §7Rain down meteor swarm",
            "§7▶ §cStage III: §6Cosmic Impact",
            "§8  §7Create a cosmic impact",
            "§8▸ §7§m------------------------§8 ◂"
        }
    ),
    
    ILLUSION_MASTER(
        "IllusionMaster",
        "§9§l🌀 §1§lILLUSION MASTER §9§l🌀",
        ChatColor.BLUE,
        Color.BLUE,
        Material.ENDER_EYE,
        new String[]{
            "§8▸ §7§m------------------------§8 ◂",
            "§9§l✦ §1MYSTICAL ESSENCE §9§l✦",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §9Stage I: §1Mirror Image",
            "§8  §7Create mirror images",
            "§7▶ §9Stage II: §1Mind Trick",
            "§8  §7Confuse your enemies",
            "§7▶ §9Stage III: §1Mass Illusion",
            "§8  §7Create mass illusions",
            "§8▸ §7§m------------------------§8 ◂"
        }
    ),
    
    CHAOS_BRINGER(
        "ChaosBringer",
        "§5§l💀 §d§lCHAOS BRINGER §5§l💀",
        ChatColor.DARK_PURPLE,
        Color.PURPLE,
        Material.WITHER_SKELETON_SKULL,
        new String[]{
            "§8▸ §7§m------------------------§8 ◂",
            "§5§l✦ §dCHAOTIC ESSENCE §5§l✦",
            "§8▸ §7§m------------------------§8 ◂",
            "§7▶ §5Stage I: §dChaos Bolt",
            "§8  §7Launch bolts of chaos",
            "§7▶ §5Stage II: §dReality Rift",
            "§8  §7Tear open reality",
            "§7▶ §5Stage III: §dChaos Storm",
            "§8  §7Unleash pure chaos",
            "§8▸ §7§m------------------------§8 ◂"
        }
    );
    
    private final String commandName;
    private final String displayName;
    private final ChatColor chatColor;
    private final Color particleColor;
    private final Material iconMaterial;
    private final String[] description;
    
    AbilityType(String commandName, String displayName, ChatColor chatColor, 
                Color particleColor, Material iconMaterial, String[] description) {
        this.commandName = commandName;
        this.displayName = displayName;
        this.chatColor = chatColor;
        this.particleColor = particleColor;
        this.iconMaterial = iconMaterial;
        this.description = description;
    }
    
    public String getCommandName() { return commandName; }
    public String getDisplayName() { return displayName; }
    public ChatColor getChatColor() { return chatColor; }
    public Color getParticleColor() { return particleColor; }
    public Material getIconMaterial() { return iconMaterial; }
    public String[] getDescription() { return description; }
}
