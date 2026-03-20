package me.roustytousty.elytrapvp.services.combat

import org.bukkit.entity.Player
import java.util.*

class CombatService {

    private val combatMap = mutableMapOf<UUID, CombatData>()
    private val combatDuration = 5_000L

    fun tag(victim: Player, attacker: Player) {
        combatMap[victim.uniqueId] = CombatData(
            lastAttacker = attacker,
            lastHitTime = System.currentTimeMillis()
        )
    }

    fun isInCombat(player: Player): Boolean {
        val data = combatMap[player.uniqueId] ?: return false
        return System.currentTimeMillis() - data.lastHitTime < combatDuration
    }

    fun getLastAttacker(player: Player): Player? {
        val data = combatMap[player.uniqueId] ?: return null
        return if (isInCombat(player)) data.lastAttacker else null
    }

    fun clear(player: Player) {
        combatMap.remove(player.uniqueId)
    }
}