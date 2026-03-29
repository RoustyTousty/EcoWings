package me.roustytousty.elytrapvp.services.upgrade

import me.roustytousty.elytrapvp.data.model.PlayerData
import org.bukkit.Material

enum class UpgradeType(
    val configKey: String,
    val material: Material,
    val displayName: String,
    val slot: Int,
    val statsSlot: Int,
    val levelGetter: (PlayerData) -> Int,
    val levelSetter: (PlayerData, Int) -> Unit
) {

    HELMET(
        "helmet", Material.LEATHER_HELMET, "Helmet", 12, 11,
        { it.helmetLevel }, { data, value -> data.helmetLevel = value }
    ),

    ELYTRA(
        "elytra", Material.ELYTRA, "Elytra", 21, 20,
        { it.elytraLevel }, { data, value -> data.elytraLevel = value }
    ),

    LEGGINGS(
        "leggings", Material.LEATHER_LEGGINGS, "Leggings", 30, 29,
        { it.leggingsLevel }, { data, value -> data.leggingsLevel = value }
    ),

    BOOTS(
        "boots", Material.LEATHER_BOOTS, "Boots", 39, 38,
        { it.bootsLevel }, { data, value -> data.bootsLevel = value }
    ),

    SWORD(
        "sword", Material.WOODEN_SWORD, "Sword", 14, 12,
        { it.swordLevel }, { data, value -> data.swordLevel = value }
    ),

    SHEARS(
        "shears", Material.SHEARS, "Shears", 23, 21,
        { it.shearsLevel }, { data, value -> data.shearsLevel = value }
    ),

    PICKAXE(
        "pickaxe", Material.WOODEN_PICKAXE, "Pickaxe", 32, 30,
        { it.pickaxeLevel }, { data, value -> data.pickaxeLevel = value }
    ),

    AXE(
        "axe", Material.WOODEN_AXE, "Axe", 41, 39,
        { it.axeLevel }, { data, value -> data.axeLevel = value }
    );

    fun getLevel(data: PlayerData): Int {
        return levelGetter.invoke(data)
    }

    fun setLevel(data: PlayerData, level: Int) {
        levelSetter.invoke(data, level)
    }
}