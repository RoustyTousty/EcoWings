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
    RECYCLER("recycler", "Recycler", 4500, 0, false, Material.HOPPER, listOf(
        "&7Breaking a block has a 50% chance", "&7of obtaining it."
    )),
    SCAVENGER("scavenger", "Scavenger", 4000, 0, false, Material.WHITE_WOOL, listOf(
        "&7Placing a block has a 20% chance", "&7of not losing the block from your", "&7inventory."
    )),
    ADRENALINE("adrenaline", "Adrenaline", 1800, 0, false, Material.IRON_BOOTS, listOf(
        "&7Killing a player gives speed 2 for", "&710 seconds"
    )),
    ANCHORED("anchored", "Anchored", 8500, 0, false, Material.ANVIL, listOf(
        "&7Take 10% less knockback."
    )),
    BLAST_DAMPENER("blast_dampener", "Blast Dampener", 700, 0, false, Material.TNT, listOf(
        "&7Take 80% less damage from explosions."
    )),
    KINETIC_SHIELD("kinetic_shield", "Kinetic Shield", 2200, 0, false, Material.SHIELD, listOf(
        "&7Take 80% less kinetic damage."
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