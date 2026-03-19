package me.roustytousty.elytrapvp.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class OnPlayerInteract : Listener {

    @EventHandler
    fun onPlayerRightClick(event: PlayerInteractEvent) {
        event.isCancelled = true
    }
}