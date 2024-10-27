package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.gui.events.WarTableMenu
import me.roustytousty.elytrapvp.utility.RegionUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent


class OnWarTableClick : Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {

        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            val block = event.clickedBlock!!
            val location = block.location
            val player = event.player

            if (RegionUtils.isLocationInRegion(location, "warTableRegion")) {
                WarTableMenu.openInventory(player)
            }
        }
    }
}