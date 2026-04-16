package me.roustytousty.elytrapvp.listeners.interactables

import me.roustytousty.elytrapvp.gui.cosmetics.CosmeticMenu
import me.roustytousty.elytrapvp.services.Services
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class OnCosmeticBlockClick : Listener {

    private val regionService = Services.regionService

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return

        if (block.type == Material.RESPAWN_ANCHOR) {
            event.isCancelled = true
        }

        if (!regionService.isInRegion(block.location, "cosmeticBlockRegion")) return

        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            val player = event.player
            CosmeticMenu.openInventory(player)
        }
    }
}