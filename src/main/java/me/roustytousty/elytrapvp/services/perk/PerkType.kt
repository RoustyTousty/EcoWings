package me.roustytousty.elytrapvp.services.perk

import org.bukkit.Material

enum class PerkType(
    val id: String,
    val displayName: String,
    val cost: Int,
    val icon: Material,
    val description: List<String>
) {
    VAMPIRE("vampire", "Vampire", 1500, Material.REDSTONE, listOf(
        "&7Heal &c1 heart &7on kill."
    )),
    TANK("tank", "Tank", 2000, Material.IRON_CHESTPLATE, listOf(
        "&7Take &c10% less &7damage."
    )),
    SCAVENGER("scavenger", "Scavenger", 1000, Material.GOLD_INGOT, listOf(
        "&7Earn &6+5 extra gold &7on kill."
    ));

    companion object {
        fun fromId(id: String): PerkType? {
            return values().find { it.id == id }
        }
    }
}