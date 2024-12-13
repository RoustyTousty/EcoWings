package me.roustytousty.elytrapvp.gui.shops

import me.roustytousty.elytrapvp.services.ShopService
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

class UtilityShopMenu : Listener {

    private val shopService = ShopService()

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory != inv) return
        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        if (e.rawSlot == 18) {
            ShopMenu.openInventory(p)
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
            inv = Bukkit.createInventory(null, 27, "Utility")
            initItems()
            player.openInventory(inv!!)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems() {
            val slots = intArrayOf(0, 8, 9, 17, 18, 26)
            for (slot in slots) {
                inv!!.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            inv!!.setItem(
                11,
                itemBuilder(
                    Material.SUGAR,
                    1,
                    false,
                    "&fDusty",
                    "&7Blocks in 10 block radius",
                    "&7get turned into dust!",
                    "",
                    "&fCost: &620g",
                    "",
                    "&7Click to buy!"
                )
            )
            inv!!.setItem(
                12,
                itemBuilder(
                    Material.TNT,
                    1,
                    false,
                    "&fExplosive",
                    "&7Blows up blocks and deals",
                    "&7damage to anyone near by!",
                    "",
                    "&fCost: &650g",
                    "",
                    "&7Click to buy!"
                )
            )


            inv!!.setItem(
                18,
                itemBuilder(
                    Material.RED_STAINED_GLASS_PANE,
                    1,
                    false,
                    "&cBack"
                )
            )
        }
    }
}