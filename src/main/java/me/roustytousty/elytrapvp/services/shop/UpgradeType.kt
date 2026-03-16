package me.roustytousty.elytrapvp.services.shop

import me.roustytousty.elytrapvp.data.model.PlayerData
import org.bukkit.Material

enum class UpgradeType(
    val configKey: String,
    val material: Material,
    val displayName: String,
    val slot: Int,
    val levelGetter: (PlayerData) -> Int,
    val levelSetter: (PlayerData, Int) -> Unit
) {

    HELMET(
        "helmet",
        Material.LEATHER_HELMET,
        "&eHelmet",
        11,
        { it.helmetLevel },
        { data, value -> data.helmetLevel = value }
    ),

    ELYTRA(
        "elytra",
        Material.ELYTRA,
        "&eElytra",
        12,
        { it.elytraLevel },
        { data, value -> data.elytraLevel = value }
    ),

    LEGGINGS(
        "leggings",
        Material.LEATHER_LEGGINGS,
        "&eLeggings",
        13,
        { it.leggingsLevel },
        { data, value -> data.leggingsLevel = value }
    ),

    BOOTS(
        "boots",
        Material.LEATHER_BOOTS,
        "&eBoots",
        14,
        { it.bootsLevel },
        { data, value -> data.bootsLevel = value }
    ),

    SWORD(
        "sword",
        Material.WOODEN_SWORD,
        "&eSword",
        15,
        { it.swordLevel },
        { data, value -> data.swordLevel = value }
    ),

    SHEARS(
        "shears",
        Material.SHEARS,
        "&eShears",
        22,
        { it.shearsLevel },
        { data, value -> data.shearsLevel = value }
    );

    fun getLevel(data: PlayerData): Int {
        return levelGetter.invoke(data)
    }

    fun setLevel(data: PlayerData, level: Int) {
        levelSetter.invoke(data, level)
    }
}