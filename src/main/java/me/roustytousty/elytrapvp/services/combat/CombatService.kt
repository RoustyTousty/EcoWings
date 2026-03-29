package me.roustytousty.elytrapvp.services.combat

import me.roustytousty.elytrapvp.services.region.RegionService
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class CombatService(
    private val regionService: RegionService,
    private val plugin: JavaPlugin
) {

    private val combatMap = mutableMapOf<UUID, CombatData>()
    private val protectionMap = mutableMapOf<UUID, Long>()
    private val combatDuration = 8_000L

    init {
        CombatWallTask(
            regionService = regionService,
            combatService = this,
            plugin = plugin
        ).start()
    }

    fun tag(victim: Player, attacker: Player?) {
        val now = System.currentTimeMillis()

        combatMap[victim.uniqueId] = CombatData(
            lastAttacker = attacker,
            lastHitTime = now
        )

        if (attacker != null) {
            val previous = combatMap[attacker.uniqueId]

            combatMap[attacker.uniqueId] = CombatData(
                lastAttacker = previous?.lastAttacker,
                lastHitTime = now
            )
        }
    }

    fun isInCombat(player: Player): Boolean {
        val data = combatMap[player.uniqueId] ?: return false
        return System.currentTimeMillis() - data.lastHitTime < combatDuration
    }

    fun getLastAttacker(player: Player): Player? {
        val data = combatMap[player.uniqueId] ?: return null
        return if (isInCombat(player)) data.lastAttacker else null
    }

    fun getRemainingTime(player: Player): Long {
        val data = combatMap[player.uniqueId] ?: return 0
        val remaining = combatDuration - (System.currentTimeMillis() - data.lastHitTime)
        return remaining.coerceAtLeast(0)
    }

    fun clear(player: Player) {
        combatMap.remove(player.uniqueId)
        protectionMap.remove(player.uniqueId)
    }

    fun applyRespawnProtection(player: Player) {
        protectionMap[player.uniqueId] = System.currentTimeMillis() + 20_000L
    }

    fun hasRespawnProtection(player: Player): Boolean {
        val expiry = protectionMap[player.uniqueId] ?: return false
        if (System.currentTimeMillis() > expiry) {
            protectionMap.remove(player.uniqueId)
            return false
        }
        return true
    }
}