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

class ConsumablesShopMenu : Listener {

    private val shopService = ShopService()

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        val inventory = e.view.title
        if (inventory != "Consumables") return

        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        when (e.rawSlot) {
            11 -> shopService.shopPurchaseItem(p, 20, clickedItem.type, 1)

            18 -> ShopMenu.openInventory(p)
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.view.title == "Consumables") {
            e.isCancelled = true
        }
    }

    companion object {

        fun openInventory(player: Player) {
            val inventory = Bukkit.createInventory(null, 27, "Consumables")
            initItems(inventory)
            player.openInventory(inventory)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems(inventory: Inventory) {
            val slots = intArrayOf(0, 8, 9, 17, 18, 26)
            for (slot in slots) {
                inventory.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            inventory.setItem(
                11,
                itemBuilder(
                    Material.APPLE,
                    1,
                    false,
                    "&eRegen apple &6x1",
                    "&7Gives regeneration I for",
                    "&730 seconds once consumed!",
                    "",
                    "&fPrice: &620g",
                    "",
                    "&7Click to buy!"
                )
            )


            inventory.setItem(
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