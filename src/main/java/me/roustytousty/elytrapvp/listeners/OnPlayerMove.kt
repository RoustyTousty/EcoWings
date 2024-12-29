package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.utility.RegionUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class OnPlayerMove : Listener {

    private val eventService = ElytraPVP.instance!!.getEventService()

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val playerLocation = player.location

        if (playerLocation.y > 85 || eventService.isEventActive("Voidless")) {
            return
        }

        val isInPVPRegion = RegionUtils.isLocationInRegion(playerLocation, "pvpRegion")
        if (!isInPVPRegion) {
            player.damage(40.0)
        }
    }
}