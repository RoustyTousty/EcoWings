package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.Services
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class OnPlayerDamage : Listener {

    private val combatService = Services.combatService
    private val regionService = Services.regionService

    @EventHandler
    fun onPlayerDamage(event: EntityDamageByEntityEvent) {
        val victim = event.entity as? Player ?: return

        val attacker = when (val damager = event.damager) {
            is Player -> damager
            is Projectile -> damager.shooter as? Player
            else -> null
        } ?: return

        val attackerInSpawn = regionService.isInRegion(attacker.location, "spawnRegion")
        val victimInSpawn = regionService.isInRegion(victim.location, "spawnRegion")

        if (attackerInSpawn || victimInSpawn) {
            event.isCancelled = true
            return
        }

        combatService.tag(victim, attacker)
    }
}