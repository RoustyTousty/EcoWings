package me.roustytousty.elytrapvp.services.cosmetic

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.inventory.meta.trim.TrimMaterial

enum class CosmeticMaterial(
    override val id: String,
    override val displayName: String,
    override val goldCost: Int,
    override val shardCost: Int,
    override val requiresEco: Boolean,
    override val icon: Material,
    val materialKey: NamespacedKey
) : ICosmetic {
    IRON("iron", "Iron", 0, 2, false, Material.IRON_INGOT, NamespacedKey.minecraft("iron")),
    GOLD("gold", "Gold", 0, 15, true, Material.GOLD_INGOT, NamespacedKey.minecraft("gold")),
    NETHERITE("netherite", "Netherite", 500, 0, false, Material.NETHERITE_INGOT, NamespacedKey.minecraft("netherite")),
    DIAMOND("diamond", "Diamond", 0, 6, false, Material.DIAMOND, NamespacedKey.minecraft("diamond")),
    EMERALD("emerald", "Emerald", 0, 5, false, Material.EMERALD, NamespacedKey.minecraft("emerald")),
    LAPIS("lapis", "Lapis", 1200, 0, false, Material.LAPIS_LAZULI, NamespacedKey.minecraft("lapis")),
    REDSTONE("redstone", "Redstone", 1200, 0, false, Material.REDSTONE, NamespacedKey.minecraft("redstone")),
    AMETHYST("amethyst", "Amethyst", 0, 10, true, Material.AMETHYST_SHARD, NamespacedKey.minecraft("amethyst")),
    QUARTZ("quartz", "Quartz", 0, 3, false, Material.QUARTZ, NamespacedKey.minecraft("quartz")),
    COPPER("copper", "Copper", 750, 0, false, Material.COPPER_INGOT, NamespacedKey.minecraft("copper"));

    fun getBukkitMaterial(): TrimMaterial? = Registry.TRIM_MATERIAL.get(materialKey)

    companion object {
        fun fromId(id: String): CosmeticMaterial? = values().find { it.id == id }
        fun getSortedMaterials(): List<CosmeticMaterial> = values().sortedWith(compareBy({ it.requiresEco }, { it.shardCost > 0 }, { it.goldCost }, { it.shardCost }))
    }
}