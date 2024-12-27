package me.roustytousty.elytrapvp.services.event

import org.bukkit.Material

interface EventIntefrace {
    val name: String
    val description: String
    val displayMaterial: Material
    val cost: Int
    val duration: Int
    var isActive: Boolean
    var contributions: Int

    fun activate()
    fun deactivate()
}