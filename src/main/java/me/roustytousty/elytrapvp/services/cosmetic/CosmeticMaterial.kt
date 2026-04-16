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
    IRON("iron", "Iron", 10, 0, false, Material.IRON_INGOT, NamespacedKey.minecraft("iron")),
    GOLD("gold", "Gold", 10, 0, false, Material.GOLD_INGOT, NamespacedKey.minecraft("gold")),
    NETHERITE("netherite", "Netherite", 10, 0, false, Material.NETHERITE_INGOT, NamespacedKey.minecraft("netherite")),
    DIAMOND("diamond", "Diamond", 10, 0, false, Material.DIAMOND, NamespacedKey.minecraft("diamond")),
    EMERALD("emerald", "Emerald", 10, 0, false, Material.EMERALD, NamespacedKey.minecraft("emerald")),
    LAPIS("lapis", "Lapis", 10, 0, false, Material.LAPIS_LAZULI, NamespacedKey.minecraft("lapis")),
    REDSTONE("redstone", "Redstone", 10, 0, false, Material.REDSTONE, NamespacedKey.minecraft("redstone")),
    AMETHYST("amethyst", "Amethyst", 10, 0, false, Material.AMETHYST_SHARD, NamespacedKey.minecraft("amethyst")),
    QUARTZ("quartz", "Quartz", 10, 0, false, Material.QUARTZ, NamespacedKey.minecraft("quartz")),
    COPPER("copper", "Copper", 10, 0, false, Material.COPPER_INGOT, NamespacedKey.minecraft("copper"));

    fun getBukkitMaterial(): TrimMaterial? = Registry.TRIM_MATERIAL.get(materialKey)

    companion object {
        fun fromId(id: String): CosmeticMaterial? = values().find { it.id == id }
        fun getSortedMaterials(): List<CosmeticMaterial> = values().sortedWith(compareBy({ it.requiresEco }, { it.shardCost > 0 }, { it.goldCost }, { it.shardCost }))
    }
}