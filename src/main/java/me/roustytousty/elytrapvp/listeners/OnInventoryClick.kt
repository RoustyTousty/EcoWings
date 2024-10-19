package me.roustytousty.elytrapvp.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType

class OnInventoryClick : Listener {

    private val armorSlots = listOf(5, 6, 7, 8)

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {

        if (event.view.type != InventoryType.PLAYER) {
            return
        }

        if (armorSlots.contains(event.rawSlot)) {
            event.isCancelled = true
        }
    }
}