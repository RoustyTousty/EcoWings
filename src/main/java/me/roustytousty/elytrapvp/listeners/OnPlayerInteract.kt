package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class OnPlayerInteract : Listener {

    private val playerService = Services.playerService
    private val regionService = Services.regionService

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val playerData = playerService.getOrCreatePlayerData(player)

        if (playerData.isBuildMode) return

        val block = event.clickedBlock ?: return
        val location = block.location

//        if (regionService.isInRegion(location, "secretEntrance")) return

        if (event.action != Action.RIGHT_CLICK_BLOCK) return

        if (block.type.isInteractable && block.type != Material.TNT) {
            event.isCancelled = true
        }
    }
}