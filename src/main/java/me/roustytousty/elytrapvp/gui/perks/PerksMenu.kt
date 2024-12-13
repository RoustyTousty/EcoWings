package me.roustytousty.elytrapvp.gui.perks

import me.roustytousty.elytrapvp.utility.ItemUtils.itemBuilder
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

        if (e.rawSlot == 45) {
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
            inv = Bukkit.createInventory(null, 54, "Perks")
            initItems()
            player.openInventory(inv!!)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems() {
            val slots = intArrayOf(0, 8, 9, 17, 18, 26, 27, 35, 36, 44, 53)
            for (slot in slots) {
                inv!!.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }


            inv!!.setItem(
                13,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&eActive perk",
                    "&7Test!"
                )
            )


            inv!!.setItem(
                29,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-1",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                30,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                31,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                32,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                33,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                38,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                39,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                40,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                41,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                42,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )

            inv!!.setItem(
                45,
                itemBuilder(
                    Material.RED_STAINED_GLASS_PANE,
                    1,
                    false,
                    "&cClose"
                )
            )
        }
    }
}