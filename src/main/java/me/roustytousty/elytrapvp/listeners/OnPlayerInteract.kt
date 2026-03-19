package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.RegionUtils
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class OnPlayerInteract : Listener {

    @EventHandler
    fun onPlayerRightClick(event: PlayerInteractEvent) {
        val player = event.player

        val playerData = Services.playerService.getOrCreatePlayerData(player)

        val buildmode = playerData.isBuildMode
        if (buildmode) {
            return
        }

        val blockLocation = event.clickedBlock?.location!!
        val isInBuildRegion = RegionUtils.isLocationInRegion(blockLocation, "buildRegion")
        val isInBuildBufferRegion = RegionUtils.isLocationInRegion(blockLocation, "buildBufferRegion")
        val isInSecretEntrance = RegionUtils.isLocationInRegion(blockLocation, "secretEntrance")

        if (isInSecretEntrance) {
            return
        }

        if (!isInBuildRegion || isInBuildBufferRegion) {
            event.isCancelled = true
            return
        }
    }
}