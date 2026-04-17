package me.roustytousty.elytrapvp.services.cosmetic

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.inventory.meta.trim.TrimPattern

enum class CosmeticPattern(
    override val id: String,
    override val displayName: String,
    override val goldCost: Int,
    override val shardCost: Int,
    override val requiresEco: Boolean,
    override val icon: Material,
    val patternKey: NamespacedKey
) : ICosmetic {
    SENTRY("sentry", "Sentry Pattern", 1500, 0, false, Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("sentry")),
    VEX("vex", "Vex Pattern", 1000, 0, false, Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("vex")),
    WILD("wild", "Wild Pattern", 5500, 0, false, Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("wild")),
    COAST("coast", "Coast Pattern", 8500, 0, false, Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("coast")),
    DUNE("dune", "Dune Pattern", 0, 12, true, Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("dune")),
    WAYFINDER("wayfinder", "Wayfinder Pattern", 9500, 0, false, Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("wayfinder")),
    RAISER("raiser", "Raiser Pattern", 0, 17, false, Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("raiser")),
    SHAPER("shaper", "Shaper Pattern", 0, 4, false, Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("shaper")),
    HOST("host", "Host Pattern", 12000, 0, false, Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("host")),
    WARD("ward", "Ward Pattern", 3000, 0, false, Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("ward")),
    SILENCE("silence", "Silence Pattern", 0, 25, true, Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("silence")),
    SNOUT("snout", "Snout Pattern", 5500, 0, true, Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("snout")),
    RIB("rib", "Rib Pattern", 3500, 0, false, Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("rib")),
    EYE("eye", "Eye Pattern", 0, 12, false, Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("eye")),
    SPIRE("spire", "Spire Pattern", 0, 8, false, Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("spire")),
    TIDE("tide", "Tide Pattern", 0, 6, true, Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("tide")),
    BOLT("bolt", "Bolt Pattern", 11000, 0, false, Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("bolt")),
    FLOW("flow", "Flow Pattern", 15000, 0, true, Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, NamespacedKey.minecraft("flow"));

    fun getBukkitPattern(): TrimPattern? = Registry.TRIM_PATTERN.get(patternKey)

    companion object {
        fun fromId(id: String): CosmeticPattern? = values().find { it.id == id }
        fun getSortedPatterns(): List<CosmeticPattern> = values().sortedWith(compareBy({ it.requiresEco }, { it.shardCost > 0 }, { it.goldCost }, { it.shardCost }))
    }
}