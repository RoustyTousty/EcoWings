package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.ElytraPVP
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent

class OnInventoryClose : Listener {

    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        val p = e.player as Player
        p.updateInventory()
        Bukkit.getScheduler().runTaskLater(ElytraPVP.instance!!, Runnable {
            p.updateInventory()
        }, 1L)
    }
}