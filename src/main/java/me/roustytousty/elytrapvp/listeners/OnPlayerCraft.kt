package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent

class OnPlayerCraft : Listener {

    @EventHandler
    fun onPlayerCraft(event: CraftItemEvent) {
        val result = event.currentItem
        if (result != null) {
            val player = event.whoClicked as Player
            MessageUtils.sendError(player, "&fItem crafting is disabled!")
            event.isCancelled = true
        }
    }
}