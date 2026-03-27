package me.roustytousty.elytrapvp.gui.shops

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.ItemUtils.itemBuilder
import me.roustytousty.elytrapvp.utility.SoundUtils
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

    private val shopService = Services.shopService
    private val SHOP_TITLE = "Consumables"

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.view.title != SHOP_TITLE) return

        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        if (e.rawSlot == 18) {
            ShopMenu.openInventory(p)
            return
        }

        val shopItem = shopService.getShopItemBySlot(SHOP_TITLE, e.rawSlot)
        if (shopItem != null) {
            shopService.tryPlayerPurchaseItem(p, shopItem)
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.view.title == SHOP_TITLE) {
            e.isCancelled = true
        }
    }

    companion object {

        fun openInventory(player: Player) {
            val inventory = Bukkit.createInventory(null, 27, "Consumables")
            initItems(inventory)
            player.openInventory(inventory)
            SoundUtils.playGuiClick(player)
        }

        private fun initItems(inventory: Inventory) {
            val slots = intArrayOf(0, 8, 9, 17, 18, 26)
            for (slot in slots) {
                inventory.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            inventory.setItem(18, itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cBack"))

            val items = Services.shopService.getShopItems("Consumables")
            for (item in items) {
                val lore = mutableListOf<String>()
                lore.addAll(item.description)
                lore.add("")
                lore.add("&fCost: &6${item.cost}g")
                lore.add("")
                lore.add("&7Click to buy!")

                inventory.setItem(
                    item.slot,
                    itemBuilder(
                        item.material,
                        1,
                        false,
                        "&e${item.displayName} &6x${item.amount}",
                        *lore.toTypedArray()
                    )
                )
            }
        }
    }
}