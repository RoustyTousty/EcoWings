package me.roustytousty.elytrapvp.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class OnPlayerDrop : Listener {

    @EventHandler
    fun onPlayerDrop(event: PlayerDropItemEvent) {
        event.isCancelled = true
    }
}