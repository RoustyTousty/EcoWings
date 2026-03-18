package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.Services
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class OnPlayerDrop : Listener {

    private val kitService = Services.kitService

    @EventHandler
    fun onPlayerDrop(event: PlayerDropItemEvent) {
        if (kitService.isKitItem(event.itemDrop.itemStack)) {
            event.isCancelled = true
        }
    }
}