package me.roustytousty.elytrapvp.services.cosmetic

import org.bukkit.Material

interface ICosmetic {
    val id: String
    val displayName: String
    val goldCost: Int
    val shardCost: Int
    val requiresEco: Boolean
    val icon: Material
}