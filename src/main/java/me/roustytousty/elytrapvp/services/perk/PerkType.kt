package me.roustytousty.elytrapvp.services.perk

import org.bukkit.Material

enum class PerkType(
    val id: String,
    val displayName: String,
    val goldCost: Int,
    val shardCost: Int,
    val requiresEco: Boolean,
    val icon: Material,
    val description: List<String>
) {
    VAMPIRE("vampire", "Vampire", 1500, 0, false, Material.REDSTONE, listOf(
        "&7Heal &c1 heart &7on kill."
    )),
    SCAVENGER("scavenger", "Scavenger", 1000, 0, false, Material.GOLD_INGOT, listOf(
        "&7Earn &6+5 extra gold &7on kill."
    )),

    TANK("tank", "Tank", 0, 50, false, Material.IRON_CHESTPLATE, listOf(
        "&7Take &c10% less &7damage."
    )),

    JUGGERNAUT("juggernaut", "Juggernaut", 0, 100, true, Material.DIAMOND_CHESTPLATE, listOf(
        "&7Gain permanent Resistance I."
    ));

    companion object {
        fun fromId(id: String): PerkType? {
            return values().find { it.id == id }
        }

        fun getSortedPerks(): List<PerkType> {
            return values().sortedWith(compareBy(
                { it.requiresEco },
                { it.shardCost > 0 },
                { it.goldCost },
                { it.shardCost }
            ))
        }
    }
}