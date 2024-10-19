package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.utility.StringUtils.parse
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent

class OnPlayerCraft : Listener {

    @EventHandler
    fun onPlayerCraft(event: CraftItemEvent) {
        val result = event.currentItem
        if (result != null) {
            event.isCancelled = true
            event.whoClicked.sendMessage(parse("&6&lEcoWings &8| &fItem crafting is disabled!"))
        }
    }
}