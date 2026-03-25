package me.roustytousty.elytrapvp.services.combat

import org.bukkit.entity.Player

data class CombatData(
    val lastAttacker: Player?,
    var lastHitTime: Long
)