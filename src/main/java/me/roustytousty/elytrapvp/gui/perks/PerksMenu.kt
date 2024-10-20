package me.roustytousty.elytrapvp.gui.perks

import me.roustytousty.elytrapvp.api.MongoDB
import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.GuiUtils.createGuiItem
import me.roustytousty.elytrapvp.utility.GuiUtils.createPlayerHead
import me.roustytousty.elytrapvp.utility.StringUtils.formatNumber
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

class PerksMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory != inv) return
        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        if (e.rawSlot == 27) {
            p.closeInventory()
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
            inv = Bukkit.createInventory(null, 54, "Stats - ${player.name}")
            initItems()
            player.openInventory(inv!!)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems() {
            val slots = intArrayOf(0, 8, 9, 17, 18, 26, 27, 35, 36, 44, 53)
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
                29,
                createGuiItem(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-1",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                30,
                createGuiItem(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                31,
                createGuiItem(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                32,
                createGuiItem(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                33,
                createGuiItem(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                38,
                createGuiItem(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                39,
                createGuiItem(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                40,
                createGuiItem(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                41,
                createGuiItem(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                42,
                createGuiItem(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )

            inv!!.setItem(
                45,
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