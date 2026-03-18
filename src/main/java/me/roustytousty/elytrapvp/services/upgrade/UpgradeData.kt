package me.roustytousty.elytrapvp.services.upgrade

import org.bukkit.inventory.ItemStack

data class UpgradeData(
    val item: ItemStack,
    val cost: Int,
    val level: Int,
    val maxed: Boolean
)