package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.RegionUtils
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class OnPlayerInteract : Listener {
    private val blockedInteractables = setOf(
        Material.CHEST,
        Material.TRAPPED_CHEST,
        Material.BARREL,
        Material.FURNACE,
        Material.BLAST_FURNACE,
        Material.SMOKER,
        Material.HOPPER,
        Material.DROPPER,
        Material.DISPENSER,

        Material.SPRUCE_DOOR,
        Material.SPRUCE_TRAPDOOR,
        Material.SPRUCE_FENCE_GATE,
        Material.SPRUCE_SIGN,
        Material.SPRUCE_BUTTON,
        Material.LEVER
    )

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val playerData = Services.playerService.getOrCreatePlayerData(player)

        if (playerData.isBuildMode) return

        val block = event.clickedBlock ?: return
        val location = block.location

        if (RegionUtils.isLocationInRegion(location, "secretEntrance")) return

        if (event.action != Action.RIGHT_CLICK_BLOCK) return

        if (block.type.isInteractable) {
            event.isCancelled = true
        }
    }
}