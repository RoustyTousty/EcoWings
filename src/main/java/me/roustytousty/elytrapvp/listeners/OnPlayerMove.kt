package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.Services
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class OnPlayerMove : Listener {

    private val eventService = Services.eventService
    private val combatService = Services.combatService
    private val regionService = Services.regionService

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val loc = player.location

        if (player.isDead) return
        if (!player.isOnline) return
        if (player.health <= 0) return

        if (loc.y <= 85 && !eventService.isEventActive("Voidless")) {
            val isInPVPRegion = regionService.isInRegion(loc, "pvpRegion")
            if (!isInPVPRegion) {
                player.health = 0.0
            }
        }

        val from = event.from
        val to = event.to

        val enteringEntrance = !regionService.isInRegion(from, "spawnEntrance") && regionService.isInRegion(to, "spawnEntrance")

        if (combatService.isInCombat(player) && enteringEntrance) {
            event.isCancelled = true
        }
    }
}