package me.roustytousty.elytrapvp.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType

class OnInventoryClick : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {

        if (event.view.type != InventoryType.CRAFTING && event.view.type != InventoryType.PLAYER) {
            return
        }

        if (event.slotType == InventoryType.SlotType.ARMOR) {
            event.isCancelled = true
        }
    }
}