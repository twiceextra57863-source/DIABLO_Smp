package com.diablosmp.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

public enum AbilityType {
    
    SOUL_REAPER(
        "Soul Reaper",
        "§5⚔ §dᔕOᑌᒪ ᖇEᗩᑭEᖇ §5⚔",
        ChatColor.DARK_PURPLE,
        Color.PURPLE,
        Material.NETHER_STAR,
        new String[]{
            "§8◈ §7──────────────── §8◈",
            "§5⬡ §dᑭᖇIᗰOᖇᗪIᗩᒪ EᔕᔕEᑎᑕE §5⬡",
            "§8◈ §7──────────────── §8◈",
            "§7» §5ᔕTᗩGE I: §dᔕOᑌᒪ ᔕᗯᗩᑭ",
            "§8├ §7E᙭ᑕᕼᗩᑎGE YOᑌᖇ ᔕOᑌᒪ ᗯITᕼ TᕼE TᗩᖇGET",
            "§8├ §7ᑕOᑎTᖇOᒪ TᕼEIᖇ ᐯEᔕᔕEᒪ ᖴOᖇ 30 ᔕEᑕOᑎᗪᔕ",
            "§8└ §7YOᑌᖇ ᗷOᗪY ᗷEᑕOᗰEᔕ IᗰᑭEᖇᐯIOᑌᔕ",
            "§8◈ §7──────────────── §8◈",
            "§7» §5ᔕTᗩGE II: §dGᖇᗩᐯITY ᒪIᑎK",
            "§8├ §7ᗩTTᗩᑕᕼ TᗩᖇGET TO YOᑌᖇ ᑕᑌᖇᔕOᖇ",
            "§8├ §7ᑕOᑎTᖇOᒪ TᕼEIᖇ ᗰOᐯEᗰEᑎT ᗯITᕼ YOᑌᖇ GᗩᘔE",
            "§8└ §7ᒪᗩᑌᑎᑕᕼ TᕼEᗰ ᒪIKE ᗩ ᑕᗩᑎᑎOᑎ",
            "§8◈ §7──────────────── §8◈",
            "§7» §5ᔕTᗩGE III: §dᔕOᑌᒪ ᔕTOᖇᗰ",
            "§8├ §7ᑌᑎᒪEᗩᔕᕼ ᗩ ᗪEᐯᗩᔕTᗩTIᑎG ᔕOᑌᒪ ᔕTOᖇᗰ",
            "§8├ §7ᗪᗩᗰᗩGE ᗩᒪᒪ EᑎEᗰIEᔕ Iᑎ 80 ᗷᒪOᑕKᔕ",
            "§8└ §7ᔕᗯᗩᑭ ᑭOᔕITIOᑎᔕ ᗯITᕼ YOᑌᖇ TᗩᖇGET",
            "§8◈ §7──────────────── §8◈",
            "§5⬡ §8[§7ᗪOᑌᗷᒪE ᑕᖇOᑌᑕᕼ TO ᔕᗯITᑕᕼ ᔕTᗩGEᔕ§8] §5⬡"
        }
    ),
    
    VOID_WALKER(
        "Void Walker",
        "§8⬡ §7ᐯOIᗪ ᗯᗩᒪKEᖇ §8⬡",
        ChatColor.DARK_GRAY,
        Color.BLACK,
        Material.ENDER_EYE,
        new String[]{
            "§8◈ §7──────────────── §8◈",
            "§8⬡ §7ᗩᗷYᔕᔕᗩᒪ EᔕᔕEᑎᑕE §8⬡",
            "§8◈ §7──────────────── §8◈",
            "§7» §8ᔕTᗩGE I: §7ᐯOIᗪ ᔕTEᑭ",
            "§8└ §7TEᒪEᑭOᖇT TᕼᖇOᑌGᕼ TᕼE ᐯOIᗪ",
            "§7» §8ᔕTᗩGE II: §7ᗪIᗰEᑎᔕIOᑎᗩᒪ ᖇIᖴT",
            "§8└ §7ᑕᖇEᗩTE ᖇIᖴTᔕ TO TᖇᗩᐯEᖇᔕE ᖇEᗩᒪITY",
            "§7» §8ᔕTᗩGE III: §7ᐯOIᗪ ᑕOᒪᒪᗩᑭᔕE",
            "§8└ §7ᑕOᒪᒪᗩᑭᔕE ᖇEᗩᒪITY ᗩᖇOᑌᑎᗪ YOᑌ",
            "§8◈ §7──────────────── §8◈"
        }
    ),
    
    BLOOD_MAGE(
        "Blood Mage",
        "§4⬡ §cᗷᒪOOᗪ ᗰᗩGE §4⬡",
        ChatColor.DARK_RED,
        Color.RED,
        Material.REDSTONE,
        new String[]{
            "§8◈ §7──────────────── §8◈",
            "§4⬡ §cᔕᗩᑎGᑌIᑎE EᔕᔕEᑎᑕE §4⬡",
            "§8◈ §7──────────────── §8◈",
            "§7» §4ᔕTᗩGE I: §cᗷᒪOOᗪ ᔕIᑭᕼOᑎ",
            "§8└ §7ᗪᖇᗩIᑎ ᒪIᖴE ᖴᖇOᗰ YOᑌᖇ EᑎEᗰIEᔕ",
            "§7» §4ᔕTᗩGE II: §cᕼEᗰOᖇᖇᕼᗩGE",
            "§8└ §7ᔕᗩᑕᖇIᖴIᑕE ᕼEᗩᒪTᕼ ᖴOᖇ IᗰᗰEᑎᔕE ᑭOᗯEᖇ",
            "§7» §4ᔕTᗩGE III: §cᗷᒪOOᗪ ᖇITᑌᗩᒪ",
            "§8└ §7ᑌᑎᒪEᗩᔕᕼ ᗩ ᗪEᐯᗩᔕTᗩTIᑎG ᗷᒪOOᗪ ᖇITᑌᗩᒪ",
            "§8◈ §7──────────────── §8◈"
        }
    );
    
    private final String simpleName;
    private final String displayName;
    private final ChatColor chatColor;
    private final Color particleColor;
    private final Material iconMaterial;
    private final String[] description;
    
    AbilityType(String simpleName, String displayName, ChatColor chatColor, Color particleColor, Material iconMaterial, String[] description) {
        this.simpleName = simpleName;
        this.displayName = displayName;
        this.chatColor = chatColor;
        this.particleColor = particleColor;
        this.iconMaterial = iconMaterial;
        this.description = description;
    }
    
    public String getSimpleName() { return simpleName; }
    public String getDisplayName() { return displayName; }
    public ChatColor getChatColor() { return chatColor; }
    public Color getParticleColor() { return particleColor; }
    public Material getIconMaterial() { return iconMaterial; }
    public String[] getDescription() { return description; }
}
