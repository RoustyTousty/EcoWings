package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.utility.StringUtils.parse
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent

class OnCraftItem : Listener {

    @EventHandler
    fun onCraft(event: CraftItemEvent) {
        val result = event.currentItem
        if (result != null) {
            event.isCancelled = true
            event.whoClicked.sendMessage(parse("&6&lWWWings &8| &fItem crafting is disabled!"))
        }
    }
}