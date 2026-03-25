package me.roustytousty.elytrapvp.services.shop

import org.bukkit.Material

data class ShopItem(
    val shopType: String,
    val slot: Int,
    val material: Material,
    val amount: Int,
    val cost: Int,
    val displayName: String,
    val description: List<String>
)