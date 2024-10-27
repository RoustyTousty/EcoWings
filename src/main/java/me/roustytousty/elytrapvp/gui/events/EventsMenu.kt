package me.roustytousty.elytrapvp.gui.events

import me.roustytousty.elytrapvp.utility.GuiUtils.createGuiItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

class EventsMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory != inv) return
        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        if (e.rawSlot == 18) {
            p.closeInventory()
            p.playSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }
    }

    @EventHandler
    private fun onInventoryClick(e: InventoryDragEvent) {
        if (e.inventory == inv) {
            e.isCancelled = true
        }
    }

    companion object {

        var inv: Inventory? = null
        fun openInventory(player: Player) {
            inv = Bukkit.createInventory(null, 27, "Events")
            initItems()
            player.openInventory(inv!!)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems() {
            val slots = intArrayOf(0, 8, 9, 17, 26)
            for (slot in slots) {
                inv!!.setItem(slot, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }


            inv!!.setItem(
                13,
                createGuiItem(
                    Material.BARRIER,
                    1,
                    false,
                    "&eActive perk",
                    "&7Test!"
                )
            )


            inv!!.setItem(
                13,
                createGuiItem(
                    Material.BARRIER,
                    1,
                    false,
                    "&eEvent-EX",
                    "&7Every player gets x and",
                    "&7x things happen when x.",
                    "",
                    "&fProgress: &640g&f/&6150g",
                    "",
                    "&7Click to donate 10g!",
                    "&7Shift-Click to donate 100g!"
                )
            )

            inv!!.setItem(
                18,
                createGuiItem(
                    Material.RED_STAINED_GLASS_PANE,
                    1,
                    false,
                    "&cClose"
                )
            )
        }
    }
}