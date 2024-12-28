package me.roustytousty.elytrapvp.services.event

import org.bukkit.Material
import org.bukkit.scheduler.BukkitTask

interface EventIntefrace {
    val name: String
    val description: String
    val displayMaterial: Material
    val cost: Int
    var contributions: Int
    val duration: Int
    var endTime: Long?
    var task: BukkitTask?
    var isActive: Boolean

    fun activate()
    fun deactivate()
}