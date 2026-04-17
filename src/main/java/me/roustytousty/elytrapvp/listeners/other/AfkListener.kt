package me.roustytousty.elytrapvp.listeners.other

import me.roustytousty.elytrapvp.services.Services
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class AfkListener : Listener {

    private val afkService = Services.afkService

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val from = event.from
        val to = event.to

        if (from.blockX == to.blockX && from.blockY == to.blockY && from.blockZ == to.blockZ) {
            return
        }

        val wasInRegion = Services.regionService.isInRegion(from, afkService.afkRegionName)
        val isInRegion = Services.regionService.isInRegion(to, afkService.afkRegionName)

        if (!wasInRegion && isInRegion) {
            afkService.startAfk(player)
            return
        }

        if (wasInRegion && !isInRegion) {
            afkService.stopAfk(player)
            return
        }

        if (afkService.isManuallyAfk(player) && !isInRegion) {
            afkService.stopAfk(player)
        }
    }
}